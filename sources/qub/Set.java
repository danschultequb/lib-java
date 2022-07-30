package qub;

public interface Set<T> extends Iterable<T>
{
    /**
     * Create a new empty {@link MutableSet}.
     * @param <T> The type of values contained by the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create()
    {
        return MutableSet.create();
    }

    /**
     * Create a new {@link MutableSet} with the provided initialValues.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param initialValues The initial values that will be added to the {@link MutableSet}.
     */
    @SafeVarargs
    public static <T> MutableSet<T> create(T... initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        return MutableSet.create(initialValues);
    }

    /**
     * Create a new {@link MutableSet} with the provided initialValues.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param initialValues The initial values that will be added to the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(Iterable<T> initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        return MutableSet.create(initialValues);
    }

    /**
     * Create a new {@link MutableSet} with the provided initialValues.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param initialValues The initial values that will be added to the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(Iterator<T> initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        return MutableSet.create(initialValues);
    }

    /**
     * Create a new empty {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param compareFunction The {@link CompareFunction} that will be used to compare values in
     *                        the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(CompareFunction<T> compareFunction)
    {
        return MutableSet.create(compareFunction);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param compareFunction The {@link CompareFunction} that will be used to compare values in
     *                        the {@link MutableSet}.
     * @param initialValues The initial values that will be added to the {@link MutableSet}.
     */
    @SafeVarargs
    public static <T> MutableSet<T> create(CompareFunction<T> compareFunction, T... initialValues)
    {
        return MutableSet.create(compareFunction, initialValues);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param compareFunction The {@link CompareFunction} that will be used to compare values in
     *                        the {@link MutableSet}.
     * @param initialValues The initial values that will be added to the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(CompareFunction<T> compareFunction, Iterable<T> initialValues)
    {
        return MutableSet.create(compareFunction, initialValues);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param compareFunction The {@link CompareFunction} that will be used to compare values in
     *                        the {@link MutableSet}.
     * @param initialValues The initial values that will be added to the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(CompareFunction<T> compareFunction, Iterator<T> initialValues)
    {
        return MutableSet.create(compareFunction, initialValues);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param equalFunction The {@link EqualFunction} that will be used to compare values in this
     * {@link Set}.
     */
    public static <T> MutableSet<T> create(EqualFunction<T> equalFunction)
    {
        return MutableSet.create(equalFunction);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param equalFunction The {@link EqualFunction} that will be used to compare values in this
     *                      {@link MutableSet}.
     * @param initialValues The initial values that will be added to the {@link MutableSet}.
     */
    @SafeVarargs
    public static <T> MutableSet<T> create(EqualFunction<T> equalFunction, T... initialValues)
    {
        return MutableSet.create(equalFunction, initialValues);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param equalFunction The {@link EqualFunction} that will be used to compare values in this
     *                      {@link MutableSet}.
     * @param initialValues The initial values that will be added to the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(EqualFunction<T> equalFunction, Iterable<T> initialValues)
    {
        return MutableSet.create(equalFunction, initialValues);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param equalFunction The {@link EqualFunction} that will be used to compare values in this
     *                      {@link MutableSet}.
     * @param initialValues The initial values that will be added to the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(EqualFunction<T> equalFunction, Iterator<T> initialValues)
    {
        return MutableSet.create(equalFunction, initialValues);
    }

    /**
     * Get whether the {@link Set} lhs is equal to the provided {@link Object} rhs.
     * @param rhs The {@link Object} to compare against the {@link Set} lhs.
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean equals(Set<T> lhs, Object rhs)
    {
        boolean result = (lhs == rhs);
        if (!result && lhs != null && rhs instanceof Iterable)
        {
            result = lhs.equals((Iterable<T>)rhs);
        }
        return result;
    }

    /**
     * Get whether this {@link Set} is equal to the provided {@link Iterable} rhs. If the provided
     * {@link Iterable} is actually a {@link Set}, then this {@link Set} will be compared against
     * the provided {@link Set} using {@link Set} comparison. If the provided {@link Iterable} is
     * not a {@link Set}, then standard {@link Iterable} comparison will be used instead.
     * @param rhs The {@link Object} to compare against the {@link Set} lhs.
     */
    public default boolean equals(Iterable<T> rhs)
    {
        boolean result = (this == rhs);
        if (!result)
        {
            result = rhs instanceof Set
                ? this.equals((Set<T>)rhs)
                : Iterable.super.equals(rhs);
        }
        return result;
    }

    /**
     * Get whether this {@link Set} equals the provided {@link Set}.
     * @param rhs The {@link Set} to compare against this {@link Set}.
     */
    public default boolean equals(Set<T> rhs)
    {
        boolean result = false;
        if (rhs != null)
        {
            int lhsCount = 0;
            result = true;
            for (final T value : this)
            {
                lhsCount++;
                if (!rhs.contains(value))
                {
                    result = false;
                    break;
                }
            }
            if (result)
            {
                result = (lhsCount == rhs.getCount());
            }
        }
        return result;
    }

    public static <T> String toString(Set<T> set)
    {
        PreCondition.assertNotNull(set, "set");

        return Iterable.toString(set, '{', '}');
    }
}
