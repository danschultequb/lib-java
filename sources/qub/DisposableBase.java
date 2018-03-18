package qub;

public abstract class DisposableBase implements Disposable
{
    @Override
    final public void close()
    {
        DisposableBase.close(this);
    }

    public static void close(Disposable disposable)
    {
        disposable.dispose();
    }
}
