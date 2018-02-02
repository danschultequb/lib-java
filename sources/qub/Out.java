package qub;

/**
 * A parameter type that can only be written to.
 */
public interface Out<T>
{
    /**
     * Set the value that this Out contains.
     * @param value The value to set.
     */
    void set(T value);

    /**
     * Clear any value that may have been set.
     */
    void clear();
}
