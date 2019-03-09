package qub;

public interface Disposable extends AutoCloseable
{
    @Override
    default void close()
    {
        dispose().awaitError();
    }
    
    boolean isDisposed();

    Result<Boolean> dispose();
}
