package qub;

/**
 * A data structure that allows values to be added and removed in First-In-First-Setable order.
 * @param <T> The type of values that can be added to this Queue.
 */
public interface BlockingQueue<T>
{
    /**
     * Get the number of elements in the BlockingQueue.
     * @return The number of elements in the BlockingQueue.
     */
    int getCount();

    /**
     * Get whether or not this BlockingQueue has any elements.
     * @return Whether or not this BlockingQueue has any elements.
     */
    boolean any();

    /**
     * Add the provided value to the Queue.
     * @param value The value to add to the Queue.
     * @return The result of adding the value to the Queue.
     */
    Result<Void> enqueue(T value);

    /**
     * Add the provided value to the Queue.
     * @param value The value to add to the Queue.
     * @return The result of adding the value to the Queue.
     */
    AsyncFunction<Result<Void>> enqueueAsync(T value);

    /**
     * Remove and return the next value from the Queue. If there are no values in the Queue, then
     * the thread will block until a value is added to the Queue.
     * @return The next value from the Queue.
     */
    Result<T> dequeue();

    /**
     * Remove and return the next value from the Queue. If there are no values in the Queue, then
     * the thread will block until a value is added to the Queue.
     * @return The next value from the Queue.
     */
    AsyncFunction<Result<T>> dequeueAsync();
}
