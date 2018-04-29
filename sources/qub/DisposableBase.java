package qub;

public abstract class DisposableBase implements Disposable
{
    @Override
    final public void close() throws Exception
    {
        DisposableBase.close(this);
    }

    public static void close(Disposable disposable) throws Exception
    {
        final Result<Boolean> result = disposable.dispose();
        result.throwError(Exception.class);
    }
}
