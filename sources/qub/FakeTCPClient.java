package qub;

public class FakeTCPClient extends TCPClientBase
{
    private final AsyncRunner asyncRunner;
    private final IPv4Address localIPAddress;
    private final int localPort;
    private final IPv4Address remoteIPAddress;
    private final int remotePort;
    private final ByteReadStream socketReadStream;
    private final ByteWriteStream socketWriteStream;
    private boolean disposed;

    private FakeTCPClient(AsyncRunner asyncRunner, IPv4Address localIPAddress, int localPort, IPv4Address remoteIPAddress, int remotePort, ByteReadStream socketReadStream, ByteWriteStream socketWriteStream)
    {
        this.asyncRunner = asyncRunner;
        this.localIPAddress = localIPAddress;
        this.localPort = localPort;
        this.remoteIPAddress = remoteIPAddress;
        this.remotePort = remotePort;
        this.socketReadStream = socketReadStream;
        this.socketWriteStream = socketWriteStream;
    }

    static Result<TCPClient> create(IPv4Address localIPAddress, int localPort, IPv4Address remoteIPAddress, int remotePort, ByteReadStream readStream, ByteWriteStream writeStream, AsyncRunner asyncRunner)
    {
        Result<TCPClient> result = FakeTCPClient.validateLocalIPAddress(localIPAddress);
        if (result == null)
        {
            result = TCPClientBase.validateLocalPort(localPort);
            if (result == null)
            {
                result = TCPClientBase.validateRemoteIPAddress(remoteIPAddress);
                if (result == null)
                {
                    result = TCPClientBase.validateRemotePort(remotePort);
                    if (result == null)
                    {
                        result = TCPClientBase.validateReadStream(readStream);
                        if (result == null)
                        {
                            result = TCPClientBase.validateWriteStream(writeStream);
                            if (result == null)
                            {
                                result = TCPClientBase.validateAsyncRunner(asyncRunner);
                                {
                                    result = Result.<TCPClient>success(new FakeTCPClient(asyncRunner, localIPAddress, localPort, remoteIPAddress, remotePort, readStream, writeStream));
                                }
                            }
                        }
                    }
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
        return disposed;
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
            disposed = true;

            final List<Throwable> errors = new ArrayList<Throwable>();

            result = socketReadStream.dispose();
            if (result.hasError())
            {
                errors.add(result.getError());
            }

            result = socketWriteStream.dispose();
            if (result.hasError())
            {
                errors.add(result.getError());
            }

            if (errors.any())
            {
                result = Result.error(ErrorIterable.from(errors));
            }
            else
            {
                result = Result.successTrue();
            }
        }
        return result;
    }
}
