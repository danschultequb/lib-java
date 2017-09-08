package qub;

/**
 * A TextWriteStream that writes bytes and Strings to the StandardError stream of the process.
 */
public class StandardErrorWriteStream extends ByteWriteStreamBase implements TextWriteStream
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
        System.err.write(toWrite);
    }

    @Override
    public void write(byte[] toWrite)
    {
        System.err.write(toWrite, 0, toWrite.length);
    }

    @Override
    public void write(byte[] toWrite, int startIndex, int length)
    {
        System.err.write(toWrite, startIndex, length);
    }

    @Override
    public void write(String toWrite)
    {
        System.err.print(toWrite);
    }

    @Override
    public void writeLine()
    {
        System.err.println();
    }

    @Override
    public void writeLine(String toWrite)
    {
        System.err.println(toWrite);
    }
}
