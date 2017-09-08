package qub;

/**
 * A TextWriteStream that writes bytes and Strings to the StandardOutput stream of the process.
 */
public class StandardOutputWriteStream extends ByteWriteStreamBase implements TextWriteStream
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

    @Override
    public void write(String toWrite)
    {
        System.out.print(toWrite);
    }

    @Override
    public void writeLine()
    {
        System.out.println();
    }

    @Override
    public void writeLine(String toWrite)
    {
        System.out.println(toWrite);
    }
}
