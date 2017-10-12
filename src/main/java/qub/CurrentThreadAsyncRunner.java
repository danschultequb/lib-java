package qub;

public class CurrentThreadAsyncRunner implements AsyncRunnerInner
{
    private final LockedSingleLinkListQueue<AsyncTask> scheduledTasks;

    public CurrentThreadAsyncRunner()
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

    @Override
    public void schedule(AsyncTask asyncTask)
    {
        scheduledTasks.enqueue(asyncTask);
    }

    @Override
    public BasicAsyncAction create(Action0 action)
    {
        return new BasicAsyncAction(this, action);
    }

    @Override
    public <T> BasicAsyncFunction<T> create(Function0<T> function)
    {
        return new BasicAsyncFunction<>(this, function);
    }

    @Override
    public AsyncAction schedule(Action0 action)
    {
        AsyncAction result = null;
        if (action != null)
        {
            final BasicAsyncAction asyncAction = create(action);
            schedule(asyncAction);
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
            final BasicAsyncFunction<T> asyncFunction = create(function);
            schedule(asyncFunction);
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
