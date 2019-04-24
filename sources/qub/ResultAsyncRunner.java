package qub;

/**
 * An interface that runs provided Actions and Functions asynchronously.
 */
public interface ResultAsyncRunner
{
    /**
     * Schedule the provided action to be run on this AsyncRunner.
     * @param action The action to run on this AsyncRunner.
     * @return The Result object that tracks the progress of the provided action.
     */
    Result<Void> run(Action0 action);

    /**
     * Schedule the provided function to be run on this AsyncRunner.
     * @param function The function to run on this AsyncRunner.
     * @param <T> The type of value that the function will return.
     * @return The Result object that tracks the progress of the provided function.
     */
    <T> Result<T> run(Function0<T> function);

    /**
     * Schedule the provided function to be run on this AsyncRunner.
     * @param function The function to run on this AsyncRunner.
     * @param <T> The type of value that the function will return.
     * @return The Result object that tracks the progress of the provided function.
     */
    <T> Result<T> runResult(Function0<Result<T>> function);
}
