package qub;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A mutex/lock that can be used to synchronize access to shared resources between multiple threads.
 * This lock uses an atomic variable to synchronize access to shared resources, and as such doesn't
 * require OS interaction.
 */
public class SpinMutex extends MutexBase
{
    private final AtomicLong acquiredByThreadId;
    private final AtomicLong acquiredCount;

    /**
     * Create a new SpinMutex that is ready to be acquired.
     */
    public SpinMutex()
    {
        acquiredByThreadId = new AtomicLong(-1);
        acquiredCount = new AtomicLong(0);
    }

    /**
     * Get whether or not this SpinMutex is currently acquired.
     * @return Whether or not this SpinMutex is currently acquired.
     */
    @Override
    public boolean isAcquired()
    {
        return acquiredByThreadId.get() != -1;
    }

    /**
     * Acquire this mutex. If the mutex is already acquired, this thread will block until the owning
     * thread releases this mutex and this thread acquires the mutex.
     */
    @Override
    public void acquire()
    {
        final long threadId = Thread.currentThread().getId();
        if (acquiredByThreadId.get() != threadId)
        {
            while (!acquiredByThreadId.compareAndSet(-1, threadId))
            {
                while (isAcquired())
                {
                }
            }
        }
        acquiredCount.incrementAndGet();
    }

    /**
     * Attempt to acquire this SpinMutex and return whether or not it was acquired.
     * @return Whether or not the SpinMutex was acquired.
     */
    @Override
    public boolean tryAcquire()
    {
        final long threadId = Thread.currentThread().getId();
        final boolean acquired = acquiredByThreadId.get() == threadId || acquiredByThreadId.compareAndSet(-1, threadId);
        if (acquired)
        {
            acquiredCount.incrementAndGet();
        }
        return acquired;
    }

    /**
     * Release this SpinMutex so that other threads can acquire it.
     * @return Whether or not this SpinMutex was released.
     */
    @Override
    public boolean release()
    {
        final long threadId = Thread.currentThread().getId();
        boolean result = false;
        if (acquiredByThreadId.get() == threadId)
        {
            if (acquiredCount.decrementAndGet() == 0)
            {
                acquiredByThreadId.compareAndSet(threadId, -1);
                result = true;
            }
        }
        return result;
    }

    @Override
    public MutexCondition createCondition()
    {
        return new SpinMutexCondition(this);
    }
}
