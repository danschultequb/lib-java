package qub;

/**
 * An AsyncRunner implementation that runs its tasks on a thread pool.
 */
public class ResultParallelAsyncRunner implements ResultAsyncScheduler
{
    @Override
    public ResultAsyncTask<Void> schedule(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return schedule(new ResultAsyncTask<>(this, action));
    }

    @Override
    public <T> ResultAsyncTask<T> schedule(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return schedule(new ResultAsyncTask<>(this, function));
    }

    @Override
    public <T> ResultAsyncTask<T> scheduleResult(Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return schedule(new ResultAsyncTask<>(this, () -> function.run().await()));
    }

    @Override
    public <T> ResultAsyncTask<T> schedule(ResultAsyncTask<T> task)
    {
        PreCondition.assertNotNull(task, "task");
        PreCondition.assertFalse(task.isCompleted(), "task.isCompleted()");

        new Thread(task::run).start();
        return task;
    }

    @Override
    public void await(Result<?> result)
    {
        PreCondition.assertNotNull(result, "result");

        while (!result.isCompleted())
        {
        }
    }
}
