package qub;

public class ManualAsyncRunner extends DisposableBase implements AsyncRunner
{
    private final Function0<Synchronization> synchronizationFunction;
    private final LockedQueue<PausedAsyncTask> scheduledTasks;
    private boolean disposed;

    public ManualAsyncRunner()
    {
        this(new Synchronization());
    }

    public ManualAsyncRunner(final Synchronization synchronization)
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

    public ManualAsyncRunner(Function0<Synchronization> synchronizationFunction)
    {
        this.synchronizationFunction = synchronizationFunction;
        this.scheduledTasks = new LockedQueue<>(new SingleLinkListQueue<PausedAsyncTask>());
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
     * Run the provided action immediately using a new ManualAsyncRunner that has been
     * registered with the AsyncRunnerRegistry for the current thread. When the provided action
     * completes, the provided ManualAsyncRunner will be removed from the
     * AsyncRunnerRegistry.
     * @param console The Console object that will provide the Synchronization object for the
     *                created ManualAsyncRunner.
     * @param action The action to run immediately with the created and registered
     *               ManualAsyncRunner.
     */
    public static void withRegistered(final Console console, Action1<ManualAsyncRunner> action)
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
     * Run the provided action immediately using a new ManualAsyncRunner that has been
     * registered with the AsyncRunnerRegistry for the current thread. When the provided action
     * completes, the provided ManualAsyncRunner will be removed from the
     * AsyncRunnerRegistry.
     * @param synchronization The Synchronization object to provide to the created
     *                        ManualAsyncRunner.
     * @param action The action to run immediately with the created and registered
     *               ManualAsyncRunner.
     */
    public static void withRegistered(final Synchronization synchronization, Action1<ManualAsyncRunner> action)
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
     * Run the provided action immediately using a new ManualAsyncRunner that has been
     * registered with the AsyncRunnerRegistry for the current thread. When the provided action
     * completes, the provided ManualAsyncRunner will be removed from the
     * AsyncRunnerRegistry.
     * @param synchronizationFunction The function that will provide a Synchronization object.
     * @param action The action to run immediately with the created and registered
     *               ManualAsyncRunner.
     */
    public static void withRegistered(final Function0<Synchronization> synchronizationFunction, Action1<ManualAsyncRunner> action)
    {
        final AsyncRunner runnerBackup = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        final ManualAsyncRunner runner = new ManualAsyncRunner(synchronizationFunction);
        AsyncRunnerRegistry.setCurrentThreadAsyncRunner(runner);
        try
        {
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
