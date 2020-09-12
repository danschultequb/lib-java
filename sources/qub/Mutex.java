package qub;

/**
 * A synchronization primitive that permits a thread to lock a section of code so that only a single
 * thread will enter at a time.
 */
public interface Mutex
{
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
    Result<Void> acquire();

    /**
     * Acquire this mutex. If the mutex is already acquired, this thread will block until the owning
     * thread releases this mutex and this thread acquires the mutex.
     */
    Result<Void> acquire(Duration2 durationTimeout);

    /**
     * Acquire this mutex. If the mutex is already acquired, this thread will block until the owning
     * thread releases this mutex and this thread acquires the mutex.
     */
    Result<Void> acquire(DateTime dateTimeTimeout);

    /**
     * Attempt to acquire this SpinMutex and return whether or not it was acquired.
     * @return Whether or not the SpinMutex was acquired.
     */
    Result<Boolean> tryAcquire();

    /**
     * Release this SpinMutex so that other threads can acquire it.
     * @return Whether or not this SpinMutex was released.
     */
    Result<Void> release();

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param action The action to run after acquiring this Mutex.
     */
    default Result<Void> criticalSection(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return Result.create(() ->
        {
            this.acquire().await();
            try
            {
                action.run();
            }
            finally
            {
                this.release().await();
            }
        });
    }

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param action The action to run after acquiring this Mutex.
     */
    default Result<Void> criticalSection(Duration2 durationTimeout, Action0 action)
    {
        PreCondition.assertNotNull(durationTimeout, "durationTimeout");
        PreCondition.assertGreaterThan(durationTimeout, Duration2.zero, "durationTimeout");
        PreCondition.assertNotNull(action, "action");

        return Result.create(() ->
        {
            this.acquire(durationTimeout).await();
            try
            {
                action.run();
            }
            finally
            {
                this.release().await();
            }
        });
    }

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param action The action to run after acquiring this Mutex.
     */
    default Result<Void> criticalSection(DateTime dateTimeTimeout, Action0 action)
    {
        PreCondition.assertNotNull(dateTimeTimeout, "dateTimeTimeout");
        PreCondition.assertNotNull(action, "action");

        return Result.create(() ->
        {
            this.acquire(dateTimeTimeout).await();
            try
            {
                action.run();
            }
            finally
            {
                this.release().await();
            }
        });
    }

    /**
     * Run the provided function after this Mutex has been acquired and automatically release the
     * Mutex when the function completes.
     * @param function The function to run after acquiring this Mutex.
     * @return The return value create the function.
     */
    default <T> Result<T> criticalSection(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return Result.create(() ->
        {
            final Value<T> result = Value.create();
            this.criticalSection(() -> { result.set(function.run()); }).await();
            return result.get();
        });
    }

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param durationTimeout The maximum amount of time to wait for this criticalSection.
     * @param function The function to run after acquiring this Mutex.
     */
    default <T> Result<T> criticalSection(Duration2 durationTimeout, Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return Result.create(() ->
        {
            final Value<T> result = Value.create();
            this.criticalSection(durationTimeout, () -> { result.set(function.run()); }).await();
            return result.get();
        });
    }

    /**
     * Run the provided action after this Mutex has been acquired and automatically release the
     * Mutex when the action completes.
     * @param dateTimeTimeout The maximum amount of time to wait for this criticalSection.
     * @param function The function to run after acquiring this Mutex.
     */
    default <T> Result<T> criticalSection(DateTime dateTimeTimeout, Function0<T> function)
    {
        PreCondition.assertNotNull(dateTimeTimeout, "dateTimeTimeout");
        PreCondition.assertNotNull(function, "function");

        PreCondition.assertNotNull(function, "function");

        return Result.create(() ->
        {
            final Value<T> result = Value.create();
            this.criticalSection(dateTimeTimeout, () -> { result.set(function.run()); }).await();
            return result.get();
        });
    }

    /**
     * Create a new condition that can be used to block until it is signaled.
     * @return A new condition.
     */
    MutexCondition createCondition();

    /**
     * Create a new MutexCondition that will be watch the provided condition to be true when it is
     * signaled.
     * @param condition The condition that must be true in order for the MutexCondition to move
     *                  forward.
     * @return The newly created MutexCondition.
     */
    MutexCondition createCondition(Function0<Boolean> condition);
}
