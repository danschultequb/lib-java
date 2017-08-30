package qub;

/**
 * The Iterator interface defines a type that can synchronously iterate over a collection of values.
 * @param <T> The type of value that the Iterator returns.
 */
public interface Iterator<T> extends java.lang.Iterable<T>
{
    /**
     * Whether or not this Iterator has begun iterating over its values.
     * @return Whether or not this Iterator has begun iterating over its values.
     */
    boolean hasStarted();

    /**
     * Whether or not this Iterator has a current value.
     * @return Whether or not this Iterator has a current value.
     */
    boolean hasCurrent();

    /**
     * Get the current value that this Iterator is pointing at.
     * @return The current value that this Iterator is pointing at.
     */
    T getCurrent();

    /**
     * Get the next value for this Iterator. Returns whether or not a new value was found.
     * @return Whether or not a new value was found.
     */
    boolean next();

    /**
     * Get the number of values that are in this Iterator. This will iterate through all of the
     * values in this Iterator. Use this method only if you care how many values are in the
     * Iterator, not what the values actually are.
     * @return The number of values that are in this Iterator.
     */
    default int getCount()
    {
        int result = hasCurrent() ? 1 : 0;
        while (next()) {
            ++result;
        }
        return result;
    }

    /**
     * Create a java.util.Iterator that will iterate over this Iterator.
     * @return A java.util.Iterator that will iterate over this Iterator.
     */
    default java.util.Iterator<T> iterator()
    {
        return new JavaIterator<>(this);
    }
}