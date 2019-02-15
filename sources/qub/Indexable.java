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
     * Get the values over the provided range.
     * @param startIndex The index at which to start the range.
     * @param length The number of values to include in the range.
     * @return The values over the provided range.
     */
    default Indexable<T> getRange(int startIndex, int length)
    {
        return skip(startIndex).take(length);
    }

    /**
     * Get the index of the first element in this Indexable that satisfies the provided condition,
     * or -1 if no element isMatch the condition.
     * @param condition The condition to compare against the elements in this Indexable.
     * @return The index of the first element that satisfies the provided condition or -1 if no
     * element isMatch the condition.
     */
    default int indexOf(Function1<T,Boolean> condition)
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
     * Create a new Indexable that restricts this Indexable to a fixed number of values.
     * @param toTake The number of values to constrain this Indexable to.
     * @return A new Indexable that restricts this Indexable to a fixed number of values.
     */
    default Indexable<T> take(int toTake)
    {
        return new TakeIndexable<>(this, toTake);
    }

    /**
     * Create a new Indexable that will skip over the first toSkip number of elements in this
     * Indexable and then return the remaining elements.
     * @param toSkip The number of elements to skip.
     * @return A new Indexable that will skip over the first toSkip number of elements in this
     * Indexable and then return the remaining elements.
     */
    default Indexable<T> skip(int toSkip)
    {
        return toSkip <= 0 ? this : new SkipIndexable<>(this, toSkip);
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

    /**
     * Validate the provided index against this Indexable.
     * @param index The index to validate.
     */
    default void validateAccessIndex(int index)
    {
        final int count = getCount();
        if (count == 0)
        {
            PreCondition.fail("Cannot access values in an empty Indexable.");
        }
        else
        {
            PreCondition.assertBetween(0, index, count - 1, "index");
        }
    }

    /**
     * Create a new Indexable from the provided values.
     * @param values The values to convert to an Indexable.
     * @return The created Indexable.
     */
    static ByteArray create(byte... values)
    {
        return ByteArray.create(values);
    }

    /**
     * Create a new ByteArray with the provided elements.
     * @param values The elements of the new ByteArray.
     * @param startIndex The start index into the values.
     * @param length The number of bytes to copy.
     * @return The new ByteArray.
     */
    static ByteArray create(byte[] values, int startIndex, int length)
    {
        return ByteArray.create(values, startIndex, length);
    }

    /**
     * Create a new Indexable from the provided values.
     * @param values The values to convert to an Indexable.
     * @return The created Indexable.
     */
    static CharacterArray create(char... values)
    {
        PreCondition.assertNotNull(values, "values");

        return CharacterArray.create(values);
    }

    /**
     * Create a new Indexable from the provided values.
     * @param values The values to convert to an Indexable.
     * @param <T> The type of values in the created Indexable.
     * @return The created Indexable.
     */
    @SafeVarargs
    static <T> Indexable<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.create(values);
    }
}
