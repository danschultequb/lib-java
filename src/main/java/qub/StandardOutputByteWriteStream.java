package qub;

/**
 * A TextWriteStream that writes bytes and Strings to the StandardOutput stream of the process.
 */
public class StandardOutputByteWriteStream implements ByteWriteStream
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
