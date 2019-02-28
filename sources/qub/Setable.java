package qub;

/**
 * A type that can only be written to.
 * @param <T> The Type of value that can be written.
 */
public interface Setable<T>
{
    /**
     * Clear any value that has been assigned to this object.
     */
    void clear();

    /**
     * Set the value that this Setable contains.
     * @param value The value to set.
     */
    void set(T value);
}
