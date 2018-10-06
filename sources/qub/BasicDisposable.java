package qub;

public abstract class BasicDisposable extends DisposableBase
{
    private boolean disposed;

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
        return null;
    }

    protected abstract void onDispose();
}
