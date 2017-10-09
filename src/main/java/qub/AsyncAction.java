package qub;

/**
 * A reference to an Action that has been scheduled to run asynchronously.
 */
public interface AsyncAction
{
    /**
     * Block and wait for this AsyncAction to complete.
     */
    void await();

    /**
     * Run the provided action when this AsyncAction completes.
     * @param action The action to run when this AsyncAction completes.
     * @return The reference to the asynchronous action that will be scheduled.
     */
    AsyncAction then(Action0 action);

    /**
     * Run the provided function when this AsyncAction completes.
     * @param function The function to run when this AsyncAction completes.
     * @return The reference to the asynchronous function that will be scheduled when this
     * AsyncAction completes.
     */
    <T> AsyncFunction<T> then(Function0<T> function);
}
