package qub;

/**
 * A reference to an Action that has been scheduled to schedule asynchronously.
 */
public interface AsyncAction extends AsyncTask
{
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

    /**
     * Schedule any following AsyncTasks on the provided AsyncRunner.
     * @param runner The AsyncRunner to schedule following AsyncTasks on.
     * @return The AsyncAction that transfers flow onto the provided AsyncRunner.
     */
    AsyncAction thenOn(AsyncRunner runner);

    /**
     * Run the provided action if this or a previous AsyncAction produces an error.
     * @param action The action to schedule if this or a previous AsyncAction produces an error.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    AsyncAction catchError(Action1<Throwable> action);

    /**
     * Run the provided function if this or a previous AsyncAction produces an error.
     * @param function The function to schedule if this or a previous AsyncAction produces an error.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    AsyncAction catchErrorAsyncAction(Function1<Throwable,AsyncAction> function);

    /**
     * Run the provided action if this or a previous AsyncAction produces an error.
     * @param action The action to schedule if this or a previous AsyncAction produces an error.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    AsyncAction catchErrorOn(AsyncRunner asyncRunner, Action1<Throwable> action);

    /**
     * Run the provided function if this or a previous AsyncAction produces an error.
     * @param function The function to schedule if this or a previous AsyncAction produces an error.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    AsyncAction catchErrorAsyncActionOn(AsyncRunner asyncRunner, Function1<Throwable,AsyncAction> function);
}
