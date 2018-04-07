package qub;

public abstract class AsyncDisposableBase extends DisposableBase implements AsyncDisposable
{
    @Override
    final public Result<Boolean> dispose()
    {
        return AsyncDisposableBase.dispose(this);
    }

    public static Result<Boolean> dispose(AsyncDisposable disposable)
    {
        return disposable.disposeAsync().awaitReturn();
    }
}
