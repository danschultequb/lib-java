package qub;

/**
 * A Clock implementation that uses the real date and time.
 */
public class JavaClock implements Clock
{
    private final AsyncRunner parallelAsyncRunner;

    public JavaClock(AsyncRunner parallelAsyncRunner)
    {
        this.parallelAsyncRunner = parallelAsyncRunner;
    }

    /**
     * Get the current time zone offset.
     * @return The current time zone offset.
     */
    public Duration getCurrentTimeZoneOffset()
    {
        final java.time.OffsetDateTime now = java.time.OffsetDateTime.now();
        final Duration result = JavaClock.getTimeZoneOffset(now);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the time zone offset of the provided date time.
     * @param offsetDateTime The date time to get the time zone offset of.
     * @return The time zone offset of the provided date time.
     */
    private static Duration getTimeZoneOffset(java.time.OffsetDateTime offsetDateTime)
    {
        PreCondition.assertNotNull(offsetDateTime, "offsetDateTime");

        final Duration zoneOffset = Duration.seconds(offsetDateTime.getOffset().getTotalSeconds());

        PostCondition.assertNotNull(zoneOffset, "zoneOffset");

        return zoneOffset;
    }

    @Override
    public DateTime getCurrentDateTime()
    {
        final java.time.OffsetDateTime now = java.time.OffsetDateTime.now();
        final long secondsSinceEpoch = now.toEpochSecond();
        final int nanosecondAdjustment = now.getNano();
        final Duration durationSinceEpoch = Duration.seconds(secondsSinceEpoch).plus(Duration.nanoseconds(nanosecondAdjustment));

        final Duration zoneOffset = JavaClock.getTimeZoneOffset(now);

        final DateTime result = DateTime.createFromDurationSinceEpoch(durationSinceEpoch, zoneOffset);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public Result<Void> scheduleAt(DateTime dateTime, Action0 action)
    {
        PreCondition.assertNotNull(dateTime, "dateTime");
        PreCondition.assertNotNull(action, "action");

        return Result.create(() ->
        {
            if (getCurrentDateTime().lessThan(dateTime))
            {
                parallelAsyncRunner.schedule(() ->
                {
                    while (this.getCurrentDateTime().lessThan(dateTime))
                    {
                    }
                }).await();
            }
            action.run();
        });
    }
}
