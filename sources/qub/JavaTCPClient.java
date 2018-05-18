package qub;

class JavaTCPClient extends TCPClientBase
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
        Result<TCPClient> result = Result.notNull(socket, "socket");
        if (result == null)
        {
            result = DisposableBase.validateNotDisposed(asyncRunner, "asyncRunner");
            if (result == null)
            {
                try
                {
                    final ByteReadStream socketReadStream = new InputStreamToByteReadStream(socket.getInputStream(), asyncRunner);
                    final ByteWriteStream socketWriteStream = new OutputStreamToByteWriteStream(socket.getOutputStream());
                    result = Result.<TCPClient>success(new JavaTCPClient(socket, asyncRunner, socketReadStream, socketWriteStream));
                }
                catch (java.io.IOException e)
                {
                    result = Result.error(e);
                }
            }
        }
        return result;
    }

    @Override
    protected ByteReadStream getReadStream()
    {
        return socketReadStream;
    }

    @Override
    protected ByteWriteStream getWriteStream()
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
}
