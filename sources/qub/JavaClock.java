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

    @Override
    public DateTime getCurrentDateTime()
    {
        final java.time.OffsetDateTime now = java.time.OffsetDateTime.now();
        final long millisecondsSinceEpoch = now.toInstant().toEpochMilli();
        final Duration durationSinceEpoch = Duration.milliseconds(millisecondsSinceEpoch);

        final Duration zoneOffset = Duration.seconds(now.getOffset().getTotalSeconds());

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
