package qub;

/**
 * An interface that runs provided Actions and Functions asynchronously.
 */
public interface AsyncRunner extends Disposable
{
    /**
     * Get the number of actions that are currently scheduled.
     * @return The number of actions that are current scheduled.
     */
    int getScheduledTaskCount();

    /**
     * Block until all scheduled actions/functions are run to completion.
     */
    void await();

    /**
     * Block until the provided AsyncTask is completed.
     * @param asyncTask The AsyncTask to wait for.
     */
    void await(AsyncTask asyncTask);

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
     * Schedule the provided action to run asynchronously.
     * @param function The function that will produce an AsyncAction.
     * @return A reference to the scheduled action.
     */
    AsyncAction scheduleAsyncAction(Function0<AsyncAction> function);

    /**
     * Schedule the provided function to run asynchronously.
     * @param function The function to run asynchronously.
     * @param <T> The type that will be returned from the asynchronous function.
     * @return A reference to the scheduled function.
     */
    <T> AsyncFunction<T> scheduleAsyncFunction(Function0<AsyncFunction<T>> function);

    /**
     * Schedule the provided AsyncTask so that it will be run.
     * @param asyncTask The AsyncTask to schedule.
     */
    void schedule(PausedAsyncTask asyncTask);

    /**
     * Create an AsyncFunction that returns a successful Result.
     * @param <T> The type of Result to return.
     * @return An AsyncFunction that returns a successful Result.
     */
    <T> AsyncFunction<Result<T>> success();

    /**
     * Create an AsyncFunction that returns a successful Result with the provided value.
     * @param value The value to set in the Result.
     * @param <T> The type of the value.
     * @return An AsyncFunction that returns a successful Result with the provided value.
     */
    <T> AsyncFunction<Result<T>> success(T value);

    /**
     * Create an AsyncFunction that returns an error Result with the provided error.
     * @param error The error.
     * @param <T> The type of value that the result would contain if it were successful.
     * @return An AsyncFunction that returns an error Result with the provided error.
     */
    <T> AsyncFunction<Result<T>> error(Throwable error);

    /**
     * Create an AsyncFunction that returns a Result with the provided value and error.
     * @param value The value to set in the Result.
     * @param error The error to set in the Result.
     * @param <T> The type of the value.
     * @return An AsyncFunction that returns a Result with the provided value and error.
     */
    <T> AsyncFunction<Result<T>> done(T value, Throwable error);

    /**
     * Create an AsyncFunction that returns an error Result if the provided value is null.
     * @param value The value to check.
     * @param parameterName The name of the parameter that the value comes from.
     * @param <T> The type of the Result that the return value must match.
     * @return An AsyncFunction if the provided value is null, or null if the provided value is not
     * null.
     */
    <T> AsyncFunction<Result<T>> notNull(Object value, String parameterName);

    /**
     * Create an AsyncFunction that returns an error Result if the provided value is not between the
     * provided lowerBound and upperBound (inclusive).
     * @param lowerBound The lower bound value.
     * @param value The value to check.
     * @param upperBound The upper bound value.
     * @param parameterName The name of the parameter that the value comes from.
     * @param <T> The type of the Result that the return value must match.
     * @return An AsyncFunction if the provided value is not between the lower and upper bounds, or
     * null if the provided value is between the lower and upper bounds.
     */
    <T> AsyncFunction<Result<T>> between(int lowerBound, int value, int upperBound, String parameterName);
}
