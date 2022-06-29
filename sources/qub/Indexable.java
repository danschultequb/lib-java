package qub;

public interface Indexable<T> extends Iterable<T>
{
    /**
     * Create a new {@link Indexable} from the provided values.
     * @param values The values to convert to an {@link Indexable}.
     * @param <T> The type of values in the created {@link Indexable}.
     */
    @SafeVarargs
    public static <T> Indexable<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.create(values);
    }

    @Override
    public default boolean any()
    {
        return this.getCount() > 0;
    }

    /**
     * Get the element at the provided index.
     * @param index The index of the element to return.
     */
    public T get(int index);

    /**
     * Get the values over the provided range.
     * @param startIndex The index at which to start the range.
     * @param length The number of values to include in the range.
     */
    public default Indexable<T> getRange(int startIndex, int length)
    {
        return this.skip(startIndex).take(length);
    }

    /**
     * Get the index of the first element in this {@link Indexable} that satisfies the provided
     * condition.
     * @param condition The condition to compare against the elements in this {@link Indexable}.
     */
    public default int indexOf(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        int result = -1;
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

        PostCondition.assertBetween(-1, result, getCount() - 1, "result");

        return result;
    }

    /**
     * Get the index of the first element in this {@link Indexable} that equals the provided value.
     * @param value The value to look for in this {@link Indexable}.
     */
    public default int indexOf(T value)
    {
        return this.indexOf((T element) -> Comparer.equal(element, value));
    }

    /**
     * Create a new {@link Indexable} that restricts this {@link Indexable} to a fixed number of
     * values.
     * @param toTake The number of values to constrain this {@link Indexable} to.
     */
    public default Indexable<T> take(int toTake)
    {
        return TakeIndexable.create(this, toTake);
    }

    /**
     * Create a new {@link Indexable} that will skip over the first toSkip elements in this
     * {@link Indexable} and then return the remaining elements.
     * @param toSkip The number of elements to skip.
     */
    public default Indexable<T> skip(int toSkip)
    {
        return SkipIndexable.create(this, toSkip);
    }

    /**
     * Convert this {@link Indexable} into an {@link Indexable} that returns values of type
     * {@link U} instead of type {@link T}.
     * @param conversion The {@link Function1} to use to convert values of type {@link T} to type
     * {@link U}.
     * @param <U> The type to convert values of type {@link T} to.
     */
    public default <U> Indexable<U> map(Function1<T,U> conversion)
    {
        return MapIndexable.create(this, conversion);
    }
}
