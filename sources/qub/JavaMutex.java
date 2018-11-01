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
    public Result<Boolean> acquire()
    {
        lock.lock();
        return Result.successTrue();
    }

    @Override
    public boolean tryAcquire()
    {
        return lock.tryLock();
    }

    @Override
    public boolean release()
    {
        boolean result;
        try
        {
            lock.unlock();
            result = true;
        }
        catch (IllegalMonitorStateException e)
        {
            result = false;
        }
        return result;
    }

    @Override
    public MutexCondition createCondition()
    {
        return new JavaMutexCondition(this, lock.newCondition());
    }
}
