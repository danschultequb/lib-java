package qub;

public final class CurrentThread
{
    /**
     * The mapping of thread IDs to registered ResultAsyncSchedulers.
     */
    private static MutableMap<Long,AsyncScheduler> asyncSchedulers = new ConcurrentHashMap<>();

    /**
     * Get the ID of the current thread.
     * @return The ID of the current thread.
     */
    static long getId()
    {
        return java.lang.Thread.currentThread().getId();
    }

    /**
     * Set the AsyncScheduler that will be registered with the current thread.
     * @param asyncRunner The AsyncScheduler that will be registered with the current thread.
     */
    static void setAsyncRunner(AsyncScheduler asyncRunner)
    {
        final long currentThreadId = getId();
        if (asyncRunner == null)
        {
            asyncSchedulers.remove(currentThreadId)
                .catchError(NotFoundException.class)
                .await();
        }
        else
        {
            asyncSchedulers.set(currentThreadId, asyncRunner);
        }
    }

    /**
     * Get the AsyncScheduler that has been registered with the current thread.
     * @return The AsyncScheduler that has been registered with the current thread.
     */
    static AsyncScheduler getAsyncRunner()
    {
        final long currentThreadId = getId();
        return asyncSchedulers.get(currentThreadId)
            .catchError(NotFoundException.class)
            .await();
    }

    /**
     * Run the provided action using the provided ManualAsyncRunner as the current thread's
     * AsyncRunner. The previous AsyncRunner for the current thread will be returned to the current
     * thread's AsyncRunner when this function is finished.
     * @param asyncScheduler The AsyncScheduler to use for the current thread for the duration
     *                    of the provided action.
     * @param action The action to run.
     */
    static void withAsyncScheduler(AsyncScheduler asyncScheduler, Action0 action)
    {
        PreCondition.assertNotNull(asyncScheduler, "asyncScheduler");
        PreCondition.assertNotNull(action, "action");

        final AsyncScheduler backupAsyncScheduler = getAsyncRunner();
        setAsyncRunner(asyncScheduler);
        try
        {
            action.run();
        }
        finally
        {
            setAsyncRunner(backupAsyncScheduler);
        }
    }

    static void withManualAsyncScheduler(Action1<ManualAsyncRunner> action)
    {
        PreCondition.assertNotNull(action, "action");

        withAsyncScheduler(ManualAsyncRunner::new, action::run);
    }

    static void withParallelAsyncScheduler(Action1<ParallelAsyncRunner> action)
    {
        PreCondition.assertNotNull(action, "action");

        withAsyncScheduler(ParallelAsyncRunner::new, action::run);
    }

    static <T extends AsyncScheduler> void withAsyncScheduler(Function0<T> creator, Action1<T> action)
    {
        PreCondition.assertNotNull(creator, "creator");
        PreCondition.assertNotNull(action, "action");

        final T asyncScheduler = creator.run();
        withAsyncScheduler(asyncScheduler, () -> action.run(asyncScheduler));
    }

    /**
     * Run the provided action with a temporary AsyncScheduler that will only exist for the
     * duration of the action.
     * @param action The action to run.
     */
    static void withAsyncScheduler(Action1<AsyncScheduler> action)
    {
        PreCondition.assertNotNull(action, "action");

        withAsyncScheduler(ManualAsyncRunner::new, action);
    }
}
