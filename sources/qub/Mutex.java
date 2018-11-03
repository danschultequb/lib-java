package qub;

/**
 * A synchronization primitive that permits a thread to lock a section of code so that only a single
 * thread will enter at a time.
 */
public interface Mutex
{
    /**
     * The Clock that will be utilized for operations with timeouts.
     * @return The Clock that will be utilized for operations with timeouts.
     */
    Clock getClock();

    /**
     * Get whether or not this Mutex is currently acquired.
     * @return Whether or not this Mutex is currently acquired.
     */
    boolean isAcquired();

    /**
     * Get whether or not this Mutex is currently acquired by the current thread.
     * @return Whether or not this Mutex is currently acquired by the current thread.
     */
    boolean isAcquiredByCurrentThread();

    /**
     * Acquire this mutex. If the mutex is already acquired, this thread will block until the owning
     * thread releases this mutex and this thread acquires the mutex.
     */
    Result<Boolean> acquire();

    /**
     * Acquire this mutex. If the mutex is already acquired, this thread will block until the owning
     * thread releases this mutex and this thread acquires the mutex.
     */
    default Result<Boolean> acquire(Duration timeout)
    {
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertNotNull(getClock(), "getClock()");

        final DateTime endDateTime = getClock().getCurrentDateTime().plus(timeout);
        return acquire(endDateTime);
    }

    /**
     * Acquire this mutex. If the mutex is already acquired, this thread will block until the owning
     * thread releases this mutex and this thread acquires the mutex.
     */
    default Result<Boolean> acquire(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertNotNull(getClock(), "getClock()");

        final Clock clock = getClock();

        Result<Boolean> result = null;
        while (result == null)
        {
            if (clock.getCurrentDateTime().greaterThanOrEqualTo(timeout))
            {
                result = Result.error(new TimeoutException());
            }
            else if (tryAcquire())
            {
                result = Result.successTrue();
            }
        }

        return result;
    }

    /**
     * Attempt to acquire this SpinMutex and return whether or not it was acquired.
     * @return Whether or not the SpinMutex was acquired.
     */
    boolean tryAcquire();

    /**
     * Release this SpinMutex so that other threads can acquire it.
     * @return Whether or not this SpinMutex was released.
     */
    boolean release();

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param action The action to run after acquiring this Mutex.
     */
    default Result<Boolean> criticalSection(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        acquire();
        try
        {
            action.run();
        }
        finally
        {
            release();
        }

        return Result.successTrue();
    }

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param action The action to run after acquiring this Mutex.
     */
    default Result<Boolean> criticalSection(Duration timeout, Action0 action)
    {
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertNotNull(action, "action");
        PreCondition.assertNotNull(getClock(), "getClock()");

        return criticalSection(getClock().getCurrentDateTime().plus(timeout), action);
    }

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param action The action to run after acquiring this Mutex.
     */
    default Result<Boolean> criticalSection(DateTime timeout, Action0 action)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertNotNull(action, "action");
        PreCondition.assertNotNull(getClock(), "getClock()");

        final Result<Boolean> result = acquire(timeout);
        if (result.equals(Result.successTrue()))
        {
            try
            {
                action.run();
            }
            finally
            {
                release();
            }
        }

        return result;
    }

    /**
     * Run the provided function after this Mutex has been acquired and automatically release the
     * Mutex when the function completes.
     * @param function The function to run after acquiring this Mutex.
     * @return The return value from the function.
     */
    default <T> T criticalSection(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        T result = null;
        acquire();
        try
        {
            result = function.run();
        }
        finally
        {
            release();
        }
        return result;
    }

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param timeout The maximum amount of time to wait for this criticalSection.
     * @param function The function to run after acquiring this Mutex.
     */
    default <T> Result<T> criticalSection(Duration timeout, Function0<T> function)
    {
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertNotNull(function, "function");
        PreCondition.assertNotNull(getClock(), "getClock()");

        return criticalSection(getClock().getCurrentDateTime().plus(timeout), function);
    }

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param timeout The maximum amount of time to wait for this criticalSection.
     * @param function The function to run after acquiring this Mutex.
     */
    default <T> Result<T> criticalSection(DateTime timeout, Function0<T> function)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertNotNull(function, "function");
        PreCondition.assertNotNull(getClock(), "getClock()");

        return criticalSectionResult(timeout, () -> Result.success(function.run()));
    }

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param timeout The maximum amount of time to wait for this criticalSection.
     * @param function The function to run after acquiring this Mutex.
     */
    default <T> Result<T> criticalSectionResult(Duration timeout, Function0<Result<T>> function)
    {
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertNotNull(function, "function");
        PreCondition.assertNotNull(getClock(), "getClock()");

        return criticalSectionResult(getClock().getCurrentDateTime().plus(timeout), function);
    }

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param timeout The maximum amount of time to wait for this criticalSection.
     * @param function The function to run after acquiring this Mutex.
     */
    default <T> Result<T> criticalSectionResult(DateTime timeout, Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertNotNull(function, "function");
        PreCondition.assertNotNull(getClock(), "getClock()");

        Result<T> result;

        final Result<Boolean> acquireResult = acquire(timeout);
        if (acquireResult.hasError())
        {
            result = Result.error(acquireResult.getError());
        }
        else
        {
            try
            {
                result = function.run();
            }
            catch (Throwable error)
            {
                result = Result.error(error);
            }
            finally
            {
                release();
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Acquire this mutex and return a Disposable that will release this mutex when it is disposed.
     * @return
     */
    default Disposable criticalSection()
    {
        acquire();
        return new BasicDisposable(this::release);
    }

    /**
     * Create a new condition that can be used to block until certain constraints are true.
     * @return A new condition.
     */
    MutexCondition createCondition();
}
