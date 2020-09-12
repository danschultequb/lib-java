package qub;

/**
 * A Stopwatch class that can determine the duration between start and stop events.
 */
public interface Stopwatch
{
    /**
     * Start the Stopwatch.
     */
    void start();

    /**
     * Stop the stopwatch and return the Duration since the start() method was called.
     * @return The Duration since the start() method was called.
     */
    Duration stop();

    /**
     * Stop the stopwatch and return the Duration since the start() method was called.
     * @return The Duration since the start() method was called.
     */
    Duration2 stop2();
}
