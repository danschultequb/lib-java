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
        PreCondition.assertFalse(this.isDisposed(), "this.isDisposed()");

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
                final int bytesRead = Math.minimum(bytes.getCount(), length);
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
        return current;
    }

    /**
     * Mark that this stream has reached the end of its content. Future reads create this stream will
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
    public Result<Long> writeAllBytes(ByteReadStream byteReadStream)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertFalse(byteReadStream.isDisposed(), "byteReadStream.isDisposed()");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertFalse(endOfStream, "endOfStream");

        return mutex.criticalSection(() ->
            Result.create(() ->
            {
                long result = 0;
                boolean bytesAdded = false;
                while (true)
                {
                    final Byte byteRead = byteReadStream.readByte()
                        .catchError(EndOfStreamException.class)
                        .awaitError();
                    if (byteRead == null)
                    {
                        break;
                    }
                    else
                    {
                        ++result;
                        bytesAdded = true;
                        bytes.add(byteRead);
                    }
                }

                if (bytesAdded)
                {
                    bytesAvailable.signalAll();
                }

                return result;
            }));
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
     * Conert this ByteReadStream to a CharacterReadStream using the provided CharacterEncoding.
     * @return A CharacterReadStream that uses the provided CharacterEncoding.
     */
    @Override
    public InMemoryCharacterStream asCharacterReadStream(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return new InMemoryCharacterStream(this, characterEncoding);
    }
}
