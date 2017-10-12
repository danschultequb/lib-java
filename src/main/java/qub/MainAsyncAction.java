package qub;

public class MainAsyncAction extends MainAsyncTask implements AsyncAction
{
    private final Action0 action;

    public MainAsyncAction(MainAsyncRunner runner, Action0 action)
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
