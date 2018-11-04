package qub;

public class LockedQueue<T> implements Queue<T>
{
    private final Queue<T> innerQueue;
    private final Mutex mutex;

    public LockedQueue(Queue<T> innerQueue)
    {
        this(innerQueue, new SpinMutex());
    }

    public LockedQueue(Queue<T> innerQueue, Mutex mutex)
    {
        this.innerQueue = innerQueue;
        this.mutex = mutex;
    }


    @Override
    public boolean any()
    {
        return mutex.criticalSection(innerQueue::any);
    }

    @Override
    public int getCount()
    {
        return mutex.criticalSection(innerQueue::getCount);
    }

    @Override
    public void enqueue(final T value)
    {
        mutex.criticalSection(() -> innerQueue.enqueue(value));
    }

    @Override
    public void enqueueAll(T[] values)
    {
        mutex.criticalSection(() -> innerQueue.enqueueAll(values));
    }

    @Override
    public void enqueueAll(Iterable<T> values)
    {
        mutex.criticalSection(() -> innerQueue.enqueueAll(values));
    }

    @Override
    public T dequeue()
    {
        return mutex.criticalSection(innerQueue::dequeue);
    }

    @Override
    public Result<T> peek()
    {
        return mutex.criticalSection(innerQueue::peek);
    }
}
