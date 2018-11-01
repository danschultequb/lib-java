package qub;

/**
 * A synchronization primitive that can be opened (allows threads through) or closed (all threads
 * block).
 */
public interface Gate
{
    /**
     * Get the Clock that will be used within operations that are dependant on time.
     * @return The Clock that will be used within operations that are dependant on time.
     */
    Clock getClock();

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

    /**
     * Attempt to go through this Gate for the provided timeout duration. If the Gate is closed,
     * then any thread that attempts to go through it will block.
     * @param timeout The amount of time to wait before this thread will abort waiting for this Gate
     *                to open.
     * @return Whether or not this thread successfully passed through this Gate.
     */
    default Result<Boolean> passThrough(Duration timeout)
    {
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertNotNull(getClock(), "getClock()");

        return passThrough(getClock().getCurrentDateTime().plus(timeout));
    }

    /**
     * Attempt to go through this Gate until the provided timeout DateTime. If the Gate is closed,
     * then any thread that attempts to go through it will block.
     * @param timeout The DateTime at which this thread will abort waiting for this Gate to open.
     * @return Whether or not this thread successfully passed through this Gate.
     */
    Result<Boolean> passThrough(DateTime timeout);
}
