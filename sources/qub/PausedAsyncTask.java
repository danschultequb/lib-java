package qub;

/**
 * A PausedAsyncTask that can be scheduled on the AsyncRunner that created it by calling this
 * PausedAsyncTasks's schedule() function.
 */
public interface PausedAsyncTask extends AsyncTask
{
    /**
     * Set the error that is active when this PausedAsyncTask is being scheduled.
     * @param incomingError The error that is active when this PausedAsyncTask is being scheduled.
     */
    void setIncomingError(Throwable incomingError);

    /**
     * Run this PausedAsyncTask and schedule all of the PausedAsyncTasks waiting on this
     * PausedAsyncTask.
     */
    void runAndSchedulePausedTasks();
}
