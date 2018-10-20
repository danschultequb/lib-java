package qub;

public class ManualAsyncRunner implements AsyncRunner
{
    private final BlockingQueue<PausedAsyncTask> scheduledTasks;
    private boolean disposed;

    public ManualAsyncRunner()
    {
        this.scheduledTasks = new JavaBlockingQueue<>(null);
    }

    @Override
    public int getScheduledTaskCount()
    {
        return scheduledTasks.getCount();
    }

    @Override
    public void markCompleted(Setable<Boolean> asyncTaskCompleted)
    {
        PreCondition.assertNotNull(asyncTaskCompleted, "asyncTaskCompleted");

        asyncTaskCompleted.set(true);
    }

    @Override
    public void schedule(PausedAsyncTask asyncTask)
    {
        PreCondition.assertNotNull(asyncTask, "asyncTask");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        scheduledTasks.enqueue(asyncTask);
    }

    @Override
    public AsyncAction schedule(String label, Action0 action)
    {
        PreCondition.assertNotNull(action, "action");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final BasicAsyncAction result = new BasicAsyncAction(new Value<AsyncRunner>(this), label, action);
        schedule(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <T> AsyncFunction<T> schedule(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final BasicAsyncFunction<T> result = new BasicAsyncFunction<>(new Value<AsyncRunner>(this), function);
        schedule(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    private void dequeueAndRunNextTask()
    {
        final Result<PausedAsyncTask> asyncTask = scheduledTasks.dequeue();
        asyncTask.throwError();
        asyncTask.getValue().runAndSchedulePausedTasks();
    }

    public void await()
    {
        while (scheduledTasks.any())
        {
            dequeueAndRunNextTask();
        }
    }

    @Override
    public void await(AsyncTask asyncTask)
    {
        PreCondition.assertNotNull(asyncTask, "asyncTask");
        PreCondition.assertFalse(asyncTask.isCompleted(), "asyncTask.isCompleted()");
        PreCondition.assertSame(this, asyncTask.getAsyncRunner(), "asyncTask.getAsyncRunner()");

        final AsyncRunner currentThreadAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        if (currentThreadAsyncRunner == this)
        {
            // If the thread that is running this is the same thread that this ManualAsyncRunner
            // is using, then let's help it along by executing the scheduled tasks until the
            // provided task is completed.
            while (!asyncTask.isCompleted())
            {
                dequeueAndRunNextTask();
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
        final Result<Boolean> result = (disposed ? Result.successFalse() : Result.successTrue());
        disposed = true;

        PostCondition.assertTrue(isDisposed(), "isDisposed()");

        return result;
    }
}
