package qub;

public class FakeNetwork implements Network
{
    private final Clock clock;
    private final Mutex mutex;
    private final MutexCondition boundTCPServerAvailable;
    private final MutableMap<IPv4Address,MutableMap<Integer,FakeTCPClient>> boundTCPClients;
    private final MutableMap<IPv4Address,MutableMap<Integer,FakeTCPServer>> boundTCPServers;
    private final MutableMap<InMemoryByteStream,Integer> streamReferenceCounts;

    private FakeNetwork(Clock clock)
    {
        this.clock = clock;
        this.mutex = (clock == null ? SpinMutex.create() : SpinMutex.create(clock));
        this.boundTCPServerAvailable = mutex.createCondition();
        this.boundTCPClients = Map.create();
        this.boundTCPServers = Map.create();
        this.streamReferenceCounts = Map.create();
    }

    public static FakeNetwork create(Clock clock)
    {
        return new FakeNetwork(clock);
    }

    @Override
    public Result<FakeTCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort)
    {
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);

        return this.createTCPClientInner(remoteIPAddress, remotePort, null);
    }

    @Override
    public Result<FakeTCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, Duration timeout)
    {
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);
        Network.validateTimeout(timeout);
        PreCondition.assertNotNull(this.clock, "this.clock");

        final DateTime dateTimeTimeout = this.clock.getCurrentDateTime().plus(timeout);
        return this.createTCPClient(remoteIPAddress, remotePort, dateTimeTimeout);
    }

    @Override
    public Result<FakeTCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, DateTime timeout)
    {
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);
        Network.validateTimeout(timeout);

        return this.createTCPClientInner(remoteIPAddress, remotePort, timeout);
    }

    private Result<FakeTCPClient> createTCPClientInner(IPv4Address remoteIPAddress, int remotePort, DateTime dateTimeTimeout)
    {
        PreCondition.assertNotNull(remoteIPAddress, "remoteIPAddress");
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);
        PreCondition.assertNotNull(this.clock, "this.clock");

        final Function0<Result<FakeTCPClient>> function = () ->
        {
            Result<FakeTCPClient> result;
            if (dateTimeTimeout != null && this.clock.getCurrentDateTime().greaterThanOrEqualTo(dateTimeTimeout))
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
                            this.mutex.acquire().await();
                        }
                        else
                        {
                            this.mutex.acquire(dateTimeTimeout).await();
                        }
                        try
                        {
                            remoteTCPServer = this.boundTCPServers.get(remoteIPAddress)
                                .then((MutableMap<Integer,FakeTCPServer> remoteTCPServers) -> remoteTCPServers.get(remotePort).await())
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
                                    this.boundTCPServerAvailable.watch(dateTimeTimeout).await();
                                }
                            }
                        }
                        finally
                        {
                            this.mutex.release().await();
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
                    final boolean localIPAddressEqualsRemoteIPAddress = clientLocalIPAddress.equals(remoteIPAddress);
                    while ((localIPAddressEqualsRemoteIPAddress && clientLocalPort == serverClientLocalPort) ||
                            !isAvailable(remoteIPAddress, serverClientLocalPort))
                    {
                        --serverClientLocalPort;

                        if (serverClientLocalPort <= 0)
                        {
                            throw new IllegalStateException("No more ports available on IP address " + remoteIPAddress);
                        }
                    }

                    final InMemoryByteStream clientToServer = createNetworkStream();
                    final InMemoryByteStream serverToClient = createNetworkStream();

                    final FakeTCPClient tcpClient = FakeTCPClient.create(this, clientLocalIPAddress, clientLocalPort, remoteIPAddress, remotePort, serverToClient, clientToServer);
                    this.addLocalTCPClient(tcpClient);

                    final FakeTCPClient serverTCPClient = FakeTCPClient.create(this, remoteIPAddress, serverClientLocalPort, clientLocalIPAddress, clientLocalPort, clientToServer, serverToClient);
                    this.addLocalTCPClient(serverTCPClient);

                    remoteTCPServer.addIncomingClient(serverTCPClient);

                    return tcpClient;
                });
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        };

        return dateTimeTimeout != null
            ? this.mutex.criticalSection(dateTimeTimeout, () -> function.run().await())
            : this.mutex.criticalSection(() -> function.run().await());
    }

    private InMemoryByteStream createNetworkStream()
    {
        final InMemoryByteStream networkStream = InMemoryByteStream.create();
        this.streamReferenceCounts.set(networkStream, 2);
        return networkStream;
    }

    private void decrementNetworkStream(InMemoryByteStream networkStream)
    {
        PreCondition.assertNotNull(networkStream, "networkStream");

        final int currentStreamReferenceCount = this.streamReferenceCounts.get(networkStream).await();
        if (currentStreamReferenceCount == 2)
        {
            networkStream.endOfStream();
            this.streamReferenceCounts.set(networkStream, 1);
        }
        else
        {
            networkStream.dispose().await();
            this.streamReferenceCounts.remove(networkStream).await();
        }
    }

    private void addLocalTCPClient(FakeTCPClient tcpClient)
    {
        PreCondition.assertNotNull(tcpClient, "tcpClient");

        this.mutex.criticalSection(() ->
        {
            this.boundTCPClients.getOrSet(tcpClient.getLocalIPAddress(), Map::create).await()
                .set(tcpClient.getLocalPort(), tcpClient);
        }).await();
    }

    @Override
    public Result<FakeTCPServer> createTCPServer(int localPort)
    {
        Network.validateLocalPort(localPort);

        return this.createTCPServer(IPv4Address.localhost, localPort);
    }

    @Override
    public Result<FakeTCPServer> createTCPServer(IPv4Address localIPAddress, int localPort)
    {
        Network.validateLocalIPAddress(localIPAddress);
        Network.validateLocalPort(localPort);

        return this.mutex.criticalSection(() ->
        {
            if (!this.isAvailable(localIPAddress, localPort))
            {
                throw Exceptions.asRuntime(new java.io.IOException("IPAddress (" + localIPAddress + ") and port (" + localPort + ") are already bound."));
            }

            final FakeTCPServer tcpServer = this.clock == null
                ? FakeTCPServer.create(localIPAddress, localPort, this)
                : FakeTCPServer.create(localIPAddress, localPort, this, this.clock);

            this.boundTCPServers.getOrSet(localIPAddress, Map::create).await()
                .set(localPort, tcpServer);
            this.boundTCPServerAvailable.signalAll();

            return tcpServer;
        });
    }

    public void serverDisposed(IPv4Address ipAddress, int port)
    {
        Network.validateIPAddress(ipAddress, "ipAddress");
        Network.validatePort(port, "port");

        this.mutex.criticalSection(() ->
        {
            this.boundTCPServers.get(ipAddress).await()
                .remove(port).await();
        }).await();
    }

    public void clientDisposed(IPv4Address localIPAddress, int localPort, InMemoryByteStream socketReadStream, InMemoryByteStream socketWriteStream)
    {
        Network.validateLocalIPAddress(localIPAddress);
        Network.validateLocalPort(localPort);
        PreCondition.assertNotNull(socketReadStream, "socketReadStream");
        PreCondition.assertNotDisposed(socketReadStream, "socketReadStream");
        PreCondition.assertNotNull(socketWriteStream, "socketWriteStream");
        PreCondition.assertNotDisposed(socketWriteStream, "socketWriteStream");

        this.mutex.criticalSection(() ->
        {
            this.decrementNetworkStream(socketReadStream);
            this.decrementNetworkStream(socketWriteStream);
            this.boundTCPClients.get(localIPAddress).await()
                .remove(localPort).await();
        }).await();
    }

    public boolean isAvailable(IPv4Address ipAddress, int port)
    {
        Network.validateIPAddress(ipAddress, "ipAddress");
        Network.validatePort(port, "port");

        return this.mutex.criticalSection(() ->
        {
            boolean result = true;

            final Map<Integer,FakeTCPClient> localTCPClients = this.boundTCPClients.get(ipAddress)
                .catchError(NotFoundException.class)
                .await();
            if (localTCPClients != null && localTCPClients.containsKey(port))
            {
                result = false;
            }
            else
            {
                final Map<Integer,FakeTCPServer> localTCPServers = this.boundTCPServers.get(ipAddress)
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
