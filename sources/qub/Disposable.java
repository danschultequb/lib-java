package qub;

public interface Disposable extends AutoCloseable
{
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
