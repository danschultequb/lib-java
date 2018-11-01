package qub;

public class SpinMutexCondition implements MutexCondition
{
    private final SpinMutex mutex;
    private final SpinGate condition;

    public SpinMutexCondition(SpinMutex mutex)
    {
        this.mutex = mutex;
        this.condition = new SpinGate(false);
    }

    @Override
    public Result<Boolean> await()
    {
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        condition.close();

        mutex.release();

        condition.passThrough();

        mutex.acquire();

        return Result.successTrue();
    }

    @Override
    public Result<Boolean> await(Duration timeout)
    {
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        return await(mutex.getClock().getCurrentDateTime().plus(timeout));
    }

    @Override
    public Result<Boolean> await(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        condition.close();

        mutex.release();

        final Result<Boolean> result = condition.passThrough(timeout);
        if (!result.hasError() && result.getValue())
        {
            mutex.acquire();
        }

        return result;
    }

    @Override
    public void signalAll()
    {
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        condition.open();
    }
}
