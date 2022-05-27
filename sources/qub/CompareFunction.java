package qub;

/**
 * A function that returns the result of comparing two values.
 */
public interface CompareFunction<T> extends Function2<T,T,Comparison>
{
    /**
     * Create a {@link CompareFunction} that compares values of the provided type.
     * @param <T> The type of values to compare.
     */
    public static <T extends Comparable<T>> CompareFunction<T> create()
    {
        return Comparer::compare;
    }
}
