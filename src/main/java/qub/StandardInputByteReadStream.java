package qub;

import java.io.IOException;

/**
 * A ByteReadStream that reads bytes from the StandardInput stream of the process.
 */
public class StandardInputByteReadStream extends ByteReadStreamBase
{
    @Override
    public boolean isOpen()
    {
        return true;
    }

    @Override
    public boolean close()
    {
        return false;
    }

    @Override
    public int readBytes(byte[] output, int startIndex, int length) throws IOException
    {
        return System.in.read(output, startIndex, length);
    }
}
