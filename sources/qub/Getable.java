package qub;

/**
 * A Type that can only be read from.
 * @param <T> The type of value that can be read.
 */
public interface Getable<T>
{
    /**
     * Get whether or not this object has a value.
     * @return Whether or not this object has a value.
     */
    boolean hasValue();

    /**
     * Get the value that this Getable contains, or null if no value has been set yet.
     * @return The value that this Getable contains, or null if no value has been set yet.
     */
    T get();
}
