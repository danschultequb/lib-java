package qub;

public class FakeNetwork implements Network
{
    private final AsyncRunner asyncRunner;
    private final Mutex mutex;
    private final Map<IPv4Address,Map<Integer,FakeTCPClient>> boundTCPClients;
    private final Map<IPv4Address,Map<Integer,FakeTCPServer>> boundTCPServers;
    private final Map<InMemoryByteStream,Integer> streamReferenceCounts;
    private final FakeDNS dns;
    private final HttpClient httpClient;

    public FakeNetwork(AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
        mutex = new SpinMutex();
        boundTCPClients = new ListMap<>();
        boundTCPServers = new ListMap<>();
        streamReferenceCounts = new ListMap<>();
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
        Result<TCPClient> result = Network.validateRemoteIPAddress(remoteIPAddress);
        if (result == null)
        {
            result = Network.validateRemotePort(remotePort);
            if (result == null)
            {
                try (final Disposable ignored = mutex.criticalSection())
                {
                    FakeTCPServer remoteTCPServer = null;

                    final Map<Integer,FakeTCPServer> remoteTCPServers = boundTCPServers.get(remoteIPAddress);
                    if (remoteTCPServers != null)
                    {
                        remoteTCPServer = remoteTCPServers.get(remotePort);
                    }

                    if (remoteTCPServer == null)
                    {
                        result = Result.error(new java.net.ConnectException("Connection refused: connect"));
                    }
                    else
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
                }
            }
        }
        return result;
    }

    private InMemoryByteStream createNetworkStream()
    {
        final InMemoryByteStream networkStream = new InMemoryByteStream(asyncRunner);
        streamReferenceCounts.set(networkStream, 2);
        return networkStream;
    }

    private void decrementNetworkStream(InMemoryByteStream networkStream)
    {
        final int currentStreamReferenceCount = streamReferenceCounts.get(networkStream);
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
        final IPv4Address localIPAddress = tcpClient.getLocalIPAddress();
        Map<Integer,FakeTCPClient> localClients = boundTCPClients.get(localIPAddress);
        if (localClients == null)
        {
            localClients = new ListMap<>();
            boundTCPClients.set(localIPAddress, localClients);
        }
        localClients.set(tcpClient.getLocalPort(), tcpClient);
    }

    @Override
    public Result<TCPServer> createTCPServer(int localPort)
    {
        return createTCPServer(IPv4Address.localhost, localPort);
    }

    @Override
    public Result<TCPServer> createTCPServer(IPv4Address localIPAddress, int localPort)
    {
        Result<TCPServer> result = Network.validateLocalIPAddress(localIPAddress);
        if (result == null)
        {
            result = Network.validateLocalPort(localPort);
            if (result == null)
            {
                try (final Disposable ignored = mutex.criticalSection())
                {
                    if (!isAvailable(localIPAddress, localPort))
                    {
                        result = Result.error(new java.io.IOException("IPAddress (" + localIPAddress + ") and port (" + localPort + ") are already bound."));
                    }
                    else
                    {
                        final FakeTCPServer tcpServer = new FakeTCPServer(localIPAddress, localPort, this, asyncRunner);

                        Map<Integer, FakeTCPServer> localTCPServers = boundTCPServers.get(localIPAddress);
                        if (localTCPServers == null)
                        {
                            localTCPServers = new ListMap<>();
                            boundTCPServers.set(localIPAddress, localTCPServers);
                        }
                        localTCPServers.set(localPort, tcpServer);

                        result = Result.success(tcpServer);
                    }
                }
            }
        }
        return result;
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
        try (final Disposable ignored = mutex.criticalSection())
        {
            boundTCPServers.get(ipAddress).remove(port);
        }
    }

    public void clientDisposed(FakeTCPClient tcpClient)
    {
        try (final Disposable ignored = mutex.criticalSection())
        {
            decrementNetworkStream((InMemoryByteStream)tcpClient.getReadStream());
            decrementNetworkStream((InMemoryByteStream)tcpClient.getWriteStream());
            boundTCPClients.get(tcpClient.getLocalIPAddress()).remove(tcpClient.getLocalPort());
        }
    }

    public boolean isAvailable(IPv4Address ipAddress, int port)
    {
        boolean result = true;

        final Map<Integer, FakeTCPClient> localTCPClients = boundTCPClients.get(ipAddress);
        if (localTCPClients != null && localTCPClients.get(port) != null)
        {
            result = false;
        }
        else
        {
            final Map<Integer, FakeTCPServer> localTCPServers = boundTCPServers.get(ipAddress);
            if (localTCPServers != null && localTCPServers.get(port) != null)
            {
                result = false;
            }
        }

        return result;
    }
}
