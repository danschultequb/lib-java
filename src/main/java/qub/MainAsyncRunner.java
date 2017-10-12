package qub;

public class MainAsyncRunner implements AsyncRunner
{
    private final LockedSingleLinkListQueue<MainAsyncTask> scheduledTasks;

    public MainAsyncRunner()
    {
        scheduledTasks = new LockedSingleLinkListQueue<>();
    }

    /**
     * Get the number of actions that are currently scheduled (ready to run, but not yet run).
     * @return The number of actions that are current scheduled (ready to run, but not yet run).
     */
    int getScheduledCount()
    {
        return scheduledTasks.getCount();
    }

    void schedule(MainAsyncTask task)
    {
        if (task != null)
        {
            scheduledTasks.enqueue(task);
        }
    }

    @Override
    public AsyncAction schedule(Action0 action)
    {
        AsyncAction result = null;
        if (action != null)
        {
            final MainAsyncAction asyncAction = new MainAsyncAction(this, action);
            schedule(asyncAction);
            result = asyncAction;
        }
        return result;
    }

    @Override
    public <T> AsyncFunction<T> schedule(final Function0<T> function)
    {
        AsyncFunction<T> result = null;
        if (function != null)
        {
            final MainAsyncFunction<T> asyncFunction = new MainAsyncFunction<>(this, function);
            schedule(asyncFunction);
            result = asyncFunction;
        }
        return result;
    }

    @Override
    public void await()
    {
        while (scheduledTasks.any())
        {
            final MainAsyncTask action = scheduledTasks.dequeue();
            action.runTaskAndSchedulePausedTasks();
        }
    }
}
