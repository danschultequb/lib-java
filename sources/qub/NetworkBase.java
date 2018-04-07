package qub;

public abstract class NetworkBase implements Network
{
    @Override
    final public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort)
    {
        return createTCPClientAsync(remoteIPAddress, remotePort).awaitReturn();
    }

    @Override
    final public Result<TCPServer> createTCPServer(int localPort)
    {
        return createTCPServerAsync(localPort).awaitReturn();
    }

    @Override
    final public Result<TCPServer> createTCPServer(IPv4Address localIPAddress, int localPort)
    {
        return createTCPServerAsync(localIPAddress, localPort).awaitReturn();
    }
}
