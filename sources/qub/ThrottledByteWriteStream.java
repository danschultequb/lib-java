package qub;

public class ThrottledByteWriteStream implements ByteWriteStream
{
    private final Function1<Integer,Integer> bytesToWriteFunction;
    private final ByteWriteStream byteWriteStream;

    private ThrottledByteWriteStream(ByteWriteStream byteWriteStream, Function1<Integer,Integer> bytesToWriteFunction)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotNull(bytesToWriteFunction, "bytesToWriteFunction");

        this.bytesToWriteFunction = bytesToWriteFunction;
        this.byteWriteStream = byteWriteStream;
    }

    public static ThrottledByteWriteStream create(ByteWriteStream byteWriteStream, int maximumBytesPerWrite)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertGreaterThanOrEqualTo(maximumBytesPerWrite, 1, "maximumBytesPerWrite");

        return ThrottledByteWriteStream.create(byteWriteStream, (Integer bytesToWrite) -> maximumBytesPerWrite);
    }

    public static ThrottledByteWriteStream create(ByteWriteStream byteWriteStream, Function1<Integer,Integer> bytesToWriteFunction)
    {
        return new ThrottledByteWriteStream(byteWriteStream, bytesToWriteFunction);
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            this.bytesToWriteFunction.run(1);
            return this.byteWriteStream.write(toWrite).await();
        });
    }

    @Override
    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            final int bytesToWrite = this.bytesToWriteFunction.run(length);
            return this.byteWriteStream.write(toWrite, startIndex, bytesToWrite).await();
        });
    }

    @Override
    public boolean isDisposed()
    {
        return this.byteWriteStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return this.byteWriteStream.dispose();
    }
}
