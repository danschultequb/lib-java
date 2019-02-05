package qub;

public class ByteArrayIterator implements Iterator<Byte>
{
    private final byte[] bytes;
    private final int startIndex;
    private final int length;
    private boolean hasStarted;
    private int currentIndex;

    public ByteArrayIterator(byte[] bytes)
    {
        this(bytes, 0, bytes.length);
    }

    public ByteArrayIterator(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertBetween(0, startIndex, bytes.length - 1, "startIndex");
        PreCondition.assertBetween(0, length, bytes.length - startIndex, "length");

        this.bytes = bytes;
        this.startIndex = startIndex;
        this.length = length;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return hasStarted && currentIndex < length;
    }

    @Override
    public Byte getCurrent()
    {
        return bytes[currentIndex];
    }

    @Override
    public boolean next()
    {
        if (!hasStarted)
        {
            hasStarted = true;
        }
        else if (currentIndex < length)
        {
            ++currentIndex;
        }
        return currentIndex < length;
    }
}
