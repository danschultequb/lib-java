package qub;

/**
 * An AsyncRunner implementation that runs its tasks concurrently on the current thread (usually the
 * main thread).
 */
public class ResultManualAsyncRunner implements ResultAsyncRunner
{
    @Override
    public Result<Void> run(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(action);
    }

    @Override
    public <T> Result<T> run(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return null;
    }

    @Override
    public <T> Result<T> runResult(Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return null;
    }
}
