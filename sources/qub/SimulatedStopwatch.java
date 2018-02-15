package qub;

public class SimulatedStopwatch implements Stopwatch
{
    private Duration elapsedTime;

    @Override
    public void start()
    {
        elapsedTime = Duration.nanoseconds(0);
    }

    @Override
    public Duration stop()
    {
        final Duration result = elapsedTime;
        elapsedTime = null;
        return result;
    }

    public void addDuration(Duration duration)
    {
        if (elapsedTime != null)
        {
            elapsedTime = elapsedTime.plus(duration);
        }
    }
}
