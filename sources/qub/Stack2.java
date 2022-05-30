package qub;

/**
 * A data structure that allows values to be added in a First-In-Last-Out order.
 * @param <T> The type of values that can be added to this {@link Stack2}.
 */
@Deprecated
public interface Stack2<T>
{
    /**
     * Create a new empty {@link Stack2}.
     * @param <T> The type of values stored in the new {@link Stack2}.
     */
    @Deprecated
    public static <T> Stack2<T> create()
    {
        return ListStack2.create();
    }

    /**
     * Create a new {@link Stack2}. The last value in the provided values will be on the top of the
     * returned {@link Stack2}.
     * @param values The values to initialize the {@link Stack2} with. The last of these values will
     *               be on the top of the returned {@link Stack2}.
     * @param <T> The type of values stored in the new {@link Stack2}.
     */
    @SafeVarargs
    @Deprecated
    public static <T> Stack2<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        return Stack2.create(Iterator.create(values));
    }

    /**
     * Create a new {@link Stack2}. The last value in the provided values will be on the top of the
     * returned {@link Stack2}.
     * @param values The values to initialize the {@link Stack2} with. The last of these values will
     *               be on the top of the returned {@link Stack2}.
     * @param <T> The type of values stored in the new {@link Stack2}.
     */
    @Deprecated
    public static <T> Stack2<T> create(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Stack2.create(values.iterate());
    }

    /**
     * Create a new {@link Stack2}. The last value in the provided values will be on the top of the
     * returned {@link Stack2}.
     * @param values The values to initialize the {@link Stack2} with. The last of these values will
     *               be on the top of the returned {@link Stack2}.
     * @param <T> The type of values stored in the new {@link Stack2}.
     */
    @Deprecated
    public static <T> Stack2<T> create(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        final Stack2<T> result = Stack2.create();
        result.pushAll(values);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get whether this {@link Stack2} contains any values.
     */
    @Deprecated
    public boolean any();

    /**
     * Get the number of values that are in this {@link Stack2}.
     */
    @Deprecated
    public int getCount();

    /**
     * Add a new value on top of this {@link Stack2}.
     * @return This object for method chaining.
     */
    @Deprecated
    public Stack2<T> push2(T value);

    /**
     * Push each of the provided values on top of this {@link Stack2}. The last value in the
     * {@link Iterable} will be the new top of this {@link Stack2}.
     * @param values The values to push on top of this {@link Stack2}.
     * @return This object for method chaining.
     */
    @Deprecated
    public Stack2<T> pushAll(Iterable<T> values);

    /**
     * Push each of the provided values on top of this {@link Stack2}. The last value in the
     * {@link Iterator} will be the new top of this {@link Stack2}.
     * @param values The values to push on top of this {@link Stack2}.
     * @return This object for method chaining.
     */
    @Deprecated
    public Stack2<T> pushAll(Iterator<T> values);

    /**
     * Remove and return the most recently pushed value.
     * @return The most recently pushed value.
     * @exception EmptyException if the {@link Stack2} is empty.
     */
    @Deprecated
    public Result<T> pop();

    /**
     * Get the most recently pushed value.
     * @return The most recently pushed value.
     * @exception EmptyException if the {@link Stack2} is empty.
     */
    @Deprecated
    public Result<T> peek();

    /**
     * Get whether this {@link Stack2} contains the provided value.
     * @param value The value to look for.
     */
    @Deprecated
    public boolean contains(T value);
}
