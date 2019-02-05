package qub;

public class BufferedByteWriteStream implements ByteWriteStream
{
    public static final int defaultInitialBufferSize = 10;
    private final ByteWriteStream byteWriteStream;
    private byte[] buffer;
    private boolean growOnNextBufferFill;
    private int currentBufferIndex;
    private int bytesInBuffer;

    public BufferedByteWriteStream(ByteWriteStream byteWriteStream)
    {
        this(byteWriteStream, defaultInitialBufferSize);
    }

    public BufferedByteWriteStream(ByteWriteStream byteWriteStream, int initialBufferSize)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertGreaterThanOrEqualTo(initialBufferSize, 1, "initialBufferSize");

        this.byteWriteStream = byteWriteStream;
        this.buffer = byteWriteStream.isDisposed() ? null : new byte[initialBufferSize];
        this.currentBufferIndex = -1;
        this.growOnNextBufferFill = false;
    }

    @Override
    public Result<Integer> writeBytes(byte[] toWrite, int startIndex, int length)
    {
        return null;
    }

    @Override
    public boolean isDisposed()
    {
        return byteWriteStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (currentBufferIndex <= 0)
        {
            result = Result.success();
        }
        else
        {
            result = byteWriteStream.writeAllBytes(buffer, 0, currentBufferIndex);
        }
        buffer = null;
        growOnNextBufferFill = false;
        currentBufferIndex = -1;
        bytesInBuffer = 0;
        return result.thenResult(byteWriteStream::dispose);
    }
}
