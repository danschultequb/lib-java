package qub;

public abstract class BasicAsyncTask implements AsyncAction, AsyncTask, PausedAsyncTask
{
    private final AsyncRunnerInner runner;
    private final List<PausedAsyncTask> pausedTasks;
    private boolean completed;

    BasicAsyncTask(AsyncRunnerInner runner)
    {
        this.runner = runner;
        this.pausedTasks = new SingleLinkList<>();
        this.completed = false;
    }

    /**
     * Get the number of PausedAsyncTasks that are waiting for this AsyncTask to complete.
     * @return The number of PausedAsyncTasks that are waiting for this AsyncTask to complete.
     */
    public int getPausedTaskCount()
    {
        return pausedTasks.getCount();
    }

    /**
     * Get whether or not this BasicAsyncTask has been run.
     * @return Whether or not this BasicAsyncTask has been run.
     */
    public boolean isCompleted()
    {
        return completed;
    }

    @Override
    public void schedule()
    {
        runner.schedule(this);
    }

    @Override
    public AsyncAction then(Action0 action)
    {
        return thenOn(runner, action);
    }

    @Override
    public <T> AsyncFunction<T> then(Function0<T> function)
    {
        AsyncFunction<T> result = null;
        if (function != null)
        {
            final PausedAsyncFunction<T> asyncFunction = runner.create(function);
            if (completed)
            {
                asyncFunction.schedule();
            }
            else
            {
                pausedTasks.add(asyncFunction);
            }
            result = asyncFunction;
        }
        return result;
    }

    @Override
    public AsyncAction thenOn(AsyncRunner runner, Action0 action)
    {
        AsyncAction result = null;
        if (runner != null && action != null)
        {
            final PausedAsyncAction asyncAction = runner.create(action);
            if (completed)
            {
                asyncAction.schedule();
            }
            else
            {
                pausedTasks.add(asyncAction);
            }
            result = asyncAction;
        }
        return result;
    }

    @Override
    public void runAndSchedulePausedTasks()
    {
        runTask();

        completed = true;

        for (final PausedAsyncTask pausedTask : pausedTasks)
        {
            pausedTask.schedule();
        }
    }

    protected abstract void runTask();
}
