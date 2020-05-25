package qub;

/**
 * A process that was created by this process.
 */
public interface ChildProcess
{
    /**
     * Wait for the running process to complete.
     * @return The exit code from the child process.
     */
    int await();
}
