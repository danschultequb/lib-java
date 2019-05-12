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
        return DateTime.local(java.util.Calendar.getInstance().getTimeInMillis());
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
                    while (getCurrentDateTime().lessThan(dateTime))
                    {
                    }
                }).await();
            }
            action.run();
        });
    }
}
