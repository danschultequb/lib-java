package qub;

/**
 * A sort {@link Action2} that can be used to sort the contents of a collection.
 */
public interface Sort
{
    /**
     * A type of {@link Sort} that sorts elements by inserting them in order.
     * See <a href="https://en.wikipedia.org/wiki/Insertion_sort">here</a> for more details.
     */
    public static final InsertionSort insertionSort = InsertionSort.create();

    /**
     * A type of {@link Sort} that performs an in-place recursive sort.
     * See <a href="https://en.wikipedia.org/wiki/Quicksort">here</a> for more details.
     */
    public static final QuickSort quickSort = QuickSort.create();

    /**
     * The default {@link Sort} that will be used to sort values when no {@link Sort} is specified.
     */
    public static final Sort defaultSort = Sort.quickSort;

    /**
     * Sort the values in the provided {@link MutableIndexable} using the default {@link Sort}.
     * @param values The values to sort.
     * @param <T> The type of values stored in the {@link MutableIndexable}.
     */
    public static <T extends java.lang.Comparable<T>> void defaultSort(MutableIndexable<T> values)
    {
        Sort.defaultSort.sort(values, T::compareTo);
    }

    /**
     * Sort the values in the provided {@link MutableIndexable} using the provided {@link Comparer}.
     * @param values The values to sort.
     * @param comparer The {@link Comparer} to use to compare values within the
     * {@link MutableIndexable}.
     * @param <T> The type of values stored in the {@link MutableIndexable}.
     */
    public static <T> void defaultSort(MutableIndexable<T> values, Function2<T,T,Integer> comparer)
    {
        Sort.defaultSort.sort(values, comparer);
    }

    /**
     * Sort the values in the provided {@link MutableIndexable} using the provided {@link Comparer}.
     * @param values The values to sort.
     * @param comparer The {@link Comparer} to use to compare values within the
     * {@link MutableIndexable}.
     * @param <T> The type of values stored in the {@link MutableIndexable}.
     */
    public static <T> void defaultSort(MutableIndexable<T> values, Comparer<T> comparer)
    {
        Sort.defaultSort.sort(values, comparer);
    }

    /**
     * Sort the values in the provided {@link MutableIndexable}.
     * @param values The values to sort.
     * @param <T> The type of values stored in the {@link MutableIndexable}.
     */
    public default <T extends java.lang.Comparable<T>> void sort(MutableIndexable<T> values)
    {
        this.sort(values, T::compareTo);
    }

    /**
     * Sort the values in the provided {@link MutableIndexable} using the provided {@link Comparer}.
     * @param values The values to sort.
     * @param comparer The {@link Comparer} to use to compare values within the
     * {@link MutableIndexable}.
     * @param <T> The type of values stored in the {@link MutableIndexable}.
     */
    public default <T> void sort(MutableIndexable<T> values, Function2<T,T,Integer> comparer)
    {
        this.sort(values, Comparer.create(comparer));
    }

    /**
     * Sort the values in the provided {@link MutableIndexable} using the provided {@link Comparer}.
     * @param values The values to sort.
     * @param comparer The {@link Comparer} to use to compare values within the
     * {@link MutableIndexable}.
     * @param <T> The type of values stored in the {@link MutableIndexable}.
     */
    public <T> void sort(MutableIndexable<T> values, Comparer<T> comparer);
}
