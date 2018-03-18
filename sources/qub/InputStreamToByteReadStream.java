package qub;

import java.io.IOException;
import java.io.InputStream;

/**
 * An adapter that converts a java.io.InputStream to a qub.ByteReadStream.
 */
public class InputStreamToByteReadStream extends ByteReadStreamBase
{
    private final InputStream inputStream;

    private boolean disposed;
    private Action1<IOException> exceptionHandler;
    private boolean hasStarted;
    private Byte current;

    public InputStreamToByteReadStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    @Override
    final public void close()
    {
        DisposableBase.close(this);
    }

    @Override
    final public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    final public Result<Boolean> dispose()
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
                inputStream.close();
                result = Result.success(true);
            }
            catch (IOException e)
            {
                result = Result.<Boolean>error(e);
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
    public CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding)
    {
        return characterEncoding == null ? null : new InputStreamReaderToCharacterReadStream(this, characterEncoding);
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
