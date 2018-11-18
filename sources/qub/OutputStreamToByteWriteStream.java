package qub;

public class OutputStreamToByteWriteStream implements ByteWriteStream
{
    private final java.io.OutputStream outputStream;
    private boolean disposed;

    public OutputStreamToByteWriteStream(java.io.OutputStream outputStream)
    {
        PreCondition.assertNotNull(outputStream, "outputStream");

        this.outputStream = outputStream;
    }

    @Override
    public Result<Boolean> writeByte(byte toWrite)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        Result<Boolean> result;
        try
        {
            outputStream.write(toWrite);
            outputStream.flush();
            result = Result.successTrue();
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public Result<Integer> writeBytes(byte[] toWrite)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        Result<Integer> result;
        try
        {
            outputStream.write(toWrite);
            outputStream.flush();
            result = Result.success(toWrite.length);
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public Result<Integer> writeBytes(byte[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertBetween(0, startIndex, toWrite.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, toWrite.length - startIndex, "length");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        Result<Integer> result;
        try
        {
            outputStream.write(toWrite, startIndex, length);
            outputStream.flush();
            result = Result.success(length);
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return new OutputStreamWriterToCharacterWriteStream(this, characterEncoding);
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (disposed)
        {
            result = Result.success(false);
        }
        else
        {
            disposed = true;
            try
            {
                outputStream.close();
                result = Result.success(true);
            }
            catch (java.io.IOException e)
            {
                result = Result.error(e);
            }
        }
        return result;
    }
}
