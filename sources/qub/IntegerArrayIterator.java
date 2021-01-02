package qub;

public class IntegerArrayIterator implements Iterator<Integer>
{
    private final int[] values;
    private final int startIndex;
    private final int length;
    private boolean hasStarted;
    private int currentIndex;

    private IntegerArrayIterator(int[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        this.values = values;
        this.startIndex = startIndex;
        this.length = length;
    }

    /**
     * Create an iterator for the provided values.
     * @param values The values to iterate over.
     * @return The iterator that will iterate over the provided values.
     */
    static IntegerArrayIterator create(int... values)
    {
        PreCondition.assertNotNull(values, "values");
        
        return IntegerArrayIterator.create(values, 0, values.length);
    }

    /**
     * Create an Iterator for the provided values.
     * @param values The values to iterate.
     * @return The Iterator for the provided values.
     */
    static IntegerArrayIterator create(int[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        return new IntegerArrayIterator(values, startIndex, length);
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasStarted && this.currentIndex < this.startIndex + length;
    }

    @Override
    public Integer getCurrent()
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
}
