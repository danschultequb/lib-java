package qub;

/**
 * An interface that runs provided Actions and Functions asynchronously.
 */
public interface AsyncRunner
{
    /**
     * Schedule the provided action to be run on this AsyncRunner.
     * @param action The action to run on this AsyncRunner.
     * @return The Result object that tracks the progress of the provided action.
     */
    Result<Void> schedule(Action0 action);

    /**
     * Schedule the provided function to be run on this AsyncRunner.
     * @param function The function to run on this AsyncRunner.
     * @param <T> The type of value that the function will return.
     * @return The Result object that tracks the progress of the provided function.
     */
    <T> Result<T> schedule(Function0<T> function);

    /**
     * Create a new PausedAsyncTask that will run the provided action when it is scheduled.
     * @param action The action to run on this AsyncRunner.
     * @return The PausedAsyncTask that can be scheduled later.
     */
    PausedAsyncTask<Void> create(Action0 action);

    /**
     * Create a new PausedAsyncTask that will run the provided action when it is scheduled.
     * @param function The function to run on this AsyncRunner.
     * @param <T> The type of value that the function will return.
     * @return The PausedAsyncTask that can be scheduled later.
     */
    <T> PausedAsyncTask<T> create(Function0<T> function);
}
