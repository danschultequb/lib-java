package qub;

/**
 * A type that can only be written to.
 * @param <T> The Type of value that can be written.
 */
public interface Setter<T>
{
    /**
     * Clear any value that has been assigned to this object.
     */
    Setter<T> clear();

    /**
     * Set the value that this Setable contains.
     * @param value The value to set.
     */
    Setter<T> set(T value);
}
