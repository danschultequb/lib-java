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
        this.values.addAll(values);
    }

    @Override
    public void enqueueAll(Iterable<T> values)
    {
        this.values.addAll(values);
    }

    @Override
    public T dequeue()
    {
        PreCondition.assertTrue(any(), "any()");

        return values.removeFirst();
    }

    @Override
    public Result<T> peek()
    {
        return values.any() ? Result.success(values.first()) : Result.error(new QueueEmptyException());
    }
}
