package qub;

public class BufferedByteReadStream implements ByteReadStream
{
    public static final int defaultInitialBufferSize = 10;
    private final ByteReadStream byteReadStream;
    private byte[] buffer;
    private boolean growOnNextBufferFill;
    private int currentBufferIndex;
    private int bytesInBuffer;
    private boolean hasStarted;

    public BufferedByteReadStream(ByteReadStream byteReadStream)
    {
        this(byteReadStream, defaultInitialBufferSize);
    }

    public BufferedByteReadStream(ByteReadStream byteReadStream, int initialBufferSize)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertLessThanOrEqualTo(1, initialBufferSize, "initialBufferSize");

        this.byteReadStream = byteReadStream;
        this.buffer = byteReadStream.isDisposed() ? null : new byte[initialBufferSize];
        this.currentBufferIndex = -1;
        this.growOnNextBufferFill = false;
    }

    @Override
    public Result<Byte> readByte()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        hasStarted = true;

        Result<Byte> result = null;
        if (currentBufferIndex < 0 || currentBufferIndex == bytesInBuffer - 1)
        {
            if (growOnNextBufferFill)
            {
                final int newBufferSize = (buffer.length * 2) + 1;
                buffer = new byte[newBufferSize];
            }

            final Result<Integer> bytesRead = byteReadStream.readBytes(buffer);
            if (bytesRead.hasError())
            {
                if (bytesRead.getError() instanceof EndOfStreamException)
                {
                    buffer = null;
                    growOnNextBufferFill = false;
                    bytesInBuffer = 0;
                    currentBufferIndex = -1;
                }
                result = bytesRead.convertError();
            }
            else
            {
                bytesInBuffer = bytesRead.getValue();
                growOnNextBufferFill = (bytesInBuffer == buffer.length);
                currentBufferIndex = -1;
            }
        }

        if (result == null)
        {
            final byte byteRead = buffer[++currentBufferIndex];
            result = Result.success(byteRead);
        }

        return result;
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return byteReadStream.getAsyncRunner();
    }

    @Override
    public boolean isDisposed()
    {
        return byteReadStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        buffer = null;
        growOnNextBufferFill = false;
        currentBufferIndex = -1;
        bytesInBuffer = 0;
        return byteReadStream.dispose();
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return 0 <= currentBufferIndex;
    }

    @Override
    public Byte getCurrent()
    {
        return hasCurrent() ? buffer[currentBufferIndex] : null;
    }

    int getBufferCapacity()
    {
        return buffer == null ? 0 : buffer.length;
    }

    int getBufferedByteCount()
    {
        return currentBufferIndex < 0 ? 0 : bytesInBuffer - currentBufferIndex;
    }

    boolean getGrowOnNextBufferFill()
    {
        return growOnNextBufferFill;
    }
}
