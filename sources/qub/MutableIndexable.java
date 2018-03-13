package qub;

public interface MutableIndexable<T> extends Indexable<T>
{
    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    void set(int index, T value);
}
