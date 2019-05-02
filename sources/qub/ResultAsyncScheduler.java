package qub;

public interface ResultAsyncScheduler extends ResultAsyncRunner
{
    /**
     * Schedule the provided task to be run on this AsyncRunner.
     * @param task The task to schedule on this AsyncRunner.
     */
    <T> ResultAsyncTask<T> schedule(ResultAsyncTask<T> task);

    /**
     * Await for the provided Result to be completed.
     * @param result The Result to await.
     */
    void await(Result<?> result);
}
