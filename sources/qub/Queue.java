package qub;

/**
 * A data structure that allows values to be added and removed in First-In-First-Out order.
 * @param <T> The type of values that can be added to this Queue.
 */
public interface Queue<T>
{
    /**
     * Get whether or not there are any values in the Queue.
     * @return Whether or not there are any values in the Queue.
     */
    boolean any();

    /**
     * Get the number of values that are in the Queue.
     * @return The number of values that are in the Queue.
     */
    int getCount();

    /**
     * Add the provided value to the Queue.
     * @param value The value to add to the Queue.
     */
    void enqueue(T value);

    /**
     * Remove and return the next value from the Queue. If there are no values in the Queue, then
     * null will be returned.
     * @return The next value from the Queue or null if the Queue is empty.
     */
    T dequeue();

    /**
     * Get the next value from the Queue without removing it. If there are no values in the Queue,
     * then null will be returned.
     * @return The next value from the Queue or null if the Queue is empty.
     */
    T peek();
}
