package qub;

public class BasicChildProcess implements ChildProcess
{
    private final Function0<Integer> processFunction;
    private final Result<?> inputResult;
    private final Result<?> outputResult;
    private final Result<?> errorResult;

    private BasicChildProcess(Function0<Integer> processFunction, Result<?> inputResult, Result<?> outputResult, Result<?> errorResult)
    {
        PreCondition.assertNotNull(processFunction, "processFunction");
        PreCondition.assertNotNull(inputResult, "inputResult");
        PreCondition.assertNotNull(outputResult, "outputResult");
        PreCondition.assertNotNull(errorResult, "errorResult");

        this.processFunction = processFunction;
        this.inputResult = inputResult;
        this.outputResult = outputResult;
        this.errorResult = errorResult;
    }

    public static BasicChildProcess create(Function0<Integer> processFunction, Result<?> inputResult, Result<?> outputResult, Result<?> errorResult)
    {
        return new BasicChildProcess(processFunction, inputResult, outputResult, errorResult);
    }

    public static BasicChildProcess create(int exitCode)
    {
        return BasicChildProcess.create(() -> exitCode, Result.success(), Result.success(), Result.success());
    }

    @Override
    public int await()
    {
        final int exitCode = processFunction.run();
        inputResult.await();
        outputResult.await();
        errorResult.await();
        return exitCode;
    }
}
