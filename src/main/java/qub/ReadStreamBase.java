package qub;

import java.io.IOException;

/**
 * A ReadStream base implementation that adds common implementations for some methods.
 */
public abstract class ReadStreamBase implements ReadStream
{
    @Override
    public byte[] readBytes(int bytesToRead) throws IOException
    {
        if (bytesToRead < 0) {
            bytesToRead = 0;
        }

        byte[] result = new byte[bytesToRead];

        if (bytesToRead > 0)
        {
            final int bytesRead = readBytes(result);
            if (bytesRead == -1)
            {
                result = null;
            }
            else if (bytesRead < bytesToRead)
            {
                final byte[] newResult = new byte[bytesRead];
                if (bytesRead > 0)
                {
                    System.arraycopy(result, 0, newResult, 0, newResult.length);
                }
                result = newResult;
            }
        }

        return result;
    }

    @Override
    public int readBytes(byte[] output) throws IOException
    {
        return readBytes(output, 0, output.length);
    }
}
