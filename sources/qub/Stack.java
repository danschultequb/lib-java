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
    public Stack()
    {
        values = new ArrayList<>();
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
}
