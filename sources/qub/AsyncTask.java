package qub;

public interface AsyncTask
{
    /**
     * Get the AsyncRunner that will run this AsyncAction.
     * @return The runner that will run this AsyncAction.
     */
    AsyncRunner getAsyncRunner();

    /**
     * Get whether or not this AsyncAction has been run.
     * @return Whether or not this AsyncAction has been run.
     */
    boolean isCompleted();

    /**
     * Block until this AsyncAction completes. If the AsyncAction is already completed, then this
     * will return immediately.
     */
    void await();

    /**
     * Get the error that is active when this PausedAsyncTask is being scheduled.
     * @return The error that is active when this PausedAsyncTask is being scheduled.
     */
    Throwable getIncomingError();

    /**
     * Get the error that exited this AsyncAction.
     * @return The error that exited this AsyncAction.
     */
    Throwable getOutgoingError();

    /**
     * Run the provided action when this AsyncAction completes.
     * @param action The action to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    AsyncAction then(Action0 action);

    /**
     * Run the provided function when this AsyncAction completes.
     * @param function The function to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous function that will be scheduled when this
     * AsyncAction completes.
     */
    <T> AsyncFunction<T> then(Function0<T> function);

    /**
     * Run the provided function when this AsyncAction completes.
     * @param function The function to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous function that will be scheduled when this
     * AsyncAction completes.
     */
    AsyncAction thenAsyncAction(Function0<AsyncAction> function);

    /**
     * Run the provided function when this AsyncAction completes.
     * @param function The function to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous function that will be scheduled when this
     * AsyncAction completes.
     */
    <T> AsyncFunction<T> thenAsyncFunction(Function0<AsyncFunction<T>> function);

    /**
     * Schedule any following AsyncTasks on the provided AsyncRunner.
     * @param runner The AsyncRunner to schedule following AsyncTasks on.
     * @return The AsyncAction that transfers flow onto the provided AsyncRunner.
     */
    AsyncTask thenOn(AsyncRunner runner);

    /**
     * Run the provided action on the provided AsyncRunner when this AsyncAction completes.
     * @param action The action to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    AsyncAction thenOn(AsyncRunner runner, Action0 action);

    /**
     * Run the provided function on the provided AsyncRunner when this AsyncAction completes.
     * @param function The function to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    <T> AsyncFunction<T> thenOn(AsyncRunner runner, Function0<T> function);

    /**
     * Run the provided function on the provided AsyncRunner when this AsyncAction completes.
     * @param function The function to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    AsyncAction thenAsyncActionOn(AsyncRunner runner, Function0<AsyncAction> function);

    /**
     * Run the provided function on the provided AsyncRunner when this AsyncAction completes.
     * @param function The function to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    <T> AsyncFunction<T> thenAsyncFunctionOn(AsyncRunner runner, Function0<AsyncFunction<T>> function);
}
