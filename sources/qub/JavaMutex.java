package qub;

public class JavaMutex implements Mutex
{
    private final java.util.concurrent.locks.ReentrantLock lock;
    private final Clock clock;

    public JavaMutex()
    {
        this(null);
    }

    public JavaMutex(Clock clock)
    {
        this.lock = new java.util.concurrent.locks.ReentrantLock();
        this.clock = clock;
    }

    @Override
    public boolean isAcquired()
    {
        return lock.isLocked();
    }

    @Override
    public boolean isAcquiredByCurrentThread()
    {
        return lock.isHeldByCurrentThread();
    }

    @Override
    public Result<Void> acquire()
    {
        lock.lock();
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
                if (clock.getCurrentDateTime().greaterThanOrEqualTo(dateTimeTimeout))
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
        return Result.create(() -> lock.tryLock());
    }

    @Override
    public Result<Void> release()
    {
        return Result.create(lock::unlock);
    }

    @Override
    public JavaMutexCondition createCondition()
    {
        return new JavaMutexCondition(this, clock, lock.newCondition(), null);
    }

    @Override
    public JavaMutexCondition createCondition(Function0<Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return new JavaMutexCondition(this, clock, lock.newCondition(), condition);
    }
}
