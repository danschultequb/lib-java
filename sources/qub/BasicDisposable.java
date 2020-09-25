package qub;

/**
 * A disposable that can be used to run a specified action when the disposable is disposed.
 */
public class BasicDisposable implements Disposable
{
    private boolean disposed;
    private final Action0 onDispose;

    /**
     * Create a new BasicDisposable that will run the provided action when it is disposed.
     * @param onDispose The action to run when the BasicDisposable is disposed.
     */
    private BasicDisposable(Action0 onDispose)
    {
        PreCondition.assertNotNull(onDispose, "onDispose");

        this.onDispose = onDispose;
    }

    /**
     * Create a new BasicDisposable that will do nothing when it is disposed.
     */
    public static BasicDisposable create()
    {
        return BasicDisposable.create(Action0.empty);
    }

    /**
     * Create a new BasicDisposable that will run the provided action when it is disposed.
     * @param onDispose The action to run when the BasicDisposable is disposed.
     */
    public static BasicDisposable create(Action0 onDispose)
    {
        return new BasicDisposable(onDispose);
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
                this.onDispose.run();
            }
            return result;
        });
    }
}
