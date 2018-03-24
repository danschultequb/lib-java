package qub;

/**
 * A PausedAsyncTask that can be scheduled on the AsyncRunner that created it by calling this
 * PausedAsyncTasks's schedule() function.
 */
public interface PausedAsyncTask
{
    /**
     * Set the error that is active when this PausedAsyncTask is being scheduled.
     * @param incomingError The error that is active when this PausedAsyncTask is being scheduled.
     */
    void setIncomingError(Throwable incomingError);

    /**
     * Schedule this PausedAsyncTask to run on the AsyncRunner that created it.
     */
    void schedule();

    /**
     * Run this PausedAsyncTask and schedule all of the PausedAsyncTasks waiting on this
     * PausedAsyncTask.
     */
    void runAndSchedulePausedTasks();
}
