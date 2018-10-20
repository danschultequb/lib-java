package qub;

public interface Network
{
    AsyncRunner getAsyncRunner();

    Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort);

    default AsyncFunction<Result<TCPClient>> createTCPClientAsync(IPv4Address remoteIPAddress, int remotePort)
    {
        return getAsyncRunner().scheduleSingle(() -> createTCPClient(remoteIPAddress, remotePort));
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

    static <T> Result<T> validateIPAddress(IPv4Address ipAddress, String parameterName)
    {
        return Result.<T>notNull(ipAddress, parameterName);
    }

    static <T> Result<T> validateLocalIPAddress(IPv4Address localIPAddress)
    {
        return Network.validateIPAddress(localIPAddress, "localIPAddress");
    }

    static <T> Result<T> validateRemoteIPAddress(IPv4Address remoteIPAddress)
    {
        return Network.validateIPAddress(remoteIPAddress, "remoteIPAddress");
    }

    static <T> Result<T> validatePort(int port, String parameterName)
    {
        return Result.<T>between(1, port, 65535, parameterName);
    }

    static <T> Result<T> validateLocalPort(int localPort)
    {
        return Network.validatePort(localPort, "localPort");
    }

    static <T> Result<T> validateRemotePort(int remotePort)
    {
        return Network.validatePort(remotePort, "remotePort");
    }

    static <T> Result<T> validateReadStream(ByteReadStream readStream)
    {
        return Disposable.validateNotDisposed(readStream, "readStream");
    }

    static <T> Result<T> validateWriteStream(ByteWriteStream writeStream)
    {
        return Disposable.validateNotDisposed(writeStream, "writeStream");
    }

    static <T> Result<T> validateAsyncRunner(AsyncRunner asyncRunner)
    {
        return Disposable.validateNotDisposed(asyncRunner, "asyncRunner");
    }
}
