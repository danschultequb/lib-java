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
    default List<T> add(T value)
    {
        return this.insert(this.getCount(), value);
    }

    /**
     * Insert the provided value into this List at the provided insertIndex.
     * @param insertIndex The insertIndex to insert the provided value at.
     * @param value The value to insert into the List.
     */
    List<T> insert(int insertIndex, T value);

    /**
     * Insert the provided values into this List at the provided insertIndex.
     * @param insertIndex The insertIndex to insert the provided values at.
     * @param values The values to insert into the List.
     * @return This object for method chaining.
     */
    default List<T> insertAll(int insertIndex, Iterable<T> values)
    {
        PreCondition.assertBetween(0, insertIndex, this.getCount(), "insertIndex");
        PreCondition.assertNotNull(values, "values");

        return this.insertAll(insertIndex, values.iterate());
    }

    /**
     * Insert the provided values into this List at the provided insertIndex.
     * @param insertIndex The insertIndex to insert the provided values at.
     * @param values The values to insert into the List.
     * @return This object for method chaining.
     */
    default List<T> insertAll(int insertIndex, Iterator<T> values)
    {
        PreCondition.assertBetween(0, insertIndex, this.getCount(), "insertIndex");
        PreCondition.assertNotNull(values, "values");

        int insertOffset = 0;
        for (final T value : values)
        {
            this.insert(insertIndex + insertOffset++, value);
        }

        return this;
    }

    /**
     * Add the provided values at the end of this List.
     * @param values The values to add.
     */
    @SuppressWarnings("unchecked")
    default List<T> addAll(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        if (values.length > 0)
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
    default List<T> addAll(Iterator<? extends T> values)
    {
        PreCondition.assertNotNull(values, "values");

        if (values.any())
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
    default List<T> addAll(Iterable<? extends T> values)
    {
        PreCondition.assertNotNull(values, "values");

        if (values.any())
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
        return this.removeAt(this.getCount() - 1);
    }

    /**
     * Remove and return the values in this list that are contained in the provided values to remove.
     * @param toRemove The values to remove from this list.
     * @return The values in this list that were removed.
     */
    @SuppressWarnings("unchecked")
    default Iterable<T> removeAll(T... toRemove)
    {
        PreCondition.assertNotNull(toRemove, "toRemove");

        return this.removeAll(Iterable.create(toRemove));
    }

    /**
     * Remove and return the values in this list that are contained in the provided values to remove.
     * @param toRemove The values to remove from this list.
     * @return The values in this list that were removed.
     */
    default Iterable<T> removeAll(Iterable<T> toRemove)
    {
        PreCondition.assertNotNull(toRemove, "toRemove");

        final Iterable<T> result = toRemove.any()
            ? this.removeAll(toRemove::contains)
            : Iterable.create();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Remove and return the values in this list that match the provided condition.
     * @param condition The condition to check against each of the values in this list.
     * @return The values in this list that were removed.
     */
    default Iterable<T> removeAll(Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        final List<T> result = List.create();
        for (int i = 0; i < this.getCount(); ++i)
        {
            final T value = this.get(i);
            if (condition.run(value))
            {
                result.add(value);
                this.removeAt(i);
                --i;
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Remove all of the elements create this List.
     */
    default List<T> clear()
    {
        while (this.any())
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
        return count > 0 && Comparer.equal(this.get(count - 1), value);
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
