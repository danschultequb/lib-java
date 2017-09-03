package qub;

/**
 * A data structure that allows values to be added and removed in First-In-First-Out order.
 * @param <T> The type of values that can be added to this Queue.
 */
public class Queue<T>
{
    private final ArrayList<T> values;

    /**
     * Create a new Queue.
     */
    public Queue()
    {
        values = new ArrayList<T>();
    }

    /**
     * Get whether or not there are any values in the Queue.
     * @return Whether or not there are any values in the Queue.
     */
    public boolean any()
    {
        return values.any();
    }

    /**
     * Get the number of values that are in the Queue.
     * @return The number of values that are in the Queue.
     */
    public int getCount()
    {
        return values.getCount();
    }

    /**
     * Add the provided value to the Queue.
     * @param value The value to add to the Queue.
     */
    public void enqueue(T value)
    {
        values.add(value);
    }

    /**
     * Remove and return the next value from the Queue. If there are no values in the Queue, then
     * null will be returned.
     * @return The next value from the Queue or null if the Queue is empty.
     */
    public T dequeue()
    {
        return values.removeAt(0);
    }

    /**
     * Get the next value from the Queue without removing it. If there are no values in the Queue,
     * then null will be returned.
     * @return The next value from the Queue or null if the Queue is empty.
     */
    public T peek()
    {
        return values.get(0);
    }

    /**
     * Iterate through the values in the Queue without removing them.
     * @return An iterator that can iterate through the values in the Queue.
     */
    public Iterator<T> iterate()
    {
        return values.iterate();
    }
}
