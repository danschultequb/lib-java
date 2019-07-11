package qub;

public class SpinMutexCondition implements MutexCondition
{
    private final SpinMutex mutex;
    private final Clock clock;
    private final SpinGate gate;
    private final Function0<Boolean> condition;

    public SpinMutexCondition(SpinMutex mutex, Clock clock)
    {
        this(mutex, clock, () -> true);
    }

    public SpinMutexCondition(SpinMutex mutex, Clock clock, Function0<Boolean> condition)
    {
        PreCondition.assertNotNull(mutex, "mutex");
        PreCondition.assertNotNull(condition, "condition");

        this.mutex = mutex;
        this.clock = clock;
        this.gate = new SpinGate(clock, false);
        this.condition = condition;
    }

    @Override
    public Result<Void> watch()
    {
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        return SyncResult.create(() ->
        {
            do
            {
                gate.close();

                mutex.release().await();

                gate.passThrough().await();

                mutex.acquire().await();
            }
            while(!condition.run());
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

        return SyncResult.create(() ->
        {
            do
            {
                gate.close();

                mutex.release().await();

                gate.passThrough(timeout).await();

                mutex.acquire(timeout).await();
            }
            while(!condition.run());
        });
    }

    @Override
    public void signalAll()
    {
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        gate.open();
    }
}
