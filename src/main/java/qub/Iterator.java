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
     * Get whether or not this Iterator contains any values. This function may move this Iterator
     * forward one position, but it can be called multiple times without consuming any of the
     * values in this Iterator.
     * @return Whether or not this Iterator contains any values.
     */
    boolean any();

    /**
     * Get whether or not this Iterator contains any values that satisfy the provided condition.
     * This function will consume as many values of this Iterator as it takes to find a value that
     * satisfies the condition.
     * @return Whether or not this Iterator contains any values that satisfy the provided condition.
     */
    boolean any(Function1<T,Boolean> condition);

    /**
     * Get the number of values that are in this Iterator. This will iterate through all of the
     * values in this Iterator. Use this method only if you care how many values are in the
     * Iterator, not what the values actually are.
     * @return The number of values that are in this Iterator.
     */
    int getCount();

    /**
     * Create a new Iterator that will iterate over no more than the provided number of values from
     * this Iterator.
     * @param toTake The number of values to take from this Iterator.
     * @return A new Iterator that will iterate over no more than the provided number of values from
     * this Iterator.
     */
    Iterator<T> take(int toTake);

    /**
     * Create a new Iterator that will skip over the first toSkip number of elements in this
     * Iterator and then iterate over the remaining elements.
     * @param toSkip The number of elements to skip.
     * @return A new Iterator that will skip over the first toSkip number of elements in this
     * Iterator and then iterate over the remaining elements.
     */
    Iterator<T> skip(int toSkip);

    /**
     * Create a java.util.Iterator that will iterate over this Iterator.
     * @return A java.util.Iterator that will iterate over this Iterator.
     */
    java.util.Iterator<T> iterator();
}