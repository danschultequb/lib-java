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
     * Create a new PausedAsyncAction for the provided action that can be scheduled by calling the
     * returned PausedAsyncAction's schedule() function.
     * @param action The action to create a PausedAsyncAction for.
     * @return The created PausedAsyncAction.
     */
    PausedAsyncAction create(Action0 action);

    /**
     * Create a new PausedAsyncFunction for the provided function that can be scheduled by calling
     * the returned PausedAsyncFunction's schedule() function.
     * @param function The function to create a PausedAsyncFunction for.
     * @return The created PausedAsyncFunction.
     */
    <T> PausedAsyncFunction<T> create(Function0<T> function);
}
