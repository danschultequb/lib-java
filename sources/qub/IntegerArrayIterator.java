package qub;

public class IntegerArrayIterator implements Iterator<Integer>
{
    private final int[] values;
    private final int startIndex;
    private final int length;
    private boolean hasStarted;
    private int currentIndex;

    public IntegerArrayIterator(int[] values)
    {
        this(values, 0, values.length);
    }

    public IntegerArrayIterator(int[] values, int startIndex, int length)
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
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return hasStarted && currentIndex < length;
    }

    @Override
    public Integer getCurrent()
    {
        PreCondition.assertTrue(hasCurrent(), "hasCurrent()");

        return values[currentIndex];
    }

    @Override
    public boolean next()
    {
        if (!hasStarted)
        {
            hasStarted = true;
            currentIndex = startIndex;
        }
        else if (currentIndex < length)
        {
            ++currentIndex;
        }
        return currentIndex < length;
    }

    /**
     * Create an iterator for the provided values.
     * @param values The values to iterate over.
     * @return The iterator that will iterate over the provided values.
     */
    static IntegerArrayIterator create(int... values)
    {
        return new IntegerArrayIterator(values);
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
}
