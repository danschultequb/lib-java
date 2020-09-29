package qub;

public class ObjectArrayIterator<T> implements Iterator<T>
{
    private final T[] values;
    private final int startIndex;
    private final int length;
    private boolean hasStarted;
    private int currentIndex;

    public ObjectArrayIterator(T[] values)
    {
        this(values, 0, values.length);
    }

    public ObjectArrayIterator(T[] values, int startIndex, int length)
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
    public T getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return values[currentIndex];
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

    /**
     * Create an iterator for the provided values.
     * @param values The values to iterate over.
     * @return The iterator that will iterate over the provided values.
     */
    @SafeVarargs
    static <T> ObjectArrayIterator<T> create(T... values)
    {
        return new ObjectArrayIterator<>(values);
    }

    /**
     * Create an Iterator for the provided values.
     * @param values The values to iterate.
     * @return The Iterator for the provided values.
     */
    static <T> ObjectArrayIterator<T> create(T[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        return new ObjectArrayIterator<>(values, startIndex, length);
    }
}
