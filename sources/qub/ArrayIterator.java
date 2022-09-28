package qub;

/**
 * An Iterator that iterates over an Array.
 * @param <T> The type of elements that the Array contains.
 */
public class ArrayIterator<T> implements Iterator<T>
{
    private final Array<T> array;
    private int currentIndex;
    private boolean hasStarted;
    private final int endIndex;
    private final int step;

    private ArrayIterator(Array<T> array, int startIndex, int endIndex)
    {
        this.array = array;
        currentIndex = startIndex;
        hasStarted = false;
        this.endIndex = endIndex;
        step = startIndex <= endIndex ? 1 : -1;
    }

    public static <T> ArrayIterator<T> create(Array<T> array)
    {
        PreCondition.assertNotNull(array, "array");

        return ArrayIterator.create(array, 0, array.getCount());
    }

    public static <T> ArrayIterator<T> create(Array<T> array, int startIndex, int endIndex)
    {
        PreCondition.assertNotNull(array, "array");

        return new ArrayIterator<>(array, startIndex, endIndex);
    }

    public static <T> ArrayIterator<T> createReverse(Array<T> array)
    {
        PreCondition.assertNotNull(array, "array");

        return new ArrayIterator<>(array, array.getCount() - 1, -1);
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasStarted && this.currentIndex != this.endIndex;
    }

    @Override
    public T getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return array.get(currentIndex);
    }

    @Override
    public boolean next()
    {
        if (!hasStarted)
        {
            hasStarted = true;
        }
        else if (currentIndex != endIndex)
        {
            currentIndex += step;
        }
        return currentIndex != endIndex;
    }
}