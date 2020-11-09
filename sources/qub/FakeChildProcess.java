package qub;

public class FakeChildProcess implements ChildProcess
{
    private Result<Integer> processResult;
    private Result<?> outputResult;
    private Result<?> errorResult;
    private ProcessState state;

    private FakeChildProcess()
    {
    }

    public static FakeChildProcess create()
    {
        return new FakeChildProcess();
    }

    public FakeChildProcess setProcessResult(Result<Integer> processResult)
    {
        PreCondition.assertNotNull(processResult, "processResult");

        this.processResult = processResult;

        return this;
    }

    public FakeChildProcess setProcessResult(int processResult)
    {
        return this.setProcessResult(Result.success(processResult));
    }

    public FakeChildProcess setOutputResult(Result<?> outputResult)
    {
        this.outputResult = outputResult;
        return this;
    }

    public FakeChildProcess setErrorResult(Result<?> errorResult)
    {
        this.errorResult = errorResult;
        return this;
    }

    @Override
    public int await()
    {
        final int exitCode = this.processResult.await();
        if (this.outputResult != null)
        {
            this.outputResult.await();
        }
        if (this.errorResult != null)
        {
            this.errorResult.await();
        }
        this.setState(ProcessState.NotRunning);
        return exitCode;
    }

    @Override
    public ProcessState getState()
    {
        return this.state;
    }

    public FakeChildProcess setState(ProcessState state)
    {
        this.state = state;

        return this;
    }
}
