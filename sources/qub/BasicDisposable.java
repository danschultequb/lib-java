package qub;

public class BasicDisposable implements Disposable
{
    private boolean isDisposed;

    protected BasicDisposable()
    {
    }

    public static BasicDisposable create()
    {
        return new BasicDisposable();
    }

    @Override
    public boolean isDisposed()
    {
        return this.isDisposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            boolean result = !this.isDisposed;
            if (result)
            {
                this.isDisposed = true;
            }
            return result;
        });
    }
}
