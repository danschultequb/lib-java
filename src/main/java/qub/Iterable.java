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
     * Create a new Iterable that will skip over the first toSkip number of elements in this
     * Iterable and then return the remaining elements.
     * @param toSkip The number of elements to skip.
     * @return A new Iterable that will skip over the first toSkip number of elements in this
     * Iterable and then return the remaining elements.
     */
    Iterable<T> skip(int toSkip);

    /**
     * Create a new Iterable that only returns the values from this Iterable that satisfy the given
     * condition.
     * @param condition The condition values must satisfy to be returned from the created Iterable.
     * @return An Iterable that only returns the values from this Iterator that satisfy the given
     * condition.
     */
    Iterable<T> where(Function1<T,Boolean> condition);

    /**
     * Convert this Iterable into an Iterable that returns values of type U instead of type T.
     * @param conversion The function to use to convert values of type T to type U.
     * @param <U> The type to convert values of type T to.
     * @return An Iterable that returns values of type U instead of type T.
     */
    <U> Iterable<U> map(Function1<T,U> conversion);

    /**
     * Convert this Iterable into an Iterable that only returns the values in this Iterable that are
     * of type or sub-classes of type U.
     * @param type The type to filter the results to.
     * @param <U> The type to return.
     * @return An Iterable that only returns the values in this Iterable that are of type or
     * sub-classes of type U.
     */
    <U> Iterable<U> instanceOf(Class<U> type);

    /**
     * Create a java.util.Iterator that will iterate over this Iterable.
     * @return A java.util.Iterator that will iterate over this Iterable.
     */
    java.util.Iterator<T> iterator();
}
