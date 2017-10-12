package qub;

public class BasicAsyncAction extends BasicAsyncTask implements AsyncAction, PausedAsyncAction
{
    private final Action0 action;

    public BasicAsyncAction(AsyncRunnerInner runner, Action0 action)
    {
        super(runner);

        this.action = action;
    }

    @Override
    protected void runTask()
    {
        action.run();
    }
}
