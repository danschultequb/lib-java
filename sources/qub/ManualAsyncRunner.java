package qub;

public class ManualAsyncRunner extends DisposableBase implements AsyncRunner
{
    private final LockedQueue<PausedAsyncTask> scheduledTasks;
    private boolean disposed;

    public ManualAsyncRunner()
    {
        this.scheduledTasks = new LockedQueue<>(new SingleLinkListQueue<PausedAsyncTask>());
    }

    @Override
    public int getScheduledTaskCount()
    {
        return scheduledTasks.getCount();
    }

    @Override
    public void schedule(PausedAsyncTask asyncTask)
    {
        if (!disposed)
        {
            scheduledTasks.enqueue(asyncTask);
        }
    }

    @Override
    public AsyncAction schedule(Action0 action)
    {
        AsyncAction result = null;
        if (action != null)
        {
            final BasicAsyncAction asyncAction = new BasicAsyncAction(new Value<AsyncRunner>(this), new Array<AsyncTask>(0), action);
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
            final BasicAsyncFunction<T> asyncFunction = new BasicAsyncFunction<>(new Value<AsyncRunner>(this), new Array<AsyncTask>(0), function);
            asyncFunction.schedule();
            result = asyncFunction;
        }
        return result;
    }

    @Override
    public AsyncAction scheduleAsyncAction(Function0<AsyncAction> function)
    {
        return schedule(new Action0()
            {
                @Override
                public void run()
                {
                }
            })
            .thenAsyncAction(function);
    }

    @Override
    public <T> AsyncFunction<T> scheduleAsyncFunction(Function0<AsyncFunction<T>> function)
    {
        return schedule(new Action0()
            {
                @Override
                public void run()
                {
                }
            })
            .thenAsyncFunction(function);
    }

    @Override
    public void await()
    {
        while (scheduledTasks.any())
        {
            final PausedAsyncTask action = scheduledTasks.dequeue();
            action.runAndSchedulePausedTasks();
        }
    }

    @Override
    public void await(AsyncTask asyncTask)
    {
        if (!asyncTask.isCompleted() && asyncTask.getAsyncRunner() == this)
        {
            if (AsyncRunnerRegistry.getCurrentThreadAsyncRunner() == this)
            {
                // If the thread that is running this is the same thread that this ManualAsyncRunner
                // is using, then let's help it along by executing the scheduled tasks until the
                // provided task is completed.
                while (!asyncTask.isCompleted() && scheduledTasks.any())
                {
                    final PausedAsyncTask action = scheduledTasks.dequeue();
                    action.runAndSchedulePausedTasks();
                }
            }
            else
            {
                // If the thread that is running this is not the same thread that this
                // ManualAsyncRunner is using, then let's just wait for the task to be completed by
                // the thread that this ManualAsyncRunner is using.
                while (!asyncTask.isCompleted())
                {
                }
            }
        }
    }

    /**
     * Run the provided action immediately using a new ManualAsyncRunner that has been
     * registered with the AsyncRunnerRegistry for the current thread. When the provided action
     * completes, the provided ManualAsyncRunner will be removed from the
     * AsyncRunnerRegistry.
     * @param action The action to run immediately with the created and registered
     *               ManualAsyncRunner.
     */
    public static void withRegistered(Action1<ManualAsyncRunner> action)
    {
        final AsyncRunner runnerBackup = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();


        try (final ManualAsyncRunner runner = new ManualAsyncRunner())
        {
            AsyncRunnerRegistry.setCurrentThreadAsyncRunner(runner);
            action.run(runner);
        }
        finally
        {
            if (runnerBackup == null)
            {
                AsyncRunnerRegistry.removeCurrentThreadAsyncRunner();
            }
            else
            {
                AsyncRunnerRegistry.setCurrentThreadAsyncRunner(runnerBackup);
            }
        }
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        final Result<Boolean> result = Result.success(!disposed);
        disposed = true;
        return result;
    }
}
