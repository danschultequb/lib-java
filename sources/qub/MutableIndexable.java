package qub;

public interface MutableIndexable<T> extends Indexable<T>
{
    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    void set(int index, T value);

    /**
     * Set the first value in this MutableIndexable.
     * @param value The value to set the first index.
     */
    default void setFirst(T value)
    {
        set(0, value);
    }

    /**
     * Set the last value in this MutableIndexable.
     * @param value The value to set at the last index.
     */
    default void setLast(T value)
    {
        set(getCount() - 1, value);
    }
}
