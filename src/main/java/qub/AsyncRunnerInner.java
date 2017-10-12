package qub;

public interface AsyncRunnerInner
{
    /**
     * Create a new PausedAsyncAction for the provided action that can be scheduled by calling the
     * returned PausedAsyncAction's schedule() function.
     * @param action The action to create a PausedAsyncAction for.
     * @return The created PausedAsyncAction.
     */
    PausedAsyncAction create(Action0 action);

    /**
     * Create a new PausedAsyncFunction for the provided function that can be scheduled by calling
     * the returned PausedAsyncFunction's schedule() function.
     * @param function The function to create a PausedAsyncFunction for.
     * @return The created PausedAsyncFunction.
     */
    <T> PausedAsyncFunction<T> create(Function0<T> function);

    /**
     * Schedule the provided AsyncTask so that it will be run.
     * @param action The AsyncTask to schedule.
     */
    void schedule(AsyncTask action);
}
