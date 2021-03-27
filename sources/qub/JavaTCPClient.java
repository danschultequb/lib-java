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

        return Result.create(() ->
        {
            TCPClient result;
            try
            {
                final ByteReadStream socketReadStream = new InputStreamToByteReadStream(socket.getInputStream());
                final ByteWriteStream socketWriteStream = OutputStreamToByteWriteStream.create(socket.getOutputStream());
                result = new JavaTCPClient(socket, socketReadStream, socketWriteStream);
            }
            catch (Throwable e)
            {
                throw Exceptions.asRuntime(e);
            }
            return result;
        });
    }

    @Override
    public boolean isDisposed()
    {
        return this.socket.isClosed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            final boolean result = !this.isDisposed();
            if (result)
            {
                try
                {
                    this.socket.close();
                }
                catch (java.io.IOException e)
                {
                    throw Exceptions.asRuntime(e);
                }
            }
            return result;
        });
    }

    @Override
    public IPv4Address getLocalIPAddress()
    {
        String localInetAddressString = this.socket.getLocalAddress().toString();
        if (localInetAddressString.startsWith("/")) {
            localInetAddressString = localInetAddressString.substring(1);
        }
        return IPv4Address.parse(localInetAddressString).await();
    }

    @Override
    public int getLocalPort()
    {
        return this.socket.getLocalPort();
    }

    @Override
    public IPv4Address getRemoteIPAddress()
    {
        final java.net.InetSocketAddress remoteInetSocketAddress = (java.net.InetSocketAddress)this.socket.getRemoteSocketAddress();
        String remoteInetAddressString = remoteInetSocketAddress.getAddress().toString();
        if (remoteInetAddressString.startsWith("/")) {
            remoteInetAddressString = remoteInetAddressString.substring(1);
        }
        return IPv4Address.parse(remoteInetAddressString).await();
    }

    @Override
    public int getRemotePort()
    {
        final java.net.InetSocketAddress remoteInetSocketAddress = (java.net.InetSocketAddress)this.socket.getRemoteSocketAddress();
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
