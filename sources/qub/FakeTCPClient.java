package qub;

public class FakeTCPClient implements TCPClient
{
    private final FakeNetwork network;
    private final IPv4Address localIPAddress;
    private final int localPort;
    private final IPv4Address remoteIPAddress;
    private final int remotePort;
    private final ByteReadStream socketReadStream;
    private final ByteWriteStream socketWriteStream;
    private boolean disposed;

    FakeTCPClient(FakeNetwork network, IPv4Address localIPAddress, int localPort, IPv4Address remoteIPAddress, int remotePort, ByteReadStream socketReadStream, ByteWriteStream socketWriteStream)
    {
        this.network = network;
        this.localIPAddress = localIPAddress;
        this.localPort = localPort;
        this.remoteIPAddress = remoteIPAddress;
        this.remotePort = remotePort;
        this.socketReadStream = socketReadStream;
        this.socketWriteStream = socketWriteStream;
    }

    public ByteReadStream getReadStream()
    {
        return this.socketReadStream;
    }

    public ByteWriteStream getWriteStream()
    {
        return this.socketWriteStream;
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

            network.clientDisposed(this);
            result = Result.successTrue();
        }
        return result;
    }

    @Override
    public IPv4Address getLocalIPAddress()
    {
        return localIPAddress;
    }

    @Override
    public int getLocalPort()
    {
        return localPort;
    }

    @Override
    public IPv4Address getRemoteIPAddress()
    {
        return remoteIPAddress;
    }

    @Override
    public int getRemotePort()
    {
        return remotePort;
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
