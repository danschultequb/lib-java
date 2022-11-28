package qub;

/**
 * A type that provides functions for get the current time and for scheduling tasks to run in the
 * future.
 */
public interface Clock
{
    /**
     * Get the current time zone offset.
     */
    public default Duration getCurrentOffset()
    {
        return this.getCurrentDateTime().getOffset();
    }

    /**
     * Get a {@link DateTime} object for the current date and time.
     */
    public DateTime getCurrentDateTime();

    /**
     * Run the provided {@link Action0} on the {@link CurrentThread}'s {@link AsyncRunner} after the
     * provided date and time.
     * @param dateTime The {@link DateTime} that the provided {@link Action0} will be run after.
     * @param action The {@link Action0} to run after the provided {@link DateTime}.
     * @return The AsyncAction associated with the scheduled action.
     */
    public Result<Void> scheduleAt(DateTime dateTime, Action0 action);

    /**
     * Run the provided action on the main AsyncRunner after the provided duration of time.
     * @param duration The duration to wait before scheduling the provided action.
     * @param action The action to run after the duration.
     * @return The AsyncAction associated with the scheduled action.
     */
    public default Result<Void> scheduleAfter(Duration duration, Action0 action)
    {
        PreCondition.assertNotNull(duration, "duration");
        PreCondition.assertNotNull(action, "action");

        return this.scheduleAt(this.getCurrentDateTime().plus(duration), action);
    }

    /**
     * Delay the current thread for the provided duration.
     * @param duration The duration to delay the current thread.
     * @return The result of delaying the current thread.
     */
    public default Result<Void> delay(Duration duration)
    {
        PreCondition.assertNotNull(duration, "duration");

        return this.scheduleAfter(duration, Action0.empty);
    }

    /**
     * Delay the current thread until the provided date and time.
     * @param dateTime The date and time to delay the current thread until.
     * @return The result of delaying the current thread.
     */
    public default Result<Void> delayUntil(DateTime dateTime)
    {
        PreCondition.assertNotNull(dateTime, "dateTime");

        return this.scheduleAt(dateTime, Action0.empty);
    }
}
