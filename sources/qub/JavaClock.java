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
        return DateTime.localNow();
    }

    @Override
    public AsyncAction scheduleAt(final DateTime dateTime, final Action0 action)
    {
        if (dateTime == null)
        {
            throw new NullPointerException();
        }
        return parallelAsyncRunner.schedule(new Action0()
            {
                @Override
                public void run()
                {
                    while (getCurrentDateTime().lessThan(dateTime))
                    {
                    }
                }
            })
            .thenOn(mainAsyncRunner, action);
    }
}
