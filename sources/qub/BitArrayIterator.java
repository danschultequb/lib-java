package qub;

public class BitArrayIterator extends IteratorBase<Integer>
{
    private final BitArray source;
    private long currentIndex;

    public BitArrayIterator(BitArray source)
    {
        this.source = source;
        this.currentIndex = -1;
    }

    @Override
    public boolean hasStarted()
    {
        return 0 <= currentIndex;
    }

    @Override
    public boolean hasCurrent()
    {
        return 0 <= currentIndex && currentIndex < source.getBitCount();
    }

    @Override
    public Integer getCurrent()
    {
        return hasCurrent() ? source.getBit(currentIndex) : null;
    }

    @Override
    public boolean next()
    {
        if (currentIndex < source.getBitCount())
        {
            ++currentIndex;
        }
        return hasCurrent();
    }
}
