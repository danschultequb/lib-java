package qub;

public abstract class BasicAsyncTask implements AsyncAction, AsyncTask, PausedAsyncTask
{
    private final AsyncRunnerInner runner;
    private final List<PausedAsyncTask> pausedTasks;

    BasicAsyncTask(AsyncRunnerInner runner)
    {
        this.runner = runner;
        this.pausedTasks = new SingleLinkList<>();
    }

    @Override
    public void schedule()
    {
        runner.schedule(this);
    }

    @Override
    public AsyncAction then(Action0 action)
    {
        AsyncAction result = null;
        if (action != null)
        {
            final PausedAsyncAction asyncAction = runner.create(action);
            pausedTasks.add(asyncAction);
            result = asyncAction;
        }
        return result;
    }

    @Override
    public <T> AsyncFunction<T> then(Function0<T> function)
    {
        AsyncFunction<T> result = null;
        if (function != null)
        {
            final PausedAsyncFunction<T> asyncFunction = runner.create(function);
            pausedTasks.add(asyncFunction);
            result = asyncFunction;
        }
        return result;
    }

    @Override
    public void runAndSchedulePausedTasks()
    {
        runTask();

        for (final PausedAsyncTask pausedTask : pausedTasks)
        {
            pausedTask.schedule();
        }
    }

    protected abstract void runTask();
}
