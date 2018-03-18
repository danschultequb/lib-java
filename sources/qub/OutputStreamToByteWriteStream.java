package qub;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamToByteWriteStream extends ByteWriteStreamBase
{
    private final OutputStream outputStream;
    private boolean disposed;
    private Action1<IOException> exceptionHandler;

    public OutputStreamToByteWriteStream(OutputStream outputStream)
    {
        this.outputStream = outputStream;
    }

    protected OutputStream getOutputStream()
    {
        return outputStream;
    }

    @Override
    public void setExceptionHandler(Action1<IOException> exceptionHandler)
    {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public boolean write(byte toWrite)
    {
        boolean result = false;
        try
        {
            outputStream.write(toWrite);
            outputStream.flush();
            result = true;
        }
        catch (IOException e)
        {
            if (exceptionHandler != null)
            {
                exceptionHandler.run(e);
            }
        }
        return result;
    }

    @Override
    public boolean write(byte[] toWrite)
    {
        boolean result = false;
        if (toWrite != null && toWrite.length > 0)
        {
            try
            {
                outputStream.write(toWrite);
                outputStream.flush();
                result = true;
            }
            catch (IOException e)
            {
                if (exceptionHandler != null)
                {
                    exceptionHandler.run(e);
                }
            }
        }
        return result;
    }

    @Override
    public boolean write(byte[] toWrite, int startIndex, int length)
    {
        boolean result = false;
        if (toWrite != null && length > 0 && 0 <= startIndex && startIndex + length <= toWrite.length)
        {
            try
            {
                outputStream.write(toWrite, startIndex, length);
                outputStream.flush();
                result = true;
            }
            catch (IOException e)
            {
                if (exceptionHandler != null)
                {
                    exceptionHandler.run(e);
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
