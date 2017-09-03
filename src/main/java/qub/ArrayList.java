package qub;

/**
 * A array-based List data structure that can expand when it gets full.
 * @param <T> The type of element stored in this List.
 */
public class ArrayList<T> implements Iterable<T>
{
    private Array<T> array;
    private int elementCount;

    public ArrayList()
    {
        array = new Array<>(0);
    }

    /**
     * Get the value at the provided index. If the index is not in the bounds of this ArrayList,
     * then null will be returned.
     * @param index The index to retrieve.
     * @return The value at the provided index.
     */
    public T get(int index)
    {
        return (0 <= index && index < elementCount) ? array.get(index) : null;
    }

    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    public void set(int index, T value)
    {
        if (0 < index && index < elementCount)
        {
            array.set(index, value);
        }
    }

    /**
     * Add the provided value at the end of this ArrayList.
     * @param value The value to add.
     */
    public void add(T value)
    {
        if (elementCount >= array.getCount())
        {
            final Array<T> newArray = new Array<T>((array.getCount() * 2) + 1);
            for (int i = 0; i < array.getCount(); ++i)
            {
                newArray.set(i, array.get(i));
            }
            array = newArray;
        }

        array.set(elementCount, value);
        ++elementCount;
    }

    /**
     * Add the provided values at the end of this ArrayList.
     * @param values The values to add.
     */
    public void addAll(T... values)
    {
        for (T value : values)
        {
            add(value);
        }
    }

    @Override
    public Iterator<T> iterate()
    {
        return array.iterate().take(elementCount);
    }

    @Override
    public boolean any()
    {
        return elementCount > 0;
    }

    @Override
    public int getCount()
    {
        return elementCount;
    }
}
