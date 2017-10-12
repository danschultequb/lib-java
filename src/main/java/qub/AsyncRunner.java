package qub;

/**
 * A class that runs provided Actions and Functions asynchronously.
 */
public interface AsyncRunner
{
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
     * Block until all scheduled actions/functions are run to completion.
     */
    void await();
}
