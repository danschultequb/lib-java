package qub;

/**
 * A reference to an Action that has been scheduled to schedule asynchronously.
 */
public interface AsyncAction
{
    /**
     * Get the AsyncRunner that will run this AsyncAction.
     * @return The runner that will run this AsyncAction.
     */
    AsyncRunner getRunner();

    /**
     * Block until this AsyncAction completes. If the AsyncAction is already completed, then this
     * will return immediately.
     */
    void await();

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
    AsyncAction thenOn(AsyncRunner runner);

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
    AsyncAction thenOnAsyncAction(AsyncRunner runner, Function0<AsyncAction> function);

    /**
     * Run the provided function on the provided AsyncRunner when this AsyncAction completes.
     * @param function The function to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    <T> AsyncFunction<T> thenOnAsyncFunction(AsyncRunner runner, Function0<AsyncFunction<T>> function);
}
