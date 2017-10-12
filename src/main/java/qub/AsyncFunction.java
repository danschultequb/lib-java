package qub;

/**
 * A Function that has been scheduled to schedule asynchronously.
 * @param <T> The type that will be returned from the asynchronous function.
 */
public interface AsyncFunction<T> extends AsyncAction
{
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
}
