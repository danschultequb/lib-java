package qub;

public class JavaMutexCondition implements MutexCondition
{
    private final JavaMutex mutex;
    private final java.util.concurrent.locks.Condition condition;

    public JavaMutexCondition(JavaMutex mutex, java.util.concurrent.locks.Condition condition)
    {
        this.mutex = mutex;
        this.condition = condition;
    }

    @Override
    public Clock getClock()
    {
        return mutex.getClock();
    }

    @Override
    public Result<Void> await()
    {
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        Result<Void> result;
        try
        {
            condition.await();
            result = Result.success();
        }
        catch (InterruptedException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public Result<Void> await(Duration timeout)
    {
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");
        PreCondition.assertNotNull(getClock(), "getClock()");

        return await(getClock().getCurrentDateTime().plus(timeout));
    }

    @Override
    public Result<Void> await(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");
        PreCondition.assertNotNull(getClock(), "getClock()");

        final Clock clock = getClock();

        Result<Void> result = null;
        while (result == null)
        {
            if (clock.getCurrentDateTime().greaterThanOrEqualTo(timeout))
            {
                result = Result.error(new TimeoutException());
            }
            else
            {
                try
                {
                    if (condition.await(1, java.util.concurrent.TimeUnit.MILLISECONDS))
                    {
                        result = Result.success();
                    }
                }
                catch (InterruptedException e)
                {
                    result = Result.error(e);
                }
            }
        }
        return result;
    }

    @Override
    public void signalAll()
    {
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        condition.signalAll();
    }
}
