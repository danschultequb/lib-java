package qub;

public interface List<T> extends MutableIndexable<T>
{
    /**
     * Create a new List with the provided initial values.
     * @param <T> The Type of elements contained by the created List.
     * @return The created List.
     */
    static <T> List<T> create()
    {
        return new ArrayList<>();
    }

    /**
     * Create a new List with the provided initial values.
     * @param initialValues The initial values to include in the new List.
     * @param <T> The Type of elements contained by the created List.
     * @return The created List.
     */
    @SuppressWarnings("unchecked")
    static <T> List<T> create(T... initialValues)
    {
        return create(Iterable.create(initialValues));
    }

    /**
     * Create a new List with the provided initial values.
     * @param initialValues The initial values to include in the new List.
     * @param <T> The Type of elements contained by the created List.
     * @return The created List.
     */
    static <T> List<T> create(Iterator<T> initialValues)
    {
        final List<T> result = List.create();
        result.addAll(initialValues);
        return result;
    }

    /**
     * Create a new List with the provided initial values.
     * @param initialValues The initial values to include in the new List.
     * @param <T> The Type of elements contained by the created List.
     * @return The created List.
     */
    static <T> List<T> create(Iterable<T> initialValues)
    {
        final List<T> result = List.create();
        result.addAll(initialValues);
        return result;
    }

    /**
     * Add the provided value at the end of this List.
     * @param value The value to add.
     */
    default void add(T value)
    {
        insert(getCount(), value);
    }

    /**
     * Insert the provided value into this List at the provided insertIndex.
     * @param insertIndex The insertIndex to insert the provided value at.
     * @param value The value to insert into the List.
     */
    void insert(int insertIndex, T value);

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    @SuppressWarnings("unchecked")
    default void addAll(T... values)
    {
        if (values != null && values.length > 0)
        {
            for (final T value : values)
            {
                add(value);
            }
        }
    }

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    default void addAll(Iterator<T> values)
    {
        if (values != null && values.any())
        {
            for (final T value : values)
            {
                add(value);
            }
        }
    }

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    default void addAll(Iterable<T> values)
    {
        if (values != null && values.any())
        {
            for (final T value : values)
            {
                add(value);
            }
        }
    }

    /**
     * Remove the first value in this List that is equal to the provided value.
     * @param value The value to remove.
     * @return Whether or not a value in this List was removed.
     */
    default boolean remove(T value)
    {
        final int indexToRemove = indexOf(value);
        final boolean result = indexToRemove != -1;
        if (result)
        {
            removeAt(indexToRemove);
        }
        return result;
    }

    /**
     * Remove and return the value at the provided index. If the index is not in the bounds of this
     * List, then no values will be removed and null will be returned.
     * @param index The index to remove from.
     * @return The value that was removed or null if the index is out of bounds.
     */
    T removeAt(int index);

    /**
     * Remove and return the first value in this List. If this List is empty, then null will be
     * returned.
     * @return The value that was removed, or null if the List was empty.
     */
    default T removeFirst()
    {
        return removeAt(0);
    }

    /**
     * Remove and return the first value in this List that isMatch the provided condition. If this
     * List is empty or if no elements match the provided condition, then null will be returned.
     * @param condition The condition to run against each element in this List.
     * @return The element that was removed, or null if no element matched the condition.
     */
    default T removeFirst(Function1<T,Boolean> condition)
    {
        final int removeIndex = indexOf(condition);
        return removeIndex < 0 ? null : removeAt(removeIndex);
    }

    /**
     * Remove and return the last value in this List. If this List is empty, then null will be
     * returned.
     * @return The value that was removed, or null if the List was empty.
     */
    default T removeLast()
    {
        final int count = getCount();
        return count == 0 ? null : removeAt(count - 1);
    }

    /**
     * Remove all of the elements from this List.
     */
    default void clear()
    {
        while (any())
        {
            removeFirst();
        }
    }

    /**
     * Get whether or not this List ends with the provided value.
     * @param value The value to look for at the end of this List.
     * @return Whether or not this List ends with the provided value.
     */
    default boolean endsWith(T value)
    {
        final int count = getCount();
        return count > 0 && Comparer.equal(get(count - 1), value);
    }

    /**
     * Get whether or not this List ends with the provided values.
     * @param values The values to look for at the end of this List.
     * @return Whether or not this List ends with the provided values.
     */
    default boolean endsWith(Iterable<T> values)
    {
        boolean result = false;

        final int count = getCount();
        final int valuesCount = values == null ? 0 : values.getCount();
        if (count >= valuesCount)
        {
            result = this.takeLast(valuesCount).equals(values);
        }

        return result;
    }

    /**
     * Validate the provided insertIndex against this List.
     * @param insertIndex The insertIndex to validate.
     */
    default void validateInsertIndex(int insertIndex)
    {
        PreCondition.assertBetween(0, insertIndex, getCount(), "insertIndex");
    }
}
