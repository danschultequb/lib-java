package qub;

public class BufferedByteWriteStream implements ByteWriteStream
{
    public static final int defaultInitialBufferSize = 1024;
    private final ByteWriteStream byteWriteStream;
    private byte[] buffer;
    private int currentBufferIndex;

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
     * Get the number of bytes currently in the buffer waiting to be written to the inner stream.
     * @return The number of bytes currently in the buffer waiting to be written to the inner
     * stream.
     */
    public int getBufferByteCount()
    {
        return currentBufferIndex;
    }

    @Override
    public Result<Boolean> writeByte(byte toWrite)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        buffer[currentBufferIndex++] = toWrite;
        final Result<Boolean> result = flushBufferIfFull().then(() -> true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public Result<Integer> writeBytes(byte[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final int bytesToAddToBuffer = Math.minimum(length, buffer.length - currentBufferIndex);
        Array.copy(toWrite, startIndex, buffer, currentBufferIndex, bytesToAddToBuffer);
        currentBufferIndex += bytesToAddToBuffer;

        final Result<Integer> result = flushBufferIfFull().then(() -> bytesToAddToBuffer);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    private Result<?> flushBufferIfFull()
    {
        Result<?> result;
        if (currentBufferIndex < buffer.length)
        {
            result = Result.success();
        }
        else
        {
            result = byteWriteStream.writeBytes(buffer, 0, currentBufferIndex)
                .then((Integer bytesWritten) ->
                {
                    if (bytesWritten == buffer.length)
                    {
                        buffer = new byte[buffer.length * 2];
                        currentBufferIndex = 0;
                    }
                    else
                    {
                        Array.copy(buffer, bytesWritten, buffer, 0, buffer.length - bytesWritten);
                        currentBufferIndex -= bytesWritten;
                    }
                });
        }

        PostCondition.assertNotNull(result, "result");

        return result;
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
            result = Result.success(false);
        }
        else
        {
            Result<Void> writeBufferResult;
            if (currentBufferIndex == 0)
            {
                writeBufferResult = Result.success();
            }
            else
            {
                writeBufferResult = byteWriteStream.writeAllBytes(buffer, 0, currentBufferIndex)
                    .then(() ->
                    {
                        buffer = null;
                        currentBufferIndex = 0;
                    });
            }
            result = writeBufferResult
                .onError(byteWriteStream::dispose)
                .thenResult(byteWriteStream::dispose);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
