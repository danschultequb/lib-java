package qub;

/**
 * An {@link Indexable} that can mutate its values.
 * @param <T> The type of value stored in this {@link MutableIndexable}.
 */
public interface MutableIndexable<T> extends Indexable<T>
{
    /**
     * Create a new {@link MutableIndexable} from the provided values.
     * @param values The values to convert to an {@link MutableIndexable}.
     * @param <T> The type of values in the created {@link MutableIndexable}.
     */
    @SafeVarargs
    public static <T> MutableIndexable<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.create(values);
    }

    /**
     * Create a new {@link MutableIndexable} from the provided values.
     * @param values The values to convert to an {@link MutableIndexable}.
     * @param <T> The type of values in the created {@link MutableIndexable}.
     */
    public static <T> MutableIndexable<T> create(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Array.create(values);
    }

    /**
     * Create a new {@link MutableIndexable} from the provided values.
     * @param values The values to convert to an {@link MutableIndexable}.
     * @param <T> The type of values in the created {@link MutableIndexable}.
     */
    public static <T> MutableIndexable<T> create(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        return List.create(values);
    }

    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    public MutableIndexable<T> set(int index, T value);

    /**
     * Set the first value in this {@link MutableIndexable}.
     * @param value The value to set at the first index.
     */
    public default MutableIndexable<T> setFirst(T value)
    {
        return this.set(0, value);
    }

    /**
     * Set the last value in this {@link MutableIndexable}.
     * @param value The value to set at the last index.
     */
    public default MutableIndexable<T> setLast(T value)
    {
        return this.set(this.getCount() - 1, value);
    }

    /**
     * Swap the values at the provided indexes.
     * @param index1 The first index.
     * @param index2 The second index.
     */
    public default MutableIndexable<T> swap(int index1, int index2)
    {
        PreCondition.assertBetween(0, index1, this.getCount() - 1, "index1");
        PreCondition.assertBetween(0, index2, this.getCount() - 1, "index2");

        if (index1 != index2)
        {
            final T tempValue = this.get(index1);
            this.set(index1, get(index2));
            this.set(index2, tempValue);
        }
        return this;
    }

    /**
     * Sort the values in this {@link MutableIndexable} using the provided {@link Function2}
     * comparer. This will change this {@link MutableIndexable} if the values are not already in
     * sorted order.
     * @param comparer The {@link Function2} comparer to use to compare the values.
     * @return This object for method chaining.
     */
    public default MutableIndexable<T> sort(Function2<T,T,Integer> comparer)
    {
        PreCondition.assertNotNull(comparer, "comparer");

        Sort.defaultSort(this, comparer);

        return this;
    }

    /**
     * Sort the values in this {@link MutableIndexable} using the provided {@link Comparer}. This
     * will change this {@link MutableIndexable} if the values are not already in sorted order.
     * @param comparer The {@link Comparer} to use to compare the values.
     * @return This object for method chaining.
     */
    public default MutableIndexable<T> sort(Comparer<T> comparer)
    {
        PreCondition.assertNotNull(comparer, "comparer");

        Sort.defaultSort(this, comparer);

        return this;
    }
}
