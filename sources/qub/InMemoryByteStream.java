package qub;

public class InMemoryByteStream implements ByteReadStream, ByteWriteStream
{
    private boolean isDisposed;
    private final ByteList bytes;
    private boolean endOfStream;
    private final Mutex mutex;
    private final MutexCondition bytesAvailable;

    private final RunnableEvent0 disposedEvent;

    protected InMemoryByteStream(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        this.bytes = ByteList.create(bytes.length).addAll(bytes);
        this.mutex = SpinMutex.create();
        this.bytesAvailable = this.mutex.createCondition(() -> isDisposed() || this.endOfStream || this.bytes.any());

        this.disposedEvent = Event0.create();
    }

    public static InMemoryByteStream create()
    {
        return InMemoryByteStream.create(new byte[0]);
    }

    public static InMemoryByteStream create(byte[] bytes)
    {
        return new InMemoryByteStream(bytes);
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

    /**
     * Get the number of bytes currently in the InMemoryByteStream.
     * @return The number of bytes currently in the InMemoryByteStream.
     */
    public int getCount()
    {
        return bytes.getCount();
    }

    @Override
    public Result<Byte> readByte()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.mutex.criticalSection(() ->
        {
            while (!this.isDisposed() && !this.endOfStream && !this.bytes.any())
            {
                bytesAvailable.watch().await();
            }

            if (this.isDisposed())
            {
                throw new IllegalStateException("this.isDisposed() cannot be true.");
            }

            if (!this.bytes.any())
            {
                throw new EmptyException();
            }

            return this.bytes.removeFirst().await();
        });
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(outputBytes, "outputBytes");
        PreCondition.assertStartIndex(startIndex, outputBytes.length);
        PreCondition.assertLength(length, startIndex, outputBytes.length);
        PreCondition.assertNotDisposed(this, "this");

        return this.mutex.criticalSection(() ->
        {
            int bytesRead = length;
            if (bytesRead > 0)
            {
                while (!this.isDisposed && !this.endOfStream && !this.bytes.any())
                {
                    this.bytesAvailable.watch().await();
                }

                if (this.isDisposed())
                {
                    throw new IllegalStateException("this.isDisposed() cannot be true.");
                }
                else if (!this.bytes.any())
                {
                    throw new EmptyException();
                }

                bytesRead = Math.minimum(bytes.getCount(), bytesRead);
                bytes.removeFirst(outputBytes, startIndex, bytesRead).await();
            }
            return bytesRead;
        });
    }

    /**
     * Register a callback to be invoked when this object is disposed.
     * @param callback The callback to invoke when this object is disposed.
     * @return A Disposable that can be disposed to remove the callback from the event.
     */
    public Disposable onDisposed(Action0 callback)
    {
        PreCondition.assertNotNull(callback, "callback");
        PreCondition.assertNotDisposed(this, "this");

        return this.disposedEvent.subscribe(callback);
    }

    @Override
    public boolean isDisposed()
    {
        return this.isDisposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return mutex.criticalSection(() ->
        {
            boolean result = !isDisposed;
            if (result)
            {
                isDisposed = true;
                bytesAvailable.signalAll();

                this.disposedEvent.run();
            }
            return result;
        });
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
    public Result<Integer> write(byte toWrite)
    {
        PreCondition.assertFalse(this.isDisposed(), "this");
        PreCondition.assertFalse(this.endOfStream, "this.endOfStream");

        return this.mutex.criticalSection(() ->
        {
            this.bytes.add(toWrite);
            this.bytesAvailable.signalAll();
            return 1;
        });
    }

    @Override
    public Result<Integer> write(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertStartIndex(startIndex, bytes.length);
        PreCondition.assertLength(length, startIndex, bytes.length);
        PreCondition.assertNotDisposed(this, "this");
        PreCondition.assertFalse(endOfStream, "endOfStream");

        return mutex.criticalSection(() ->
        {
            for (int i = 0; i < length; ++i)
            {
                this.bytes.add(bytes[startIndex + i]);
            }
            bytesAvailable.signalAll();
            return length;
        });
    }
}
