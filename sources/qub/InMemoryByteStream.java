package qub;

public class InMemoryByteStream implements ByteReadStream, ByteWriteStream
{
    private final AsyncRunner asyncRunner;
    private boolean disposed;
    private final ByteList bytes;
    private Byte current;
    private boolean started;
    private boolean endOfStream;
    private final Mutex mutex;
    private final MutexCondition bytesAvailable;
    private Integer maxBytesPerRead;
    private Integer maxBytesPerWrite;

    public InMemoryByteStream()
    {
        this(null, null);
    }

    public InMemoryByteStream(AsyncRunner asyncRunner)
    {
        this(null, asyncRunner);
    }

    public InMemoryByteStream(byte[] bytes)
    {
        this(bytes, null);
    }

    public InMemoryByteStream(byte[] bytes, AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
        this.bytes = ByteList.createFromBytes(bytes);
        this.mutex = new SpinMutex();
        this.bytesAvailable = this.mutex.createCondition();
    }

    /**
     * Get the bytes currently in this InMemoryByteStream. This will not change the streams
     * contents.
     * @return The bytes currently in this InMemoryByteStream.
     */
    public byte[] getBytes()
    {
        return bytes.toByteArray();
    }

    @Override
    public int getCount()
    {
        return bytes.getCount();
    }

    /**
     * Get the maximum number of bytes that can be read with a readBytes() call, or null if no limit
     * has been set.
     * @return The maximum number of bytes that can be read with a readBytes() call or null if no
     * limit has been set.
     */
    public Integer getMaxBytesPerRead()
    {
        return maxBytesPerRead;
    }

    /**
     * Set the maximum number of bytes that can be read with a readBytes() call.
     * @param maxBytesPerRead The maximum number of bytes that can be read with a readBytes() call.
     * @return This object for method chaining.
     */
    public InMemoryByteStream setMaxBytesPerRead(Integer maxBytesPerRead)
    {
        PreCondition.assertTrue(maxBytesPerRead == null || maxBytesPerRead >= 1, "maxBytesPerRead (" + maxBytesPerRead + ") == null || maxBytesPerRead (" + maxBytesPerRead + ") >= 1");

        this.maxBytesPerRead = maxBytesPerRead;

        return this;
    }

    /**
     * Get the maximum number of bytes that can be written with a writeBytes() call, or null if no
     * limit has been set.
     * @return The maximum number of bytes that can be written with a writeBytes() call or null if
     * no limit has been set.
     */
    public Integer getMaxBytesPerWrite()
    {
        return maxBytesPerWrite;
    }

    /**
     * Set the maximum number of bytes that can be written with a writeBytes() call.
     * @param maxBytesPerWrite The maximum number of bytes that can be written with a writeBytes()
     *                         call.
     * @return This object for method chaining.
     */
    public InMemoryByteStream setMaxBytesPerWrite(Integer maxBytesPerWrite)
    {
        PreCondition.assertTrue(maxBytesPerWrite == null || maxBytesPerWrite >= 1, "maxBytesPerWrite (" + maxBytesPerWrite + ") == null || maxBytesPerWrite (" + maxBytesPerWrite + ") >= 1");

        this.maxBytesPerWrite = maxBytesPerWrite;

        return this;
    }

    @Override
    public Result<Byte> readByte()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return mutex.criticalSection(() ->
        {
            started = true;

            while (!isDisposed() && !endOfStream && !bytes.any())
            {
                bytesAvailable.await();
            }

            Result<Byte> result;
            if (isDisposed())
            {
                result = Result.error(new IllegalStateException("isDisposed() cannot be true."));
            }
            else
            {
                current = bytes.any() ? bytes.removeFirst() : null;
                result = current != null
                    ? Result.success(current)
                    : Result.error(new EndOfStreamException());
            }

            return result;
        });
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertStartIndex(startIndex, outputBytes.length);
        PreCondition.assertLength(length, startIndex, outputBytes.length);
        PreCondition.assertNotDisposed(this);

        return mutex.criticalSection(() ->
        {
            started = true;

            while (!disposed && !endOfStream && !bytes.any())
            {
                bytesAvailable.await();
            }

            Result<Integer> result;
            if (isDisposed())
            {
                result = Result.error(new IllegalStateException("isDisposed() cannot be true."));
            }
            else if (!bytes.any())
            {
                current = null;
                result = Result.error(new EndOfStreamException());
            }
            else
            {
                int bytesRead = Math.minimum(bytes.getCount(), length);
                if (maxBytesPerRead != null && maxBytesPerRead < bytesRead)
                {
                    bytesRead = maxBytesPerRead;
                }
                for (int i = 0; i < bytesRead; ++i)
                {
                    outputBytes[startIndex + i] = bytes.removeFirst();
                }
                current = outputBytes[startIndex + bytesRead - 1];
                result = Result.success(bytesRead);
            }
            return result;
        });
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return mutex.criticalSection(() ->
        {
            Result<Boolean> result;
            if (!disposed)
            {
                disposed = true;
                current = null;
                bytesAvailable.signalAll();
                result = Result.successTrue();
            }
            else
            {
                result = Result.successFalse();
            }
            return result;
        });
    }

    @Override
    public boolean hasStarted()
    {
        return started;
    }

    @Override
    public boolean hasCurrent()
    {
        return current != null;
    }

    @Override
    public Byte getCurrent()
    {
        PreCondition.assertTrue(hasCurrent(), "hasCurrent()");

        return current;
    }

    /**
     * Mark that this stream has reached the end of its content. Future reads create this stream will
     * return null.
     * @return This object for method chaining.
     */
    public InMemoryByteStream endOfStream()
    {
        return mutex.criticalSection(() ->
        {
            if (!endOfStream)
            {
                endOfStream = true;
                bytesAvailable.signalAll();
            }
            return this;
        });
    }

    @Override
    public Result<Integer> writeByte(byte toWrite)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertFalse(endOfStream, "endOfStream");

        return mutex.criticalSection(() ->
        {
            bytes.add(toWrite);
            bytesAvailable.signalAll();
            return Result.successOne();
        });
    }

    @Override
    public Result<Integer> writeBytes(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(bytes, "bytes");
        PreCondition.assertStartIndex(startIndex, bytes.length);
        PreCondition.assertLength(length, startIndex, bytes.length);
        PreCondition.assertNotDisposed(this);
        PreCondition.assertFalse(endOfStream, "endOfStream");

        return mutex.criticalSection(() ->
        {
            int bytesWritten = length;
            if (maxBytesPerWrite != null && maxBytesPerWrite < length)
            {
                bytesWritten = maxBytesPerWrite;
            }
            for (int i = 0; i < bytesWritten; ++i)
            {
                this.bytes.add(bytes[startIndex + i]);
            }
            bytesAvailable.signalAll();
            return Result.success(bytesWritten);
        });
    }

    /**
     * Convert this ByteReadStream to a CharacterReadStream using the default CharacterEncoding.
     * @return A CharacterReadStream that uses the default CharacterEncoding.
     */
    @Override
    public InMemoryCharacterStream asCharacterReadStream()
    {
        return asCharacterReadStream(CharacterEncoding.UTF_8);
    }

    /**
     * Convert this ByteReadStream to a CharacterReadStream using the provided CharacterEncoding.
     * @return A CharacterReadStream that uses the provided CharacterEncoding.
     */
    @Override
    public InMemoryCharacterStream asCharacterReadStream(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return new InMemoryCharacterStream(this, characterEncoding);
    }
}
