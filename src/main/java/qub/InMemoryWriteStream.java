package qub;

public class InMemoryWriteStream extends WriteStreamBase
{
    private final ArrayList<Byte> bytes;

    public InMemoryWriteStream()
    {
        bytes = new ArrayList<Byte>();
    }

    public byte[] getBytes()
    {
        final byte[] result = new byte[bytes.getCount()];
        for (int i = 0; i < result.length; ++i)
        {
            result[i] = bytes.get(i);
        }
        return result;
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
        final int endIndex = Math.minimum(toWrite.length, startIndex + length);
        for (int i = startIndex; i < endIndex; ++i)
        {
            write(toWrite[i]);
        }
    }
}