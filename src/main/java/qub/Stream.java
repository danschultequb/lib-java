package qub;

/**
 * A generic interface for any type of Stream.
 */
public interface Stream
{
    /**
     * Get whether or not this stream is open.
     * @return Whether or not this stream is open.
     */
    boolean isOpen();

    /**
     * Close this stream and return whether or not the stream was closed as a result of this call.
     * @return Whether or not the stream was closed as a result of this call.
     */
    boolean close();
}
