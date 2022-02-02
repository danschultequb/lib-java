package qub;

/**
 * A mutex/lock that can be used to synchronize access to shared resources between multiple threads.
 * This lock uses an atomic variable to synchronize access to shared resources, and as such doesn't
 * require OS interaction.
 */
public class SpinMutex implements Mutex
{
    private final LongValue acquiredByThreadId;
    private final Clock clock;

    private SpinMutex(Clock clock)
    {
        this.acquiredByThreadId = LongValue.create(-1);
        this.clock = clock;
    }

    public static SpinMutex create()
    {
        return new SpinMutex(null);
    }

    public static SpinMutex create(Clock clock)
    {
        PreCondition.assertNotNull(clock, "clock");

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
        return Result.create(() ->
        {
            final long threadId = Thread.currentThread().getId();
            while (!this.acquiredByThreadId.compareAndSet(-1, threadId))
            {
                while (this.isAcquired())
                {
                }
            }
        });
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
        return Result.create(() ->
        {
            final long threadId = Thread.currentThread().getId();
            return this.acquiredByThreadId.compareAndSet(-1, threadId);
        });
    }

    @Override
    public Result<Void> release()
    {
        PreCondition.assertTrue(this.isAcquiredByCurrentThread(), "this.isAcquiredByCurrentThread()");

        return Result.create(() ->
        {
            this.acquiredByThreadId.set(-1);
        });
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
