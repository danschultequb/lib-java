package qub;

public class SimulatedStopwatch implements Stopwatch
{
    private Duration2 elapsedTime;

    @Override
    public void start()
    {
        elapsedTime = Duration2.nanoseconds(0);
    }

    @Override
    public Duration stop()
    {
        final Duration result = new Duration(elapsedTime.getValue(), elapsedTime.getUnits());
        elapsedTime = null;
        return result;
    }

    @Override
    public Duration2 stop2()
    {
        final Duration2 result = elapsedTime;
        elapsedTime = null;
        return result;
    }

    public void addDuration(Duration2 duration)
    {
        if (elapsedTime != null)
        {
            elapsedTime = elapsedTime.plus(duration);
        }
    }
}
