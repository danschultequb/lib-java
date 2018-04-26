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
    public Result<Byte> readByte()
    {
        hasStarted = true;

        Result<Byte> result;
        try
        {
            final int byteAsInt = inputStream.read();
            if (byteAsInt == -1)
            {
                current = null;
            }
            else
            {
                current = (byte)byteAsInt;
            }
            result = Result.success(current);
        }
        catch (IOException e)
        {
            current = null;
            result = Result.error(e);
        }

        return result;
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        hasStarted = true;

        Result<Integer> result;
        try
        {
            final int bytesRead = inputStream.read(outputBytes, startIndex, length);
            if (bytesRead == -1)
            {
                current = null;
                result = Result.success(null);
            }
            else
            {
                current = outputBytes[startIndex + bytesRead - 1];
                result = Result.success(bytesRead);
            }
        }
        catch (IOException e)
        {
            result = Result.error(e);
        }

        return result;
    }

    @Override
    public void setExceptionHandler(Action1<IOException> exceptionHandler)
    {
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
        return readByte().getValue() != null;
    }
}
