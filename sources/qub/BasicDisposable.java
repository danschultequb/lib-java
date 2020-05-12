package qub;

/**
 * A disposable that can be used to run a specified action when the disposable is disposed.
 */
public class BasicDisposable implements Disposable
{
    private boolean disposed;
    private final Action0 onDisposed;

    /**
     * Create a new BasicDisposable that will run the provided action when it is disposed.
     * @param onDisposed The action to run when the BasicDisposable is disposed.
     */
    private BasicDisposable(Action0 onDisposed)
    {
        PreCondition.assertNotNull(onDisposed, "onDisposed");

        this.onDisposed = onDisposed;
    }

    /**
     * Create a new BasicDisposable that will run the provided action when it is disposed.
     * @param onDisposed The action to run when the BasicDisposable is disposed.
     */
    public static BasicDisposable create(Action0 onDisposed)
    {
        return new BasicDisposable(onDisposed);
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
                this.onDisposed.run();
            }
            return result;
        });
    }
}
