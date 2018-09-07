package qub;

/**
 * A data structure that allows values to be added and removed in First-In-First-Setable order.
 * @param <T> The type of values that can be added to this Queue.
 */
public class ListQueue<T> implements Queue<T>
{
    private final List<T> values;

    /**
     * Create a new Queue.
     */
    public ListQueue(List<T> values)
    {
        this.values = values;
    }

    @Override
    public boolean any()
    {
        return values.any();
    }

    @Override
    public int getCount()
    {
        return values.getCount();
    }

    @Override
    public void enqueue(T value)
    {
        values.add(value);
    }

    @Override
    public void enqueueAll(T[] values)
    {
        PreCondition.assertNotNull(values, "values");

        enqueueAll(Array.fromValues(values));
    }

    @Override
    public void enqueueAll(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values.addAll(values);
    }

    @Override
    public T dequeue()
    {
        return values.removeFirst();
    }

    @Override
    public T peek()
    {
        return values.first();
    }
}
