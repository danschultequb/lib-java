package qub;

public interface MutableIndexable<T> extends Indexable<T>
{
    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    void set(int index, T value);

    /**
     * Set the first value in this MutableIndexable.
     * @param value The value to set the first index.
     */
    default void setFirst(T value)
    {
        set(0, value);
    }

    /**
     * Set the last value in this MutableIndexable.
     * @param value The value to set at the last index.
     */
    default void setLast(T value)
    {
        set(getCount() - 1, value);
    }

    /**
     * Swap the values at the provided indexes.
     * @param index1 The first index.
     * @param index2 The second index.
     */
    default void swap(int index1, int index2)
    {
        PreCondition.assertBetween(0, index1, getCount() - 1, "index1");
        PreCondition.assertBetween(0, index2, getCount() - 1, "index2");

        if (index1 != index2)
        {
            final T tempValue = get(index1);
            set(index1, get(index2));
            set(index2, tempValue);
        }
    }

    /**
     * Sort the values in this MutableIndexable using the provided lessThan method. This will change
     * this MutableIndexable if the values are not already in sorted order.
     * @param lessThan The function to use to compare the values.
     * @return This MutableIndexable after it has been sorted.
     */
    default MutableIndexable<T> sort(Function2<T,T,Boolean> lessThan)
    {
        PreCondition.assertNotNull(lessThan, "lessThan");

        final int count = getCount();
        for (int i = 0; i < count; ++i)
        {
            int minimumValueIndex = i;
            T minimumValue = get(i);
            for (int j = i + 1; j < count; ++j)
            {
                final T currentValue = get(j);
                if (lessThan.run(currentValue, minimumValue))
                {
                    minimumValueIndex = j;
                    minimumValue = currentValue;
                }
            }
            swap(i, minimumValueIndex);
        }

        return this;
    }

    /**
     * Sort the values in the provided MutableIndexable using their compareTo() method. This will
     * change the MutableIndexable if the values are not already in sorted order.
     * @param values The values to sort.
     * @param <T> The type of values to sort.
     * @return The sorted MutableIndexable.
     */
    static <T extends Comparable<T>> MutableIndexable<T> sort(MutableIndexable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        return values.sort(Comparer::lessThan);
    }
}
