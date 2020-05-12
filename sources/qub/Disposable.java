package qub;

public interface Disposable extends AutoCloseable
{
    /**
     * Create a new Disposable that will invoke the provided action when it is disposed.
     * @param onDisposed The action to invoke when the returned Disposable is disposed.
     * @return The new Disposable.
     */
    static Disposable create(Action0 onDisposed)
    {
        return BasicDisposable.create(onDisposed);
    }

    @Override
    default void close()
    {
        Disposable.close(this);
    }

    static void close(Disposable disposable)
    {
        PreCondition.assertNotNull(disposable, "disposable");

        disposable.dispose().await();
    }
    
    boolean isDisposed();

    Result<Boolean> dispose();
}
