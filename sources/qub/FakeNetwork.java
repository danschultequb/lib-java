package qub;

public class FakeNetwork implements Network
{
    private final AsyncRunner asyncRunner;
    private final Mutex mutex;
    private final MutexCondition boundTCPServerAvailable;
    private final MutableMap<IPv4Address,MutableMap<Integer,FakeTCPClient>> boundTCPClients;
    private final MutableMap<IPv4Address,MutableMap<Integer,FakeTCPServer>> boundTCPServers;
    private final MutableMap<InMemoryByteStream,Integer> streamReferenceCounts;
    private final FakeDNS dns;
    private final HttpClient httpClient;

    public FakeNetwork(AsyncRunner asyncRunner)
    {
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");

        this.asyncRunner = asyncRunner;
        mutex = new SpinMutex(asyncRunner.getClock());
        boundTCPServerAvailable = mutex.createCondition();
        boundTCPClients = MutableMap.create();
        boundTCPServers = MutableMap.create();
        streamReferenceCounts = MutableMap.create();
        dns = new FakeDNS();
        httpClient = new BasicHttpClient(this);
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }

    @Override
    public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort)
    {
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);

        return createTCPClientInner(remoteIPAddress, remotePort, null);
    }

    @Override
    public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, DateTime timeout)
    {
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);
        Network.validateTimeout(timeout);

        return createTCPClientInner(remoteIPAddress, remotePort, timeout);
    }

    private Result<TCPClient> createTCPClientInner(IPv4Address remoteIPAddress, int remotePort, DateTime timeout)
    {
        return mutex.criticalSection(() ->
        {
            Result<TCPClient> result = null;

            FakeTCPServer remoteTCPServer = null;

            final Clock clock = getAsyncRunner().getClock();

            if (timeout != null && clock.getCurrentDateTime().greaterThanOrEqualTo(timeout))
            {
                result = Result.error(new TimeoutException());
            }
            else
            {
                while (remoteTCPServer == null && result == null)
                {
                    final Result<MutableMap<Integer,FakeTCPServer>> remoteTCPServersResult = boundTCPServers.get(remoteIPAddress);
                    if (!remoteTCPServersResult.hasError())
                    {
                        final Result<FakeTCPServer> remoteTCPServerResult = remoteTCPServersResult.getValue().get(remotePort);
                        remoteTCPServer = remoteTCPServerResult.getValue();
                    }

                    if (remoteTCPServer == null)
                    {
                        if (timeout == null)
                        {
                            result = Result.error(new java.net.ConnectException("Connection refused: connect"));
                        }
                        else
                        {
                            final Result<Boolean> awaitResult = boundTCPServerAvailable.await(timeout);
                            if (awaitResult.hasError())
                            {
                                result = Result.error(awaitResult.getError());
                            }
                        }
                    }
                }
            }

            if (result == null)
            {
                final IPv4Address clientLocalIPAddress = IPv4Address.localhost;
                int clientLocalPort = 65535;
                while (1 <= clientLocalPort && !isAvailable(clientLocalIPAddress, clientLocalPort))
                {
                    --clientLocalPort;
                }

                if (clientLocalPort < 1)
                {
                    result = Result.error(new IllegalStateException("No more ports available on IP address " + clientLocalIPAddress));
                }
                else
                {
                    int serverClientLocalPort = 65535;
                    while (1 <= serverClientLocalPort && !isAvailable(remoteIPAddress, serverClientLocalPort))
                    {
                        --serverClientLocalPort;
                    }

                    if (serverClientLocalPort < 1)
                    {
                        result = Result.error(new IllegalStateException("No more ports available on IP address " + remoteIPAddress));
                    }
                    else
                    {
                        final InMemoryByteStream clientToServer = createNetworkStream();
                        final InMemoryByteStream serverToClient = createNetworkStream();

                        final FakeTCPClient tcpClient = new FakeTCPClient(this, asyncRunner, clientLocalIPAddress, clientLocalPort, remoteIPAddress, remotePort, serverToClient, clientToServer);
                        addLocalTCPClient(tcpClient);

                        final FakeTCPClient serverTCPClient = new FakeTCPClient(this, asyncRunner, remoteIPAddress, serverClientLocalPort, clientLocalIPAddress, clientLocalPort, clientToServer, serverToClient);
                        addLocalTCPClient(serverTCPClient);

                        remoteTCPServer.addIncomingClient(serverTCPClient);

                        result = Result.success(tcpClient);
                    }
                }
            }
            return result;
        });
    }

    private InMemoryByteStream createNetworkStream()
    {
        final InMemoryByteStream networkStream = new InMemoryByteStream(asyncRunner);
        streamReferenceCounts.set(networkStream, 2);
        return networkStream;
    }

    private void decrementNetworkStream(InMemoryByteStream networkStream)
    {
        PreCondition.assertNotNull(networkStream, "networkStream");

        final int currentStreamReferenceCount = streamReferenceCounts.get(networkStream).throwErrorOrGetValue();
        if (currentStreamReferenceCount == 1)
        {
            networkStream.dispose();
            streamReferenceCounts.remove(networkStream);
        }
        else
        {
            streamReferenceCounts.set(networkStream, currentStreamReferenceCount - 1);
        }
    }

    private void addLocalTCPClient(FakeTCPClient tcpClient)
    {
        PreCondition.assertNotNull(tcpClient, "tcpClient");

        final IPv4Address localIPAddress = tcpClient.getLocalIPAddress();
        MutableMap<Integer,FakeTCPClient> localClients = boundTCPClients.get(localIPAddress).getValue();
        if (localClients == null)
        {
            localClients = MutableMap.create();
            boundTCPClients.set(localIPAddress, localClients);
        }
        localClients.set(tcpClient.getLocalPort(), tcpClient);
    }

    @Override
    public Result<TCPServer> createTCPServer(int localPort)
    {
        Network.validateLocalPort(localPort);

        return createTCPServer(IPv4Address.localhost, localPort);
    }

    @Override
    public Result<TCPServer> createTCPServer(IPv4Address localIPAddress, int localPort)
    {
        Network.validateLocalIPAddress(localIPAddress);
        Network.validateLocalPort(localPort);

        return mutex.criticalSection(() ->
        {
            Result<TCPServer> result;
            if (!isAvailable(localIPAddress, localPort))
            {
                result = Result.error(new java.io.IOException("IPAddress (" + localIPAddress + ") and port (" + localPort + ") are already bound."));
            }
            else
            {
                final FakeTCPServer tcpServer = new FakeTCPServer(localIPAddress, localPort, this, asyncRunner);

                MutableMap<Integer, FakeTCPServer> localTCPServers = boundTCPServers.get(localIPAddress).getValue();
                if (localTCPServers == null)
                {
                    localTCPServers = MutableMap.create();
                    boundTCPServers.set(localIPAddress, localTCPServers);
                }
                localTCPServers.set(localPort, tcpServer);

                result = Result.success(tcpServer);

                boundTCPServerAvailable.signalAll();
            }
            return result;
        });

    }

    @Override
    public HttpClient getHttpClient()
    {
        return httpClient;
    }

    @Override
    public Result<Boolean> isConnected()
    {
        return Result.successTrue();
    }

    @Override
    public FakeDNS getDNS()
    {
        return dns;
    }

    public void serverDisposed(IPv4Address ipAddress, int port)
    {
        PreCondition.assertNotNull(ipAddress, "ipAddress");
        Network.validatePort(port, "port");

        mutex.criticalSection(() ->
        {
            boundTCPServers.get(ipAddress).getValue().remove(port);
        });
    }

    public void clientDisposed(FakeTCPClient tcpClient)
    {
        PreCondition.assertNotNull(tcpClient, "tcpClient");

        mutex.criticalSection(() ->
        {
            decrementNetworkStream((InMemoryByteStream)tcpClient.getReadStream());
            decrementNetworkStream((InMemoryByteStream)tcpClient.getWriteStream());
            boundTCPClients.get(tcpClient.getLocalIPAddress()).getValue().remove(tcpClient.getLocalPort());
        });
    }

    public boolean isAvailable(IPv4Address ipAddress, int port)
    {
        PreCondition.assertNotNull(ipAddress, "ipAddress");
        Network.validatePort(port, "port");

        return mutex.criticalSection(() ->
        {
            boolean result = true;

            final Map<Integer, FakeTCPClient> localTCPClients = boundTCPClients.get(ipAddress).getValue();
            if (localTCPClients != null && localTCPClients.containsKey(port))
            {
                result = false;
            }
            else
            {
                final Map<Integer, FakeTCPServer> localTCPServers = boundTCPServers.get(ipAddress).getValue();
                if (localTCPServers != null && localTCPServers.containsKey(port))
                {
                    result = false;
                }
            }

            return result;
        });
    }
}
