package qub;

/**
 * A {@link Clock} implementation that uses the real date and time.
 */
public class JavaClock implements Clock
{
    private final AsyncRunner parallelAsyncRunner;

    private JavaClock(AsyncRunner parallelAsyncRunner)
    {
        PreCondition.assertNotNull(parallelAsyncRunner, "parallelAsyncRunner");

        this.parallelAsyncRunner = parallelAsyncRunner;
    }

    public static JavaClock create(AsyncRunner parallelAsyncRunner)
    {
        return new JavaClock(parallelAsyncRunner);
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

        return CurrentThread.getAsyncRunner().await()
            .schedule(() ->
            {
                if (this.getCurrentDateTime().lessThan(dateTime))
                {
                    this.parallelAsyncRunner.schedule(() ->
                    {
                        while (this.getCurrentDateTime().lessThan(dateTime))
                        {
                            CurrentThread.yield();
                        }
                    }).await();
                }
                action.run();
            });
    }
}
