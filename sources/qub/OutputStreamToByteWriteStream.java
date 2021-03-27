package qub;

public class OutputStreamToByteWriteStream extends BasicDisposable implements ByteWriteStream
{
    private final java.io.OutputStream outputStream;
    private final boolean autoFlush;

    private OutputStreamToByteWriteStream(java.io.OutputStream outputStream, boolean autoFlush)
    {
        PreCondition.assertNotNull(outputStream, "outputStream");

        this.outputStream = outputStream;
        this.autoFlush = autoFlush;
    }

    public static OutputStreamToByteWriteStream create(java.io.OutputStream outputStream)
    {
        return OutputStreamToByteWriteStream.create(outputStream, true);
    }

    public static OutputStreamToByteWriteStream create(java.io.OutputStream outputStream, boolean autoFlush)
    {
        return new OutputStreamToByteWriteStream(outputStream, autoFlush);
    }

    public Result<Void> flush()
    {
        Result<Void> result;
        try
        {
            this.outputStream.flush();
            result = Result.success();
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        PreCondition.assertNotDisposed(this, "this");

        Result<Integer> result;
        try
        {
            this.outputStream.write(toWrite);
            if (this.autoFlush)
            {
                this.outputStream.flush();
            }
            result = Result.successOne();
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public Result<Integer> write(byte[] toWrite)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            try
            {
                this.outputStream.write(toWrite);
                if (this.autoFlush)
                {
                    this.outputStream.flush();
                }
            }
            catch (java.io.IOException e)
            {
                throw Exceptions.asRuntime(e);
            }
            return toWrite.length;
        });
    }

    @Override
    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertNonEmptyStartIndex(startIndex, toWrite.length);
        PreCondition.assertNonEmptyLength(length, startIndex, toWrite.length);
        PreCondition.assertNotDisposed(this, "this");

        Result<Integer> result;
        try
        {
            this.outputStream.write(toWrite, startIndex, length);
            if (this.autoFlush)
            {
                this.outputStream.flush();
            }
            result = Result.success(length);
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            final boolean result = super.dispose().await();
            if (result)
            {
                try
                {
                    this.outputStream.close();
                }
                catch (java.io.IOException e)
                {
                    throw Exceptions.asRuntime(e);
                }
            }
            return result;
        });
    }
}
