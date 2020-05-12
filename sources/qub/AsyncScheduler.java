package qub;

public interface AsyncScheduler extends AsyncRunner
{
    /**
     * Schedule the provided action to be run on this AsyncRunner.
     * @param action The action to run on this AsyncRunner.
     * @return The Result object that tracks the progress of the provided action.
     */
    AsyncTask<Void> schedule(Action0 action);

    /**
     * Schedule the provided function to be run on this AsyncRunner.
     * @param function The function to run on this AsyncRunner.
     * @param <T> The type of value that the function will return.
     * @return The Result object that tracks the progress of the provided function.
     */
    <T> AsyncTask<T> schedule(Function0<T> function);

    /**
     * Create a new PausedAsyncTask that will run the provided action when it is scheduled.
     * @param action The action to run on this AsyncRunner.
     * @return The PausedAsyncTask that can be scheduled later.
     */
    AsyncTask<Void> create(Action0 action);

    /**
     * Create a new PausedAsyncTask that will run the provided action when it is scheduled.
     * @param function The function to run on this AsyncRunner.
     * @param <T> The type of value that the function will return.
     * @return The PausedAsyncTask that can be scheduled later.
     */
    <T> AsyncTask<T> create(Function0<T> function);

    /**
     * Schedule the provided task to be run on this AsyncRunner.
     * @param task The task to schedule on this AsyncRunner.
     */
    <T> AsyncTask<T> schedule(AsyncTask<T> task);

    /**
     * Await for the provided Result to be completed.
     * @param result The Result to await.
     */
    void await(Result<?> result);
}
