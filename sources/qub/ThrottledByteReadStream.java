package qub;

public class ThrottledByteReadStream implements ByteReadStream
{
    private final Function1<Integer,Integer> bytesToReadFunction;
    private final ByteReadStream byteReadStream;

    private ThrottledByteReadStream(ByteReadStream byteReadStream, Function1<Integer,Integer> bytesToReadFunction)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertNotNull(bytesToReadFunction, "bytesToReadFunction");

        this.byteReadStream = byteReadStream;
        this.bytesToReadFunction = bytesToReadFunction;
    }

    public static ThrottledByteReadStream create(ByteReadStream byteReadStream, int maximumBytesPerRead)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertGreaterThanOrEqualTo(maximumBytesPerRead, 1, "maximumBytesPerRead");

        return ThrottledByteReadStream.create(byteReadStream, (Integer bytesToRead) -> maximumBytesPerRead);
    }

    public static ThrottledByteReadStream create(ByteReadStream byteReadStream, Function1<Integer,Integer> bytesToReadFunction)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertNotNull(bytesToReadFunction, "bytesToReadFunction");

        return new ThrottledByteReadStream(byteReadStream, bytesToReadFunction);
    }

    @Override
    public Result<Byte> readByte()
    {
        PreCondition.assertNotDisposed(this, "this.isDisposed()");

        return Result.create(() ->
        {
            this.bytesToReadFunction.run(1);
            return this.byteReadStream.readByte().await();
        });
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(outputBytes, "outputBytes");
        PreCondition.assertStartIndex(startIndex, outputBytes.length);
        PreCondition.assertLength(length, startIndex, outputBytes.length);
        PreCondition.assertNotDisposed(this, "this.isDisposed()");

        return Result.create(() ->
        {
            final int bytesToRead = this.bytesToReadFunction.run(length);
            return this.byteReadStream.readBytes(outputBytes, startIndex, bytesToRead).await();
        });
    }

    @Override
    public boolean isDisposed()
    {
        return this.byteReadStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return this.byteReadStream.dispose();
    }
}
