package qub;

/**
 * A data structure that allows values to be added and removed in First-In-First-Out order.
 * @param <T> The type of values that can be added to this Queue.
 */
public abstract class QueueBase<T> implements Queue<T>
{
    private final List<T> values;

    /**
     * Create a new Queue.
     */
    protected QueueBase(List<T> values)
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
