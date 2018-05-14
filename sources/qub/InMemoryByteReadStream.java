package qub;

public class InMemoryByteReadStream extends ByteReadStreamBase
{
    private final AsyncRunner asyncRunner;
    private boolean disposed;
    private final List<Byte> bytes;
    private Byte current;
    private boolean started;
    private boolean endOfStream;
    private final Mutex mutex;
    private final MutexCondition bytesAvailable;

    public InMemoryByteReadStream()
    {
        this(null, null);
    }

    public InMemoryByteReadStream(AsyncRunner asyncRunner)
    {
        this(null, asyncRunner);
    }

    public InMemoryByteReadStream(byte[] bytes)
    {
        this(bytes, null);
    }

    public InMemoryByteReadStream(byte[] bytes, AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
        this.bytes = new ArrayList<Byte>();
        if (bytes != null && bytes.length > 0)
        {
            for (final byte b : bytes)
            {
                this.bytes.add(b);
            }
        }
        this.mutex = new SpinMutex();
        this.bytesAvailable = this.mutex.createCondition();
    }

    @Override
    public Result<Byte> readByte()
    {
        Result<Byte> result = ByteReadStreamBase.validateByteReadStream(this);
        if (result == null)
        {
            try (final Disposable criticalSection = mutex.criticalSection())
            {
                started = true;

                while (!disposed && !endOfStream && !bytes.any())
                {
                    bytesAvailable.await();
                }

                if (disposed)
                {
                    result = ByteReadStreamBase.validateByteReadStream(this);
                }
                else
                {
                    current = bytes.removeFirst();
                    result = Result.success(current);
                }
            }
        }
        return result;
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        Result<Integer> result = ByteReadStreamBase.validateByteReadStream(this);
        if (result == null)
        {
            result = ByteReadStreamBase.validateOutputBytes(outputBytes);
            if (result == null)
            {
                result = ByteReadStreamBase.validateStartIndex(startIndex, outputBytes);
                if (result == null)
                {
                    result = ByteReadStreamBase.validateLength(length, outputBytes, startIndex);
                    if (result == null)
                    {
                        try (final Disposable criticalSection = mutex.criticalSection())
                        {
                            started = true;

                            while (!disposed && !endOfStream && !bytes.any())
                            {
                                bytesAvailable.await();
                            }

                            if (disposed)
                            {
                                result = ByteReadStreamBase.validateByteReadStream(this);
                            }
                            else
                            {
                                Integer bytesRead;
                                if (!bytes.any())
                                {
                                    bytesRead = null;
                                }
                                else
                                {
                                    if (bytes.getCount() < length)
                                    {
                                        bytesRead = bytes.getCount();
                                    }
                                    else
                                    {
                                        bytesRead = length;
                                    }

                                    for (int i = 0; i < bytesRead; ++i)
                                    {
                                        outputBytes[startIndex + i] = bytes.removeFirst();
                                    }
                                    current = outputBytes[startIndex + bytesRead - 1];
                                }
                                result = Result.success(bytesRead);
                            }
                        }
                    }
                }
            }
        }
        return result;
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
        try (final Disposable criticalSection = mutex.criticalSection())
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
        }
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
        return current;
    }

    @Override
    public boolean next()
    {
        return readByte() != null;
    }

    /**
     * Mark that this stream has reached the end of its content. Future reads from this stream will
     * return null.
     * @return This InMemoryByteReadStream.
     */
    public InMemoryByteReadStream endOfStream()
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            if (!endOfStream)
            {
                endOfStream = true;
                bytesAvailable.signalAll();
            }
            return this;
        }
    }
}
