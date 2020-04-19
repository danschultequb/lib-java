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
        PreCondition.assertFalse(this.isDisposed(), "this.isDisposed()");

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
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

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
        Result<Integer> result;
        if (currentBufferIndex < buffer.length)
        {
            result = Result.successZero();
        }
        else
        {
            result = this.flush();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Flush the bytes in the buffer and return how many bytes were written to the inner stream.
     * It's possible that not all of the bytes in the buffer will be written to the inner stream.
     * @return The number of bytes that were written to the inner stream.
     */
    public Result<Integer> flush()
    {
        PreCondition.assertNotDisposed(this);

        return byteWriteStream.write(buffer, 0, currentBufferIndex)
            .onValue((Integer bytesWritten) ->
            {
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
        Result<Boolean> result;
        if (isDisposed())
        {
            result = Result.successFalse();
        }
        else
        {
            Result<Integer> writeBufferResult;
            if (currentBufferIndex == 0)
            {
                writeBufferResult = Result.successZero();
            }
            else
            {
                writeBufferResult = byteWriteStream.writeAll(buffer, 0, currentBufferIndex);
            }
            result = writeBufferResult
                .then(() ->
                {
                    buffer = null;
                    currentBufferIndex = 0;
                    return byteWriteStream.dispose().await();
                });
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
