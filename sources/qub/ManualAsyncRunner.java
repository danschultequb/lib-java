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

    private ManualAsyncRunner()
    {
        this.scheduledTasks = Locked.create(List.create());
    }

    public static ManualAsyncRunner create()
    {
        return new ManualAsyncRunner();
    }

    /**
     * Get the AsyncTasks that have been scheduled to run on this AsyncRunner.
     * @return The AsyncTasks that have been scheduled to run on this AsyncRunner.
     */
    public Iterable<AsyncTask<?>> getScheduledTasks()
    {
        return this.scheduledTasks.unlock((List<AsyncTask<?>> tasks) -> tasks);
    }

    /**
     * Get the number of AsyncTasks that have been scheduled to run on this AsyncRunner.
     * @return The number of AsyncTasks that have been scheduled to run on this AsyncRunner.
     */
    public int getScheduledTaskCount()
    {
        return this.scheduledTasks.unlock((List<AsyncTask<?>> tasks) -> tasks.getCount());
    }

    @Override
    public AsyncTask<Void> schedule(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.schedule(this.create(action));
    }

    @Override
    public <T> AsyncTask<T> schedule(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return this.schedule(this.create(function));
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
    public <T> AsyncTask<T> schedule(AsyncTask<T> task)
    {
        PreCondition.assertNotNull(task, "task");
        PreCondition.assertFalse(task.isCompleted(), "task.isCompleted()");

        this.scheduledTasks.unlock((List<AsyncTask<?>> tasks) ->
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
            final AsyncTask<?> asyncTask = this.scheduledTasks.unlock((List<AsyncTask<?>> tasks) -> tasks.any() ? tasks.removeFirst() : null);
            if (asyncTask != null)
            {
                asyncTask.run();
            }
            else
            {
                CurrentThread.yield();
            }
        }
    }
}
