package qub;

/**
 * A ByteReadStream decorator that counts the number of bytes that are read.
 */
public class ByteReadStreamCounter extends ByteReadStreamBase
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
        return bytesRead * Byte.SIZE;
    }

    @Override
    public Result<Byte> readByte()
    {
        final Result<Byte> result = innerStream.readByte();
        if (result.getValue() != null)
        {
            ++bytesRead;
        }
        return result;
    }

    @Override
    public Result<byte[]> readBytes(int bytesToRead)
    {
        final Result<byte[]> result = innerStream.readBytes(bytesToRead);
        if (result.getValue() != null)
        {
            bytesRead += result.getValue().length;
        }
        return result;
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes)
    {
        final Result<Integer> result = innerStream.readBytes(outputBytes);
        if (result.getValue() != null)
        {
            bytesRead += result.getValue();
        }
        return result;
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        final Result<Integer> result = innerStream.readBytes(outputBytes, startIndex, length);
        if (result.getValue() != null)
        {
            bytesRead += result.getValue();
        }
        return result;
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
