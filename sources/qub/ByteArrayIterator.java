package qub;

public class ByteArrayIterator implements Iterator<Byte>
{
    private final byte[] bytes;
    private boolean hasStarted;
    private int currentIndex;
    private final int endIndex;

    public ByteArrayIterator(byte[] bytes)
    {
        this(bytes, 0, bytes.length);
    }

    public ByteArrayIterator(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertStartIndex(startIndex, bytes.length);
        PreCondition.assertLength(length, startIndex, bytes.length);

        this.bytes = bytes;
        this.currentIndex = startIndex;
        this.endIndex = startIndex + length;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return hasStarted && currentIndex < endIndex;
    }

    @Override
    public Byte getCurrent()
    {
        PreCondition.assertTrue(hasCurrent(), "hasCurrent()");

        return bytes[currentIndex];
    }

    @Override
    public boolean next()
    {
        if (!hasStarted)
        {
            hasStarted = true;
        }
        else if (currentIndex < endIndex)
        {
            ++currentIndex;
        }
        return currentIndex < endIndex;
    }
}
