package qub;

public class ParallelAsyncRunner implements AsyncRunner
{
    private final java.util.concurrent.atomic.AtomicInteger scheduledTaskCount;
    private final Mutex spinMutex;
    private Function0<Clock> clockGetter;
    private volatile boolean disposed;

    public ParallelAsyncRunner()
    {
        scheduledTaskCount = new java.util.concurrent.atomic.AtomicInteger(0);
        spinMutex = new SpinMutex();
    }

    @Override
    public void setClockGetter(Function0<Clock> clockGetter)
    {
        PreCondition.assertNotNull(clockGetter, "clockGetter");

        this.clockGetter = clockGetter;
    }

    @Override
    public Clock getClock()
    {
        PreCondition.assertNotNull(clockGetter, "clockGetter");

        return clockGetter.run();
    }

    @Override
    public int getScheduledTaskCount()
    {
        return spinMutex.criticalSection(scheduledTaskCount::get);
    }

    @Override
    public void markCompleted(Setable<Boolean> asyncTaskCompleted)
    {
        PreCondition.assertNotNull(asyncTaskCompleted, "asyncTaskCompleted");

        spinMutex.criticalSection(() ->
        {
            scheduledTaskCount.decrementAndGet();
            asyncTaskCompleted.set(true);
        });
    }

    @Override
    public void schedule(final PausedAsyncTask asyncTask)
    {
        PreCondition.assertNotNull(asyncTask, "asyncTask");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        if (!disposed)
        {
            spinMutex.criticalSection(scheduledTaskCount::incrementAndGet);
            final java.lang.Thread thread = new java.lang.Thread(() ->
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
            });
            thread.start();
        }
    }

    @Override
    public AsyncAction schedule(String label, Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        final BasicAsyncAction result = new BasicAsyncAction(new Value<>(this), label, action);
        schedule(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <T> AsyncFunction<T> schedule(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        final BasicAsyncFunction<T> result = new BasicAsyncFunction<>(new Value<>(this), function);
        schedule(result);

        PostCondition.assertNotNull(result, "result");

        return result;
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
