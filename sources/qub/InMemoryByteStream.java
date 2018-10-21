package qub;

public class InMemoryByteStream implements ByteReadStream, ByteWriteStream
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
        this.bytes = new ArrayList<>();
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
    public int getCount()
    {
        return bytes.getCount();
    }

    @Override
    public Result<Byte> readByte()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        Result<Byte> result = null;
        try (final Disposable ignored = mutex.criticalSection())
        {
            started = true;

            while (!disposed && !endOfStream && !bytes.any())
            {
                bytesAvailable.await();
            }

            result = Result.equal(false, isDisposed(), "byteReadStream.isDisposed()");
            if (result == null)
            {
                current = bytes.any() ? bytes.removeFirst() : null;
                result = Result.success(current);
            }
        }
        return result;
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertBetween(0, startIndex, outputBytes.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, outputBytes.length - startIndex, "length");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        Result<Integer> result;
        try (final Disposable ignored = mutex.criticalSection())
        {
            started = true;

            while (!disposed && !endOfStream && !bytes.any())
            {
                bytesAvailable.await();
            }

            result = Result.equal(false, isDisposed(), "isDisposed()");
            if (result == null)
            {
                Integer bytesRead;
                if (!bytes.any())
                {
                    bytesRead = null;
                    current = null;
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
        try (final Disposable ignored = mutex.criticalSection())
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

    /**
     * Mark that this stream has reached the end of its content. Future reads from this stream will
     * return null.
     * @return This InMemoryByteStream.
     */
    public InMemoryByteStream endOfStream()
    {
        try (final Disposable ignored = mutex.criticalSection())
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
            result = Disposable.validateNotDisposed(this, "byteWriteStream");
            if (result == null)
            {
                try (final Disposable ignored = mutex.criticalSection())
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
            try (final Disposable ignored = mutex.criticalSection())
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
            try (final Disposable ignored = mutex.criticalSection())
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
            try (final Disposable ignored = mutex.criticalSection())
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
