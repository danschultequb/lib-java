package qub;

/**
 * A Function that has been scheduled to schedule asynchronously.
 * @param <T> The type that will be returned from the asynchronous function.
 */
public interface AsyncFunction<T> extends AsyncAction
{
    /**
     * Block until this AsyncAction completes. If the AsyncAction is already completed, then this
     * will return immediately and returns the function's return value.
     * @return The return value from this AsyncFunction.
     */
    T awaitReturn();

    /**
     * Run the provided action with this AsyncFunction's result when this AsyncFunction completes.
     * @param action The action to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    AsyncAction then(Action1<T> action);

    /**
     * Run the provided function with this AsyncFunction's result when this AsyncFunction completes.
     * @param function The function to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous function that will be scheduled when this
     * AsyncFunction completes.
     */
    <U> AsyncFunction<U> then(Function1<T,U> function);

    /**
     * Run the provided function when this AsyncAction completes.
     * @param action The action to schedule when this AsyncFunction completes.
     * @return The reference to the asynchronous function that will be scheduled when this
     * AsyncFunction completes.
     */
    AsyncAction thenAsyncAction(Function1<T,AsyncAction> action);

    /**
     * Run the provided function when this AsyncAction completes.
     * @param function The function to schedule when this AsyncFunction completes.
     * @return The reference to the asynchronous function that will be scheduled when this
     * AsyncAction completes.
     */
    <U> AsyncFunction<U> thenAsyncFunction(Function1<T,AsyncFunction<U>> function);

    /**
     * Schedule any following AsyncTasks on the provided AsyncRunner.
     * @param runner The AsyncRunner to schedule following AsyncTasks on.
     * @return The AsyncFunction that transfers flow onto the provided AsyncRunner.
     */
    AsyncFunction<T> thenOn(AsyncRunner runner);

    /**
     * Run the provided action with this AsyncFunction's result on the provided AsyncRunner when
     * this AsyncFunction completes.
     * @param action The action to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    AsyncAction thenOn(AsyncRunner runner, Action1<T> action);

    /**
     * Run the provided function with this AsyncFunction's result on the provided AsyncRunner  when
     * this AsyncFunction completes.
     * @param function The function to schedule when this AsyncAction completes.
     * @return The reference to the asynchronous function that will be scheduled when this
     * AsyncFunction completes.
     */
    <U> AsyncFunction<U> thenOn(AsyncRunner runner, Function1<T,U> function);
}
