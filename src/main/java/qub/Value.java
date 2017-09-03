package qub;

/**
 * A wrapper around a value of type T.
 * @param <T> The type of the inner value.
 */
public class Value<T>
{
    private T value;
    private boolean hasValue;

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

    /**
     * Get whether or not this Value has a value.
     * @return Whether or not this Value has a value.
     */
    public boolean hasValue()
    {
        return hasValue;
    }

    /**
     * Get the value that this Value contains, or null if no value has been set yet.
     * @return The value that this Value contains, or null if no value has been set yet.
     */
    public T get()
    {
        return value;
    }

    /**
     * Set the value that this Value contains.
     * @param value The value to set.
     */
    public void set(T value)
    {
        this.value = value;
        hasValue = true;
    }

    /**
     * Clear any value that may have been set.
     */
    public void clear()
    {
        this.value = null;
        hasValue = false;
    }
}
