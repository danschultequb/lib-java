package qub;

public interface Disposable extends AutoCloseable
{
    void close() throws Exception;
    
    boolean isDisposed();

    Result<Boolean> dispose();
}
