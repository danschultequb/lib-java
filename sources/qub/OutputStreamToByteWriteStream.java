package qub;

import java.io.OutputStream;

public class OutputStreamToByteWriteStream implements ByteWriteStream
{
    private final OutputStream outputStream;
    private boolean closed;
    private Action1<Exception> exceptionHandler;

    public OutputStreamToByteWriteStream(OutputStream outputStream)
    {
        this.outputStream = outputStream;
    }

    protected OutputStream getOutputStream()
    {
        return outputStream;
    }

    @Override
    public void setExceptionHandler(Action1<Exception> exceptionHandler)
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
        catch (Exception e)
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
            catch (Exception e)
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
            catch (Exception e)
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
    public boolean isOpen()
    {
        return !closed;
    }

    @Override
    public void close()
    {
        if (isOpen())
        {
            try
            {
                outputStream.close();
                closed = true;
            }
            catch (Exception e)
            {
                if (exceptionHandler != null)
                {
                    exceptionHandler.run(e);
                }
            }
        }
    }
}
