package qub;

/**
 * An adapter that converts a java.io.InputStream to a qub.ByteReadStream.
 */
public class InputStreamToByteReadStream implements ByteReadStream
{
    private final java.io.InputStream inputStream;

    private boolean disposed;
    private boolean hasStarted;
    private Byte current;

    public InputStreamToByteReadStream(java.io.InputStream inputStream)
    {
        PreCondition.assertNotNull(inputStream, "inputStream");

        this.inputStream = inputStream;
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
            result = Result.successFalse();
        }
        else
        {
            disposed = true;
            try
            {
                inputStream.close();
                result = Result.successTrue();
            }
            catch (java.io.IOException e)
            {
                result = Result.error(e);
            }
        }
        return result;
    }

    @Override
    public Result<Byte> readByte()
    {
        PreCondition.assertNotDisposed(this);

        Result<Byte> result;
        try
        {
            hasStarted = true;

            final int byteAsInt = inputStream.read();
            if (byteAsInt == -1)
            {
                current = null;
                result = Result.endOfStream();
            }
            else
            {
                current = (byte)byteAsInt;
                result = Result.success(current);
            }
        }
        catch (java.io.IOException e)
        {
            current = null;
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(outputBytes, "outputBytes");
        PreCondition.assertStartIndex(startIndex, outputBytes.length);
        PreCondition.assertLength(length, startIndex, outputBytes.length);
        PreCondition.assertNotDisposed(this);

        Result<Integer> result;
        try
        {
            if (length == 0)
            {
                result = Result.successZero();
            }
            else
            {
                hasStarted = true;

                final int bytesRead = inputStream.read(outputBytes, startIndex, length);
                if (bytesRead == -1)
                {
                    current = null;
                    result = Result.endOfStream();
                }
                else
                {
                    current = outputBytes[startIndex + bytesRead - 1];
                    result = Result.success(bytesRead);
                }
            }
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
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
}
