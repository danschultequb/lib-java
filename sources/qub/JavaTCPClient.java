package qub;

class JavaTCPClient implements TCPClient
{
    private final java.net.Socket socket;
    private final ByteReadStream socketReadStream;
    private final ByteWriteStream socketWriteStream;

    private JavaTCPClient(java.net.Socket socket, ByteReadStream socketReadStream, ByteWriteStream socketWriteStream)
    {
        this.socket = socket;
        this.socketReadStream = socketReadStream;
        this.socketWriteStream = socketWriteStream;
    }

    static Result<TCPClient> create(java.net.Socket socket)
    {
        PreCondition.assertNotNull(socket, "socket");
        PreCondition.assertFalse(socket.isClosed(), "socket.isClosed()");

        Result<TCPClient> result;
        try
        {
            final ByteReadStream socketReadStream = new InputStreamToByteReadStream(socket.getInputStream());
            final ByteWriteStream socketWriteStream = new OutputStreamToByteWriteStream(socket.getOutputStream());
            result = Result.success(new JavaTCPClient(socket, socketReadStream, socketWriteStream));
        }
        catch (Throwable e)
        {
            result = Result.error(e);
        }
        return result;
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

    @Override
    public Result<Byte> readByte()
    {
        return this.socketReadStream.readByte();
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return this.socketReadStream.readBytes(outputBytes, startIndex, length);
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        return this.socketWriteStream.write(toWrite);
    }

    @Override
    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        return this.socketWriteStream.write(toWrite, startIndex, length);
    }
}
