package qub;

public class InMemoryByteStream implements ByteReadStream, ByteWriteStream
{
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
        this(null);
    }

    public InMemoryByteStream(byte[] bytes)
    {
        this.bytes = ByteList.createFromBytes(bytes);
        this.mutex = new SpinMutex();
        this.bytesAvailable = this.mutex.createCondition(() -> isDisposed() || this.endOfStream || this.bytes.any());
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
                bytesAvailable.watch().await();
            }

            if (isDisposed())
            {
                throw new IllegalStateException("isDisposed() cannot be true.");
            }

            current = bytes.any() ? bytes.removeFirst() : null;
            if (current == null)
            {
                throw new EndOfStreamException();
            }
            return current;
        });
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertNonEmptyStartIndex(startIndex, outputBytes.length);
        PreCondition.assertNonEmptyLength(length, startIndex, outputBytes.length);
        PreCondition.assertNotDisposed(this);

        return mutex.criticalSection(() ->
        {
            started = true;

            while (!disposed && !endOfStream && !bytes.any())
            {
                bytesAvailable.watch().await();
            }

            if (this.isDisposed())
            {
                throw new IllegalStateException("isDisposed() cannot be true.");
            }
            else if (!bytes.any())
            {
                current = null;
                throw new EndOfStreamException();
            }

            int bytesRead = Math.minimum(bytes.getCount(), length);
            if (maxBytesPerRead != null && maxBytesPerRead < bytesRead)
            {
                bytesRead = maxBytesPerRead;
            }
            bytes.removeFirstBytes(outputBytes, startIndex, bytesRead);
            current = outputBytes[startIndex + bytesRead - 1];
            return bytesRead;
        });
    }

    @Override
    public boolean isDisposed()
    {
        return this.disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return mutex.criticalSection(() ->
        {
            boolean result = !disposed;
            if (result)
            {
                disposed = true;
                current = null;
                bytesAvailable.signalAll();
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
        }).await();
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
            return 1;
        });
    }

    @Override
    public Result<Integer> writeBytes(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(bytes, "bytes");
        PreCondition.assertNonEmptyStartIndex(startIndex, bytes.length);
        PreCondition.assertNonEmptyLength(length, startIndex, bytes.length);
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
            return bytesWritten;
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
