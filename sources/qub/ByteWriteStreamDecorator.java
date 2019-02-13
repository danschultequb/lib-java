package qub;

/**
 * A ByteWriteStream decorator that can add funtionality to an existing ByteWriteStream.
 */
public class ByteWriteStreamDecorator implements ByteWriteStream
{
    protected final ByteWriteStream innerStream;
    private final Function3<byte[],Integer,Integer,Result<Integer>> writeBytesFunction;

    public ByteWriteStreamDecorator(ByteWriteStream innerStream)
    {
        this(innerStream, null);
    }

    public ByteWriteStreamDecorator(ByteWriteStream innerStream, Function3<byte[],Integer,Integer,Result<Integer>> writeBytesFunction)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        this.innerStream = innerStream;
        this.writeBytesFunction = writeBytesFunction;
    }

    @Override
    final public boolean isDisposed()
    {
        return innerStream.isDisposed();
    }

    @Override
    final public Result<Boolean> dispose()
    {
        return innerStream.dispose();
    }

    @Override
    public Result<Integer> writeBytes(byte[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);
        PreCondition.assertNotNull(writeBytesFunction, "writeBytesFunction");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final Result<Integer> result = writeBytesFunction.run(toWrite, startIndex, length);

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
