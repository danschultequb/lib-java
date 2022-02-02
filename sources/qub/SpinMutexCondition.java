package qub;

public class SpinMutexCondition implements MutexCondition
{
    private final SpinMutex mutex;
    private final Clock clock;
    private final SpinGate gate;
    private final Function0<Boolean> condition;

    private SpinMutexCondition(SpinMutex mutex, Clock clock, Function0<Boolean> condition)
    {
        PreCondition.assertNotNull(mutex, "mutex");

        this.mutex = mutex;
        this.clock = clock;
        this.gate = SpinGate.create(clock);
        this.condition = condition;
    }

    public static SpinMutexCondition create(SpinMutex mutex, Clock clock)
    {
        PreCondition.assertNotNull(mutex, "mutex");

        return new SpinMutexCondition(mutex, clock, null);
    }

    public static SpinMutexCondition create(SpinMutex mutex, Clock clock, Function0<Boolean> condition)
    {
        PreCondition.assertNotNull(mutex, "mutex");
        PreCondition.assertNotNull(condition, "condition");

        return new SpinMutexCondition(mutex, clock, condition);
    }

    @Override
    public Result<Void> watch()
    {
        PreCondition.assertTrue(this.mutex.isAcquiredByCurrentThread(), "this.mutex.isAcquiredByCurrentThread()");

        return Result.create(() ->
        {
            boolean done = (this.condition != null && this.condition.run());
            while (!done)
            {
                this.gate.close();

                this.mutex.release().await();
                try
                {
                    this.gate.passThrough().await();
                }
                finally
                {
                    this.mutex.acquire().await();
                }

                done = (this.condition == null || this.condition.run());
            }
        });
    }

    @Override
    public Result<Void> watch(Duration timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertTrue(this.mutex.isAcquiredByCurrentThread(), "this.mutex.isAcquiredByCurrentThread()");
        PreCondition.assertNotNull(this.clock, "this.clock");

        final DateTime dateTimeTimeout = this.clock.getCurrentDateTime().plus(timeout);
        return this.watch(dateTimeTimeout);
    }

    @Override
    public Result<Void> watch(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertTrue(this.mutex.isAcquiredByCurrentThread(), "this.mutex.isAcquiredByCurrentThread()");
        PreCondition.assertNotNull(this.clock, "this.clock");

        return Result.create(() ->
        {
            boolean done = (this.condition != null && this.condition.run());
            while (!done)
            {
                this.gate.close();

                this.mutex.release().await();
                try
                {
                    this.gate.passThrough(timeout).await();
                }
                finally
                {
                    this.mutex.acquire().await();
                }

                done = (this.condition == null || this.condition.run());
            }
        });
    }

    @Override
    public void signalAll()
    {
        PreCondition.assertTrue(this.mutex.isAcquiredByCurrentThread(), "this.mutex.isAcquiredByCurrentThread()");

        this.gate.open();
    }
}
