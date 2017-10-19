package qub;

/**
 * An interface that runs provided Actions and Functions asynchronously.
 */
public interface AsyncRunner
{
    /**
     * Get the number of actions that are currently scheduled.
     * @return The number of actions that are current scheduled.
     */
    int getScheduledTaskCount();

    /**
     * Block until all scheduled actions/functions are run to completion.
     */
    void await();

    /**
     * Schedule the provided action to run asynchronously.
     * @param action The action to run asynchronously.
     * @return A reference to the scheduled action.
     */
    AsyncAction schedule(Action0 action);

    /**
     * Schedule the provided function to run asynchronously.
     * @param function The function to run asynchronously.
     * @param <T> The type that will be returned from the asynchronous function.
     * @return A reference to the scheduled function.
     */
    <T> AsyncFunction<T> schedule(Function0<T> function);

    /**
     * Schedule the provided AsyncTask so that it will be run.
     * @param asyncTask The AsyncTask to schedule.
     */
    void schedule(PausedAsyncTask asyncTask);
}
