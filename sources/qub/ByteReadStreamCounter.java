package qub;

/**
 * A ByteReadStream decorator that counts the number of bytes that are read.
 */
public class ByteReadStreamCounter implements ByteReadStream
{
    private final ByteReadStream innerStream;
    private long bytesRead;

    public ByteReadStreamCounter(ByteReadStream innerStream)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        this.innerStream = innerStream;
    }

    /**
     * Get the number of bytes that have been read.
     * @return The number of bytes that have been read.
     */
    public long getBytesRead()
    {
        return bytesRead;
    }

    /**
     * Get the number of bits that have been read.
     * @return The number of bits that have been read.
     */
    public long getBitsRead()
    {
        return bytesRead * Bytes.bitCount;
    }

    @Override
    public Result<Byte> readByte()
    {
        PreCondition.assertNotDisposed(this);

        return innerStream.readByte()
            .onValue((Byte value) ->
            {
                 ++bytesRead;
            });
    }

    @Override
    public Result<byte[]> readBytes(int bytesToRead)
    {
        PreCondition.assertGreaterThanOrEqualTo(bytesToRead, 1, "bytesToRead");
        PreCondition.assertNotDisposed(this);

        return innerStream.readBytes(bytesToRead)
            .onValue((byte[] values) ->
            {
                bytesRead += values.length;
            });
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes)
    {
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertNotDisposed(this);

        return innerStream.readBytes(outputBytes)
            .onValue((Integer bytesRead) ->
            {
                this.bytesRead += bytesRead;
            });
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertStartIndex(startIndex, outputBytes.length);
        PreCondition.assertLength(length, startIndex, outputBytes.length);
        PreCondition.assertNotDisposed(this);

        return innerStream.readBytes(outputBytes, startIndex, length)
            .onValue((Integer bytesRead) ->
            {
                this.bytesRead += bytesRead;
            });
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return innerStream.getAsyncRunner();
    }

    @Override
    public boolean isDisposed()
    {
        return innerStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return innerStream.dispose();
    }

    @Override
    public boolean hasStarted()
    {
        return innerStream.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return innerStream.hasCurrent();
    }

    @Override
    public Byte getCurrent()
    {
        return innerStream.getCurrent();
    }
}
