package qub;

public class DisposableAction extends BasicDisposable
{
    private final Action0 action;

    private DisposableAction(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        this.action = action;
    }

    public static DisposableAction create(Action0 action)
    {
        return new DisposableAction(action);
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            final boolean result = super.dispose().await();
            if (result)
            {
                this.action.run();
            }
            return result;
        });
    }
}
