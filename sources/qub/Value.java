package qub;

/**
 * A wrapper around a value of type T.
 * @param <T> The type of the inner value.
 */
public class Value<T> implements Getable<T>, Setable<T>
{
    private volatile T value;
    private volatile boolean hasValue;

    /**
     * Create a new Value with no inner value.
     */
    public Value()
    {
        value = null;
        hasValue = false;
    }

    /**
     * Create a new Value with the provided inner value.
     * @param value The inner value to store in this Value object.
     */
    public Value(T value)
    {
        this.value = value;
        hasValue = true;
    }

    @Override
    public boolean hasValue()
    {
        return hasValue;
    }

    @Override
    public T get()
    {
        return value;
    }

    @Override
    public void set(T value)
    {
        this.value = value;
        hasValue = true;
    }

    @Override
    public void clear()
    {
        value = null;
        hasValue = false;
    }

    @Override
    public String toString()
    {
        return value == null ? null : value.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object rhs)
    {
        return rhs instanceof Value && equals((Value<T>)rhs);
    }

    public boolean equals(Value<T> rhs)
    {
        return rhs != null && hasValue == rhs.hasValue && Comparer.equal(value, rhs.value);
    }
}
