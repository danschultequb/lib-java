package qub;

/**
 * A {@link Synchronization} primitive that permits a {@link Thread} to lock a section of code so
 * that only a single {@link Thread} will enter at a time.
 */
public interface Mutex
{
    /**
     * Get whether this {@link Mutex} is acquired.
     */
    boolean isAcquired();

    /**
     * Get whether this {@link Mutex} is acquired by the {@link CurrentThread}.
     */
    boolean isAcquiredByCurrentThread();

    /**
     * Acquire this {@link Mutex}. If the {@link Mutex} is already acquired, the
     * {@link CurrentThread} will block until the owning {@link Thread} releases this {@link Mutex}.
     */
    Result<Void> acquire();

    /**
     * Acquire this {@link Mutex}. If the {@link Mutex} is already acquired, the
     * {@link CurrentThread} will block for the provided {@link Duration} for the owning
     * {@link Thread} to release this {@link Mutex}. If the owning {@link Thread} doesn't release
     * this {@link Mutex} in time, then a {@link TimeoutException} will be returned.
     */
    Result<Void> acquire(Duration durationTimeout);

    /**
     * Acquire this {@link Mutex}. If the {@link Mutex} is already acquired, the
     * {@link CurrentThread} will until the provided {@link DateTime} for the owning {@link Thread}
     * to release this {@link Mutex}. If the owning {@link Thread} doesn't release this
     * {@link Mutex} in time, then a {@link TimeoutException} will be returned.
     */
    Result<Void> acquire(DateTime dateTimeTimeout);

    /**
     * Try to acquire this {@link Mutex} and return whether it was acquired.
     */
    Result<Boolean> tryAcquire();

    /**
     * Release this {@link Mutex} so that other {@link Thread}s can acquire it.
     */
    Result<Void> release();

    /**
     * Run the provided {@link Action0} after this {@link Mutex} has been acquired. This
     * {@link Mutex} will automatically be released when the {@link Action0} completes.
     * @param action The {@link Action0} to run after acquiring this {@link Mutex}.
     */
    public default Result<Void> criticalSection(Action0 action)
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
    default Result<Void> criticalSection(Duration durationTimeout, Action0 action)
    {
        PreCondition.assertNotNull(durationTimeout, "durationTimeout");
        PreCondition.assertGreaterThan(durationTimeout, Duration.zero, "durationTimeout");
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
    default <T> Result<T> criticalSection(Duration durationTimeout, Function0<T> function)
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
