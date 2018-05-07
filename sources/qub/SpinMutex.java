package qub;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A mutex/lock that can be used to synchronize access to shared resources between multiple threads.
 * This lock uses an atomic variable to synchronize access to shared resources, and as such doesn't
 * require OS interaction.
 */
public class SpinMutex extends MutexBase
{
    private final AtomicBoolean acquired;

    /**
     * Create a new SpinMutex that is ready to be acquired.
     */
    public SpinMutex()
    {
        acquired = new AtomicBoolean(false);
    }

    /**
     * Get whether or not this SpinMutex is currently acquired.
     * @return Whether or not this SpinMutex is currently acquired.
     */
    @Override
    public boolean isAcquired()
    {
        return acquired.get();
    }

    /**
     * Acquire this mutex. If the mutex is already acquired, this thread will block until the owning
     * thread releases this mutex and this thread acquires the mutex.
     */
    @Override
    public void acquire()
    {
        while (!tryAcquire())
        {
        }
    }

    /**
     * Attempt to acquire this SpinMutex and return whether or not it was acquired.
     * @return Whether or not the SpinMutex was acquired.
     */
    @Override
    public boolean tryAcquire()
    {
        return acquired.compareAndSet(false, true);
    }

    /**
     * Release this SpinMutex so that other threads can acquire it.
     * @return Whether or not this SpinMutex was released.
     */
    @Override
    public boolean release()
    {
        return acquired.compareAndSet(true, false);
    }
}
