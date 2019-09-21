package qub;

/**
 * A ProcessRunner implementation that doesn't invoke external processes.
 */
public class FakeProcessRunner implements ProcessRunner
{
    private final List<FakeProcessRun> fakeProcessRuns;

    /**
     * Create a new FakeProcessRunner.
     */
    public FakeProcessRunner()
    {
        this.fakeProcessRuns = List.create();
    }

    /**
     * Add the provided fake process run to this runner so that when a matching process run is
     * requested, the provided fake process run will be returned instead.
     * @param fakeProcessRun The fake process run to add.
     * @return This object for method chaining.
     */
    public FakeProcessRunner add(FakeProcessRun fakeProcessRun)
    {
        PreCondition.assertNotNull(fakeProcessRun, "fakeProcessRun");

        this.fakeProcessRuns.add(fakeProcessRun);

        return this;
    }

    @Override
    public Result<Integer> run(File executableFile, Iterable<String> arguments, Folder workingFolder, ByteReadStream redirectedInputStream, Action1<ByteReadStream> redirectedOutputStream, Action1<ByteReadStream> redirectedErrorStream)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(workingFolder, "workingFolder");

        Result<Integer> result;

        final FakeProcessRun matchingRun = this.fakeProcessRuns.first((FakeProcessRun run) -> run.matches(executableFile, arguments, workingFolder));
        if (matchingRun == null)
        {
            result = Result.error(new NotFoundException("No fake process run found for " + Strings.escapeAndQuote(ProcessRunner.getCommand(executableFile, arguments, workingFolder)) + "."));
        }
        else
        {
            result = Result.success(matchingRun.getExitCode());
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
