package qub;

public interface Set<T> extends Iterable<T>
{
    /**
     * Create a new empty Set.
     * @param <T> The type of elements contained by the created Set.
     * @return The created List.
     */
    static <T> Set<T> empty()
    {
        return new ListSet<>();
    }

    /**
     * Create a new empty Set.
     * @param initialValues The initial values for the resulting Set to contain.
     * @param <T> The type of elements contained by the created Set.
     * @return The created List.
     */
    @SuppressWarnings("unchecked")
    static <T> Set<T> create(T... initialValues)
    {
        return Set.create(Iterable.create(initialValues));
    }

    /**
     * Create a new Set from the provided values.
     * @param initialValues The initial values for the resulting Set to contain.
     * @param <T> The type of elements contained by the created Set.
     * @return The created List.
     */
    static <T> Set<T> create(Iterator<T> initialValues)
    {
        final Set<T> result = Set.empty();
        result.addAll(initialValues);
        return result;
    }

    /**
     * Create a new Set from the provided values.
     * @param initialValues The initial values for the resulting Set to contain.
     * @param <T> The type of elements contained by the created Set.
     * @return The created List.
     */
    static <T> Set<T> create(Iterable<T> initialValues)
    {
        final Set<T> result = Set.empty();
        result.addAll(initialValues);
        return result;
    }

    /**
     * Add the provided value to this Set if it doesn't already exist.
     * @param value The value to add.
     */
    Set<T> add(T value);

    /**
     * Add the provided values to this Set.
     * @param values The values to add.
     */
    @SuppressWarnings("unchecked")
    default Set<T> addAll(T... values)
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
     * Add the provided values to this Set.
     * @param values The values to add.
     */
    default Set<T> addAll(Iterator<T> values)
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
     * Add the provided values to this Set.
     * @param values The values to add.
     */
    default Set<T> addAll(Iterable<T> values)
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
     * Remove the value from this Set that is equal to the provided value.
     * @param value The value to remove.
     * @return Whether or not a value in this List was removed.
     */
    Result<Void> remove(T value);

    /**
     * Remove all of the elements from this Set.
     */
    Set<T> clear();

    /**
     * Get whether or not the lhs Iterable contains equal elements in the same order as the provided
     * rhs Iterable.
     * @param rhs The Iterable to compare against this Iterable.
     * @return Whether or not this Iterable contains equal elements in the same order as the
     * provided Iterable.
     */
    @SuppressWarnings("unchecked")
    static <T> boolean equals(Set<T> lhs, Object rhs)
    {
        PreCondition.assertNotNull(lhs, "lhs");

        return rhs instanceof Set
            ? lhs.equals((Set<T>)rhs)
            : Iterable.equals(lhs, rhs);
    }

    /**
     * Get whether or not this Set equals the provided Set.
     * @param rhs The Set to compare against this Set.
     * @return Whether or not this Set equals the provided Set.
     */
    default boolean equals(Set<T> rhs)
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

    static <T> String toString(Set<T> set)
    {
        PreCondition.assertNotNull(set, "set");

        return Iterable.toString(set, '{', '}');
    }
}
