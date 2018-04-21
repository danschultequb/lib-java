package qub;

public class ParallelAsyncRunner extends AsyncRunnerBase
{
    private final java.util.concurrent.atomic.AtomicInteger scheduledTaskCount;
    private final java.util.concurrent.atomic.AtomicInteger runningTaskCount;
    private volatile boolean disposed;

    private final Action0 decrementScheduledTaskCount = new Action0()
    {
        @Override
        public void run()
        {
            scheduledTaskCount.decrementAndGet();
        }
    };

    public ParallelAsyncRunner()
    {
        scheduledTaskCount = new java.util.concurrent.atomic.AtomicInteger(0);
        runningTaskCount = new java.util.concurrent.atomic.AtomicInteger(0);
    }

    @Override
    public int getScheduledTaskCount()
    {
        return scheduledTaskCount.get();
    }

    @Override
    public void schedule(final PausedAsyncTask asyncTask)
    {
        if (!disposed)
        {
            scheduledTaskCount.incrementAndGet();
            new java.lang.Thread(new Action0()
            {
                @Override
                public void run()
                {
                    runningTaskCount.incrementAndGet();
                    AsyncRunnerRegistry.setCurrentThreadAsyncRunner(ParallelAsyncRunner.this);
                    try
                    {
                        asyncTask.setAfterChildTasksScheduledBeforeCompletedAction(decrementScheduledTaskCount);
                        asyncTask.runAndSchedulePausedTasks();
                        runningTaskCount.decrementAndGet();
                    }
                    finally
                    {
                        AsyncRunnerRegistry.removeCurrentThreadAsyncRunner();
                    }
                }
            }).start();
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
    public void await()
    {
        while (scheduledTaskCount.get() > 0 || runningTaskCount.get() > 0)
        {
        }
    }

    @Override
    public void await(AsyncTask asyncTask)
    {
        while (!asyncTask.isCompleted())
        {
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
            result = Result.success(false);
        }
        else
        {
            disposed = true;
            result = Result.success(true);
        }
        return result;
    }
}
