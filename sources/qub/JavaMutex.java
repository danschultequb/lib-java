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
    public Clock getClock()
    {
        return clock;
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
    public MutexCondition createCondition()
    {
        return new JavaMutexCondition(this, lock.newCondition());
    }
}
