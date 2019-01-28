package qub;

class JavaNetwork implements Network
{
    private final AsyncRunner asyncRunner;
    private final JavaHttpClient httpClient;
    private final DNS dns;

    JavaNetwork(AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
        httpClient = new JavaHttpClient(this);
        dns = new JavaDNS();
    }

    @Override
    public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort)
    {
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);

        Result<TCPClient> result;
        try
        {
            final byte[] remoteIPAddressBytes = remoteIPAddress.toBytes();
            final java.net.InetAddress remoteInetAddress = java.net.InetAddress.getByAddress(remoteIPAddressBytes);
            final java.net.Socket socket = new java.net.Socket(remoteInetAddress, remotePort);
            result = JavaTCPClient.create(socket, asyncRunner);
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort, DateTime timeout)
    {
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);
        Network.validateTimeout(timeout);
        validateClock();

        Result<TCPClient> result;
        try
        {
            final byte[] remoteIPAddressBytes = remoteIPAddress.toBytes();
            final java.net.InetAddress remoteInetAddress = java.net.InetAddress.getByAddress(remoteIPAddressBytes);
            final java.net.SocketAddress socketAddress = new java.net.InetSocketAddress(remoteInetAddress, remotePort);
            final java.net.Socket socket = new java.net.Socket();
            socket.connect(socketAddress);

            result = JavaTCPClient.create(socket, asyncRunner);
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public Result<TCPServer> createTCPServer(int localPort)
    {
        Network.validateLocalPort(localPort);

        return JavaTCPServer.create(localPort, getAsyncRunner());
    }

    @Override
    public Result<TCPServer> createTCPServer(IPv4Address localIPAddress, int localPort)
    {
        Network.validateLocalIPAddress(localIPAddress);
        Network.validateLocalPort(localPort);

        return JavaTCPServer.create(localIPAddress, localPort, getAsyncRunner());
    }

    @Override
    public HttpClient getHttpClient()
    {
        return httpClient;
    }

    @Override
    public Result<Boolean> isConnected()
    {
        final DNS dns = getDNS();
        return dns.resolveHost("www.google.com")
            .thenResult(Result::successTrue)
            .catchErrorResult(Result::successFalse);
    }

    @Override
    public DNS getDNS()
    {
        return dns;
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }
}
