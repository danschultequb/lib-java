package qub;

public interface Indexable<T> extends Iterable<T>
{
    /**
     * Get the element at the provided index. If the provided index is outside of the bounds of this
     * Indexable, then null will be returned.
     * @param index The index of the element to return.
     * @return The element at the provided index, or null if the provided index is out of bounds.
     */
    T get(int index);

    /**
     * Get the index of the first element in this Indexable that satisfies the provided condition,
     * or -1 if no element matches the condition.
     * @param condition The condition to compare against the elements in this Indexable.
     * @return The index of the first element that satisfies the provided condition or -1 if no
     * element matches the condition.
     */
    int indexOf(Function1<T,Boolean> condition);

    /**
     * Get the index of the first element in this Indexable that equals the provided value or -1 if
     * no element equals the value.
     * @param value The value to look for in this Indexable.
     * @return The index of the first element that equals the provided value or -1 if no element
     * equals the provided value.
     */
    int indexOf(T value);

    /**
     * Convert this Indexable into an Indexable that returns values of type U instead of type T.
     * @param conversion The function to use to convert values of type T to type U.
     * @param <U> The type to convert values of type T to.
     * @return An Indexable that returns values of type U instead of type T.
     */
    <U> Indexable<U> map(Function1<T,U> conversion);
}
