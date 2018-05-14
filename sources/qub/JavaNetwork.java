package qub;

class JavaNetwork implements Network
{
    private final AsyncRunner asyncRunner;

    JavaNetwork(AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
    }

    @Override
    public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort)
    {
        return JavaTCPClient.create(remoteIPAddress, remotePort, getAsyncRunner());
    }

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
    public Result<TCPServer> createTCPServer(int localPort)
    {
        return JavaTCPServer.create(localPort, getAsyncRunner());
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
    public Result<TCPServer> createTCPServer(IPv4Address localIPAddress, int localPort)
    {
        return JavaTCPServer.create(localIPAddress, localPort, getAsyncRunner());
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

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }

    private static <T> AsyncFunction<Result<T>> async(AsyncRunner asyncRunner, Function0<Result<T>> function)
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
}
