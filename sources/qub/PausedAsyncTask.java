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
     * Schedule this PausedAsyncTask to run on the AsyncRunner that created it.
     */
    void schedule();

    /**
     * Set the action that will be run after this task's paused child tasks have been scheduled, but
     * before this task is marked as being completed.
     * @param afterChildTasksScheduledBeforeCompletedAction The action to run.
     */
    void setAfterChildTasksScheduledBeforeCompletedAction(Action0 afterChildTasksScheduledBeforeCompletedAction);

    /**
     * Run this PausedAsyncTask and schedule all of the PausedAsyncTasks waiting on this
     * PausedAsyncTask.
     */
    void runAndSchedulePausedTasks();
}
