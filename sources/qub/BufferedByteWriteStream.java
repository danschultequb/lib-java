package qub;

public class BufferedByteWriteStream implements ByteWriteStream
{
    private final ByteWriteStream byteWriteStream;
    private final int maximumBufferSize;
    private byte[] buffer;
    private int currentBufferIndex;

    private BufferedByteWriteStream(ByteWriteStream byteWriteStream, int initialBufferSize, int maximumBufferSize)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertGreaterThanOrEqualTo(initialBufferSize, 1, "initialBufferSize");
        PreCondition.assertGreaterThanOrEqualTo(maximumBufferSize, initialBufferSize, "maximumBufferSize");

        this.byteWriteStream = byteWriteStream;
        this.maximumBufferSize = maximumBufferSize;
        this.buffer = byteWriteStream.isDisposed() ? null : new byte[initialBufferSize];
    }

    public static BufferedByteWriteStream create(ByteWriteStream byteWriteStream)
    {
        return BufferedByteWriteStream.create(byteWriteStream, 10000, 100000);
    }

    public static BufferedByteWriteStream create(ByteWriteStream byteWriteStream, int bufferSize)
    {
        return BufferedByteWriteStream.create(byteWriteStream, bufferSize, bufferSize);
    }

    public static BufferedByteWriteStream create(ByteWriteStream byteWriteStream, int initialBufferSize, int maximumBufferSize)
    {
        return new BufferedByteWriteStream(byteWriteStream, initialBufferSize, maximumBufferSize);
    }

    /**
     * Get the number of bytes that can be stored in the buffer. This number can change as the
     * buffer expands after calls to writeBytes().
     * @return The number of bytes that can be stored in the buffer.
     */
    public int getBufferCapacity()
    {
        return buffer == null ? 0 : buffer.length;
    }

    /**
     * Get the maximum number of bytes that the buffer will grow to.
     * @return The maximum number of bytes that the buffer will grow to.
     */
    public int getMaximumBufferSize()
    {
        return maximumBufferSize;
    }

    /**
     * Get the number of bytes currently in the buffer waiting to be written to the inner stream.
     * @return The number of bytes currently in the buffer waiting to be written to the inner
     * stream.
     */
    public int getBufferByteCount()
    {
        return currentBufferIndex;
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        PreCondition.assertFalse(this.isDisposed(), "this");

        return Result.create(() ->
        {
            buffer[currentBufferIndex++] = toWrite;
            flushBufferIfFull().await();
            return 1;
        });
    }

    @Override
    public Result<Integer> write(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(bytes, "bytes");
        PreCondition.assertStartIndex(startIndex, bytes.length);
        PreCondition.assertLength(length, startIndex, bytes.length);
        PreCondition.assertNotDisposed(this, "this");

        final int bytesToAddToBuffer = Math.minimum(length, buffer.length - currentBufferIndex);
        Array.copy(bytes, startIndex, buffer, currentBufferIndex, bytesToAddToBuffer);
        currentBufferIndex += bytesToAddToBuffer;

        final Result<Integer> result = flushBufferIfFull().then(() -> bytesToAddToBuffer);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If the buffer is full, then write its contents to the inner ByteWriteStream.
     * @return The number of bytes that were written to the inner ByteWriteStream.
     */
    private Result<Integer> flushBufferIfFull()
    {
        return Result.create(() ->
        {
            return this.currentBufferIndex < this.buffer.length
                ? 0
                : this.flushOnce().await();
        });
    }

    /**
     * Flush the bytes in the buffer and return how many bytes were written to the inner stream.
     * It's possible that not all of the bytes in the buffer will be written to the inner stream.
     * @return The number of bytes that were written to the inner stream.
     */
    private Result<Integer> flushOnce()
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            final int bytesWritten = this.byteWriteStream.write(this.buffer, 0, this.currentBufferIndex).await();
            if (bytesWritten == buffer.length)
            {
                final int newBufferLength = Math.minimum(maximumBufferSize, buffer.length * 2);
                if (newBufferLength != buffer.length)
                {
                    buffer = new byte[newBufferLength];
                }
                currentBufferIndex = 0;
            }
            else
            {
                Array.copy(buffer, bytesWritten, buffer, 0, buffer.length - bytesWritten);
                currentBufferIndex -= bytesWritten;
            }
            return bytesWritten;
        });
    }

    /**
     * Flush all of the bytes in the buffer and return how many bytes were written to the inner
     * stream.
     * @return The number of bytes that were written to the inner stream.
     */
    public Result<Integer> flush()
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            int bytesWritten = this.flushOnce().await();
            while (this.currentBufferIndex > 0)
            {
                bytesWritten += this.flushOnce().await();
            }
            return bytesWritten;
        });
    }

    @Override
    public boolean isDisposed()
    {
        return byteWriteStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            boolean result = false;
            if (!this.isDisposed())
            {
                if (this.currentBufferIndex > 0)
                {
                    this.byteWriteStream.writeAll(this.buffer, 0, this.currentBufferIndex).await();
                }

                this.buffer = null;
                this.currentBufferIndex = 0;
                result = this.byteWriteStream.dispose().await();
            }
            return result;
        });
    }
}
