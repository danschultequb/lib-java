package qub;

public class ManualAsyncRunner extends AsyncRunnerBase
{
    private final BlockingQueue<PausedAsyncTask> scheduledTasks;
    private boolean disposed;

    public ManualAsyncRunner()
    {
        this.scheduledTasks = new JavaBlockingQueue<PausedAsyncTask>(null);
    }

    @Override
    public int getScheduledTaskCount()
    {
        return scheduledTasks.getCount();
    }

    @Override
    public void markCompleted(final Setable<Boolean> asyncTaskCompleted)
    {
        asyncTaskCompleted.set(true);
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
            final BasicAsyncAction asyncAction = new BasicAsyncAction(new Value<AsyncRunner>(this), action);
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
            final BasicAsyncFunction<T> asyncFunction = new BasicAsyncFunction<>(new Value<AsyncRunner>(this), function);
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
            final Result<PausedAsyncTask> asyncTaskResult = scheduledTasks.dequeue();
            final PausedAsyncTask asyncTask = asyncTaskResult.getValue();
            if (asyncTask != null)
            {
                asyncTask.runAndSchedulePausedTasks();
            }
        }
    }

    @Override
    public void await(AsyncTask asyncTask)
    {
        if (!asyncTask.isCompleted())
        {
            if (asyncTask.getAsyncRunner() == this && AsyncRunnerRegistry.getCurrentThreadAsyncRunner() == this)
            {
                // If the thread that is running this is the same thread that this ManualAsyncRunner
                // is using, then let's help it along by executing the scheduled tasks until the
                // provided task is completed.
                while (!asyncTask.isCompleted())
                {
                    final Result<PausedAsyncTask> asyncTaskToRunResult = scheduledTasks.dequeue();
                    final PausedAsyncTask asyncTaskToRun = asyncTaskToRunResult.getValue();
                    if (asyncTaskToRun != null)
                    {
                        asyncTaskToRun.runAndSchedulePausedTasks();
                    }
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
        catch (Exception ignored)
        {
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
        Result<Boolean> result;
        if (disposed)
        {
            result = Result.successFalse();
        }
        else
        {
            disposed = true;
            result = Result.successTrue();
        }
        return result;
    }
}
