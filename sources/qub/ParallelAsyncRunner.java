package qub;

public class ParallelAsyncRunner extends AsyncRunnerBase
{
    private final java.util.concurrent.atomic.AtomicInteger scheduledTaskCount;
    private final SpinMutex spinMutex;
    private volatile boolean disposed;

    public ParallelAsyncRunner()
    {
        scheduledTaskCount = new java.util.concurrent.atomic.AtomicInteger(0);
        spinMutex = new SpinMutex();
    }

    @Override
    public int getScheduledTaskCount()
    {
        return spinMutex.criticalSection(new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                return scheduledTaskCount.get();
            }
        });
    }

    @Override
    public void markCompleted(final Setable<Boolean> asyncTaskCompleted)
    {
        spinMutex.criticalSection(new Action0()
        {
            @Override
            public void run()
            {
                asyncTaskCompleted.set(true);
                scheduledTaskCount.decrementAndGet();
            }
        });
    }

    @Override
    public void schedule(final PausedAsyncTask asyncTask)
    {
        if (!disposed)
        {
            spinMutex.criticalSection(new Action0()
            {
                @Override
                public void run()
                {
                    scheduledTaskCount.incrementAndGet();
                }
            });
            new java.lang.Thread(new Action0()
            {
                @Override
                public void run()
                {
                    AsyncRunnerRegistry.setCurrentThreadAsyncRunner(ParallelAsyncRunner.this);
                    try
                    {
                        asyncTask.runAndSchedulePausedTasks();
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
            final BasicAsyncFunction<T> asyncFunction = new BasicAsyncFunction<T>(new Value<AsyncRunner>(this), function);
            asyncFunction.schedule();
            result = asyncFunction;
        }
        return result;
    }

    @Override
    public void await()
    {
        while (getScheduledTaskCount() > 0)
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
