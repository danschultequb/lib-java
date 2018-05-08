package qub;

import java.util.concurrent.locks.Condition;

public class JavaMutexCondition implements MutexCondition
{
    private final Condition condition;

    public JavaMutexCondition(Condition condition)
    {
        this.condition = condition;
    }

    @Override
    public Result<Boolean> await()
    {
        Result<Boolean> result;
        try
        {
            condition.await();
            result = Result.successTrue();
        }
        catch (InterruptedException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public void signalAll()
    {
        condition.signalAll();
    }
}
