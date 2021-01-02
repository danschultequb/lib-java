package qub;

public class LongArrayIterator implements Iterator<Long>
{
    private final long[] values;
    private final int startIndex;
    private final int length;
    private boolean hasStarted;
    private int currentIndex;

    private LongArrayIterator(long[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        this.values = values;
        this.startIndex = startIndex;
        this.length = length;
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasStarted && this.currentIndex < this.startIndex + this.length;
    }

    @Override
    public Long getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.values[this.currentIndex];
    }

    @Override
    public boolean next()
    {
        if (!this.hasStarted)
        {
            this.hasStarted = true;
            this.currentIndex = this.startIndex;
        }
        else if (this.hasCurrent())
        {
            ++this.currentIndex;
        }
        return this.hasCurrent();
    }

    /**
     * Create an iterator for the provided values.
     * @param values The values to iterate over.
     * @return The iterator that will iterate over the provided values.
     */
    static LongArrayIterator create(long... values)
    {
        PreCondition.assertNotNull(values, "values");

        return LongArrayIterator.create(values, 0, values.length);
    }

    /**
     * Create an Iterator for the provided values.
     * @param values The values to iterate.
     * @return The Iterator for the provided values.
     */
    static LongArrayIterator create(long[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        return new LongArrayIterator(values, startIndex, length);
    }
}
