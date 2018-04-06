package qub;

/**
 * An interface that runs provided Actions and Functions asynchronously.
 */
public interface AsyncRunner extends Disposable
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
     * Block until the provided AsyncTask is completed.
     * @param asyncTask The AsyncTask to wait for.
     */
    void await(AsyncTask asyncTask);

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
     * Schedule the provided action to run asynchronously.
     * @param function The function that will produce an AsyncAction.
     * @return A reference to the scheduled action.
     */
    AsyncAction scheduleAsyncAction(Function0<AsyncAction> function);

    /**
     * Schedule the provided function to run asynchronously.
     * @param function The function to run asynchronously.
     * @param <T> The type that will be returned from the asynchronous function.
     * @return A reference to the scheduled function.
     */
    <T> AsyncFunction<T> scheduleAsyncFunction(Function0<AsyncFunction<T>> function);

    /**
     * Schedule the provided AsyncTask so that it will be run.
     * @param asyncTask The AsyncTask to schedule.
     */
    void schedule(PausedAsyncTask asyncTask);

    /**
     * Create an AsyncFunction that returns a successful Return object with no value.
     * @return An AsyncFunction that returns a successful Return object with no value.
     */
    //AsyncFunction<Result> success();

    /**
     * Create an AsyncFunction that returns a successful Return object with the provided value.
     * @return An AsyncFunction that returns a successful Return object with the provided value.
     */
    //<T> AsyncFunction<Result<T>> success(T value);
}
