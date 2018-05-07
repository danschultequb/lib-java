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
     * Get the number of parent tasks for this AsyncTask.
     * @return
     */
    int getParentTaskCount();

    /**
     * Get the parent task at the provided index.
     * @param index The parent task index.
     * @return The parent task at the provided index.
     */
    AsyncTask getParentTask(int index);

    /**
     * Get whether or not the provided AsyncTask is a parent task of this AsyncTask.
     * @param asyncTask The AsyncTask to check.
     * @return Whether or not the provided AsyncTask is a parent task of this AsyncTask.
     */
    boolean parentTasksContain(AsyncTask asyncTask);

    /**
     * Add the provided AsyncTask as a parent task of this AsyncTask.
     * @param parentTask The AsyncTask to add as a parent of this AsyncTask.
     */
    void addParentTask(AsyncTask parentTask);

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
