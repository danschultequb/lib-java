package qub;

public class FakeByteReadStream implements ByteReadStream
{
    private final Function0<Result<Byte>> readByteFunction;
    private final Function3<byte[],Integer,Integer,Result<Integer>> readBytesFunction;
    private boolean isDisposed;

    private FakeByteReadStream(Function0<Result<Byte>> readByteFunction, Function3<byte[],Integer,Integer,Result<Integer>> readBytesFunction)
    {
        PreCondition.assertNotNull(readByteFunction, "readByteFunction");
        PreCondition.assertNotNull(readBytesFunction, "readBytesFunction");

        this.readByteFunction = readByteFunction;
        this.readBytesFunction = readBytesFunction;
    }

    public static FakeByteReadStream create(Function0<Result<Byte>> readByteFunction)
    {
        PreCondition.assertNotNull(readByteFunction, "readByteFunction");

        final Function3<byte[],Integer,Integer,Result<Integer>> readBytesFunction = (byte[] outputBytes, Integer startIndex, Integer length) ->
        {
            return Result.create(() ->
            {
                for (int i = 0; i < length; ++i)
                {
                    outputBytes[startIndex + i] = readByteFunction.run().await();
                }
                return length;
            });
        };

        return FakeByteReadStream.create(readByteFunction, readBytesFunction);
    }

    public static FakeByteReadStream create(Function3<byte[],Integer,Integer,Result<Integer>> readBytesFunction)
    {
        PreCondition.assertNotNull(readBytesFunction, "readBytesFunction");

        final Function0<Result<Byte>> readByteFunction = () ->
        {
            return Result.create(() ->
            {
                final byte[] outputBytes = new byte[1];
                readBytesFunction.run(outputBytes, 0, outputBytes.length).await();
                return outputBytes[0];
            });
        };

        return FakeByteReadStream.create(readByteFunction, readBytesFunction);
    }

    public static FakeByteReadStream create(Function0<Result<Byte>> readByteFunction, Function3<byte[],Integer,Integer,Result<Integer>> readBytesFunction)
    {
        PreCondition.assertNotNull(readByteFunction, "readByteFunction");
        PreCondition.assertNotNull(readBytesFunction, "readBytesFunction");

        return new FakeByteReadStream(readByteFunction, readBytesFunction);
    }

    @Override
    public Result<Byte> readByte()
    {
        PreCondition.assertNotDisposed(this, "this.isDisposed()");

        return this.readByteFunction.run();
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(outputBytes, "outputBytes");
        PreCondition.assertStartIndex(startIndex, outputBytes.length);
        PreCondition.assertLength(length, startIndex, outputBytes.length);
        PreCondition.assertNotDisposed(this, "this.isDisposed()");

        return this.readBytesFunction.run(outputBytes, startIndex, length);
    }

    @Override
    public boolean isDisposed()
    {
        return this.isDisposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (this.isDisposed)
        {
            result = Result.successFalse();
        }
        else
        {
            this.isDisposed = true;
            result = Result.successTrue();
        }
        return result;
    }
}
