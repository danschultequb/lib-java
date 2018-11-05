package qub;

public class SpinMutexCondition implements MutexCondition
{
    private final SpinMutex mutex;
    private final SpinGate condition;

    public SpinMutexCondition(SpinMutex mutex)
    {
        PreCondition.assertNotNull(mutex, "mutex");

        this.mutex = mutex;
        this.condition = new SpinGate(mutex.getClock(), false);
    }

    @Override
    public Clock getClock()
    {
        return mutex.getClock();
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
        PreCondition.assertNotNull(getClock(), "getClock()");

        return await(mutex.getClock().getCurrentDateTime().plus(timeout));
    }

    @Override
    public Result<Boolean> await(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");
        PreCondition.assertNotNull(getClock(), "getClock()");

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
