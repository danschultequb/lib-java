package qub;

/**
 * A reference to an AsyncTask that has been scheduled to schedule asynchronously.
 */
public interface AsyncTask
{
    /**
     * Get the AsyncRunner that will run this AsyncAction.
     * @return The runner that will run this AsyncAction.
     */
    AsyncRunner getAsyncRunner();

    /**
     * Get the parent tasks that must be run before this AsyncTask can be run.
     * @return The parent tasks that must be run before this AsyncTask can be run.
     */
    Indexable<AsyncTask> getParentTasks();

    /**
     * Get whether or not this AsyncAction has been run.
     * @return Whether or not this AsyncAction has been run.
     */
    boolean isCompleted();

    /**
     * Block until this AsyncAction completes. If the AsyncAction is already completed, then this
     * will return immediately.
     */
    void await();

    /**
     * Get the error that is active when this PausedAsyncTask is being scheduled.
     * @return The error that is active when this PausedAsyncTask is being scheduled.
     */
    Throwable getIncomingError();

    /**
     * Get the error that exited this AsyncAction.
     * @return The error that exited this AsyncAction.
     */
    Throwable getOutgoingError();
}
