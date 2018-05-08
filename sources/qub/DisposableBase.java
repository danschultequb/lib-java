package qub;

public abstract class DisposableBase implements Disposable
{
    @Override
    public void close()
    {
        DisposableBase.close(this);
    }

    public static void close(Disposable disposable)
    {
        final Result<Boolean> result = disposable.dispose();
        result.throwError();
    }
}
