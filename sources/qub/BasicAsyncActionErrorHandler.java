package qub;

public class BasicAsyncActionErrorHandler extends BasicAsyncAction
{
    private final Action1<Throwable> action;

    public BasicAsyncActionErrorHandler(AsyncRunner runner, Action1<Throwable> action)
    {
        this(runner, runner.getSynchronization(), action);
    }

    public BasicAsyncActionErrorHandler(AsyncRunner runner, Synchronization synchronization, Action1<Throwable> action)
    {
        super(runner, synchronization, null);

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
