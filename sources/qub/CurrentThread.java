package qub;

public final class CurrentThread
{
    /**
     * The mapping of thread IDs to registered ResultAsyncSchedulers.
     */
    private static final MutableMap<Long,AsyncScheduler> asyncSchedulers = ConcurrentHashMap.create();

    /**
     * Get the ID of the current thread.
     */
    public static long getId()
    {
        return java.lang.Thread.currentThread().getId();
    }

    /**
     * Yield the current thread's execution so that the scheduler can execute a different thread.
     */
    public static void yield()
    {
        java.lang.Thread.yield();
    }

    /**
     * Set the {@link AsyncScheduler} that will be registered with the current thread.
     * @param asyncRunner The {@link AsyncScheduler} that will be registered with the current thread.
     */
    public static void setAsyncRunner(AsyncScheduler asyncRunner)
    {
        final long currentThreadId = CurrentThread.getId();
        if (asyncRunner == null)
        {
            CurrentThread.asyncSchedulers.remove(currentThreadId)
                .catchError(NotFoundException.class)
                .await();
        }
        else
        {
            CurrentThread.asyncSchedulers.set(currentThreadId, asyncRunner);
        }
    }

    /**
     * Get the {@link AsyncScheduler} that has been registered with the current thread.
     */
    static Result<AsyncScheduler> getAsyncRunner()
    {
        final long currentThreadId = getId();
        return CurrentThread.asyncSchedulers.get(currentThreadId)
            .convertError(NotFoundException.class, () ->
            {
                return new NotFoundException("No " + Types.getTypeName(AsyncRunner.class) + " has been registered with the current thread (id: " + currentThreadId + ").");
            });
    }

    /**
     * Run the provided {@link Action0} using the provided {@link AsyncScheduler} as the current
     * thread's {@link AsyncRunner}. The previous {@link AsyncRunner} for the current thread will be
     * restored as the current thread's {@link AsyncRunner} when the provided {@link Action0} is
     * finished.
     * @param asyncScheduler The {@link AsyncScheduler} to use for the current thread for the
     *                       duration of the provided {@link Action0}.
     * @param action The {@link Action0} to run.
     */
    public static void withAsyncScheduler(AsyncScheduler asyncScheduler, Action0 action)
    {
        PreCondition.assertNotNull(asyncScheduler, "asyncScheduler");
        PreCondition.assertNotNull(action, "action");

        final AsyncScheduler backupAsyncScheduler = CurrentThread.getAsyncRunner().catchError(NotFoundException.class).await();
        CurrentThread.setAsyncRunner(asyncScheduler);
        try
        {
            action.run();
        }
        finally
        {
            CurrentThread.setAsyncRunner(backupAsyncScheduler);
        }
    }

    public static void withManualAsyncScheduler(Action1<ManualAsyncRunner> action)
    {
        PreCondition.assertNotNull(action, "action");

        CurrentThread.withAsyncScheduler(ManualAsyncRunner::create, action::run);
    }

    public static void withParallelAsyncScheduler(Action1<ParallelAsyncRunner> action)
    {
        PreCondition.assertNotNull(action, "action");

        CurrentThread.withAsyncScheduler(ParallelAsyncRunner::create, action::run);
    }

    public static <T extends AsyncScheduler> void withAsyncScheduler(Function0<T> creator, Action1<T> action)
    {
        PreCondition.assertNotNull(creator, "creator");
        PreCondition.assertNotNull(action, "action");

        final T asyncScheduler = creator.run();
        CurrentThread.withAsyncScheduler(asyncScheduler, () -> action.run(asyncScheduler));
    }

    /**
     * Run the provided {@link Action1} with a temporary {@link AsyncScheduler} that will only exist
     * for the duration of the {@link Action1}.
     * @param action The {@link Action1} to run.
     */
    public static void withAsyncScheduler(Action1<AsyncScheduler> action)
    {
        PreCondition.assertNotNull(action, "action");

        CurrentThread.withAsyncScheduler(ManualAsyncRunner::create, action);
    }
}
