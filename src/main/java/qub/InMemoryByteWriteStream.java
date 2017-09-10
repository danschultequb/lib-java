package qub;

public class InMemoryByteWriteStream implements ByteWriteStream
{
    private ArrayList<Byte> bytes;

    public InMemoryByteWriteStream()
    {
        bytes = new ArrayList<Byte>();
    }

    @Override
    public boolean isOpen()
    {
        return bytes != null;
    }

    @Override
    public boolean close()
    {
        final boolean closed = bytes != null;
        bytes = null;
        return closed;
    }

    public byte[] getBytes()
    {
        byte[] result;
        if (bytes == null)
        {
            result = null;
        }
        else
        {
            result = new byte[bytes.getCount()];
            for (int i = 0; i < result.length; ++i)
            {
                result[i] = bytes.get(i);

            }
        }
        return result;
    }

    @Override
    public void write(byte toWrite)
    {
        if (bytes != null)
        {
            bytes.add(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite)
    {
        write(toWrite, 0, toWrite.length);
    }

    @Override
    public void write(byte[] toWrite, int startIndex, int length)
    {
        if (bytes != null)
        {
            final int endIndex = Math.minimum(toWrite.length, startIndex + length);
            for (int i = startIndex; i < endIndex; ++i)
            {
                write(toWrite[i]);
            }
        }
    }
}
