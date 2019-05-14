package qub;

/**
 * An AsyncRunner implementation that runs its tasks concurrently on the current thread (usually the
 * main thread).
 */
public class ManualAsyncRunner implements AsyncScheduler
{
    /**
     * The AsyncTasks that have been scheduled to run on this AsyncRunner.
     */
    private final Locked<List<AsyncTask<?>>> scheduledTasks;

    public ManualAsyncRunner()
    {
        scheduledTasks = new Locked<>(List.create());
    }

    /**
     * Get the AsyncTasks that have been scheduled to run on this AsyncRunner.
     * @return The AsyncTasks that have been scheduled to run on this AsyncRunner.
     */
    public Iterable<AsyncTask<?>> getScheduledTasks()
    {
        return scheduledTasks.get((List<AsyncTask<?>> tasks) -> tasks);
    }

    /**
     * Get the number of AsyncTasks that have been scheduled to run on this AsyncRunner.
     * @return The number of AsyncTasks that have been scheduled to run on this AsyncRunner.
     */
    public int getScheduledTaskCount()
    {
        return scheduledTasks.get((List<AsyncTask<?>> tasks) -> tasks.getCount());
    }

    @Override
    public AsyncTask<Void> schedule(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return schedule(create(action));
    }

    @Override
    public <T> AsyncTask<T> schedule(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return schedule(create(function));
    }

    @Override
    public <T> AsyncTask<T> scheduleResult(Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return schedule(createResult(function));
    }

    @Override
    public AsyncTask<Void> create(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return new AsyncTask<>(this, action);
    }

    @Override
    public <T> AsyncTask<T> create(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return new AsyncTask<>(this, function);
    }

    @Override
    public <T> AsyncTask<T> createResult(Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return new AsyncTask<>(this, () -> function.run().await());
    }

    @Override
    public <T> AsyncTask<T> schedule(AsyncTask<T> task)
    {
        PreCondition.assertNotNull(task, "task");
        PreCondition.assertFalse(task.isCompleted(), "task.isCompleted()");

        scheduledTasks.get((List<AsyncTask<?>> tasks) ->
        {
            tasks.add(task);
        });

        return task;
    }

    @Override
    public void await(Result<?> result)
    {
        PreCondition.assertNotNull(result, "result");

        while (!result.isCompleted())
        {
            final AsyncTask<?> asyncTask = scheduledTasks.get((List<AsyncTask<?>> tasks) -> tasks.any() ? tasks.removeFirst() : null);
            if (asyncTask != null)
            {
                asyncTask.run();
            }
        }
    }
}
