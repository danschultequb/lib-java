package qub;

public interface ResultAsyncScheduler
{
    /**
     * Schedule the provided task to be run on this AsyncRunner.
     * @param task The task to schedule on this AsyncRunner.
     */
    void schedule(ResultAsyncTask<?> task);
}
