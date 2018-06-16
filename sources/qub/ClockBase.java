package qub;

/**
 * An abstract base class for the Clock interface that contains implementations for methods that are
 * the same across multiple Clock implementations.
 */
public abstract class ClockBase implements Clock
{
    @Override
    public AsyncAction scheduleAfter(Duration duration, Action0 action)
    {
        return ClockBase.runAfter(this, duration, action);
    }

    /**
     * An implementation of Clock.scheduleAfter(Duration,Action0) that defers to
     * Clock.scheduleAt(DateTime,Action0).
     * @param clock The caller of the function.
     * @param duration The duration call the action after.
     * @param action The action that will be scheduled after the duration.
     * @return The AsyncAction associated with the scheduled action.
     */
    public static AsyncAction runAfter(Clock clock, Duration duration, Action0 action)
    {
        final DateTime currentDateTime = clock.getCurrentDateTime();
        final DateTime dateTime = currentDateTime.plus(duration);
        return clock.scheduleAt(dateTime, action);
    }
}
