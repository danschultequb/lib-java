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
    public Result<Void> watch()
    {
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        return Result.create(() ->
        {
            condition.close();

            mutex.release().await();

            condition.passThrough().await();

            mutex.acquire().await();
        });
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

        return Result.create(() ->
        {
            condition.close();

            mutex.release().await();

            condition.passThrough(timeout).await();

            mutex.acquire(timeout).await();
        });
    }

    @Override
    public void signalAll()
    {
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        condition.open();
    }
}
