package qub;

public class JavaMutex implements Mutex
{
    private final java.util.concurrent.locks.ReentrantLock lock;

    public JavaMutex()
    {
        this.lock = new java.util.concurrent.locks.ReentrantLock();
    }

    @Override
    public boolean isAcquired()
    {
        return lock.isLocked();
    }

    @Override
    public void acquire()
    {
        lock.lock();
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
        return new JavaMutexCondition(lock.newCondition());
    }
}
