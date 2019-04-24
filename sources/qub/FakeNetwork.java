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
        boundTCPClients = Map.create();
        boundTCPServers = Map.create();
        streamReferenceCounts = Map.create();
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

    private Result<TCPClient> createTCPClientInner(IPv4Address remoteIPAddress, int remotePort, DateTime dateTimeTimeout)
    {
        PreCondition.assertNotNull(remoteIPAddress, "remoteIPAddress");
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);

        final Function0<Result<TCPClient>> function = () ->
        {
            Result<TCPClient> result;
            if (dateTimeTimeout != null && getAsyncRunner().getClock().getCurrentDateTime().greaterThanOrEqualTo(dateTimeTimeout))
            {
                result = Result.error(new TimeoutException());
            }
            else
            {
                result = Result.create(() ->
                {
                    FakeTCPServer remoteTCPServer = null;

                    while (remoteTCPServer == null)
                    {
                        remoteTCPServer = boundTCPServers.get(remoteIPAddress)
                            .thenResult((MutableMap<Integer,FakeTCPServer> remoteTCPServers) -> remoteTCPServers.get(remotePort))
                            .catchErrorResult(NotFoundException.class, () ->
                            {
                                return dateTimeTimeout == null
                                    ? Result.error(new java.net.ConnectException("Connection refused: connect"))
                                    : boundTCPServerAvailable.await(dateTimeTimeout).then(() -> null);
                            })
                            .await();
                    }

                    final IPv4Address clientLocalIPAddress = IPv4Address.localhost;
                    int clientLocalPort = 65535;
                    while (1 <= clientLocalPort && !isAvailable(clientLocalIPAddress, clientLocalPort))
                    {
                        --clientLocalPort;
                    }

                    if (clientLocalPort < 1)
                    {
                        throw new IllegalStateException("No more ports available on IP address " + clientLocalIPAddress);
                    }

                    int serverClientLocalPort = 65535;
                    while (1 <= serverClientLocalPort && !isAvailable(remoteIPAddress, serverClientLocalPort))
                    {
                        --serverClientLocalPort;
                    }

                    if (serverClientLocalPort < 1)
                    {
                        throw new IllegalStateException("No more ports available on IP address " + remoteIPAddress);
                    }

                    final InMemoryByteStream clientToServer = createNetworkStream();
                    final InMemoryByteStream serverToClient = createNetworkStream();

                    final FakeTCPClient tcpClient = new FakeTCPClient(this, asyncRunner, clientLocalIPAddress, clientLocalPort, remoteIPAddress, remotePort, serverToClient, clientToServer);
                    addLocalTCPClient(tcpClient);

                    final FakeTCPClient serverTCPClient = new FakeTCPClient(this, asyncRunner, remoteIPAddress, serverClientLocalPort, clientLocalIPAddress, clientLocalPort, clientToServer, serverToClient);
                    addLocalTCPClient(serverTCPClient);

                    remoteTCPServer.addIncomingClient(serverTCPClient);

                    return tcpClient;
                });
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        };

        return dateTimeTimeout != null
            ? mutex.criticalSectionResult(dateTimeTimeout, function)
            : mutex.criticalSectionResult(function);
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

        final int currentStreamReferenceCount = streamReferenceCounts.get(networkStream).await();
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

        boundTCPClients.getOrSet(tcpClient.getLocalIPAddress(), Map::create)
            .then((MutableMap<Integer,FakeTCPClient> localClients) ->
            {
                localClients.set(tcpClient.getLocalPort(), tcpClient);
            });
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

        return mutex.criticalSectionResult(() ->
        {
            Result<TCPServer> result;
            if (!isAvailable(localIPAddress, localPort))
            {
                result = Result.error(new java.io.IOException("IPAddress (" + localIPAddress + ") and port (" + localPort + ") are already bound."));
            }
            else
            {
                final FakeTCPServer tcpServer = new FakeTCPServer(localIPAddress, localPort, this, asyncRunner);

                boundTCPServers.getOrSet(localIPAddress, Map::create)
                    .then((MutableMap<Integer,FakeTCPServer> localTCPServers) ->
                    {
                        localTCPServers.set(localPort, tcpServer);
                    });


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
        Network.validateIPAddress(ipAddress, "ipAddress");
        Network.validatePort(port, "port");

        mutex.criticalSection(() ->
        {
            boundTCPServers.get(ipAddress)
                .then((MutableMap<Integer,FakeTCPServer> tcpServers) -> tcpServers.remove(port));
        });
    }

    public void clientDisposed(FakeTCPClient tcpClient)
    {
        PreCondition.assertNotNull(tcpClient, "tcpClient");

        mutex.criticalSection(() ->
        {
            decrementNetworkStream((InMemoryByteStream)tcpClient.getReadStream());
            decrementNetworkStream((InMemoryByteStream)tcpClient.getWriteStream());
            boundTCPClients.get(tcpClient.getLocalIPAddress())
                .then((MutableMap<Integer,FakeTCPClient> portToClientMap) ->
                {
                    portToClientMap.remove(tcpClient.getLocalPort());
                });
        });
    }

    public boolean isAvailable(IPv4Address ipAddress, int port)
    {
        Network.validateIPAddress(ipAddress, "ipAddress");
        Network.validatePort(port, "port");

        return mutex.criticalSection(() ->
        {
            boolean result = true;

            final Map<Integer,FakeTCPClient> localTCPClients = boundTCPClients.get(ipAddress)
                .catchError(NotFoundException.class)
                .await();
            if (localTCPClients != null && localTCPClients.containsKey(port))
            {
                result = false;
            }
            else
            {
                final Map<Integer,FakeTCPServer> localTCPServers = boundTCPServers.get(ipAddress)
                    .catchError(NotFoundException.class)
                    .await();
                if (localTCPServers != null && localTCPServers.containsKey(port))
                {
                    result = false;
                }
            }
            return result;
        }).await();
    }
}
