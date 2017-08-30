package qub;

/**
 * An Iterator that iterates over an Array.
 * @param <T> The type of elements that the Array contains.
 */
class ArrayIterator<T> implements qub.Iterator<T>
{
    private final Array<T> array;
    private int currentIndex;

    /**
     * Create a new Iterator that will iterate over the provided Array.
     * @param array The Array to iterate over.
     */
    ArrayIterator(Array<T> array)
    {
        this.array = array;
        currentIndex = -1;
    }

    @Override
    public boolean hasStarted()
    {
        return 0 <= currentIndex;
    }

    @Override
    public boolean hasCurrent()
    {
        return hasStarted() && currentIndex < array.getCount();
    }

    @Override
    public T getCurrent() {
        return hasCurrent() ? array.get(currentIndex) : null;
    }

    @Override
    public boolean next() {
        if (currentIndex < array.getCount()) {
            ++currentIndex;
        }
        return hasCurrent();
    }
}