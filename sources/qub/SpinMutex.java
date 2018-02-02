package qub;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A mutex/lock that can be used to synchronize access to shared resources between multiple threads.
 * This lock uses an atomic variable to synchronize access to shared resources, and as such doesn't
 * require OS interaction.
 */
public class SpinMutex
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
    boolean isAcquired()
    {
        return acquired.get();
    }

    /**
     * Acquire this mutex. If the mutex is already acquired, this thread will block until the owning
     * thread releases this mutex and this thread acquires the mutex.
     */
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
    public boolean tryAcquire()
    {
        return acquired.compareAndSet(false, true);
    }

    /**
     * Release this SpinMutex so that other threads can acquire it.
     * @return Whether or not this SpinMutex was released.
     */
    public boolean release()
    {
        return acquired.compareAndSet(true, false);
    }

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param action The action to run after acquiring this Mutex.
     */
    public void criticalSection(Action0 action)
    {
        if (action != null)
        {
            acquire();
            try
            {
                action.run();
            }
            finally
            {
                release();
            }
        }
    }

    /**
     * Run the provided function after this Mutex has been acquired and automatically release the
     * Mutex when the function completes.
     * @param function The function to run after acquiring this Mutex.
     * @return The return value from the function.
     */
    public <T> T criticalSection(Function0<T> function)
    {
        T result = null;
        if (function != null)
        {
            acquire();
            try
            {
                result = function.run();
            }
            finally
            {
                release();
            }
        }
        return result;
    }
}
