package qub;

import java.util.concurrent.atomic.AtomicBoolean;

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
        condition.close();

        mutex.release();

        condition.passThrough();

        mutex.acquire();

        return Result.successTrue();
    }

    @Override
    public void signalAll()
    {
        condition.open();
    }
}
