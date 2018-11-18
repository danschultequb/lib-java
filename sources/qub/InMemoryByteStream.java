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

        return mutex.criticalSection(() ->
        {
            Result<Byte> result;
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
            return result;
        });
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertBetween(0, startIndex, outputBytes.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, outputBytes.length - startIndex, "length");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return mutex.criticalSection(() ->
        {
            started = true;

            while (!disposed && !endOfStream && !bytes.any())
            {
                bytesAvailable.await();
            }

            Result<Integer> result = Result.equal(false, isDisposed(), "isDisposed()");
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
        return current;
    }

    /**
     * Mark that this stream has reached the end of its content. Future reads from this stream will
     * return null.
     * @return This InMemoryByteStream.
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
    public Result<Integer> writeBytes(byte[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertBetween(0, startIndex, toWrite.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, toWrite.length - startIndex, "length");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertFalse(endOfStream, "endOfStream");

        return mutex.criticalSection(() ->
        {
            for (int i = 0; i < length; ++i)
            {
                bytes.add(toWrite[startIndex + i]);
            }
            bytesAvailable.signalAll();
            return Result.success(length);
        });
    }

    @Override
    public Result<Boolean> writeAllBytes(ByteReadStream byteReadStream)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertFalse(byteReadStream.isDisposed(), "byteReadStream.isDisposed()");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertFalse(endOfStream, "endOfStream");

        return mutex.criticalSection(() ->
        {
            Result<Boolean> result = null;
            boolean bytesAdded = false;
            while (result == null)
            {
                final Result<Byte> readByteResult = byteReadStream.readByte();
                if (readByteResult.hasError())
                {
                    result = Result.error(readByteResult.getError());
                }
                else
                {
                    final Byte byteRead = readByteResult.getValue();
                    if (byteRead == null)
                    {
                        result = Result.successTrue();
                    }
                    else
                    {
                        bytesAdded = true;
                        bytes.add(byteRead);
                    }
                }
            }

            if (bytesAdded)
            {
                bytesAvailable.signalAll();
            }

            return result;
        });
    }
}
