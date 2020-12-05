package qub;

/**
 * A Stopwatch class that can determine the duration between start and stop events.
 */
public class Stopwatch2 implements Stopwatch
{
    private final Clock clock;
    private DateTime startTime;

    private Stopwatch2(Clock clock)
    {
        PreCondition.assertNotNull(clock, "clock");

        this.clock = clock;
    }

    public static Stopwatch2 create(Clock clock)
    {
        return new Stopwatch2(clock);
    }

    /**
     * Get whether or not this stopwatch has started.
     * @return
     */
    public boolean hasStarted()
    {
        return this.startTime != null;
    }

    /**
     * Start the Stopwatch.
     */
    public Stopwatch2 start2()
    {
        PreCondition.assertFalse(this.hasStarted(), "this.hasStarted()");

        this.startTime = this.clock.getCurrentDateTime();

        PostCondition.assertTrue(this.hasStarted(), "this.hasStarted()");

        return this;
    }

    @Override
    @Deprecated
    public void start()
    {
        this.start2();
    }

    /**
     * Stop the stopwatch and return the Duration since the start() method was called.
     * @return The Duration since the start() method was called.
     */
    public Duration stop()
    {
        PreCondition.assertTrue(this.hasStarted(), "this.hasStarted()");

        final DateTime endTime = this.clock.getCurrentDateTime();
        final Duration result = endTime.minus(this.startTime);
        this.startTime = null;

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Duration.zero, "result");
        PostCondition.assertFalse(this.hasStarted(), "this.hasStarted()");

        return result;
    }
}
