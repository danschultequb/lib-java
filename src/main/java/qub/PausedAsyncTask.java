package qub;

/**
 * A PausedAsyncTask that can be scheduled on the AsyncRunner that created it by calling this
 * PausedAsyncTasks's schedule() function.
 */
public interface PausedAsyncTask
{
    /**
     * Schedule this PausedAsyncTask to run on the AsyncRunner that created it.
     */
    void schedule();

    /**
     * Schedule this PausedAsyncTask to run on the provided AsyncRunner.
     * @param runner The AsyncRunner to schedule this PausedAsyncTask on.
     */
    void scheduleOn(AsyncRunner runner);

    /**
     * Run this PausedAsyncTask and schedule all of the PausedAsyncTasks waiting on this
     * PausedAsyncTask.
     */
    void runAndSchedulePausedTasks();
}
