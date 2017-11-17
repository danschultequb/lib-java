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
     * Get the first value in this Iterable.
     * @return The first value of this Iterable, or null if this Iterable is empty.
     */
    T first();

    /**
     * Get the first value in this Iterable that matches the provided condition.
     * @return The first value of this Iterable that matches the provided condition, or null if this
     * Iterable has no values that match the condition.
     */
    T first(Function1<T,Boolean> condition);

    /**
     * Get the last value in this Iterable.
     * @return The last value in this Iterable, or null if this Iterable is empty.
     */
    T last();

    /**
     * Get the last value in this Iterable that matches the provided condition.
     * @param condition The condition to run against each of the values in this Iterable.
     * @return The last value of this Iterable that matches the provided condition, or null if this
     * Iterable has no values that match the condition.
     */
    T last(Function1<T,Boolean> condition);

    /**
     * Get whether or not this Iterable contains the provided value using the standard equals()
     * method to compare values.
     * @param value The value to look for in this Iterator.
     * @return Whether or not this Iterator contains the provided value.
     */
    boolean contains(T value);

    /**
     * Get whether or not this Iterable contains a value that matches the provided condition.
     * @param condition The condition to check against the values in this Iterable.
     * @return Whether or not this Iterable contains a value that matches the provided condition.
     */
    boolean contains(Function1<T,Boolean> condition);

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
     * Create a new Iterable will skip over the last element in this Iterable and return the
     * remaining elements.
     * @return A new Iterable that will skip over the last element in this Iterable and return the
     * remaining elements.
     */
    Iterable<T> skipLast();

    /**
     * Create a new Iterable that will skip over the last toSkip elements in this Iterable and
     * return the remaining elements.
     * @param toSkip The number of elements to skip from the end.
     * @return A new Iterable that will skip over the last toSkip elements in this Iterable and
     * return the remaining elements.
     */
    Iterable<T> skipLast(int toSkip);

    /**
     * Create a new Iterator that will skip over the elements in this Iterator until it finds an
     * element that makes the provided condition true. The returned Iterator will start at the
     * element after the element that made the condition true.
     * @param condition The condition.
     * @return a new Iterator that will skip over the elements in this Iterator until it finds an
     * element that makes the provided condition true.
     */
    Iterable<T> skipUntil(Function1<T,Boolean> condition);

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
     * Get whether or not this Iterable contains equal elements in the same order as the provided
     * Iterable.
     * @param rhs The Iterable to compare against this Iterable.
     * @return Whether or not this Iterable contains equal elements in the same order as the
     * provided Iterable.
     */
    boolean equals(Object rhs);

    /**
     * Get whether or not this Iterable contains equal elements in the same order as the provided
     * Iterable.
     * @param rhs The Iterable to compare against this Iterable.
     * @return Whether or not this Iterable contains equal elements in the same order as the
     * provided Iterable.
     */
    boolean equals(Iterable<T> rhs);

    /**
     * Create a java.util.Iterator that will iterate over this Iterable.
     * @return A java.util.Iterator that will iterate over this Iterable.
     */
    java.util.Iterator<T> iterator();
}
