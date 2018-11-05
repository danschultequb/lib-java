package qub;

public interface Network
{
    AsyncRunner getAsyncRunner();

    Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort);

    default AsyncFunction<Result<TCPClient>> createTCPClientAsync(IPv4Address remoteIPAddress, int remotePort)
    {
        validateRemoteIPAddress(remoteIPAddress);
        validateRemotePort(remotePort);
        validateAsyncRunner();

        return getAsyncRunner().scheduleSingle(() -> createTCPClient(remoteIPAddress, remotePort));
    }

    default Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, Duration timeout)
    {
        validateRemoteIPAddress(remoteIPAddress);
        validateRemotePort(remotePort);
        validateTimeout(timeout);
        validateClock();

        return createTCPClient(remoteIPAddress, remotePort, getAsyncRunner().getClock().getCurrentDateTime().plus(timeout));
    }

    default AsyncFunction<Result<TCPClient>> createTCPClientAsync(IPv4Address remoteIPAddress, int remotePort, Duration timeout)
    {
        validateRemoteIPAddress(remoteIPAddress);
        validateRemotePort(remotePort);
        validateTimeout(timeout);
        validateAsyncRunner();
        validateClock();

        return getAsyncRunner().scheduleSingle(() -> createTCPClient(remoteIPAddress, remotePort, timeout));
    }

    Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, DateTime timeout);

    default AsyncFunction<Result<TCPClient>> createTCPClientAsync(IPv4Address remoteIPAddress, int remotePort, DateTime timeout)
    {
        validateRemoteIPAddress(remoteIPAddress);
        validateRemotePort(remotePort);
        validateTimeout(timeout);
        validateAsyncRunner();
        validateClock();

        return getAsyncRunner().scheduleSingle(() -> createTCPClient(remoteIPAddress, remotePort, timeout));
    }

    Result<TCPServer> createTCPServer(int localPort);

    default AsyncFunction<Result<TCPServer>> createTCPServerAsync(int localPort)
    {
        return getAsyncRunner().scheduleSingle(() -> createTCPServer(localPort));
    }

    Result<TCPServer> createTCPServer(IPv4Address localIPAddress, int localPort);

    default AsyncFunction<Result<TCPServer>> createTCPServerAsync(IPv4Address localIPAddress, int localPort)
    {
        return getAsyncRunner().scheduleSingle(() -> createTCPServer(localIPAddress, localPort));
    }

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
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
    }

    static void validateTimeout(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
    }

    default void validateAsyncRunner()
    {
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");
        PreCondition.assertFalse(getAsyncRunner().isDisposed(), "getAsyncRunner().isDisposed()");
    }

    default void validateClock()
    {
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");
        PreCondition.assertNotNull(getAsyncRunner().getClock(), "getAsyncRunner().getClock()");
    }
}
