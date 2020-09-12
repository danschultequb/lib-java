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
                while (this.isAcquired())
                {
                }
            }
        }
        acquiredCount.increment();

        return Result.success();
    }

    @Override
    public Result<Void> acquire(Duration2 durationTimeout)
    {
        PreCondition.assertNotNull(durationTimeout, "durationTimeout");
        PreCondition.assertGreaterThan(durationTimeout, Duration2.zero, "durationTimeout");
        PreCondition.assertNotNull(this.clock, "this.clock");

        return this.acquire(this.clock.getCurrentDateTime().plus(durationTimeout));
    }

    @Override
    public Result<Void> acquire(DateTime dateTimeTimeout)
    {
        PreCondition.assertNotNull(dateTimeTimeout, "dateTimeTimeout");
        PreCondition.assertNotNull(this.clock, "this.clock");

        return Result.create(() ->
        {
            while (true)
            {
                if (this.clock.getCurrentDateTime().greaterThanOrEqualTo(dateTimeTimeout))
                {
                    throw new TimeoutException();
                }
                else if (this.tryAcquire().await())
                {
                    break;
                }
            }
        });
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
    public SpinMutexCondition createCondition()
    {
        return new SpinMutexCondition(this, clock, null);
    }

    @Override
    public SpinMutexCondition createCondition(Function0<Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return new SpinMutexCondition(this, clock, condition);
    }
}
