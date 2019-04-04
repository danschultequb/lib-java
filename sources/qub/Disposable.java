package qub;

public interface Disposable extends AutoCloseable
{
    @Override
    default void close()
    {
        dispose().await();
    }
    
    boolean isDisposed();

    Result<Boolean> dispose();
}
