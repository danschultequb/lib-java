package qub;

/**
 * A data structure that allows values to be added and removed in First-In-First-Out order.
 * @param <T> The type of values that can be added to this {@link Queue}.
 */
public interface Queue<T>
{
    /**
     * Create a new empty {@link Queue}.
     * @param <T> The type of values stored in the {@link Queue}.
     */
    public static <T> Queue<T> create()
    {
        return ListQueue.create();
    }

    /**
     * Create a new {@link Queue} with the provided initial values.
     * @param initialValues The initial values to populate the new {@link Queue} with.
     * @param <T> The type of values stored in the new {@link Queue}.
     */
    public static <T> Queue<T> create(Iterable<T> initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        final Queue<T> result = Queue.create();
        result.enqueueAll(initialValues);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(initialValues.getCount(), result.getCount(), "result.getCount()");

        return result;
    }

    /**
     * Get whether there are any values in the {@link Queue}.
     */
    boolean any();

    /**
     * Get the number of values that are in the {@link Queue}.
     */
    int getCount();

    /**
     * Add the provided value to the {@link Queue}.
     */
    Queue<T> enqueue(T value);

    /**
     * Add the provided values to the {@link Queue}.
     * @param values The values to add to the {@link Queue}.
     */
    default Queue<T> enqueueAll(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        for (final T value : values)
        {
            this.enqueue(value);
        }

        return this;
    }

    /**
     * Add the provided values to the {@link Queue}.
     * @param values The values to add to the {@link Queue}.
     */
    default Queue<T> enqueueAll(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        for (final T value : values)
        {
            this.enqueue(value);
        }

        return this;
    }

    /**
     * Remove and return the next value from the {@link Queue}.
     * @exception EmptyException if the {@link Queue} is empty.
     */
    Result<T> dequeue();

    /**
     * Get the next value create the {@link Queue} without removing it.
     * @exception EmptyException if the {@link Queue} is empty.
     */
    Result<T> peek();
}
