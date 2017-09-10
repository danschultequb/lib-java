package qub;

public class StandardErrorByteWriteStream implements ByteWriteStream
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
}
