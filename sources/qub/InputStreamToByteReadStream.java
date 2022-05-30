package qub;

/**
 * An adapter that converts a java.io.InputStream to a qub.ByteReadStream.
 */
public class InputStreamToByteReadStream implements ByteReadStream
{
    private final java.io.InputStream inputStream;

    private boolean disposed;

    private InputStreamToByteReadStream(java.io.InputStream inputStream)
    {
        PreCondition.assertNotNull(inputStream, "inputStream");

        this.inputStream = inputStream;
    }

    public static InputStreamToByteReadStream create(java.io.InputStream inputStream)
    {
        return new InputStreamToByteReadStream(inputStream);
    }

    @Override
    final public boolean isDisposed()
    {
        return this.disposed;
    }

    @Override
    final public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            boolean result = false;
            if (!this.disposed)
            {
                this.disposed = true;
                try
                {
                    this.inputStream.close();
                    result = true;
                }
                catch (java.io.IOException e)
                {
                    throw Exceptions.asRuntime(e);
                }
            }
            return result;
        });
    }

    @Override
    public Result<Byte> readByte()
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            byte result;
            try
            {
                final int byteAsInt = this.inputStream.read();
                if (byteAsInt == -1)
                {
                    throw new EmptyException();
                }
                result = (byte)byteAsInt;
            }
            catch (java.io.IOException e)
            {
                throw Exceptions.asRuntime(e);
            }
            return result;
        });
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(outputBytes, "outputBytes");
        PreCondition.assertStartIndex(startIndex, outputBytes.length);
        PreCondition.assertLength(length, startIndex, outputBytes.length);
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            int result = 0;
            if (length > 0)
            {
                try
                {
                    result = this.inputStream.read(outputBytes, startIndex, length);
                    if (result == -1)
                    {
                        throw new EmptyException();
                    }
                }
                catch (java.io.IOException e)
                {
                    throw Exceptions.asRuntime(e);
                }

            }
            return result;
        });
    }
}
