package qub;

public class BufferedByteReadStream implements ByteReadStream
{
    private final ByteReadStream byteReadStream;
    private final int maximumBufferSize;
    private byte[] buffer;
    private boolean growOnNextBufferFill;
    private int currentBufferIndex;
    private int bytesInBuffer;

    public BufferedByteReadStream(ByteReadStream byteReadStream)
    {
        this(byteReadStream, 10000, 100000);
    }

    public BufferedByteReadStream(ByteReadStream byteReadStream, int bufferSize)
    {
        this(byteReadStream, bufferSize, bufferSize);
    }

    public BufferedByteReadStream(ByteReadStream byteReadStream, int initialBufferSize, int maximumBufferSize)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertGreaterThanOrEqualTo(initialBufferSize, 1, "initialBufferSize");
        PreCondition.assertGreaterThanOrEqualTo(maximumBufferSize, initialBufferSize, "maximumBufferSize");

        this.byteReadStream = byteReadStream;
        this.maximumBufferSize = maximumBufferSize;
        this.buffer = byteReadStream.isDisposed() ? null : new byte[initialBufferSize];
        this.currentBufferIndex = -1;
        this.growOnNextBufferFill = false;
    }

    @Override
    public Result<Byte> readByte()
    {
        PreCondition.assertNotDisposed(this);

        Result<Byte> result;
        if (currentBufferIndex < 0 || currentBufferIndex == bytesInBuffer - 1)
        {
            if (growOnNextBufferFill && buffer.length < maximumBufferSize)
            {
                buffer = new byte[Math.minimum(maximumBufferSize, buffer.length * 2)];
            }

            result = byteReadStream.readBytes(buffer)
                .then((Integer bytesRead) ->
                {
                    bytesInBuffer = bytesRead;
                    growOnNextBufferFill = (buffer.length == bytesRead);
                    currentBufferIndex = 0;
                    return buffer[currentBufferIndex];
                })
                .onError(EndOfStreamException.class, () ->
                {
                    buffer = null;
                    growOnNextBufferFill = false;
                    bytesInBuffer = 0;
                    currentBufferIndex = -1;
                });
        }
        else
        {
            result = Result.success(buffer[++currentBufferIndex]);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertStartIndex(startIndex, outputBytes.length);
        PreCondition.assertLength(length, startIndex, outputBytes.length);
        PreCondition.assertNotDisposed(this);

        Result<Integer> result;

        if (currentBufferIndex < 0 || currentBufferIndex == bytesInBuffer - 1)
        {
            if (growOnNextBufferFill && buffer.length < maximumBufferSize)
            {
                buffer = new byte[Math.minimum(maximumBufferSize, buffer.length * 2)];
            }

            result = byteReadStream.readBytes(buffer)
                .then((Integer bytesRead) ->
                {
                    bytesInBuffer = bytesRead;
                    growOnNextBufferFill = (buffer.length == bytesRead);

                    final int bytesToCopy = Math.minimum(bytesRead, length);
                    Array.copy(buffer, 0, outputBytes, startIndex, bytesToCopy);
                    currentBufferIndex = bytesToCopy - 1;

                    return bytesToCopy;
                })
                .onError(EndOfStreamException.class, () ->
                {
                    buffer = null;
                    growOnNextBufferFill = false;
                    bytesInBuffer = 0;
                    currentBufferIndex = -1;
                });
        }
        else
        {
            final int bytesToCopy = Math.minimum(length, bytesInBuffer - (currentBufferIndex + 1));
            Array.copy(buffer, currentBufferIndex + 1, outputBytes, startIndex, bytesToCopy);
            currentBufferIndex += bytesToCopy;

            result = Result.success(bytesToCopy);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
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

    /**
     * Get the number of bytes that are in the buffer.
     * @return The number of bytes that are in the buffer.
     */
    public int getBufferSize()
    {
        return buffer == null ? 0 : buffer.length;
    }

    /**
     * Get the maximum byte count that the buffer can grow to.
     * @return The maximum byte count that the buffer can grow to.
     */
    public int getMaximumBufferSize()
    {
        return maximumBufferSize;
    }

    /**
     * Get the number of unread bytes in the buffer.
     * @return The number of unread bytes in the buffer.
     */
    public int getBufferedByteCount()
    {
        return currentBufferIndex < 0 ? 0 : bytesInBuffer - currentBufferIndex;
    }

    /**
     * Get whether or not the buffer will be expanded on the next buffer fill.
     * @return Get whether or not the buffer will be expanded on the next buffer fill.
     */
    public boolean getGrowOnNextBufferFill()
    {
        return growOnNextBufferFill;
    }
}
