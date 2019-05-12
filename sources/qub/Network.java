package qub;

public interface Network
{
    Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort);

    Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, Duration timeout);

    Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, DateTime timeout);

    Result<TCPServer> createTCPServer(int localPort);

    Result<TCPServer> createTCPServer(IPv4Address localIPAddress, int localPort);

    HttpClient getHttpClient();

    /**
     * Get whether or not this device is connected to a network.
     * @return Whether or not this device is connected to a network.
     */
    Result<Boolean> isConnected();

    DNS getDNS();

    static void validateIPAddress(IPv4Address ipAddress, String parameterName)
    {
        PreCondition.assertNotNull(ipAddress, parameterName);
    }

    static void validateLocalIPAddress(IPv4Address localIPAddress)
    {
        Network.validateIPAddress(localIPAddress, "localIPAddress");
    }

    static void validateRemoteIPAddress(IPv4Address remoteIPAddress)
    {
        Network.validateIPAddress(remoteIPAddress, "remoteIPAddress");
    }

    static void validatePort(int port, String parameterName)
    {
        PreCondition.assertBetween(1, port, 65535, parameterName);
    }

    static void validateLocalPort(int localPort)
    {
        Network.validatePort(localPort, "localPort");
    }

    static void validateRemotePort(int remotePort)
    {
        Network.validatePort(remotePort, "remotePort");
    }

    static void validateTimeout(Duration timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
    }

    static void validateTimeout(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
    }
}
