package qub;

public abstract class NetworkBase implements Network
{
    @Override
    final public AsyncFunction<TCPClient> createTCPClientAsync(final IPv4Address remoteIPAddress, final int remotePort)
    {
        return async(this, new Function0<TCPClient>()
        {
            @Override
            public TCPClient run()
            {
                return createTCPClient(remoteIPAddress, remotePort).getValue();
            }
        });
    }

    @Override
    final public AsyncFunction<TCPServer> createTCPServerAsync(final int localPort)
    {
        return async(this, new Function0<TCPServer>()
        {
            @Override
            public TCPServer run()
            {
                return createTCPServer(localPort).getValue();
            }
        });
    }

    @Override
    final public AsyncFunction<TCPServer> createTCPServerAsync(final IPv4Address localIPAddress, final int localPort)
    {
        return async(this, new Function0<TCPServer>()
        {
            @Override
            public TCPServer run()
            {
                return createTCPServer(localIPAddress, localPort).getValue();
            }
        });
    }

    private static <T> AsyncFunction<T> async(NetworkBase network, final Function0<T> function)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return network.getAsyncRunner()
            .schedule(function)
            .thenOn(currentRunner);
    }
}
