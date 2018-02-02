package qub;

/**
 * A generic interface for any type of Stream.
 */
public interface Stream extends AutoCloseable
{
    /**
     * Get whether or not this stream is open.
     * @return Whether or not this stream is open.
     */
    boolean isOpen();

    /**
     * Close this stream.
     */
    void close();
}
