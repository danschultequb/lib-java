package qub;

public abstract class NetworkBase implements Network
{
    @Override
    public AsyncFunction<Result<TCPClient>> createTCPClientAsync(final IPv4Address remoteIPAddress, final int remotePort)
    {
        return async(getAsyncRunner(), new Function0<Result<TCPClient>>()
        {
            @Override
            public Result<TCPClient> run()
            {
                return createTCPClient(remoteIPAddress, remotePort);
            }
        });
    }
    
    @Override
    public AsyncFunction<Result<TCPServer>> createTCPServerAsync(final int localPort)
    {
        return async(getAsyncRunner(), new Function0<Result<TCPServer>>()
        {
            @Override
            public Result<TCPServer> run()
            {
                return createTCPServer(localPort);
            }
        });
    }

    @Override
    public AsyncFunction<Result<TCPServer>> createTCPServerAsync(final IPv4Address localIPAddress, final int localPort)
    {
        return async(getAsyncRunner(), new Function0<Result<TCPServer>>()
        {
            @Override
            public Result<TCPServer> run()
            {
                return createTCPServer(localIPAddress, localPort);
            }
        });
    }

    public static <T> AsyncFunction<Result<T>> async(AsyncRunner asyncRunner, Function0<Result<T>> function)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<T>> result = currentAsyncRunner.notNull(asyncRunner, "asyncRunner");
        if (result == null)
        {
            result = currentAsyncRunner.equal(false, asyncRunner.isDisposed(), "asyncRunner.isDisposed()");
            if (result == null)
            {
                result = asyncRunner.schedule(function).thenOn(currentAsyncRunner);
            }
        }

        return result;
    }
    
    public static <T> Result<T> validateIPAddress(IPv4Address ipAddress, String parameterName)
    {
        return Result.<T>notNull(ipAddress, parameterName);
    }

    public static <T> Result<T> validateLocalIPAddress(IPv4Address localIPAddress)
    {
        return NetworkBase.validateIPAddress(localIPAddress, "localIPAddress");
    }

    public static <T> Result<T> validateRemoteIPAddress(IPv4Address remoteIPAddress)
    {
        return NetworkBase.validateIPAddress(remoteIPAddress, "remoteIPAddress");
    }

    public static <T> Result<T> validatePort(int port, String parameterName)
    {
        return Result.<T>between(1, port, 65535, parameterName);
    }

    public static <T> Result<T> validateLocalPort(int localPort)
    {
        return NetworkBase.validatePort(localPort, "localPort");
    }

    public static <T> Result<T> validateRemotePort(int remotePort)
    {
        return NetworkBase.validatePort(remotePort, "remotePort");
    }

    public static <T> Result<T> validateReadStream(ByteReadStream readStream)
    {
        return DisposableBase.validateNotDisposed(readStream, "readStream");
    }

    public static <T> Result<T> validateWriteStream(ByteWriteStream writeStream)
    {
        return DisposableBase.validateNotDisposed(writeStream, "writeStream");
    }

    public static <T> Result<T> validateAsyncRunner(AsyncRunner asyncRunner)
    {
        return DisposableBase.validateNotDisposed(asyncRunner, "asyncRunner");
    }
}
