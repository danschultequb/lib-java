package qub;

public class ByteArrayIterator implements Iterator<Byte>
{
    private final ByteArray byteArray;
    private boolean hasStarted;
    private int currentIndex;

    public ByteArrayIterator(ByteArray byteArray)
    {
        this.byteArray = byteArray;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return hasStarted && currentIndex < byteArray.getCount();
    }

    @Override
    public Byte getCurrent()
    {
        return byteArray.get(currentIndex);
    }

    @Override
    public boolean next()
    {
        if (!hasStarted)
        {
            hasStarted = true;
        }
        else if (currentIndex < byteArray.getCount())
        {
            ++currentIndex;
        }
        return currentIndex < byteArray.getCount();
    }
}
