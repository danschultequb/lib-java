package qub;

public class SpinMutexCondition implements MutexCondition
{
    private final SpinMutex mutex;
    private final Clock clock;
    private final SpinGate condition;

    public SpinMutexCondition(SpinMutex mutex, Clock clock)
    {
        PreCondition.assertNotNull(mutex, "mutex");

        this.mutex = mutex;
        this.clock = clock;
        this.condition = new SpinGate(clock, false);
    }

    @Override
    public Result<Void> await()
    {
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        condition.close();

        mutex.release().await();

        condition.passThrough();

        mutex.acquire().await();

        return Result.success();
    }

    @Override
    public Result<Void> await(Duration timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");
        PreCondition.assertNotNull(clock, "clock");

        final DateTime dateTimeTimeout = clock.getCurrentDateTime().plus(timeout);
        return await(dateTimeTimeout);
    }

    @Override
    public Result<Void> await(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");
        PreCondition.assertNotNull(clock, "clock");

        return Result.create(() ->
        {
            condition.close();

            mutex.release().await();

            if (condition.passThrough(timeout).await())
            {
                mutex.acquire().await();
            }
        });
    }

    @Override
    public void signalAll()
    {
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        condition.open();
    }
}
