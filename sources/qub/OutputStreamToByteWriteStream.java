package qub;

public class OutputStreamToByteWriteStream implements ByteWriteStream
{
    private final java.io.OutputStream outputStream;
    private boolean disposed;
    private final boolean autoFlush;

    public OutputStreamToByteWriteStream(java.io.OutputStream outputStream)
    {
        this(outputStream, true);
    }

    public OutputStreamToByteWriteStream(java.io.OutputStream outputStream, boolean autoFlush)
    {
        PreCondition.assertNotNull(outputStream, "outputStream");

        this.outputStream = outputStream;
        this.autoFlush = autoFlush;
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
            try
            {
                this.outputStream.close();
                result = Result.successTrue();
            }
            catch (java.io.IOException e)
            {
                result = Result.error(e);
            }
        }
        return result;
    }
}
