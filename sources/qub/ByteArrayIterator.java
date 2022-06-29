package qub;

public class ByteArrayIterator implements Iterator<Byte>
{
    private final byte[] bytes;
    private boolean hasStarted;
    private int currentIndex;
    private final int endIndex;

    private ByteArrayIterator(byte[] bytes, int startIndex, int length)
    {
        this.bytes = bytes;
        this.currentIndex = startIndex;
        this.endIndex = startIndex + length;
    }

    public static ByteArrayIterator create(byte[] bytes)
    {
        return ByteArrayIterator.create(bytes, 0, bytes.length);
    }

    public static ByteArrayIterator create(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertStartIndex(startIndex, bytes.length);
        PreCondition.assertLength(length, startIndex, bytes.length);

        return new ByteArrayIterator(bytes, startIndex, length);
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasStarted && this.currentIndex < this.endIndex;
    }

    @Override
    public Byte getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.bytes[this.currentIndex];
    }

    @Override
    public boolean next()
    {
        if (!this.hasStarted)
        {
            this.hasStarted = true;
        }
        else if (this.currentIndex < this.endIndex)
        {
            ++this.currentIndex;
        }
        return this.currentIndex < this.endIndex;
    }
}
