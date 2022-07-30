package qub;

/**
 * A {@link ListSet} that maintains the order that values are added to it.
 * @param <T> The type of value that this {@link ListSet} contains.
 */
public class ListSet<T> implements MutableSet<T>
{
    private final EqualFunction<T> equalFunction;
    private final List<T> values;

    private ListSet(EqualFunction<T> equalFunction)
    {
        PreCondition.assertNotNull(equalFunction, "equalFunction");

        this.equalFunction = equalFunction;
        this.values = List.create();
    }

    /**
     * Create a new {@link ListSet}.
     * @param <T> The type of elements contained by the created {@link ListSet}.
     */
    public static <T> ListSet<T> create()
    {
        return ListSet.create(EqualFunction.create());
    }

    /**
     * Create a new {@link ListSet}.
     * @param <T> The type of elements contained by the created {@link ListSet}.
     * @param initialValues The initial values of the {@link ListSet}.
     */
    @SafeVarargs
    public static <T> ListSet<T> create(T... initialValues)
    {
        return ListSet.create(EqualFunction.create(), initialValues);
    }

    /**
     * Create a new {@link ListSet}.
     * @param <T> The type of elements contained by the created {@link ListSet}.
     * @param initialValues The initial values of the {@link ListSet}.
     */
    public static <T> ListSet<T> create(Iterable<T> initialValues)
    {
        return ListSet.create(EqualFunction.create(), initialValues);
    }

    /**
     * Create a new {@link ListSet}.
     * @param <T> The type of elements contained by the created {@link ListSet}.
     * @param initialValues The initial values of the {@link ListSet}.
     */
    public static <T> ListSet<T> create(Iterator<T> initialValues)
    {
        return ListSet.create(EqualFunction.create(), initialValues);
    }

    /**
     * Create a new {@link ListSet}.
     * @param <T> The type of elements contained by the created {@link ListSet}.
     * @param equalFunction The {@link EqualFunction} that will be used to compare values in this
     *                      {@link ListSet}.
     */
    public static <T> ListSet<T> create(EqualFunction<T> equalFunction)
    {
        return new ListSet<>(equalFunction);
    }

    /**
     * Create a new {@link ListSet}.
     * @param <T> The type of elements contained by the created {@link ListSet}.
     * @param equalFunction The {@link EqualFunction} that will be used to compare values in this
     *                      {@link ListSet}.
     * @param initialValues The initial values of the {@link ListSet}.
     */
    @SafeVarargs
    public static <T> ListSet<T> create(EqualFunction<T> equalFunction, T... initialValues)
    {
        PreCondition.assertNotNull(equalFunction, "equalFunction");
        PreCondition.assertNotNull(initialValues, "initialValues");

        return ListSet.create(equalFunction, Iterable.create(initialValues));
    }

    /**
     * Create a new {@link ListSet}.
     * @param <T> The type of elements contained by the created {@link ListSet}.
     * @param equalFunction The {@link EqualFunction} that will be used to compare values in this
     *                      {@link ListSet}.
     * @param initialValues The initial values of the {@link ListSet}.
     */
    public static <T> ListSet<T> create(EqualFunction<T> equalFunction, Iterable<T> initialValues)
    {
        PreCondition.assertNotNull(equalFunction, "equalFunction");
        PreCondition.assertNotNull(initialValues, "initialValues");

        return ListSet.create(equalFunction, initialValues.iterate());
    }

    /**
     * Create a new {@link ListSet}.
     * @param <T> The type of elements contained by the created {@link ListSet}.
     * @param equalFunction The {@link EqualFunction} that will be used to compare values in this
     *                      {@link ListSet}.
     * @param initialValues The initial values of the {@link ListSet}.
     */
    public static <T> ListSet<T> create(EqualFunction<T> equalFunction, Iterator<T> initialValues)
    {
        PreCondition.assertNotNull(equalFunction, "equalFunction");
        PreCondition.assertNotNull(initialValues, "initialValues");

        final ListSet<T> result = ListSet.create(equalFunction);
        result.addAll(initialValues);
        return result;
    }

    @Override
    public Iterator<T> iterate()
    {
        return this.values.iterate();
    }

    @Override
    public boolean any()
    {
        return this.values.any();
    }

    @Override
    public boolean contains(T value)
    {
        return this.values.contains(value, this.equalFunction);
    }

    @Override
    public int getCount()
    {
        return this.values.getCount();
    }

    @Override
    public Result<T> first()
    {
        return this.values.first();
    }

    @Override
    public Result<T> last()
    {
        return this.values.last();
    }

    @Override
    public boolean add(T value)
    {
        boolean result = !this.contains(value);
        if (result)
        {
            this.values.add(value);
        }
        return result;
    }

    @Override
    public Result<T> remove(T value)
    {
        return Result.create(() ->
        {
            final int valueIndex = this.values.indexOf((T existingValue) ->
            {
                return this.equalFunction.run(existingValue, value);
            });
            if (valueIndex == -1)
            {
                throw new NotFoundException("Could not find the value " + value + ".");
            }
            return this.values.removeAt(valueIndex);
        });
    }

    @Override
    public void clear()
    {
        this.values.clear();
    }

    @Override
    public int hashCode()
    {
        return this.values.hashCode();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Set.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Set.toString(this);
    }
}
