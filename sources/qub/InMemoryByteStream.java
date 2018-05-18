package qub;

public class InMemoryByteStream extends ByteReadStreamBase implements ByteWriteStream
{
    private final AsyncRunner asyncRunner;
    private boolean disposed;
    private final List<Byte> bytes;
    private Byte current;
    private boolean started;
    private boolean endOfStream;
    private final Mutex mutex;
    private final MutexCondition bytesAvailable;

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

    /**
     * Get the bytes currently in this InMemoryByteStream. This will not change the streams
     * contents.
     * @return The bytes currently in this InMemoryByteStream.
     */
    public byte[] getBytes()
    {
        return !isDisposed() ? Array.toByteArray(bytes) : null;
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
     * @return This InMemoryByteStream.
     */
    public InMemoryByteStream endOfStream()
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

    @Override
    public Result<Boolean> write(byte toWrite)
    {
        Result<Boolean> result = InMemoryByteStream.validateNotEndOfStream(this);
        if (result == null)
        {
            result = DisposableBase.validateNotDisposed(this, "byteWriteStream");
            if (result == null)
            {
                try (final Disposable criticalSection = mutex.criticalSection())
                {
                    bytes.add(toWrite);
                    bytesAvailable.signalAll();
                }
                result = Result.successTrue();
            }
        }
        return result;
    }

    @Override
    public Result<Boolean> write(byte[] toWrite)
    {
        Result<Boolean> result = InMemoryByteStream.validateNotEndOfStream(this);
        if (result == null)
        {
            try (final Disposable criticalSection = mutex.criticalSection())
            {
                result = ByteWriteStreamBase.write(this, toWrite);
                if (!result.hasError())
                {
                    bytesAvailable.signalAll();
                }
            }
        }
        return result;
    }

    @Override
    public Result<Boolean> write(byte[] toWrite, int startIndex, int length)
    {
        Result<Boolean> result = InMemoryByteStream.validateNotEndOfStream(this);
        if (result == null)
        {
            try (final Disposable criticalSection = mutex.criticalSection())
            {
                result = ByteWriteStreamBase.write(this, toWrite, startIndex, length);
                if (!result.hasError())
                {
                    bytesAvailable.signalAll();
                }
            }
        }
        return result;
    }

    @Override
    public Result<Boolean> writeAll(ByteReadStream byteReadStream)
    {
        Result<Boolean> result = InMemoryByteStream.validateNotEndOfStream(this);
        if (result == null)
        {
            try (final Disposable criticalSection = mutex.criticalSection())
            {
                result = ByteWriteStreamBase.writeAll(this, byteReadStream);
                if (!result.hasError())
                {
                    bytesAvailable.signalAll();
                }
            }
        }
        return result;
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream()
    {
        return ByteWriteStreamBase.asCharacterWriteStream(this);
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream(CharacterEncoding encoding)
    {
        return ByteWriteStreamBase.asCharacterWriteStream(this, encoding);
    }

    @Override
    public LineWriteStream asLineWriteStream()
    {
        return ByteWriteStreamBase.asLineWriteStream(this);
    }

    @Override
    public LineWriteStream asLineWriteStream(CharacterEncoding encoding)
    {
        return ByteWriteStreamBase.asLineWriteStream(this, encoding);
    }

    @Override
    public LineWriteStream asLineWriteStream(String lineSeparator)
    {
        return ByteWriteStreamBase.asLineWriteStream(this, lineSeparator);
    }

    @Override
    public LineWriteStream asLineWriteStream(CharacterEncoding encoding, String lineSeparator)
    {
        return ByteWriteStreamBase.asLineWriteStream(this, encoding, lineSeparator);
    }

    private static <T> Result<T> validateNotEndOfStream(InMemoryByteStream byteWriteStream)
    {
        Result<T> result = null;
        if (byteWriteStream.endOfStream)
        {
            result = Result.error(new IllegalStateException("Cannot write to a InMemoryByteStream that has been marked as ended."));
        }
        return result;
    }
}
