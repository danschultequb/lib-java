package qub;

import java.io.IOException;

public class FakeNetwork extends NetworkBase
{
    private final AsyncRunner asyncRunner;
    private final Mutex mutex;
    private final Map<IPv4Address,Map<Integer,FakeTCPClient>> boundTCPClients;
    private final Map<IPv4Address,Map<Integer,FakeTCPServer>> boundTCPServers;

    public FakeNetwork(AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
        mutex = new SpinMutex();
        boundTCPClients = new ListMap<>();
        boundTCPServers = new ListMap<>();
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }

    @Override
    public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort)
    {
        Result<TCPClient> result = NetworkBase.validateRemoteIPAddress(remoteIPAddress);
        if (result == null)
        {
            result = NetworkBase.validateRemotePort(remotePort);
            if (result == null)
            {
                try (final Disposable criticalSection = mutex.criticalSection())
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
                            final InMemoryByteStream clientToServer = new InMemoryByteStream(asyncRunner);
                            final InMemoryByteStream serverToClient = new InMemoryByteStream(asyncRunner);
                            final FakeTCPClient tcpClient = new FakeTCPClient(asyncRunner, clientLocalIPAddress, clientLocalPort, remoteIPAddress, remotePort, serverToClient, clientToServer);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Result<TCPServer> createTCPServer(int localPort)
    {
        return createTCPServer(IPv4Address.localhost, localPort);
    }

    @Override
    public Result<TCPServer> createTCPServer(IPv4Address localIPAddress, int localPort)
    {
        Result<TCPServer> result = NetworkBase.validateLocalIPAddress(localIPAddress);
        if (result == null)
        {
            result = NetworkBase.validateLocalPort(localPort);
            if (result == null)
            {
                try (final Disposable criticalSection = mutex.criticalSection())
                {
                    if (!isAvailable(localIPAddress, localPort))
                    {
                        result = Result.error(new IOException("IPAddress (" + localIPAddress + ") and port (" + localPort + ") are already bound."));
                    }
                    else
                    {
                        final FakeTCPServer tcpServer = new FakeTCPServer(asyncRunner);

                        Map<Integer, FakeTCPServer> localTCPServers = boundTCPServers.get(localIPAddress);
                        if (localTCPServers == null)
                        {
                            localTCPServers = new ListMap<>();
                            boundTCPServers.set(localIPAddress, localTCPServers);
                        }
                        localTCPServers.set(localPort, tcpServer);

                        result = Result.<TCPServer>success(tcpServer);
                    }
                }
            }
        }
        return result;
    }

    private boolean isAvailable(IPv4Address ipAddress, int port)
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
