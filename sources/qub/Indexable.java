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
    default int indexOf(Function1<T,Boolean> condition)
    {
        int result = -1;
        if (condition != null)
        {
            int index = 0;
            for (final T element : this)
            {
                if (condition.run(element))
                {
                    result = index;
                    break;
                }
                else
                {
                    ++index;
                }
            }
        }
        return result;
    }

    /**
     * Get the index of the first element in this Indexable that equals the provided value or -1 if
     * no element equals the value.
     * @param value The value to look for in this Indexable.
     * @return The index of the first element that equals the provided value or -1 if no element
     * equals the provided value.
     */
    default int indexOf(T value)
    {
        return indexOf(element -> Comparer.equal(element, value));
    }

    /**
     * Convert this Indexable into an Indexable that returns values of type U instead of type T.
     * @param conversion The function to use to convert values of type T to type U.
     * @param <U> The type to convert values of type T to.
     * @return An Indexable that returns values of type U instead of type T.
     */
    default <U> Indexable<U> map(Function1<T,U> conversion)
    {
        return new MapIndexable<>(this, conversion);
    }
}
