package qub;

/**
 * A reference to an Action that has been scheduled to schedule asynchronously.
 */
public interface AsyncAction extends AsyncTask
{
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
