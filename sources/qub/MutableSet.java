package qub;

/**
 * A {@link Set} that can have its contents changed.
 * @param <T> The type of value stored in this {@link MutableSet}.
 */
public interface MutableSet<T> extends Set<T>
{
    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     */
    public static <T> MutableSet<T> create()
    {
        return MutableSet.create(EqualFunction.create());
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param initialValues The initial values of the {@link MutableSet}.
     */
    @SafeVarargs
    public static <T> MutableSet<T> create(T... initialValues)
    {
        return MutableSet.create(EqualFunction.create(), initialValues);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param initialValues The initial values of the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(Iterable<T> initialValues)
    {
        return MutableSet.create(EqualFunction.create(), initialValues);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param initialValues The initial values of the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(Iterator<T> initialValues)
    {
        return MutableSet.create(EqualFunction.create(), initialValues);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param compareFunction The {@link CompareFunction} that will be used to compare values in
     *                        this {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(CompareFunction<T> compareFunction)
    {
        return MutableSet.create(EqualFunction.create(compareFunction));
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param compareFunction The {@link CompareFunction} that will be used to compare values in
     *                        this {@link MutableSet}.
     * @param initialValues The initial values of the {@link MutableSet}.
     */
    @SafeVarargs
    public static <T> MutableSet<T> create(CompareFunction<T> compareFunction, T... initialValues)
    {
        return MutableSet.create(EqualFunction.create(compareFunction), initialValues);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param compareFunction The {@link CompareFunction} that will be used to compare values in
     *                        this {@link MutableSet}.
     * @param initialValues The initial values of the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(CompareFunction<T> compareFunction, Iterable<T> initialValues)
    {
        return MutableSet.create(EqualFunction.create(compareFunction), initialValues);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param compareFunction The {@link CompareFunction} that will be used to compare values in
     *                        this {@link MutableSet}.
     * @param initialValues The initial values of the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(CompareFunction<T> compareFunction, Iterator<T> initialValues)
    {
        return MutableSet.create(EqualFunction.create(compareFunction), initialValues);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param equalFunction The {@link EqualFunction} that will be used to compare values in this
     *                      {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(EqualFunction<T> equalFunction)
    {
        return ListSet.create(equalFunction);
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param equalFunction The {@link EqualFunction} that will be used to compare values in this
     *                      {@link MutableSet}.
     * @param initialValues The initial values of the {@link MutableSet}.
     */
    @SafeVarargs
    public static <T> MutableSet<T> create(EqualFunction<T> equalFunction, T... initialValues)
    {
        PreCondition.assertNotNull(equalFunction, "equalFunction");
        PreCondition.assertNotNull(initialValues, "initialValues");

        return MutableSet.create(equalFunction, Iterable.create(initialValues));
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param equalFunction The {@link EqualFunction} that will be used to compare values in this
     *                      {@link MutableSet}.
     * @param initialValues The initial values of the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(EqualFunction<T> equalFunction, Iterable<T> initialValues)
    {
        PreCondition.assertNotNull(equalFunction, "equalFunction");
        PreCondition.assertNotNull(initialValues, "initialValues");

        return MutableSet.create(equalFunction, initialValues.iterate());
    }

    /**
     * Create a new {@link MutableSet}.
     * @param <T> The type of elements contained by the created {@link MutableSet}.
     * @param equalFunction The {@link EqualFunction} that will be used to compare values in this
     *                      {@link MutableSet}.
     * @param initialValues The initial values of the {@link MutableSet}.
     */
    public static <T> MutableSet<T> create(EqualFunction<T> equalFunction, Iterator<T> initialValues)
    {
        PreCondition.assertNotNull(equalFunction, "equalFunction");
        PreCondition.assertNotNull(initialValues, "initialValues");

        final MutableSet<T> result = MutableSet.create(equalFunction);
        result.addAll(initialValues);
        return result;
    }

    /**
     * Add the provided value to this {@link MutableSet}. If the provided value was added, then this
     * function will return true. If the provided value already existed in this {@link MutableSet},
     * then this function will return false.
     * @param value The value to add.
     */
    public boolean add(T value);

    /**
     * Add the provided values to this {@link MutableSet}. If any of the provided values were added,
     * then this function will return true. If the provided values already existed in this
     * {@link MutableSet}, then this function will return false.
     * @param values The values to add.
     */
    @SuppressWarnings("unchecked")
    default boolean addAll(T... values)
    {
        boolean result = false;
        if (values != null && values.length > 0)
        {
            for (final T value : values)
            {
                if (this.add(value))
                {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Add the provided values to this {@link MutableSet}. If any of the provided values were added,
     * then this function will return true. If the provided values already existed in this
     * {@link MutableSet}, then this function will return false.
     * @param values The values to add.
     */
    default boolean addAll(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        boolean result = false;
        for (final T value : values)
        {
            if (this.add(value))
            {
                result = true;
            }
        }
        return result;
    }

    /**
     * Add the provided values to this {@link MutableSet}. If any of the provided values were added,
     * then this function will return true. If the provided values already existed in this
     * {@link MutableSet}, then this function will return false.
     * @param values The values to add.
     */
    default boolean addAll(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        boolean result = false;
        for (final T value : values)
        {
            if (this.add(value))
            {
                result = true;
            }
        }
        return result;
    }

    /**
     * Remove the value from this {@link MutableSet} that is equal to the provided value.
     * @param value The value to remove.
     * @return The removed value.
     */
    public Result<T> remove(T value);

    /**
     * Remove all of the elements from this {@link MutableSet}.
     */
    public void clear();
}
