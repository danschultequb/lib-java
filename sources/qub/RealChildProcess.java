package qub;

public class RealChildProcess implements ChildProcess
{
    private final java.lang.Process process;
    private final Result<Void> outputResult;
    private final Result<Void> errorResult;
    private Result<Integer> processResult;

    private RealChildProcess(java.lang.Process process, Result<Void> outputResult, Result<Void> errorResult)
    {
        PreCondition.assertNotNull(process, "process");
        PreCondition.assertNotNull(outputResult, "outputResult");
        PreCondition.assertNotNull(errorResult, "errorResult");

        this.process = process;
        this.outputResult = outputResult;
        this.errorResult = errorResult;
    }

    public static RealChildProcess create(java.lang.Process process, Result<Void> outputResult, Result<Void> errorResult)
    {
        return new RealChildProcess(process, outputResult, errorResult);
    }

    @Override
    public int await()
    {
        if (this.processResult == null)
        {
            this.processResult = Result.create(() ->
            {
                try
                {
                    final int exitCode = this.process.waitFor();
                    this.outputResult.await();
                    this.errorResult.await();
                    return exitCode;
                }
                catch (InterruptedException e)
                {
                    throw Exceptions.asRuntime(e);
                }
            });
        }
        return this.processResult.await();
    }

    @Override
    public ProcessState getState()
    {
        return this.process.isAlive()
            ? ProcessState.Running
            : ProcessState.NotRunning;
    }
}
