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
    boolean any(Function1<T,Boolean> condition);

    /**
     * Create a new Iterable that restricts this Iterable to a fixed number of values.
     * @param toTake The number of values to constrain this Iterable to.
     * @return A new Iterable that restricts this Iterable to a fixed number of values.
     */
    Iterable<T> take(int toTake);

    /**
     * Create a new Iterable that only returns the values from this Iterable that satisfy the given
     * condition.
     * @param condition The condition values must satisfy to be returned from the created Iterable.
     * @return An Iterable that only returns the values from this Iterator that satisfy the given
     * condition.
     */
    Iterable<T> where(Function1<T,Boolean> condition);

    /**
     * Create a java.util.Iterator that will iterate over this Iterable.
     * @return A java.util.Iterator that will iterate over this Iterable.
     */
    java.util.Iterator<T> iterator();
}
