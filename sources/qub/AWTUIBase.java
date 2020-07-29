package qub;

public class AWTUIBase extends UIBase
{
    private final AsyncScheduler parallelAsyncRunner;

    protected AWTUIBase(Display display, AsyncRunner mainAsyncRunner, AsyncScheduler parallelAsyncRunner)
    {
        super(display, mainAsyncRunner);

        PreCondition.assertNotNull(parallelAsyncRunner, "parallelAsyncRunner");

        this.parallelAsyncRunner = parallelAsyncRunner;
    }

    public static AWTUIBase create(Display display, AsyncRunner mainAsyncRunner, AsyncScheduler parallelAsyncRunner)
    {
        return new AWTUIBase(display, mainAsyncRunner, parallelAsyncRunner);
    }

    public static AWTUIBase create(Process process)
    {
        return AWTUIBase.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner());
    }

    /**
     * This method should be called from the AWT UI Event Dispatcher thread to register the
     * parallel async runner with that thread.
     */
    public void registerUIEventThread()
    {
        final AsyncScheduler asyncRunner = CurrentThread.getAsyncRunner()
            .catchError(NotFoundException.class)
            .await();
        if (asyncRunner == null)
        {
            CurrentThread.setAsyncRunner(this.parallelAsyncRunner);
        }
    }
}
