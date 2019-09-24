package qub;

/**
 * A ProcessFactory that builds and runs fake processes.
 */
public class FakeProcessFactory implements ProcessFactory
{
    private final List<FakeProcessRun> fakeProcessRuns;
    private final Path workingFolderPath;

    /**
     * Create a new FakeProcessFactory.
     */
    public FakeProcessFactory(String workingFolderPath)
    {
        this(Path.parse(workingFolderPath));
    }

    /**
     * Create a new FakeProcessFactory.
     */
    public FakeProcessFactory(Path workingFolderPath)
    {
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");
        PreCondition.assertTrue(workingFolderPath.isRooted(), "workingFolderPath.isRooted()");

        this.fakeProcessRuns = List.create();
        this.workingFolderPath = workingFolderPath;
    }

    /**
     * Create a new FakeProcessBuilderFactory.
     */
    public FakeProcessFactory(Folder workingFolder)
    {
        this(workingFolder.getPath());
    }

    /**
     * Get the path to the folder that executables will be "run" from.
     * @return The path to the folder that executables will be "run" from.
     */
    @Override
    public Path getWorkingFolderPath()
    {
        return this.workingFolderPath;
    }

    /**
     * Add the provided FakeProcessRun to this FakeProcessBuilderFactory. When a ProcessBuilder that
     * was created by this FakeProcessBuilderFactory is run, the list of added FakeProcessRuns will
     * be searched instead of running the actual executable file. If a matching FakeProcessRun is
     * found, then its result will be performed. If no matching FakeProcessRun is found, then an
     * error will be returned.
     * @param fakeProcessRun The FakeProcessRun to add.
     * @return This object for method chaining.
     */
    public FakeProcessFactory add(FakeProcessRun fakeProcessRun)
    {
        PreCondition.assertNotNull(fakeProcessRun, "fakeProcessRun");

        this.fakeProcessRuns.add(fakeProcessRun);

        return this;
    }

    @Override
    public Result<Integer> run(Path executablePath, Iterable<String> arguments, Path workingFolderPath, ByteReadStream redirectedInputStream, Action1<ByteReadStream> redirectedOutputStream, Action1<ByteReadStream> redirectedErrorStream)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");

        FakeProcessRun partialMatch = null;
        FakeProcessRun match = null;
        final int fakeProcessRunCount = this.fakeProcessRuns.getCount();
        for (int i = fakeProcessRunCount - 1; 0 <= i; --i)
        {
            final FakeProcessRun fakeProcessRun = this.fakeProcessRuns.get(i);

            if (executablePath.equals(fakeProcessRun.getExecutablePath()) && arguments.equals(fakeProcessRun.getArguments()))
            {
                final Path runWorkingFolderPath = fakeProcessRun.getWorkingFolderPath();
                if (workingFolderPath.equals(runWorkingFolderPath))
                {
                    match = fakeProcessRun;
                    break;
                }
                else if (runWorkingFolderPath == null && partialMatch == null)
                {
                    partialMatch = fakeProcessRun;
                }
            }
        }

        if (match == null && partialMatch != null)
        {
            match = partialMatch;
        }

        Result<Integer> result;
        if (match != null)
        {
            result = Result.success(match.getExitCode());
        }
        else
        {
            final String command = ProcessFactory.getCommand(executablePath, arguments, workingFolderPath);
            result = Result.error(new NotFoundException("No fake process run found for " + Strings.escapeAndQuote(command) + "."));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public Result<ProcessBuilder> getProcessBuilder(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return Result.success(new ProcessBuilder(this, executablePath, this.workingFolderPath));
    }
}