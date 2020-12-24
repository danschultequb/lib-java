package qub;

public class InMemoryBitStream implements BitWriteStream
{
    private boolean disposed;

    private InMemoryBitStream()
    {
    }

    public static InMemoryBitStream create()
    {
        return new InMemoryBitStream();
    }

    @Override
    public Result<Integer> write(int bit)
    {
        PreCondition.assertBetween(Bits.minimum, bit, Bits.maximum, "bit");

        return null;
    }

    @Override
    public boolean isDisposed()
    {
        return this.disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (this.disposed)
        {
            result = Result.successFalse();
        }
        else
        {
            this.disposed = true;
            result = Result.successTrue();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
