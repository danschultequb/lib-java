package qub;

import java.io.IOException;

/**
 * A WriteStream that writes bytes to the StandardOutput stream of the process.
 */
public class StandardOutputWriteStream implements WriteStream
{
    @Override
    public void write(byte toWrite)
    {
        System.out.write(toWrite);
    }

    @Override
    public void write(byte[] toWrite)
    {
        System.out.write(toWrite, 0, toWrite.length);
    }

    @Override
    public void write(byte[] toWrite, int startIndex, int length)
    {
        System.out.write(toWrite, startIndex, length);
    }
}
