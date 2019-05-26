package qub;

public class JavaMutexCondition implements MutexCondition
{
    private final JavaMutex mutex;
    private final Clock clock;
    private final java.util.concurrent.locks.Condition condition;

    public JavaMutexCondition(JavaMutex mutex, Clock clock, java.util.concurrent.locks.Condition condition)
    {
        PreCondition.assertNotNull(mutex, "mutex");
        PreCondition.assertNotNull(condition, "condition");

        this.mutex = mutex;
        this.clock = clock;
        this.condition = condition;
    }

    @Override
    public Result<Void> watch()
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
    public Result<Void> watch(Duration timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");
        PreCondition.assertNotNull(clock, "clock");

        final DateTime dateTimeTimeout = clock.getCurrentDateTime().plus(timeout);
        return watch(dateTimeTimeout);
    }

    @Override
    public Result<Void> watch(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");
        PreCondition.assertNotNull(clock, "clock");

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
