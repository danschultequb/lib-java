package qub;

public class BasicAsyncAction extends BasicAsyncTask implements AsyncAction, PausedAsyncAction
{
    private static final Action0 emptyAction0 = new Action0()
    {
        @Override
        public void run()
        {
        }
    };

    private final Action0 action;

    public BasicAsyncAction(AsyncRunner runner, Action0 action)
    {
        this(runner, runner.getSynchronization(), action);
    }

    public BasicAsyncAction(AsyncRunner runner, Synchronization synchronization, Action0 action)
    {
        super(runner, synchronization);

        this.action = action;
    }

    @Override
    protected void runTask()
    {
        final Throwable error = getIncomingError();
        if (error != null)
        {
            setOutgoingError(error);
        }
        else
        {
            if (action != null)
            {
                action.run();
            }
        }
    }

    @Override
    public AsyncAction thenOn(AsyncRunner runner)
    {
        return runner == getRunner() ? this : thenOn(runner, emptyAction0);
    }
}
