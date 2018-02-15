package qub;

/**
 * A Stopwatch implementation that uses Java's built in nanoTime() function.
 */
public class JavaStopwatch implements Stopwatch
{
    private Long startTime;

    @Override
    public void start()
    {
        startTime = System.nanoTime();
    }

    @Override
    public Duration stop()
    {
        Duration result = null;
        if (startTime != null)
        {
            result = Duration.nanoseconds(System.nanoTime() - startTime);
            startTime = null;
        }
        return result;
    }
}
