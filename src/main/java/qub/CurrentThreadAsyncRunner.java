package qub;

public class CurrentThreadAsyncRunner implements AsyncRunnerInner
{
    private final LockedSingleLinkListQueue<AsyncTask> scheduledTasks;

    public CurrentThreadAsyncRunner()
    {
        scheduledTasks = new LockedSingleLinkListQueue<>();
    }

    /**
     * Get the number of actions that are currently scheduled.
     * @return The number of actions that are current scheduled.
     */
    int getScheduledTaskCount()
    {
        return scheduledTasks.getCount();
    }

    @Override
    public void schedule(AsyncTask asyncTask)
    {
        scheduledTasks.enqueue(asyncTask);
    }

    @Override
    public PausedAsyncAction create(Action0 action)
    {
        return new BasicAsyncAction(this, action);
    }

    @Override
    public <T> PausedAsyncFunction<T> create(Function0<T> function)
    {
        return new BasicAsyncFunction<>(this, function);
    }

    @Override
    public AsyncAction schedule(Action0 action)
    {
        AsyncAction result = null;
        if (action != null)
        {
            final PausedAsyncAction asyncAction = create(action);
            asyncAction.schedule();
            result = asyncAction;
        }
        return result;
    }

    @Override
    public <T> AsyncFunction<T> schedule(Function0<T> function)
    {
        AsyncFunction<T> result = null;
        if (function != null)
        {
            final PausedAsyncFunction<T> asyncFunction = create(function);
            asyncFunction.schedule();
            result = asyncFunction;
        }
        return result;
    }

    /**
     * Block until all scheduled actions/functions are run to completion.
     */
    public void await()
    {
        while (scheduledTasks.any())
        {
            final AsyncTask action = scheduledTasks.dequeue();
            action.runAndSchedulePausedTasks();
        }
    }
}
