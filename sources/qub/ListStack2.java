package qub;

@Deprecated
public class ListStack2<T> implements Stack2<T>
{
    private final List<T> values;

    /**
     * Create a new empty {@link ListStack2}.
     */
    private ListStack2()
    {
        this.values = List.create();
    }

    /**
     * Create a new empty {@link ListStack2}.
     * @param <T> The type of values stored in the new {@link ListStack2}.
     */
    public static <T> ListStack2<T> create()
    {
        return new ListStack2<>();
    }

    /**
     * Create a new {@link ListStack2}. The last value in the provided values will be on the top of the
     * returned {@link ListStack2}.
     * @param values The values to initialize the {@link ListStack2} with. The last of these values will
     *               be on the top of the returned {@link ListStack2}.
     * @param <T> The type of values stored in the new {@link ListStack2}.
     */
    @SafeVarargs
    public static <T> ListStack2<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        return ListStack2.create(Iterator.create(values));
    }

    /**
     * Create a new {@link ListStack2}. The last value in the provided values will be on the top of the
     * returned {@link ListStack2}.
     * @param values The values to initialize the {@link ListStack2} with. The last of these values will
     *               be on the top of the returned {@link ListStack2}.
     * @param <T> The type of values stored in the new {@link ListStack2}.
     */
    public static <T> ListStack2<T> create(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        return ListStack2.create(values.iterate());
    }

    /**
     * Create a new {@link ListStack2}. The last value in the provided values will be on the top of the
     * returned {@link ListStack2}.
     * @param values The values to initialize the {@link ListStack2} with. The last of these values will
     *               be on the top of the returned {@link ListStack2}.
     * @param <T> The type of values stored in the new {@link ListStack2}.
     */
    public static <T> ListStack2<T> create(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        final ListStack2<T> result = ListStack2.create();
        result.pushAll(values);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public boolean any()
    {
        return this.values.any();
    }

    @Override
    public int getCount()
    {
        return this.values.getCount();
    }

    @Override
    public ListStack2<T> push2(T value)
    {
        this.values.add(value);

        return this;
    }

    @Override
    public ListStack2<T> pushAll(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values.addAll(values);
        return this;
    }

    @Override
    public ListStack2<T> pushAll(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values.addAll(values);
        return this;
    }

    @Override
    public Result<T> pop()
    {
        return Result.create(() ->
        {
            if (!this.any())
            {
                throw new EmptyException();
            }
            return this.values.removeLast();
        });
    }

    @Override
    public Result<T> peek()
    {
        return Result.create(() ->
        {
            if (!this.any())
            {
                throw new EmptyException();
            }
            return this.values.last();
        });
    }

    @Override
    public boolean contains(T value)
    {
        return this.values.contains(value);
    }

    @Override
    public String toString()
    {
        return this.values.toString();
    }
}
