package qub;

class JavaTCPClient implements TCPClient
{
    private final java.net.Socket socket;
    private final AsyncRunner asyncRunner;
    private final ByteReadStream socketReadStream;
    private final ByteWriteStream socketWriteStream;

    private JavaTCPClient(java.net.Socket socket, AsyncRunner asyncRunner, ByteReadStream socketReadStream, ByteWriteStream socketWriteStream)
    {
        this.socket = socket;
        this.asyncRunner = asyncRunner;
        this.socketReadStream = socketReadStream;
        this.socketWriteStream = socketWriteStream;
    }

    static Result<TCPClient> create(java.net.Socket socket, AsyncRunner asyncRunner)
    {
        PreCondition.assertNotNull(socket, "socket");
        PreCondition.assertFalse(socket.isClosed(), "socket.isClosed()");
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");
        PreCondition.assertFalse(asyncRunner.isDisposed(), "asyncRunner.isDisposed()");

        Result<TCPClient> result;
        try
        {
            final ByteReadStream socketReadStream = new InputStreamToByteReadStream(socket.getInputStream(), asyncRunner);
            final ByteWriteStream socketWriteStream = new OutputStreamToByteWriteStream(socket.getOutputStream());
            result = Result.success(new JavaTCPClient(socket, asyncRunner, socketReadStream, socketWriteStream));
        }
        catch (Throwable e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public ByteReadStream getReadStream()
    {
        return socketReadStream;
    }

    @Override
    public ByteWriteStream getWriteStream()
    {
        return socketWriteStream;
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }

    @Override
    public boolean isDisposed()
    {
        return socket.isClosed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (isDisposed())
        {
            result = Result.successFalse();
        }
        else
        {
            try
            {
                socket.close();
                result = Result.successTrue();
            }
            catch (java.io.IOException e)
            {
                result = Result.error(e);
            }
        }
        return result;
    }

    @Override
    public IPv4Address getLocalIPAddress()
    {
        String localInetAddressString = socket.getLocalAddress().toString();
        if (localInetAddressString.startsWith("/")) {
            localInetAddressString = localInetAddressString.substring(1);
        }
        return IPv4Address.parse(localInetAddressString);
    }

    @Override
    public int getLocalPort()
    {
        return socket.getLocalPort();
    }

    @Override
    public IPv4Address getRemoteIPAddress()
    {
        final java.net.InetSocketAddress remoteInetSocketAddress = (java.net.InetSocketAddress)socket.getRemoteSocketAddress();
        String remoteInetAddressString = remoteInetSocketAddress.getAddress().toString();
        if (remoteInetAddressString.startsWith("/")) {
            remoteInetAddressString = remoteInetAddressString.substring(1);
        }
        return IPv4Address.parse(remoteInetAddressString);
    }

    @Override
    public int getRemotePort()
    {
        final java.net.InetSocketAddress remoteInetSocketAddress = (java.net.InetSocketAddress)socket.getRemoteSocketAddress();
        return remoteInetSocketAddress.getPort();
    }
}
