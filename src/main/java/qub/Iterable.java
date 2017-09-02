package qub;

/**
 * An interface of a collection that can have its contents iterated through multiple times.
 * @param <T> The type of value that this Iterable contains.
 */
public interface Iterable<T> extends java.lang.Iterable<T>
{
    /**
     * Get an Iterator that will iterate over the contents of this Iterable.
     * @return An Iterator that will iterate over the contents of this Iterable.
     */
    Iterator<T> iterate();

    /**
     * Get whether or not this Iterable contains any values.
     * @return Whether or not this Iterable contains any values.
     */
    boolean any();

    /**
     * Get the number of values that are in this Iterable.
     * @return The number of values that are in this Iterable.
     */
    int getCount();

    /**
     * Get whether or not this Iterable contains any values that satisfy the provided condition.
     * @return Whether or not this Iterable contains any values that satisfy the provided condition.
     */
    default boolean any(Function1<T,Boolean> condition)
    {
        return iterate().any(condition);
    }

    /**
     * Create a new Iterable that restricts this Iterable to a fixed number of values.
     * @param toTake The number of values to constrain this Iterable to.
     * @return A new Iterable that restricts this Iterable to a fixed number of values.
     */
    default Iterable<T> take(int toTake)
    {
        return new TakeIterable<>(this, toTake);
    }

    /**
     * Create a java.util.Iterator that will iterate over this Iterable.
     * @return A java.util.Iterator that will iterate over this Iterable.
     */
    default java.util.Iterator<T> iterator()
    {
        return iterate().iterator();
    }
}
