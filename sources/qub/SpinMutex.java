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

    private SpinMutex(Clock clock)
    {
        this.acquiredByThreadId = LongValue.create(-1);
        this.acquiredCount = LongValue.create(0);
        this.clock = clock;
    }

    public static SpinMutex create()
    {
        return new SpinMutex(null);
    }

    public static SpinMutex create(Clock clock)
    {
        return new SpinMutex(clock);
    }

    @Override
    public boolean isAcquired()
    {
        return this.acquiredByThreadId.get() != -1;
    }

    @Override
    public boolean isAcquiredByCurrentThread()
    {
        return this.acquiredByThreadId.get() == Thread.currentThread().getId();
    }

    @Override
    public Result<Void> acquire()
    {
        final long threadId = Thread.currentThread().getId();
        if (this.acquiredByThreadId.get() != threadId)
        {
            while (!this.acquiredByThreadId.compareAndSet(-1, threadId))
            {
                while (this.isAcquired())
                {
                }
            }
        }
        this.acquiredCount.increment();

        return Result.success();
    }

    @Override
    public Result<Void> acquire(Duration durationTimeout)
    {
        PreCondition.assertNotNull(durationTimeout, "durationTimeout");
        PreCondition.assertGreaterThan(durationTimeout, Duration.zero, "durationTimeout");
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
        final boolean acquired = this.acquiredByThreadId.get() == threadId || this.acquiredByThreadId.compareAndSet(-1, threadId);
        if (acquired)
        {
            this.acquiredCount.increment();
        }
        return Result.success(acquired);
    }

    @Override
    public Result<Void> release()
    {
        final long threadId = Thread.currentThread().getId();
        if (this.acquiredByThreadId.get() == threadId)
        {
            if (this.acquiredCount.decrement().get() == 0)
            {
                this.acquiredByThreadId.set(-1);
            }
        }
        return Result.success();
    }

    @Override
    public SpinMutexCondition createCondition()
    {
        return SpinMutexCondition.create(this, this.clock);
    }

    @Override
    public SpinMutexCondition createCondition(Function0<Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return SpinMutexCondition.create(this, this.clock, condition);
    }
}
