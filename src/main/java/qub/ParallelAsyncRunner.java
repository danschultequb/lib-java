package qub;

import java.util.concurrent.atomic.AtomicInteger;

public class ParallelAsyncRunner implements AsyncRunner
{
    private final AtomicInteger scheduledTaskCount;

    public ParallelAsyncRunner()
    {
        scheduledTaskCount = new AtomicInteger(0);
    }

    @Override
    public int getScheduledTaskCount()
    {
        return scheduledTaskCount.get();
    }

    @Override
    public void schedule(final PausedAsyncTask asyncTask)
    {
        scheduledTaskCount.incrementAndGet();
        new java.lang.Thread(new Action0()
        {
            @Override
            public void run()
            {
                AsyncRunnerRegistry.setCurrentThreadAsyncRunner(ParallelAsyncRunner.this);
                try
                {
                    asyncTask.runAndSchedulePausedTasks();
                    scheduledTaskCount.decrementAndGet();
                }
                finally
                {
                    AsyncRunnerRegistry.removeCurrentThreadAsyncRunner();
                }
            }
        }).start();
    }

    @Override
    public AsyncAction schedule(Action0 action)
    {
        AsyncAction result = null;
        if (action != null)
        {
            final PausedAsyncAction asyncAction = new BasicAsyncAction(this, action);
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
            final PausedAsyncFunction<T> asyncFunction = new BasicAsyncFunction<>(this, function);
            asyncFunction.schedule();
            result = asyncFunction;
        }
        return result;
    }

    @Override
    public void await()
    {
        while (scheduledTaskCount.get() > 0)
        {
        }
    }
}
