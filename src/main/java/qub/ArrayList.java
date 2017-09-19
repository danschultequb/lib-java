package qub;

/**
 * A array-based List data structure that can expand when it gets full.
 * @param <T> The type of element stored in this List.
 */
public class ArrayList<T> extends IterableBase<T>
{
    private Array<T> array;
    private int elementCount;

    public ArrayList()
    {
        this(new Array<T>(0));
    }

    private ArrayList(Array<T> array)
    {
        this.array = array;
        this.elementCount = array.getCount();
    }

    private boolean inBounds(int index)
    {
        return 0 <= index && index < elementCount;
    }

    /**
     * Get the value at the provided index. If the index is not in the bounds of this ArrayList,
     * then null will be returned.
     * @param index The index to retrieve.
     * @return The value at the provided index.
     */
    public T get(int index)
    {
        return inBounds(index) ? array.get(index) : null;
    }

    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    public void set(int index, T value)
    {
        if (inBounds(index))
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
        if (values != null && values.length > 0)
        {
            for (final T value : values)
            {
                add(value);
            }
        }
    }

    /**
     * Add the provided values at the end of this ArrayList.
     * @param values The values to add.
     */
    public void addAll(Iterator<T> values)
    {
        if (values != null && values.any())
        {
            for (final T value : values)
            {
                add(value);
            }
        }
    }

    /**
     * Add the provided values at the end of this ArrayList.
     * @param values The values to add.
     */
    public void addAll(Iterable<T> values)
    {
        if (values != null && values.any())
        {
            for (final T value : values)
            {
                add(value);
            }
        }
    }

    /**
     * Remove and return the value at the provided index. If the index is not in the bounds of this
     * ArrayList, then no values will be removed and null will be returned.
     * @param index The index to remove from.
     * @return The value that was removed or null if the index is out of bounds.
     */
    public T removeAt(int index)
    {
        T result = null;

        if (inBounds(index))
        {
            result = get(index);
            for (int i = index; i < getCount() - 1; ++i)
            {
                set(i, get(i + 1));
            }
            --elementCount;
        }

        return result;
    }

    /**
     * Remove and return the first value in this ArrayList. If this ArrayList is empty, then null
     * will be returned.
     * @return The value that was removed, or null if the ArrayList was empty.
     */
    public T removeFirst()
    {
        return removeAt(0);
    }

    public void clear()
    {
        elementCount = 0;
        array.setAll(null);
    }

    @Override
    public Iterator<T> iterate()
    {
        return array.iterate().take(elementCount);
    }

    /**
     * Get an Iterator that will iterate over the contents of this ArrayList in reverse.
     * @return An Iterator that will iterate over the contents of this ArrayList in reverse.
     */
    public Iterator<T> iterateReverse()
    {
        return array.iterateReverse().skip(array.getCount() - elementCount);
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

    public static <T> ArrayList<T> fromValues(T... values)
    {
        final Array<T> array = Array.fromValues(values);
        return new ArrayList<>(array);
    }

    public static <T> ArrayList<T> fromValues(Iterator<T> values)
    {
        final ArrayList<T> result = new ArrayList<>();
        result.addAll(values);
        return result;
    }

    public static <T> ArrayList<T> fromValues(Iterable<T> values)
    {
        final Array<T> array = Array.fromValues(values);
        return new ArrayList<>(array);
    }
}
