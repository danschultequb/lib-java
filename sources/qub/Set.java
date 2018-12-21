package qub;

public interface Set<T> extends Iterable<T>
{
    /**
     * Create a new empty Set.
     * @param <T> The type of elements contained by the created Set.
     * @return The created List.
     */
    static <T> Set<T> create()
    {
        return new ListSet<>();
    }

    /**
     * Create a new Set from the provided values.
     * @param initialValues The initial values for the resulting Set to contain.
     * @param <T> The type of elements contained by the created Set.
     * @return The created List.
     */
    static <T> Set<T> create(Iterable<T> initialValues)
    {
        final Set<T> result = Set.create();
        result.addAll(initialValues);
        return result;
    }

    /**
     * Add the provided value to this Set if it doesn't already exist.
     * @param value The value to add.
     */
    void add(T value);

    /**
     * Add the provided values to this Set.
     * @param values The values to add.
     */
    default void addAll(T[] values)
    {
        if (values != null && values.length > 0)
        {
            for (final T value : values)
            {
                add(value);
            }
        }
    }

    /**
     * Add the provided values to this Set.
     * @param values The values to add.
     */
    default void addAll(Iterator<T> values)
    {
        if (values != null && values.any())
        {
            for (final T value : values)
            {
                add(value);
            }
        }
    }

    /**
     * Add the provided values to this Set.
     * @param values The values to add.
     */
    default void addAll(Iterable<T> values)
    {
        if (values != null && values.any())
        {
            for (final T value : values)
            {
                add(value);
            }
        }
    }

    /**
     * Remove the value from this Set that is equal to the provided value.
     * @param value The value to remove.
     * @return Whether or not a value in this List was removed.
     */
    Result<Boolean> remove(T value);

    /**
     * Remove all of the elements from this Set.
     */
    void clear();
}
