package qub;

public abstract class MainAsyncTask implements AsyncAction
{
    private final MainAsyncRunner runner;
    private final List<MainAsyncTask> pausedTasks;

    protected MainAsyncTask(MainAsyncRunner runner)
    {
        this.runner = runner;
        this.pausedTasks = new SingleLinkList<>();
    }

    @Override
    public AsyncAction then(Action0 action)
    {
        AsyncAction result = null;
        if (action != null)
        {
            final MainAsyncAction asyncAction = new MainAsyncAction(runner, action);
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
            final MainAsyncFunction<T> asyncFunction = new MainAsyncFunction<>(runner, function);
            pausedTasks.add(asyncFunction);
            result = asyncFunction;
        }
        return result;
    }

    public void runTaskAndSchedulePausedTasks()
    {
        runTask();

        for (final MainAsyncTask pausedTask : pausedTasks)
        {
            runner.schedule(pausedTask);
        }
    }

    protected abstract void runTask();
}
