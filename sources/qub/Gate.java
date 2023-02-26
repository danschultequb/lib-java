package qub;

/**
 * A synchronization primitive that can be opened (allows threads through) or closed (all threads
 * block).
 */
public interface Gate
{
    /**
     * Get whether this {@link Gate} is currently open.
     */
    public Result<Boolean> isOpen();

    /**
     * Open this {@link Gate} to let threads through and return whether this {@link Gate} was closed
     * before this function.
     */
    public Result<Boolean> open();

    /**
     * Close this {@link Gate} so that threads will block and return whether this {@link Gate} was
     * open before this function.
     */
    public Result<Boolean> close();

    /**
     * Attempt to go through this Gate. If the Gate is closed, then any thread that attempts to go
     * through it will block.
     */
    public Result<Void> passThrough();

    /**
     * Attempt to go through this {@link Gate} for the provided timeout duration. If this
     * {@link Gate} is closed, then any thread that attempts to go through it will block.
     * @param timeout The amount of time to wait before this thread will abort waiting for this
     *                {@link Gate} to open.
     */
    public Result<Void> passThrough(Duration timeout);

    /**
     * Attempt to go through this {@link Gate} until the provided timeout {@link DateTime}. If this
     * {@link Gate} is closed, then any thread that attempts to go through it will block.
     * @param timeout The {@link DateTime} at which this thread will abort waiting for this
     * {@link Gate} to open.
     */
    public Result<Void> passThrough(DateTime timeout);
}
