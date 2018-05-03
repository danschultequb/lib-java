package qub;

import java.io.IOException;
import java.io.InputStream;

/**
 * An adapter that converts a java.io.InputStream to a qub.ByteReadStream.
 */
public class InputStreamToByteReadStream extends ByteReadStreamBase
{
    private final AsyncRunner asyncRunner;
    private final InputStream inputStream;

    private boolean disposed;
    private boolean hasStarted;
    private Byte current;

    public InputStreamToByteReadStream(InputStream inputStream, AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
        this.inputStream = inputStream;
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
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
        Result<Byte> result = ByteReadStreamBase.validateByteReadStream(this);
        if (result == null)
        {
            try
            {
                hasStarted = true;

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
        }
        return result;
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        Result<Integer> result = ByteReadStreamBase.validateByteReadStream(this);
        if (result == null)
        {
            result = ByteReadStreamBase.validateOutputBytes(outputBytes);
            if (result == null)
            {
                result = ByteReadStreamBase.validateStartIndex(startIndex, outputBytes);
                if (result == null)
                {
                    result = ByteReadStreamBase.validateLength(length, outputBytes, startIndex);
                    if (result == null)
                    {
                        try
                        {
                            hasStarted = true;

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
                    }
                }
            }
        }
        return result;
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
