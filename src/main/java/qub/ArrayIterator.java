package qub;

/**
 * An Iterator that iterates over an Array.
 * @param <T> The type of elements that the Array contains.
 */
class ArrayIterator<T> extends IteratorBase<T>
{
    private final Array<T> array;
    private int currentIndex;
    private boolean hasStarted;
    private final int endIndex;
    private final int step;

    /**
     * Create a new Iterator that will iterate over the provided Array.
     * @param array The Array to iterate over.
     */
    ArrayIterator(Array<T> array)
    {
        this(array, 0, array.getCount());
    }

    ArrayIterator(Array<T> array, int startIndex, int endIndex)
    {
        this.array = array;
        currentIndex = startIndex;
        hasStarted = false;
        this.endIndex = endIndex;
        step = startIndex <= endIndex ? 1 : -1;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return hasStarted() && currentIndex != endIndex;
    }

    @Override
    public T getCurrent() {
        return hasCurrent() ? array.get(currentIndex) : null;
    }

    @Override
    public boolean next() {
        if (!hasStarted)
        {
            hasStarted = true;
        }
        else if (currentIndex != endIndex)
        {
            currentIndex += step;
        }
        return hasCurrent();
    }
}