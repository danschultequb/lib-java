package qub;

public interface Set2<T> extends Iterable<T>
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
     * {@link Set2}.
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
     * Get whether the {@link Set2} lhs is equal to the provided {@link Object} rhs.
     * @param rhs The {@link Object} to compare against the {@link Set2} lhs.
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean equals(Set2<T> lhs, Object rhs)
    {
        boolean result = (lhs == rhs);
        if (!result && lhs != null)
        {
            result = rhs instanceof Set2
                ? lhs.equals((Set2<T>)rhs)
                : Iterable.equals(lhs, rhs);
        }
        return result;
    }

    /**
     * Get whether or not this Set equals the provided Set.
     * @param rhs The Set to compare against this Set.
     * @return Whether or not this Set equals the provided Set.
     */
    public default boolean equals(Set2<T> rhs)
    {
        boolean result = false;
        if (rhs != null)
        {
            result = true;
            for (final T value : this)
            {
                if (!rhs.contains(value))
                {
                    result = false;
                    break;
                }
            }
            if (result)
            {
                result = getCount() == rhs.getCount();
            }
        }
        return result;
    }

    public static <T> String toString(Set2<T> set)
    {
        PreCondition.assertNotNull(set, "set");

        return Iterable.toString(set, '{', '}');
    }
}
