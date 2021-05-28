package qub;

/**
 * An AsyncRunner implementation that runs its tasks on a thread pool.
 */
public class ParallelAsyncRunner implements AsyncScheduler
{
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

        new Thread(() ->
        {
            CurrentThread.setAsyncRunner(this);
            task.run();
        }).start();
        return task;
    }

    @Override
    public void await(Result<?> result)
    {
        PreCondition.assertNotNull(result, "result");

        while (!result.isCompleted())
        {
            CurrentThread.yield();
        }
    }
}
