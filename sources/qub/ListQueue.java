package qub;

/**
 * A data structure that allows values to be added and removed in First-In-First-Out order.
 * @param <T> The type of values that can be added to this {@link ListQueue}.
 */
public class ListQueue<T> implements Queue<T>
{
    private final List<T> values;

    /**
     * Create a new {@link ListQueue}.
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
        return Result.create(() ->
        {
            if (!this.any())
            {
                throw new EmptyException();
            }
            return this.values.removeFirst();
        });
    }

    @Override
    public Result<T> peek()
    {
        return Result.create(() ->
        {
            if (!this.any())
            {
                throw new EmptyException();
            }
            return this.values.first();
        });
    }
}
