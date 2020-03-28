package qub;

public interface List<T> extends MutableIndexable<T>
{
    /**
     * Create a new List with the provided initial values.
     * @param initialValues The initial values to include in the new List.
     * @param <T> The Type of elements contained by the created List.
     * @return The created List.
     */
    @SafeVarargs
    static <T> List<T> create(T... initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        return List.create(Iterable.create(initialValues));
    }

    /**
     * Create a new List with the provided initial values.
     * @param initialValues The initial values to include in the new List.
     * @param <T> The Type of elements contained by the created List.
     * @return The created List.
     */
    static <T> List<T> create(Iterable<T> initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        return List.create(initialValues.iterate());
    }

    /**
     * Create a new List with the provided initial values.
     * @param initialValues The initial values to include in the new List.
     * @param <T> The Type of elements contained by the created List.
     * @return The created List.
     */
    static <T> List<T> create(Iterator<T> initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        final ArrayList<T> result = new ArrayList<>();
        result.addAll(initialValues);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    List<T> set(int index, T value);

    /**
     * Add the provided value at the end of this List.
     * @param value The value to add.
     */
    default void add(T value)
    {
        this.insert(getCount(), value);
    }

    /**
     * Insert the provided value into this List at the provided insertIndex.
     * @param insertIndex The insertIndex to insert the provided value at.
     * @param value The value to insert into the List.
     */
    List<T> insert(int insertIndex, T value);

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    @SuppressWarnings("unchecked")
    default List<T> addAll(T... values)
    {
        if (values != null && values.length > 0)
        {
            for (final T value : values)
            {
                this.add(value);
            }
        }
        return this;
    }

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    default void addAll(Iterator<? extends T> values)
    {
        if (values != null && values.any())
        {
            for (final T value : values)
            {
                this.add(value);
            }
        }
    }

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    default List<T> addAll(Iterable<? extends T> values)
    {
        if (values != null && values.any())
        {
            for (final T value : values)
            {
                this.add(value);
            }
        }
        return this;
    }

    /**
     * Remove the first value in this List that is equal to the provided value.
     * @param value The value to remove.
     * @return Whether or not a value in this List was removed.
     */
    default boolean remove(T value)
    {
        final int indexToRemove = this.indexOf(value);
        final boolean result = indexToRemove != -1;
        if (result)
        {
            this.removeAt(indexToRemove);
        }
        return result;
    }

    /**
     * Remove and return the value at the provided index. If the index is not in the bounds of this
     * List, then no values will be removed and null will be returned.
     * @param index The index to remove create.
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
        return this.removeAt(0);
    }

    /**
     * Remove and return the first valuesToRemove values from this List. If the List does not have
     * enough values, then all of the values in the list will be removed. If the List is empty,
     * then null will be returned.
     * @param valuesToRemove The number of values to remove.
     * @return The values that were removed or null if the List was empty.
     */
    default Iterable<T> removeFirst(int valuesToRemove)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "list");
        PreCondition.assertNonEmptyLength(valuesToRemove, 0, this.getCount(), "valuesToRemove");

        final List<T> result = List.create();
        for (int i = 0; i < valuesToRemove; ++i)
        {
            result.add(this.removeFirst());
        }
        return result;
    }

    /**
     * Remove and return the first value in this List that isMatch the provided condition. If this
     * List is empty or if no elements match the provided condition, then null will be returned.
     * @param condition The condition to run against each element in this List.
     * @return The element that was removed, or null if no element matched the condition.
     */
    default T removeFirst(Function1<T,Boolean> condition)
    {
        final int removeIndex = this.indexOf(condition);
        return removeIndex < 0 ? null : this.removeAt(removeIndex);
    }

    /**
     * Remove and return the last value in this List. If this List is empty, then null will be
     * returned.
     * @return The value that was removed, or null if the List was empty.
     */
    default T removeLast()
    {
        final int count = this.getCount();
        return count == 0 ? null : this.removeAt(count - 1);
    }

    /**
     * Remove all of the elements create this List.
     */
    default List<T> clear()
    {
        while (any())
        {
            this.removeFirst();
        }
        return this;
    }

    /**
     * Get whether or not this List ends with the provided value.
     * @param value The value to look for at the end of this List.
     * @return Whether or not this List ends with the provided value.
     */
    default boolean endsWith(T value)
    {
        final int count = this.getCount();
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

        final int count = this.getCount();
        final int valuesCount = values == null ? 0 : values.getCount();
        if (count >= valuesCount)
        {
            result = this.takeLast(valuesCount).equals(values);
        }

        return result;
    }
}
