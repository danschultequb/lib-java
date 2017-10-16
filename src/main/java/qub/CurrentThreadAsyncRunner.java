package qub;

public class CurrentThreadAsyncRunner implements AsyncRunnerInner
{
    private final LockedSingleLinkListQueue<AsyncTask> scheduledTasks;

    public CurrentThreadAsyncRunner()
    {
        scheduledTasks = new LockedSingleLinkListQueue<>();
    }

    @Override
    public int getScheduledTaskCount()
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

    @Override
    public void await()
    {
        while (scheduledTasks.any())
        {
            final AsyncTask action = scheduledTasks.dequeue();
            action.runAndSchedulePausedTasks();
        }
    }

    /**
     * Run the provided action immediately using a new CurrentThreadAsyncRunner that has been
     * registered with the AsyncRunnerRegistry for the current thread. When the provided action
     * completes, the provided CurrentThreadAsyncRunner will be removed from the
     * AsyncRunnerRegistry.
     * @param action The action to run immediately with the created and registered
     *               CurrentThreadAsyncRunner.
     */
    public static void withRegistered(Action1<CurrentThreadAsyncRunner> action)
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        AsyncRunnerRegistry.setCurrentThreadAsyncRunner(runner);
        try
        {
            action.run(runner);
        }
        finally
        {
            AsyncRunnerRegistry.removeCurrentThreadAsyncRunner();
        }
    }
}
