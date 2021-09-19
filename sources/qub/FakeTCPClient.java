package qub;

public class FakeTCPClient implements TCPClient
{
    private final FakeNetwork network;
    private final IPv4Address localIPAddress;
    private final int localPort;
    private final IPv4Address remoteIPAddress;
    private final int remotePort;
    private final InMemoryByteStream socketReadStream;
    private final InMemoryByteStream socketWriteStream;
    private boolean disposed;

    private FakeTCPClient(FakeNetwork network, IPv4Address localIPAddress, int localPort, IPv4Address remoteIPAddress, int remotePort, InMemoryByteStream socketReadStream, InMemoryByteStream socketWriteStream)
    {
        this.network = network;
        this.localIPAddress = localIPAddress;
        this.localPort = localPort;
        this.remoteIPAddress = remoteIPAddress;
        this.remotePort = remotePort;
        this.socketReadStream = socketReadStream;
        this.socketWriteStream = socketWriteStream;
    }

    public static FakeTCPClient create(FakeNetwork network, IPv4Address localIPAddress, int localPort, IPv4Address remoteIPAddress, int remotePort, InMemoryByteStream socketReadStream, InMemoryByteStream socketWriteStream)
    {
        PreCondition.assertNotNull(network, "network");
        Network.validateLocalIPAddress(localIPAddress);
        Network.validateLocalPort(localPort);
        Network.validateRemoteIPAddress(remoteIPAddress);
        Network.validateRemotePort(remotePort);
        PreCondition.assertNotNull(socketReadStream, "socketReadStream");
        PreCondition.assertNotDisposed(socketReadStream, "socketReadStream");
        PreCondition.assertNotNull(socketWriteStream, "socketWriteStream");
        PreCondition.assertNotDisposed(socketWriteStream, "socketWriteStream");

        return new FakeTCPClient(network, localIPAddress, localPort, remoteIPAddress, remotePort, socketReadStream, socketWriteStream);
    }

    @Override
    public boolean isDisposed()
    {
        return this.disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            boolean result = false;
            if (!this.disposed)
            {
                this.disposed = true;
                this.network.clientDisposed(this.localIPAddress, this.localPort, this.socketReadStream, this.socketWriteStream);

                result = true;
            }
            return result;
        });
    }

    @Override
    public IPv4Address getLocalIPAddress()
    {
        return this.localIPAddress;
    }

    @Override
    public int getLocalPort()
    {
        return this.localPort;
    }

    @Override
    public IPv4Address getRemoteIPAddress()
    {
        return this.remoteIPAddress;
    }

    @Override
    public int getRemotePort()
    {
        return this.remotePort;
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
