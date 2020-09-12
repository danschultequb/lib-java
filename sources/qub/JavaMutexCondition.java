package qub;

public class JavaMutexCondition implements MutexCondition
{
    private final JavaMutex mutex;
    private final Clock clock;
    private final java.util.concurrent.locks.Condition conditionVariable;
    private final Function0<Boolean> conditionFunction;

    public JavaMutexCondition(JavaMutex mutex, Clock clock, java.util.concurrent.locks.Condition conditionVariable, Function0<Boolean> conditionFunction)
    {
        PreCondition.assertNotNull(mutex, "mutex");
        PreCondition.assertNotNull(conditionVariable, "conditionVariable");

        this.mutex = mutex;
        this.clock = clock;
        this.conditionVariable = conditionVariable;
        this.conditionFunction = conditionFunction;
    }

    @Override
    public Result<Void> watch()
    {
        PreCondition.assertTrue(mutex.isAcquiredByCurrentThread(), "mutex.isAcquiredByCurrentThread()");

        Result<Void> result = null;
        while (result == null)
        {
            try
            {
                if (conditionFunction != null && conditionFunction.run())
                {
                    result = Result.success();
                }
                else
                {
                    conditionVariable.await();
                    if (conditionFunction == null)
                    {
                        result = Result.success();
                    }
                }
            }
            catch (InterruptedException e)
            {
                result = Result.error(e);
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public Result<Void> watch(Duration2 timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertGreaterThan(timeout, Duration2.zero, "timeout");
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
            else if (conditionFunction != null && conditionFunction.run())
            {
                result = Result.success();
            }
            else
            {
                try
                {
                    if (conditionVariable.await(1, java.util.concurrent.TimeUnit.MILLISECONDS) && conditionFunction == null)
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

        conditionVariable.signalAll();
    }
}
