package qub;

public abstract class LockedQueueBase<T> implements Queue<T>
{
    private final Queue<T> innerQueue;
    private final SpinMutex mutex;

    protected LockedQueueBase(Queue<T> innerQueue)
    {
        this.innerQueue = innerQueue;
        this.mutex = new SpinMutex();
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
    public T dequeue()
    {
        return mutex.criticalSection(innerQueue::dequeue);
    }

    @Override
    public T peek()
    {
        return mutex.criticalSection(innerQueue::peek);
    }
}
