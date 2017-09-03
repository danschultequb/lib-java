package qub;

public class InMemoryWriteStream implements WriteStream
{
    private final ArrayList<Byte> bytes;

    public InMemoryWriteStream()
    {
        bytes = new ArrayList<Byte>();
    }

    @Override
    public void write(byte toWrite)
    {
        bytes.add(toWrite);
    }

    @Override
    public void write(byte[] toWrite)
    {
        write(toWrite, 0, toWrite.length);
    }

    @Override
    public void write(byte[] toWrite, int startIndex, int length)
    {
        for (int i = startIndex; i < toWrite.length && i < length; ++i)
        {
            write(toWrite[i]);
        }
    }
}
