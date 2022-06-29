package qub;

/**
 * A Type that can only be read from.
 * @param <T> The type of value that can be read.
 */
public interface Getter<T>
{
    /**
     * Get whether this object has a value.
     */
    boolean hasValue();

    /**
     * Get the value that this {@link Getter} contains, or null if no value has been set yet.
     */
    T get();
}
