package qub;

public class FakeNetwork implements Network
{
    private final Clock clock;
    private final Mutex mutex;
    private final MutexCondition boundTCPServerAvailable;
    private final MutableMap<IPv4Address,MutableMap<Integer,FakeTCPClient>> boundTCPClients;
    private final MutableMap<IPv4Address,MutableMap<Integer,FakeTCPServer>> boundTCPServers;
    private final MutableMap<InMemoryByteStream,Integer> streamReferenceCounts;
    private final FakeDNS dns;
    private final HttpClient httpClient;

    public FakeNetwork(Clock clock)
    {
        PreCondition.assertNotNull(clock, "clock");

        this.clock = clock;
        mutex = new SpinMutex(clock);
        boundTCPServerAvailable = mutex.createCondition();
        boundTCPClients = Map.create();
        boundTCPServers = Map.create();
        streamReferenceCounts = Map.create();
        dns = new FakeDNS();
        httpClient = new BasicHttpClient(this);
    }

    @Override
    public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort)
    {
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);

        return createTCPClientInner(remoteIPAddress, remotePort, null);
    }

    @Override
    public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, Duration timeout)
    {
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);
        Network.validateTimeout(timeout);

        final DateTime dateTimeTimeout = clock.getCurrentDateTime().plus(timeout);
        return createTCPClient(remoteIPAddress, remotePort, dateTimeTimeout);
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
            if (dateTimeTimeout != null && clock.getCurrentDateTime().greaterThanOrEqualTo(dateTimeTimeout))
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
                        if (dateTimeTimeout == null)
                        {
                            mutex.acquire().await();
                        }
                        else
                        {
                            mutex.acquire(dateTimeTimeout).await();
                        }
                        try
                        {
                            remoteTCPServer = boundTCPServers.get(remoteIPAddress)
                                .thenResult((MutableMap<Integer,FakeTCPServer> remoteTCPServers) -> remoteTCPServers.get(remotePort))
                                .catchError(NotFoundException.class)
                                .await();
                            if (remoteTCPServer == null)
                            {
                                if (dateTimeTimeout == null)
                                {
                                    throw Exceptions.asRuntime(new java.net.ConnectException("Connection refused: connect"));
                                }
                                else
                                {
                                    boundTCPServerAvailable.watch(dateTimeTimeout).await();
                                }
                            }
                        }
                        finally
                        {
                            mutex.release().await();
                        }
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

                    final FakeTCPClient tcpClient = new FakeTCPClient(this, clientLocalIPAddress, clientLocalPort, remoteIPAddress, remotePort, serverToClient, clientToServer);
                    addLocalTCPClient(tcpClient);

                    final FakeTCPClient serverTCPClient = new FakeTCPClient(this, remoteIPAddress, serverClientLocalPort, clientLocalIPAddress, clientLocalPort, clientToServer, serverToClient);
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
        final InMemoryByteStream networkStream = new InMemoryByteStream();
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

        mutex.criticalSection(() ->
        {
            boundTCPClients.getOrSet(tcpClient.getLocalIPAddress(), Map::create)
                .await()
                .set(tcpClient.getLocalPort(), tcpClient);
        }).await();
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
                final FakeTCPServer tcpServer = new FakeTCPServer(localIPAddress, localPort, this, clock);

                boundTCPServers.getOrSet(localIPAddress, Map::create)
                    .await()
                    .set(localPort, tcpServer);
                boundTCPServerAvailable.signalAll();

                result = Result.success(tcpServer);
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
                .await()
                .remove(port);
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
                .await()
                .remove(tcpClient.getLocalPort());
        }).await();
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
