package qub;

public interface Sort
{
    /**
     * Sort the provided List in place using the provided comparer.
     * @param indexable The List to sort.
     * @param comparer The comparer to use when comparing two values in the List.
     * @param <T> The type of values in the List.
     */
    <T> void sort(MutableIndexable<T> indexable, Function2<T,T,Comparison> comparer);

    /**
     * Sort the provided List in place using the provided comparer.
     * @param indexable The List to sort.
     * @param <T> The type of values in the List.
     */
    default <T extends Comparable<T>> void sort(MutableIndexable<T> indexable)
    {
        sort(indexable, Comparer::compare);
    }
}
