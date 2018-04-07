package qub;

class JavaNetwork extends NetworkBase
{
    private final AsyncRunner asyncRunner;

    JavaNetwork(AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
    }

    @Override
    public AsyncFunction<Result<TCPClient>> createTCPClientAsync(IPv4Address remoteIPAddress, int remotePort)
    {
        return JavaTCPClient.createAsync(remoteIPAddress, remotePort, getAsyncRunner());
    }

    @Override
    public AsyncFunction<Result<TCPServer>> createTCPServerAsync(int localPort)
    {
        return JavaTCPServer.createAsync(localPort, getAsyncRunner());
    }

    @Override
    public AsyncFunction<Result<TCPServer>> createTCPServerAsync(IPv4Address localIPAddress, int localPort)
    {
        return JavaTCPServer.createAsync(localIPAddress, localPort, getAsyncRunner());
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }
}
