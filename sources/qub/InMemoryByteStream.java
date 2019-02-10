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
        this.bytes = ByteList.create(bytes);
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

    /**
     * Set the maximum number of bytes that can be written per writeBytes() call.
     * @param maxBytesPerWrite The maximum number of bytes that can be written per writeBytes()
     *                         call.
     */
    public void setMaxBytesPerWrite(Integer maxBytesPerWrite)
    {
        this.maxBytesPerWrite = maxBytesPerWrite;
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
                result = current != null ? Result.success(current) : Result.error(new EndOfStreamException());
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
        PreCondition.assertFalse(this.isDisposed(), "this.isDisposed()");

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
                result = current != null ? Result.success(bytesRead) : Result.error(new EndOfStreamException());
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
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertFalse(endOfStream, "endOfStream");

        return mutex.criticalSection(() ->
        {
            final int bytesToWrite = maxBytesPerWrite == null ? length : maxBytesPerWrite;
            for (int i = 0; i < bytesToWrite; ++i)
            {
                bytes.add(toWrite[startIndex + i]);
            }
            bytesAvailable.signalAll();
            return Result.success(bytesToWrite);
        });
    }

    @Override
    public Result<Void> writeAllBytes(ByteReadStream byteReadStream)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertFalse(byteReadStream.isDisposed(), "byteReadStream.isDisposed()");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertFalse(endOfStream, "endOfStream");

        return mutex.criticalSection(() ->
        {
            Result<Void> result = null;
            boolean bytesAdded = false;
            while (result == null)
            {
                final Result<Byte> readByteResult = byteReadStream.readByte();
                if (readByteResult.hasError())
                {
                    if (readByteResult.getError() instanceof EndOfStreamException)
                    {
                        result = Result.success();
                    }
                    else
                    {
                        result = Result.error(readByteResult.getError());
                    }
                }
                else
                {
                    final Byte byteRead = readByteResult.getValue();
                    if (byteRead == null)
                    {
                        result = Result.success();
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
