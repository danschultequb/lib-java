package qub;

public class JavaMutexCondition implements MutexCondition
{
    private final JavaMutex mutex;
    private final Clock clock;
    private final java.util.concurrent.locks.Condition conditionVariable;
    private final Function0<Boolean> conditionFunction;

    private JavaMutexCondition(JavaMutex mutex, Clock clock, java.util.concurrent.locks.Condition conditionVariable, Function0<Boolean> conditionFunction)
    {
        PreCondition.assertNotNull(mutex, "mutex");
        PreCondition.assertNotNull(conditionVariable, "conditionVariable");

        this.mutex = mutex;
        this.clock = clock;
        this.conditionVariable = conditionVariable;
        this.conditionFunction = conditionFunction;
    }

    public static JavaMutexCondition create(JavaMutex mutex, Clock clock, java.util.concurrent.locks.Condition conditionVariable)
    {
        PreCondition.assertNotNull(mutex, "mutex");
        PreCondition.assertNotNull(conditionVariable, "conditionVariable");

        return new JavaMutexCondition(mutex, clock, conditionVariable, null);
    }

    public static JavaMutexCondition create(JavaMutex mutex, Clock clock, java.util.concurrent.locks.Condition conditionVariable, Function0<Boolean> conditionFunction)
    {
        PreCondition.assertNotNull(mutex, "mutex");
        PreCondition.assertNotNull(conditionVariable, "conditionVariable");
        PreCondition.assertNotNull(conditionFunction, "conditionFunction");

        return new JavaMutexCondition(mutex, clock, conditionVariable, conditionFunction);
    }

    @Override
    public Result<Void> watch()
    {
        PreCondition.assertTrue(this.mutex.isAcquiredByCurrentThread(), "this.mutex.isAcquiredByCurrentThread()");

        Result<Void> result = null;
        while (result == null)
        {
            try
            {
                if (this.conditionFunction != null && this.conditionFunction.run())
                {
                    result = Result.create();
                }
                else
                {
                    this.conditionVariable.await();
                    if (conditionFunction == null)
                    {
                        result = Result.create();
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

        Result<Void> result = null;
        while (result == null)
        {
            if (this.clock.getCurrentDateTime().greaterThanOrEqualTo(timeout))
            {
                result = Result.error(new TimeoutException());
            }
            else if (this.conditionFunction != null && this.conditionFunction.run())
            {
                result = Result.create();
            }
            else
            {
                try
                {
                    if (this.conditionVariable.await(1, java.util.concurrent.TimeUnit.MILLISECONDS) && this.conditionFunction == null)
                    {
                        result = Result.create();
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
        PreCondition.assertTrue(this.mutex.isAcquiredByCurrentThread(), "this.mutex.isAcquiredByCurrentThread()");

        this.conditionVariable.signalAll();
    }
}
