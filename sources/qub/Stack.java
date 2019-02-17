package qub;

/**
 * A data structure that allows values to be added in a First-In-Last-Setable order.
 * @param <T> The type of values that can be added to this Stack.
 */
public class Stack<T>
{
    private final List<T> values;

    /**
     * Create a new empty Stack.
     */
    private Stack()
    {
        values = List.empty();
    }

    /**
     * Create a new Stack.
     * @param <T> The type of values stored in the new Stack.
     * @return The new Stack.
     */
    public static <T> Stack<T> empty()
    {
        return new Stack<>();
    }

    /**
     * Create a new Stack.
     * @param <T> The type of values stored in the new Stack.
     * @return The new Stack.
     */
    @SafeVarargs
    public static <T> Stack<T> create(T... values)
    {
        PreCondition.assertNotNull(values, "values");

        final Stack<T> result = Stack.empty();
        for (final T value : values)
        {
            result.push(value);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(values.length, result.getCount(), "result.getCount()");

        return result;
    }

    /**
     * Get whether or not this Stack contains any values.
     * @return Whether or not this Stack contains any values.
     */
    public boolean any()
    {
        return values.any();
    }

    /**
     * Get the number of values that are in this Stack.
     * @return The number of values that are in this Stack.
     */
    public int getCount()
    {
        return values.getCount();
    }

    /**
     * Add a new value on top of this Stack.
     */
    public void push(T value)
    {
        values.add(value);
    }

    /**
     * Push each of the provided values on top of this Stack.
     * @param values The values to push on top of this Stack.
     */
    public void pushAll(Iterable<T> values)
    {
        if (values != null)
        {
            for (final T value : values)
            {
                push(value);
            }
        }
    }

    /**
     * Remove and return the most recently pushed value. If the Stack is empty, then null will be
     * returned.
     * @return The most recently pushed value, or null if the Stack is empty.
     */
    public T pop()
    {
        return !any() ? null : values.removeAt(getCount() - 1);
    }

    /**
     * Get the most recently pushed value. If the Stack is empty, then null will be returned.
     * @return The most recently pushed value, or null if the Stack is empty.
     */
    public T peek()
    {
        return !any() ? null : values.get(getCount() - 1);
    }

    /**
     * Get whether or not this Stack contains the provided value.
     * @param value The value to look for.
     * @return Whether or not this Stack contains the provided value.
     */
    public boolean contains(T value)
    {
        return values.contains(value);
    }

    /**
     * Get whether or not this Stack does not contain the provided value.
     * @param value The value to look for.
     * @return Whether or not this Stack does not contain the provided value.
     */
    public boolean doesNotContain(T value)
    {
        return !contains(value);
    }
}
