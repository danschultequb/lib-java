package qub;

import java.util.concurrent.atomic.AtomicInteger;

public class ParallelAsyncRunner implements AsyncRunnerInner
{
    private final AtomicInteger scheduledTaskCount;

    public ParallelAsyncRunner()
    {
        scheduledTaskCount = new AtomicInteger(0);
    }

    /**
     * Get the number of actions that are currently scheduled.
     * @return The number of actions that are current scheduled.
     */
    int getScheduledCount()
    {
        return scheduledTaskCount.get();
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
    public void schedule(final AsyncTask asyncTask)
    {
        scheduledTaskCount.incrementAndGet();
        new java.lang.Thread(new Action0()
        {
            @Override
            public void run()
            {
                asyncTask.runAndSchedulePausedTasks();
                scheduledTaskCount.decrementAndGet();
            }
        }).start();
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
        while (scheduledTaskCount.get() > 0)
        {
        }
    }
}
