package qub;

/**
 * A disposable that can be used to run a specified action when the disposable is disposed.
 */
public class BasicDisposable extends DisposableBase
{
    private boolean disposed;
    private final Action0 onDisposed;

    /**
     * Create a new BasicDisposable with no onDisposed action.
     */
    public BasicDisposable()
    {
        this(null);
    }

    /**
     * Create a new BasicDisposable that will run the provided action when it is disposed.
     * @param onDisposed The action to run when the BasicDisposable is disposed.
     */
    public BasicDisposable(Action0 onDisposed)
    {
        this.onDisposed = onDisposed;
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (disposed)
        {
            result = Result.successFalse();
        }
        else
        {
            onDispose();

            disposed = true;
            result = Result.successTrue();
        }
        return result;
    }

    protected void onDispose()
    {
        if (onDisposed != null)
        {
            onDisposed.run();
        }
    }
}
