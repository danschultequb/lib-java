package qub;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamToByteWriteStream extends ByteWriteStreamBase
{
    private final OutputStream outputStream;
    private boolean disposed;

    public OutputStreamToByteWriteStream(OutputStream outputStream)
    {
        this.outputStream = outputStream;
    }

    protected OutputStream getOutputStream()
    {
        return outputStream;
    }

    @Override
    public Result<Boolean> write(byte toWrite)
    {
        Result<Boolean> result;
        try
        {
            outputStream.write(toWrite);
            outputStream.flush();
            result = Result.successTrue();
        }
        catch (IOException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public Result<Boolean> write(byte[] toWrite)
    {
        Result<Boolean> result = Result.notNull(toWrite, "toWrite");
        if (result == null)
        {
            result = Result.greaterThan(0, toWrite.length, "toWrite.length");
            if (result == null)
            {
                try
                {
                    outputStream.write(toWrite);
                    outputStream.flush();
                    result = Result.successTrue();
                }
                catch (IOException e)
                {
                    result = Result.error(e);
                }
            }
        }
        return result;
    }

    @Override
    public Result<Boolean> write(byte[] toWrite, int startIndex, int length)
    {
        Result<Boolean> result = Result.notNull(toWrite, "toWrite");
        if (result == null)
        {
            result = Result.greaterThan(0, toWrite.length, "toWrite.length");
            if (result == null)
            {
                result = Result.between(0, startIndex, toWrite.length - 1, "startIndex");
                if (result == null)
                {
                    result = Result.between(1, length, toWrite.length - startIndex, "length");
                    if (result == null)
                    {
                        try
                        {
                            outputStream.write(toWrite, startIndex, length);
                            outputStream.flush();
                            result = Result.successTrue();
                        }
                        catch (IOException e)
                        {
                            result = Result.error(e);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream(CharacterEncoding encoding)
    {
        return encoding == null ? null : new OutputStreamWriterToCharacterWriteStream(this, encoding);
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
            catch (IOException e)
            {
                result = Result.<Boolean>error(e);
            }
        }
        return result;
    }
}
