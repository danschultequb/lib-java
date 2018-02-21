package qub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelAsyncRunner implements AsyncRunner
{
    private final Function0<Synchronization> synchronizationFunction;
    private final AtomicInteger scheduledTaskCount;
    private final ExecutorService executorService;

    public ParallelAsyncRunner(final Synchronization synchronization)
    {
        this(new Function0<Synchronization>()
        {
            @Override
            public Synchronization run()
            {
                return synchronization;
            }
        });
    }

    public ParallelAsyncRunner(Function0<Synchronization> synchronizationFunction)
    {
        this.synchronizationFunction = synchronizationFunction;
        scheduledTaskCount = new AtomicInteger(0);
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public Synchronization getSynchronization()
    {
        return synchronizationFunction.run();
    }

    @Override
    public int getScheduledTaskCount()
    {
        return scheduledTaskCount.get();
    }

    @Override
    public void schedule(final PausedAsyncTask asyncTask)
    {
        if (!executorService.isShutdown())
        {
            scheduledTaskCount.incrementAndGet();
            executorService.execute(new Action0()
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
            });
        }
    }

    @Override
    public AsyncAction schedule(Action0 action)
    {
        AsyncAction result = null;
        if (action != null)
        {
            final Synchronization synchronization = getSynchronization();
            final PausedAsyncAction asyncAction = new BasicAsyncAction(this, synchronization, action);
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
            final Synchronization synchronization = getSynchronization();
            final PausedAsyncFunction<T> asyncFunction = new BasicAsyncFunction<>(this, synchronization, function);
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

    @Override
    public void close()
    {
        executorService.shutdownNow();
    }
}
