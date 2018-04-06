package qub;

public interface Getable<T>
{
    /**
     * Get whether or not this Getable has a value.
     * @return Whether or not this Getable has a value.
     */
    boolean hasValue();

    /**
     * Get the value that this Getable contains, or null if no value has been set yet.
     * @return The value that this Getable contains, or null if no value has been set yet.
     */
    T get();
}
