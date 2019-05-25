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
        return Result.success(lock.tryLock());
    }

    @Override
    public Result<Void> release()
    {
        Result<Void> result;
        try
        {
            lock.unlock();
            result = Result.success();
        }
        catch (IllegalMonitorStateException e)
        {
            result = Result.error(e);
        }
        return result;
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
    public MutexCondition createCondition()
    {
        return new JavaMutexCondition(this, clock, lock.newCondition());
    }
}
