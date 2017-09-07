package qub;

/**
 * An in-memory implementation of a ByteReadStream.
 */
public class InMemoryByteReadStream extends ByteReadStreamBase
{
    private ArrayList<Byte> bytes;

    public InMemoryByteReadStream()
    {
        bytes = new ArrayList<>();
    }

    @Override
    public boolean isOpen()
    {
        return bytes != null;
    }

    @Override
    public boolean close()
    {
        final boolean closed = (bytes != null);
        bytes = null;
        return closed;
    }

    /**
     * Get the bytes that are waiting to be read from this InMemoryByteReadStream.
     * @return The bytes that are waiting to be read from this InMemoryByteReadStream.
     */
    public byte[] getBytes()
    {
        byte[] result;
        if (!isOpen())
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

    /**
     * Add the provided bytes to this InMemoryByteReadStream.
     * @param bytesToAdd The bytes to add to this InMemoryByteReadStream.
     */
    public void add(byte... bytesToAdd)
    {
        if (isOpen() && bytesToAdd != null && bytesToAdd.length > 0)
        {
            for (byte byteToAdd : bytesToAdd)
            {
                bytes.add(byteToAdd);
            }
        }
    }

    public void add(String toAdd)
    {
        if (isOpen())
        {
            add(toAdd, CharacterEncoding.ASCII);
        }
    }

    public void add(String toAdd, CharacterEncoding encoding)
    {
        if (isOpen())
        {
            add(encoding.encode(toAdd));
        }
    }

    @Override
    public int readBytes(byte[] output, int startIndex, int length)
    {
        int bytesRead;
        if (!isOpen())
        {
            bytesRead = -1;
        }
        else
        {
            bytesRead = Math.maximum(0, Math.minimum(length, bytes.getCount()));

            for (int i = 0; i < bytesRead; ++i)
            {
                output[startIndex + i] = bytes.removeFirst();
            }
        }

        return bytesRead;
    }
}
