package qub;

public class LockedByteWriteStream implements ByteWriteStream
{
    private final Locked<ByteWriteStream> innerStream;

    private LockedByteWriteStream(Locked<ByteWriteStream> innerStream)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        this.innerStream = innerStream;
    }

    public static LockedByteWriteStream create(ByteWriteStream innerStream)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        return new LockedByteWriteStream(Locked.create(innerStream));
    }

    public static LockedByteWriteStream create(ByteWriteStream innerStream, Mutex mutex)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");
        PreCondition.assertNotNull(mutex, "mutex");

        return new LockedByteWriteStream(Locked.create(innerStream, mutex));
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.write(toWrite).await();
            });
        });
    }

    @Override
    public Result<Integer> write(byte[] toWrite)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.write(toWrite).await();
            });
        });
    }

    @Override
    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.write(toWrite, startIndex, length).await();
            });
        });
    }

    @Override
    public Result<Integer> writeAll(byte[] toWrite)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.writeAll(toWrite).await();
            });
        });
    }

    @Override
    public Result<Integer> writeAll(byte[] toWrite, int startIndex, int length)
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.writeAll(toWrite, startIndex, length).await();
            });
        });
    }

    @Override
    public Result<Long> writeAll(ByteReadStream byteReadStream)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");

        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.writeAll(byteReadStream).await();
            });
        });
    }

    @Override
    public Result<Long> writeAll(ByteReadStream byteReadStream, int initialBufferCapacity)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertGreaterThanOrEqualTo(initialBufferCapacity, 1, "initialBufferCapacity");

        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.writeAll(byteReadStream, initialBufferCapacity).await();
            });
        });
    }

    @Override
    public void close()
    {
        this.innerStream.unlock(Disposable::close);
    }

    @Override
    public boolean isDisposed()
    {
        return this.innerStream.unlock(Disposable::isDisposed);
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            return this.innerStream.unlock(stream ->
            {
                return stream.dispose().await();
            });
        });
    }
}
