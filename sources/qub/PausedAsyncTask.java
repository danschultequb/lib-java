package qub;

/**
 * An AsyncTask that has not yet been scheduled.
 * @param <T> The type of value that the AsyncTask will return when it is scheduled and run.
 */
public interface PausedAsyncTask<T> extends Result<T>
{
    /**
     * Schedule this PausedAsyncTask to run on its AsyncRunner.
     */
    void schedule();
}
