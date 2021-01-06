package qub;

public class TakeByteReadStream implements ByteReadStream
{
    private final ByteReadStream innerStream;
    private final long toTake;

    private long taken;

    private TakeByteReadStream(ByteReadStream innerStream, long toTake)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");
        PreCondition.assertGreaterThanOrEqualTo(toTake, 0, "toTake");

        this.innerStream = innerStream;
        this.toTake = toTake;
    }

    public static TakeByteReadStream create(ByteReadStream innerStream, long toTake)
    {
        return new TakeByteReadStream(innerStream, toTake);
    }

    @Override
    public Result<Byte> readByte()
    {
        return Result.create(() ->
        {
            if (this.taken >= this.toTake)
            {
                throw new EndOfStreamException();
            }
            final Byte result = this.innerStream.readByte().await();
            this.taken++;

            return result;
        });
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return Result.create(() ->
        {
            final long remaining = this.toTake - this.taken;
            if (remaining == 0)
            {
                throw new EndOfStreamException();
            }
            final int bytesToRead = (int)Math.minimum(remaining, length);
            final Integer bytesRead = this.innerStream.readBytes(outputBytes, startIndex, bytesToRead).await();
            this.taken += bytesRead;
            return bytesRead;
        });
    }

    @Override
    public boolean isDisposed()
    {
        return this.innerStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return this.innerStream.dispose();
    }
}
