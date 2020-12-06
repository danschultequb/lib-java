package qub;

/**
 * A type that provides functions for get the current time and for scheduling tasks to run in the
 * future.
 */
public interface Clock
{
    /**
     * Get the current time zone offset.
     * @return The current time zone offset.
     */
    default Duration getCurrentOffset()
    {
        return this.getCurrentDateTime().getOffset();
    }

    /**
     * Get a DateTime object for the current date and time.
     * @return A DateTime object for the current date and time.
     */
    DateTime getCurrentDateTime();

    /**
     * Run the provided action on the main AsyncRunner after the provided date and time.
     * @param dateTime The date and time that the provided action will be run after.
     * @param action The action to run after the date and time.
     * @return The AsyncAction associated with the scheduled action.
     */
    Result<Void> scheduleAt(DateTime dateTime, Action0 action);

    /**
     * Run the provided action on the main AsyncRunner after the provided duration of time.
     * @param duration The duration to wait before scheduling the provided action.
     * @param action The action to run after the duration.
     * @return The AsyncAction associated with the scheduled action.
     */
    default Result<Void> scheduleAfter(Duration duration, Action0 action)
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
    default Result<Void> delay(Duration duration)
    {
        PreCondition.assertNotNull(duration, "duration");
        PreCondition.assertGreaterThanOrEqualTo(duration, Duration.zero, "duration");

        return this.scheduleAfter(duration, Action0.empty);
    }

    /**
     * Create a Stopwatch object based on this Clock.
     * @return The Stopwatch object based on this Clock.
     */
    @Deprecated
    default Stopwatch2 createStopwatch()
    {
        return Stopwatch2.create(this);
    }

    /**
     * Create a Stopwatch object based on this Clock.
     * @return The Stopwatch object based on this Clock.
     */
    default Stopwatch3 createStopwatch3()
    {
        return Stopwatch3.create(this);
    }
}
