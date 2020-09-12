package qub;

public interface Network
{
    Result<? extends TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort);

    Result<? extends TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, Duration2 timeout);

    Result<? extends TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, DateTime timeout);

    Result<? extends TCPServer> createTCPServer(int localPort);

    Result<? extends TCPServer> createTCPServer(IPv4Address localIPAddress, int localPort);

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

    static void validateTimeout(Duration2 timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertGreaterThan(timeout, Duration2.zero, "timeout");
    }

    static void validateTimeout(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
    }
}
