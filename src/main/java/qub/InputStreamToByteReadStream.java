package qub;

import java.io.IOException;
import java.io.InputStream;

/**
 * An adapter that converts a java.io.InputStream to a qub.ByteReadStream.
 */
public class InputStreamToByteReadStream extends ByteReadStreamBase
{
    private final InputStream inputStream;

    private boolean closed;
    private Action1<IOException> exceptionHandler;
    private boolean hasStarted;
    private Byte current;

    InputStreamToByteReadStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    @Override
    public boolean isOpen()
    {
        return !closed;
    }

    @Override
    public boolean close()
    {
        boolean result = false;
        if (!closed)
        {
            try
            {
                inputStream.close();
                result = true;
                closed = true;
            }
            catch (IOException e)
            {
                handleException(e);
            }
        }
        return result;
    }

    @Override
    public Byte readByte()
    {
        hasStarted = true;

        int byteAsInt = -1;
        try
        {
            byteAsInt = inputStream.read();
        }
        catch (IOException e)
        {
            handleException(e);
        }

        if (byteAsInt == -1)
        {
            current = null;
        }
        else
        {
            current = (byte)byteAsInt;
        }

        return current;
    }

    @Override
    public int readBytes(byte[] outputBytes, int startIndex, int length)
    {
        hasStarted = true;

        int bytesRead = -1;
        try
        {
            bytesRead = inputStream.read(outputBytes, startIndex, length);
        }
        catch (IOException e)
        {
            handleException(e);
        }

        if (bytesRead == -1)
        {
            current = null;
        }
        else
        {
            current = outputBytes[bytesRead - 1];
        }

        return bytesRead;
    }

    @Override
    public void setExceptionHandler(Action1<IOException> exceptionHandler)
    {
        this.exceptionHandler = exceptionHandler;
    }

    private void handleException(IOException e)
    {
        if (exceptionHandler != null)
        {
            exceptionHandler.run(e);
        }
    }

    @Override
    public CharacterReadStream asCharacterReadStream(CharacterEncoding encoding)
    {
        return encoding == null ? null : new InputStreamReaderToCharacterReadStream(this, encoding);
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return current != null;
    }

    @Override
    public Byte getCurrent()
    {
        return current;
    }

    @Override
    public boolean next()
    {
        return readByte() != null;
    }
}
