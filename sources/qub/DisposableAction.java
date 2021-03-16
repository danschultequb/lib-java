package qub;

/**
 * A disposable that can be used to run a specified action when the disposable is disposed.
 */
public class DisposableAction implements Disposable
{
    private final Action0 action;
    private boolean disposed;

    private DisposableAction(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        this.action = action;
    }

    /**
     * Create a new DisposableAction that will run the provided action when it is disposed.
     * @param action The action to run when the DisposableAction is disposed.
     */
    public static DisposableAction create(Action0 action)
    {
        return new DisposableAction(action);
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            final boolean result = !this.disposed;
            if (result)
            {
                this.disposed = true;
                this.action.run();
            }
            return result;
        });
    }
}
