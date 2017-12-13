package qub;

/**
 * A synchronization primitive that can be opened (allows threads through) or closed (all threads
 * block).
 */
public interface Gate
{
    /**
     * Get whether or not this Gate is currently open.
     * @return Whether or not this Gate is currently open.
     */
    boolean isOpen();

    /**
     * Open this Gate to let threads through and return whether or not the Gate was closed before
     * this function.
     * @return Whether or not this function opened this Gate.
     */
    boolean open();

    /**
     * Close this Gate so that threads will block and return whether or not the Gate was open before
     * this function.
     * @return Whether or not this function closed this Gate.
     */
    boolean close();

    /**
     * Attempt to go through this Gate. If the Gate is closed, then any thread that attempts to go
     * through it will block.
     */
    void passThrough();
}
