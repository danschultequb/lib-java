package qub;

/**
 * A data structure that allows values to be added in a First-In-Last-Out order.
 * @param <T> The type of values that can be added to this {@link Stack}.
 */
public interface Stack<T>
{
    /**
     * Create a new empty {@link Stack}.
     * @param <T> The type of values stored in the new {@link Stack}.
     */
    public static <T> Stack<T> create()
    {
        return ListStack.create();
    }

    /**
     * Create a new {@link Stack}. The last value in the provided values will be on the top of the
     * returned {@link Stack}.
     * @param values The values to initialize the {@link Stack} with. The last of these values will
     *               be on the top of the returned {@link Stack}.
     * @param <T> The type of values stored in the new {@link Stack}.
     */
    @SafeVarargs
    public static <T> Stack<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        return Stack.create(Iterator.create(values));
    }

    /**
     * Create a new {@link Stack}. The last value in the provided values will be on the top of the
     * returned {@link Stack}.
     * @param values The values to initialize the {@link Stack} with. The last of these values will
     *               be on the top of the returned {@link Stack}.
     * @param <T> The type of values stored in the new {@link Stack}.
     */
    public static <T> Stack<T> create(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Stack.create(values.iterate());
    }

    /**
     * Create a new {@link Stack}. The last value in the provided values will be on the top of the
     * returned {@link Stack}.
     * @param values The values to initialize the {@link Stack} with. The last of these values will
     *               be on the top of the returned {@link Stack}.
     * @param <T> The type of values stored in the new {@link Stack}.
     */
    public static <T> Stack<T> create(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        final Stack<T> result = Stack.create();
        result.pushAll(values);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get whether this {@link Stack} contains any values.
     */
    public boolean any();

    /**
     * Get the number of values that are in this {@link Stack}.
     */
    public int getCount();

    /**
     * Add a new value on top of this {@link Stack}.
     * @return This object for method chaining.
     */
    public Stack<T> push(T value);

    /**
     * Push each of the provided values on top of this {@link Stack}. The last value in the
     * {@link Iterable} will be the new top of this {@link Stack}.
     * @param values The values to push on top of this {@link Stack}.
     * @return This object for method chaining.
     */
    public Stack<T> pushAll(Iterable<T> values);

    /**
     * Push each of the provided values on top of this {@link Stack}. The last value in the
     * {@link Iterator} will be the new top of this {@link Stack}.
     * @param values The values to push on top of this {@link Stack}.
     * @return This object for method chaining.
     */
    public Stack<T> pushAll(Iterator<T> values);

    /**
     * Remove and return the most recently pushed value.
     * @return The most recently pushed value.
     * @exception EmptyException if the {@link Stack} is empty.
     */
    public Result<T> pop();

    /**
     * Get the most recently pushed value.
     * @return The most recently pushed value.
     * @exception EmptyException if the {@link Stack} is empty.
     */
    public Result<T> peek();

    /**
     * Get whether this {@link Stack} contains the provided value.
     * @param value The value to look for.
     */
    public boolean contains(T value);
}
