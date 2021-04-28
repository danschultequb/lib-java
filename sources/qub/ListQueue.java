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
    protected ListQueue(List<T> values)
    {
        this.values = values;
    }

    public static <T> ListQueue<T> create()
    {
        return new ListQueue<>(List.create());
    }

    @Override
    public boolean any()
    {
        return this.values.any();
    }

    @Override
    public int getCount()
    {
        return this.values.getCount();
    }

    @Override
    public ListQueue<T> enqueue(T value)
    {
        this.values.add(value);

        return this;
    }

    @Override
    public ListQueue<T> enqueueAll(Iterable<T> values)
    {
        this.values.addAll(values);

        return this;
    }

    @Override
    public Result<T> dequeue()
    {
        return this.values.any()
            ? Result.success(this.values.removeFirst())
            : Result.error(new QueueEmptyException());
    }

    @Override
    public Result<T> peek()
    {
        return this.values.any()
            ? Result.success(this.values.first())
            : Result.error(new QueueEmptyException());
    }
}
