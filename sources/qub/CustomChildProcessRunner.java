package qub;

/**
 * A {@link ChildProcessRunner} that can have custom behavior assigned at runtime.
 */
public class CustomChildProcessRunner extends ChildProcessRunnerDecorator
{
    private Action0 beforeChildProcessStarted;

    protected CustomChildProcessRunner(ChildProcessRunner innerRunner)
    {
        super(innerRunner);
    }

    public static CustomChildProcessRunner create(ChildProcessRunner innerRunner)
    {
        return new CustomChildProcessRunner(innerRunner);
    }

    /**
     * Set the {@link Action0} that will be invoked before a child process is started.
     * @param beforeChildProcessStarted The {@link Action0} that will be invoked before a child
     *                                  process is started.
     * @return This object for method chaining.
     */
    public CustomChildProcessRunner setBeforeChildProcessStarted(Action0 beforeChildProcessStarted)
    {
        this.beforeChildProcessStarted = beforeChildProcessStarted;

        return this;
    }

    @Override
    public Result<? extends ChildProcess> start(ChildProcessParameters parameters)
    {
        return Result.create(() ->
        {
            if (this.beforeChildProcessStarted != null)
            {
                this.beforeChildProcessStarted.run();
            }

            return super.start(parameters).await();
        });
    }
}
