package qub;

/**
 * A function that returns whether two values are equal.
 * @param <T> The type of values that this function operates on.
 */
public interface EqualFunction<T> extends Function2<T,T,Boolean>
{
    /**
     * Create an {@link EqualFunction} function that uses the default Object.equals(Object) method to
     * determine if two objects are equal.
     * @param <T> The type of objects to compare.
     */
    public static <T> EqualFunction<T> create()
    {
        return (T lhs, T rhs) ->
        {
            return (lhs == rhs) ||
                (lhs != null && rhs != null && lhs.equals(rhs));
        };
    }

    /**
     * Create an {@link EqualFunction} from the provided {@link Comparer}.
     * @param comparer The {@link Comparer}.
     * @param <T> The type of values that the {@link EqualFunction} and {@link Comparer} operate
     *           on.
     */
    public static <T> EqualFunction<T> create(Comparer<T> comparer)
    {
        PreCondition.assertNotNull(comparer, "comparer");

        return (T lhs, T rhs) -> { return comparer.run(lhs, rhs) == Comparison.Equal; };
    }

    /**
     * Create an {@link EqualFunction} from the provided {@link CompareFunction}.
     * @param compareFunction The {@link Comparer} function.
     * @param <T> The type of values that the {@link EqualFunction} and {@link Comparer} operate
     *           on.
     */
    public static <T> EqualFunction<T> create(CompareFunction<T> compareFunction)
    {
        PreCondition.assertNotNull(compareFunction, "compareFunction");

        return (T lhs, T rhs) -> { return compareFunction.run(lhs, rhs) == Comparison.Equal; };
    }
}
