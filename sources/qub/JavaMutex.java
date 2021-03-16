package qub;

public class JavaMutex implements Mutex
{
    private final java.util.concurrent.locks.ReentrantLock lock;
    private final Clock clock;

    private JavaMutex(Clock clock)
    {
        this.lock = new java.util.concurrent.locks.ReentrantLock();
        this.clock = clock;
    }

    public static JavaMutex create()
    {
        return new JavaMutex(null);
    }

    public static JavaMutex create(Clock clock)
    {
        return new JavaMutex(clock);
    }

    @Override
    public boolean isAcquired()
    {
        return this.lock.isLocked();
    }

    @Override
    public boolean isAcquiredByCurrentThread()
    {
        return this.lock.isHeldByCurrentThread();
    }

    @Override
    public Result<Void> acquire()
    {
        this.lock.lock();
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
        return Result.create(() -> this.lock.tryLock());
    }

    @Override
    public Result<Void> release()
    {
        return Result.create(() ->
        {
            try
            {
                this.lock.unlock();
            }
            catch (java.lang.IllegalMonitorStateException e)
            {
            }
        });
    }

    @Override
    public JavaMutexCondition createCondition()
    {
        return JavaMutexCondition.create(this, this.clock, this.lock.newCondition());
    }

    @Override
    public JavaMutexCondition createCondition(Function0<Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        return JavaMutexCondition.create(this, this.clock, this.lock.newCondition(), condition);
    }
}
