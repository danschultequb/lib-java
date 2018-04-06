package qub;

public class BasicAsyncActionErrorHandler extends BasicAsyncAction
{
    private final Action1<Throwable> action;

    public BasicAsyncActionErrorHandler(Getable<AsyncRunner> runner, Iterable<AsyncTask> parentTasks, Action1<Throwable> action)
    {
        super(runner, parentTasks, null);

        this.action = action;
    }

    @Override
    protected void runTask()
    {
        final Throwable error = getIncomingError();
        if (error != null)
        {
            action.run(error);
        }
    }
}
