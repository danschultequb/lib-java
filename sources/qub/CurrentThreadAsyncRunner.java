package qub;

public class CurrentThreadAsyncRunner implements AsyncRunner
{
    private final Function0<Synchronization> synchronizationFunction;
    private final LockedSingleLinkListQueue<PausedAsyncTask> scheduledTasks;

    public CurrentThreadAsyncRunner(final Synchronization synchronization)
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

    public CurrentThreadAsyncRunner(Function0<Synchronization> synchronizationFunction)
    {
        this.synchronizationFunction = synchronizationFunction;
        this.scheduledTasks = new LockedSingleLinkListQueue<>();
    }

    @Override
    public Synchronization getSynchronization()
    {
        return synchronizationFunction.run();
    }

    @Override
    public int getScheduledTaskCount()
    {
        return scheduledTasks.getCount();
    }

    @Override
    public void schedule(PausedAsyncTask asyncTask)
    {
        scheduledTasks.enqueue(asyncTask);
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
        while (scheduledTasks.any())
        {
            final PausedAsyncTask action = scheduledTasks.dequeue();
            action.runAndSchedulePausedTasks();
        }
    }

    /**
     * Run the provided action immediately using a new CurrentThreadAsyncRunner that has been
     * registered with the AsyncRunnerRegistry for the current thread. When the provided action
     * completes, the provided CurrentThreadAsyncRunner will be removed from the
     * AsyncRunnerRegistry.
     * @param console The Console object that will provide the Synchronization object for the
     *                created CurrentThreadAsyncRunner.
     * @param action The action to run immediately with the created and registered
     *               CurrentThreadAsyncRunner.
     */
    public static void withRegistered(final Console console, Action1<CurrentThreadAsyncRunner> action)
    {
        final Function0<Synchronization> synchronizationFunction = new Function0<Synchronization>()
        {
            @Override
            public Synchronization run()
            {
                return console.getSynchronization();
            }
        };
        withRegistered(synchronizationFunction, action);
    }

    /**
     * Run the provided action immediately using a new CurrentThreadAsyncRunner that has been
     * registered with the AsyncRunnerRegistry for the current thread. When the provided action
     * completes, the provided CurrentThreadAsyncRunner will be removed from the
     * AsyncRunnerRegistry.
     * @param synchronization The Synchronization object to provide to the created
     *                        CurrentThreadAsyncRunner.
     * @param action The action to run immediately with the created and registered
     *               CurrentThreadAsyncRunner.
     */
    public static void withRegistered(final Synchronization synchronization, Action1<CurrentThreadAsyncRunner> action)
    {
        final Function0<Synchronization> synchronizationFunction = new Function0<Synchronization>()
        {
            @Override
            public Synchronization run()
            {
                return synchronization;
            }
        };
        withRegistered(synchronizationFunction, action);
    }

    /**
     * Run the provided action immediately using a new CurrentThreadAsyncRunner that has been
     * registered with the AsyncRunnerRegistry for the current thread. When the provided action
     * completes, the provided CurrentThreadAsyncRunner will be removed from the
     * AsyncRunnerRegistry.
     * @param synchronizationFunction The function that will provide a Synchronization object.
     * @param action The action to run immediately with the created and registered
     *               CurrentThreadAsyncRunner.
     */
    public static void withRegistered(final Function0 synchronizationFunction, Action1<CurrentThreadAsyncRunner> action)
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner(synchronizationFunction);

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
