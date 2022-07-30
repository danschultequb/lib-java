package qub;

public class ListStack<T> implements Stack<T>
{
    private final List<T> values;

    /**
     * Create a new empty {@link ListStack}.
     */
    private ListStack()
    {
        this.values = List.create();
    }

    /**
     * Create a new empty {@link ListStack}.
     * @param <T> The type of values stored in the new {@link ListStack}.
     */
    public static <T> ListStack<T> create()
    {
        return new ListStack<>();
    }

    /**
     * Create a new {@link ListStack}. The last value in the provided values will be on the top of the
     * returned {@link ListStack}.
     * @param values The values to initialize the {@link ListStack} with. The last of these values will
     *               be on the top of the returned {@link ListStack}.
     * @param <T> The type of values stored in the new {@link ListStack}.
     */
    @SafeVarargs
    public static <T> ListStack<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        return ListStack.create(Iterator.create(values));
    }

    /**
     * Create a new {@link ListStack}. The last value in the provided values will be on the top of the
     * returned {@link ListStack}.
     * @param values The values to initialize the {@link ListStack} with. The last of these values will
     *               be on the top of the returned {@link ListStack}.
     * @param <T> The type of values stored in the new {@link ListStack}.
     */
    public static <T> ListStack<T> create(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        return ListStack.create(values.iterate());
    }

    /**
     * Create a new {@link ListStack}. The last value in the provided values will be on the top of the
     * returned {@link ListStack}.
     * @param values The values to initialize the {@link ListStack} with. The last of these values will
     *               be on the top of the returned {@link ListStack}.
     * @param <T> The type of values stored in the new {@link ListStack}.
     */
    public static <T> ListStack<T> create(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        final ListStack<T> result = ListStack.create();
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
    public ListStack<T> push(T value)
    {
        this.values.add(value);

        return this;
    }

    @Override
    public ListStack<T> pushAll(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values.addAll(values);

        return this;
    }

    @Override
    public ListStack<T> pushAll(Iterator<T> values)
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
        return this.values.last();
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
