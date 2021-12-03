package qub;

public abstract class ChildProcessRunnerDecorator implements ChildProcessRunner
{
    private final ChildProcessRunner innerRunner;

    protected ChildProcessRunnerDecorator(ChildProcessRunner innerRunner)
    {
        PreCondition.assertNotNull(innerRunner, "innerRunner");

        this.innerRunner = innerRunner;
    }

    @Override
    public Result<? extends ChildProcess> start(ChildProcessParameters parameters)
    {
        return this.innerRunner.start(parameters);
    }
}
