package qub;

public interface AsyncRunnerInner extends AsyncRunner
{
    /**
     * Schedule the provided AsyncTask so that it will be run.
     * @param asyncTask The AsyncTask to schedule.
     */
    void schedule(AsyncTask asyncTask);
}
