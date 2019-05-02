package qub;

/**
 * An AsyncRunner implementation that runs its tasks concurrently on the current thread (usually the
 * main thread).
 */
public class ResultManualAsyncRunner implements ResultAsyncScheduler
{
    /**
     * The AsyncTasks that have been scheduled to run on this AsyncRunner.
     */
    private final List<ResultAsyncTask<?>> scheduledTasks;

    public ResultManualAsyncRunner()
    {
        scheduledTasks = List.create();
    }

    /**
     * Get the AsyncTasks that have been scheduled to run on this AsyncRunner.
     * @return The AsyncTasks that have been scheduled to run on this AsyncRunner.
     */
    public Iterable<ResultAsyncTask<?>> getScheduledTasks()
    {
        return scheduledTasks;
    }

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

        scheduledTasks.add(task);

        return task;
    }

    @Override
    public void await(Result<?> result)
    {
        PreCondition.assertNotNull(result, "result");

        while (!result.isCompleted())
        {
            if (scheduledTasks.any())
            {
                scheduledTasks.removeFirst().run();
            }
        }
    }
}
