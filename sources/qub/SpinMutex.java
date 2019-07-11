package qub;

/**
 * A mutex/lock that can be used to synchronize access to shared resources between multiple threads.
 * This lock uses an atomic variable to synchronize access to shared resources, and as such doesn't
 * require OS interaction.
 */
public class SpinMutex implements Mutex
{
    private final LongValue acquiredByThreadId;
    private final LongValue acquiredCount;
    private final Clock clock;

    public SpinMutex()
    {
        this(null);
    }

    /**
     * Create a new SpinMutex that is ready to be acquired.
     */
    public SpinMutex(Clock clock)
    {
        acquiredByThreadId = LongValue.create(-1);
        acquiredCount = LongValue.create(0);
        this.clock = clock;
    }

    @Override
    public boolean isAcquired()
    {
        return acquiredByThreadId.get() != -1;
    }

    @Override
    public boolean isAcquiredByCurrentThread()
    {
        return acquiredByThreadId.get() == Thread.currentThread().getId();
    }

    @Override
    public Result<Void> acquire()
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
        acquiredCount.increment();

        return Result.success();
    }

    @Override
    public Result<Void> acquire(Duration durationTimeout)
    {
        PreCondition.assertNotNull(durationTimeout, "durationTimeout");
        PreCondition.assertGreaterThan(durationTimeout, Duration.zero, "durationTimeout");
        PreCondition.assertNotNull(clock, "clock");

        final DateTime dateTimeTimeout = clock.getCurrentDateTime().plus(durationTimeout);
        return acquire(dateTimeTimeout);
    }

    @Override
    public Result<Void> acquire(DateTime dateTimeTimeout)
    {
        PreCondition.assertNotNull(dateTimeTimeout, "dateTimeTimeout");
        PreCondition.assertNotNull(clock, "clock");

        Result<Void> result;
        while (true)
        {
            if (clock.getCurrentDateTime().greaterThanOrEqualTo(dateTimeTimeout))
            {
                result = Result.error(new TimeoutException());
                break;
            }
            else if (tryAcquire().await())
            {
                result = Result.success();
                break;
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public Result<Boolean> tryAcquire()
    {
        final long threadId = Thread.currentThread().getId();
        final boolean acquired = acquiredByThreadId.get() == threadId || acquiredByThreadId.compareAndSet(-1, threadId);
        if (acquired)
        {
            acquiredCount.increment();
        }
        return Result.success(acquired);
    }

    @Override
    public Result<Void> release()
    {
        final long threadId = Thread.currentThread().getId();
        if (acquiredByThreadId.get() == threadId)
        {
            if (acquiredCount.decrement().get() == 0)
            {
                acquiredByThreadId.set(-1);
            }
        }
        return Result.success();
    }

    @Override
    public Result<Void> criticalSection(Duration durationTimeout, Action0 action)
    {
        PreCondition.assertNotNull(durationTimeout, "durationTimeout");
        PreCondition.assertGreaterThan(durationTimeout, Duration.zero, "durationTimeout");
        PreCondition.assertNotNull(action, "action");
        PreCondition.assertNotNull(clock, "clock");

        final DateTime dateTimeTimeout = clock.getCurrentDateTime().plus(durationTimeout);
        return criticalSection(dateTimeTimeout, action);
    }

    @Override
    public <T> Result<T> criticalSection(Duration durationTimeout, Function0<T> function)
    {
        PreCondition.assertGreaterThan(durationTimeout, Duration.zero, "durationTimeout");
        PreCondition.assertNotNull(function, "function");
        PreCondition.assertNotNull(clock, "clock");

        final DateTime dateTimeTimeout = clock.getCurrentDateTime().plus(durationTimeout);
        return criticalSection(dateTimeTimeout, function);
    }

    @Override
    public <T> Result<T> criticalSectionResult(Duration durationTimeout, Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(durationTimeout, "durationTimeout");
        PreCondition.assertGreaterThan(durationTimeout, Duration.zero, "durationTimeout");
        PreCondition.assertNotNull(function, "function");
        PreCondition.assertNotNull(clock, "clock");

        final DateTime dateTimeTimeout = clock.getCurrentDateTime().plus(durationTimeout);
        return criticalSectionResult(dateTimeTimeout, function);
    }

    @Override
    public SpinMutexCondition createCondition()
    {
        return createCondition(() -> true);
    }

    @Override
    public SpinMutexCondition createCondition(Function0<Boolean> condition)
    {
        return new SpinMutexCondition(this, clock, condition);
    }
}
