package qub;

/**
 * An array class that contains a generic type of element.
 */
public class ObjectArray<T> implements Array<T>
{
    private final Object[] values;

    private ObjectArray(Object[] values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values = values;
    }

    /**
     * Create a new ObjectArray&lt;T&gt; with the provided length.
     * @param length The number of elements to create in the new ObjectArray&lt;T&gt;.
     * @param <T> The type of value to store in the array.
     * @return The new ObjectArray&lt;T&gt;.
     */
    public static <T> ObjectArray<T> createWithLength(int length)
    {
        PreCondition.assertGreaterThanOrEqualTo(length, 0, "length");

        return new ObjectArray<>(new Object[length]);
    }

    /**
     * Create a new ObjectArray&lt;%&gt; with the provided values.
     * @param values The values to initialize the array with.
     * @param <T> The type of values to store in the array.
     * @return The new ObjectArray&lt;T&gt;.
     */
    @SafeVarargs
    public static <T> ObjectArray<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new ObjectArray<>(values);
    }

    @Override
    public int getCount()
    {
        return values.length;
    }

    @Override
    public ObjectArray<T> set(int index, T value)
    {
        PreCondition.assertIndexAccess(index, this.getCount(), "index");

        values[index] = value;

        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index)
    {
        PreCondition.assertIndexAccess(index, this.getCount(), "index");

        return (T)values[index];
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }
}
