package qub;

/**
 * An {@link Iterable} that cannot contain duplicate values.
 * @param <T> The type of values stored in this {@link Set}.
 */
public interface Set<T> extends Iterable<T>
{
    /**
     * Create a new {@link Set} from the provided values.
     * @param initialValues The initial values for the resulting {@link Set} to contain.
     * @param <T> The type of elements contained by the created {@link Set}.
     */
    @SafeVarargs
    public static <T> Set<T> create(T... initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        return Set.create(Iterable.create(initialValues));
    }

    /**
     * Create a new {@link Set} from the provided values.
     * @param initialValues The initial values for the resulting {@link Set} to contain.
     * @param <T> The type of elements contained by the created {@link Set}.
     */
    public static <T> Set<T> create(Iterable<T> initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        return Set.create(initialValues.iterate());
    }

    /**
     * Create a new {@link Set} from the provided values.
     * @param initialValues The initial values for the resulting {@link Set} to contain.
     * @param <T> The type of elements contained by the created {@link Set}.
     */
    public static <T> Set<T> create(Iterator<T> initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        final Set<T> result = ListSet.create();
        result.addAll(initialValues);
        return result;
    }

    /**
     * Add the provided value to this {@link Set} if it doesn't already exist.
     * @param value The value to add.
     */
    public Set<T> add(T value);

    /**
     * Add the provided values to this {@link Set}.
     * @param values The values to add.
     */
    @SuppressWarnings("unchecked")
    public default Set<T> addAll(T... values)
    {
        if (values != null && values.length > 0)
        {
            for (final T value : values)
            {
                add(value);
            }
        }
        return this;
    }

    /**
     * Add the provided values to this {@link Set}.
     * @param values The values to add.
     */
    public default Set<T> addAll(Iterator<T> values)
    {
        if (!Iterator.isNullOrEmpty(values))
        {
            for (final T value : values)
            {
                add(value);
            }
        }
        return this;
    }

    /**
     * Add the provided values to this {@link Set}.
     * @param values The values to add.
     */
    public default Set<T> addAll(Iterable<T> values)
    {
        if (!Iterable.isNullOrEmpty(values))
        {
            for (final T value : values)
            {
                add(value);
            }
        }
        return this;
    }

    /**
     * Remove the value from this {@link Set} that is equal to the provided value.
     * @param value The value to remove.
     * @exception NotFoundException if the value isn't found.
     */
    public Result<Void> remove(T value);

    /**
     * Remove the elements from this Set.
     * @return This object for method chaining.
     */
    public Set<T> clear();

    /**
     * Get whether the lhs {@link Set} contains equal elements as the provided rhs {@link Object}.
     * @param rhs The {@link Object} to compare against this {@link Set}.
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean equals(Set<T> lhs, Object rhs)
    {
        PreCondition.assertNotNull(lhs, "lhs");

        return rhs instanceof Set
            ? lhs.equals((Set<T>)rhs)
            : Iterable.equals(lhs, rhs);
    }

    /**
     * Get whether this {@link Set} equals the provided {@link Set}.
     * @param rhs The {@link Set} to compare against this {@link Set}.
     */
    public default boolean equals(Set<T> rhs)
    {
        boolean result = rhs != null && this.getCount() == rhs.getCount();
        if (result)
        {
            for (final T value : this)
            {
                if (!rhs.contains(value))
                {
                    result = false;
                    break;
                }
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
