package qub;

/**
 * A reference to an Action that has been scheduled to schedule asynchronously.
 */
public interface AsyncAction
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
}
