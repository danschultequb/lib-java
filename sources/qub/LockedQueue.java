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
        return mutex.criticalSection(new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return innerQueue.any();
            }
        });
    }

    @Override
    public int getCount()
    {
        return mutex.criticalSection(new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                return innerQueue.getCount();
            }
        });
    }

    @Override
    public void enqueue(final T value)
    {
        mutex.criticalSection(new Action0()
        {
            @Override
            public void run()
            {
                innerQueue.enqueue(value);
            }
        });
    }

    @Override
    public T dequeue()
    {
        return mutex.criticalSection(new Function0<T>()
        {
            @Override
            public T run()
            {
                return innerQueue.dequeue();
            }
        });
    }

    @Override
    public T peek()
    {
        return mutex.criticalSection(new Function0<T>()
        {
            @Override
            public T run()
            {
                return innerQueue.peek();
            }
        });
    }
}
