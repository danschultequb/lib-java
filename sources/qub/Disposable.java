package qub;

public interface Disposable extends AutoCloseable
{
    void close();
    
    boolean isDisposed();

    Result<Boolean> dispose();
}
