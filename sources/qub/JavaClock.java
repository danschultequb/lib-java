package qub;

/**
 * A Clock implementation that uses the real date and time.
 */
public class JavaClock implements Clock
{
    private final AsyncRunner mainAsyncRunner;
    private final AsyncRunner parallelAsyncRunner;

    public JavaClock(AsyncRunner mainAsyncRunner, AsyncRunner parallelAsyncRunner)
    {
        this.mainAsyncRunner = mainAsyncRunner;
        this.parallelAsyncRunner = parallelAsyncRunner;
    }

    @Override
    public DateTime getCurrentDateTime()
    {
        return DateTime.local(java.util.Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public AsyncAction scheduleAt(final DateTime dateTime, final Action0 action)
    {
        PreCondition.assertNotNull(dateTime, "dateTime");
        PreCondition.assertNotNull(action, "action");

        return parallelAsyncRunner.schedule(() ->
            {
                while (getCurrentDateTime().lessThan(dateTime))
                {
                }
            })
            .thenOn(mainAsyncRunner, action);
    }
}
