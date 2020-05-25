package qub;

/**
 * A ProcessFactory that builds and runs fake processes.
 */
public class FakeProcessFactory implements ProcessFactory
{
    private final AsyncRunner asyncRunner;
    private final List<FakeProcessRun> fakeProcessRuns;
    private final Path workingFolderPath;

    /**
     * Create a new FakeProcessFactory.
     */
    public FakeProcessFactory(AsyncRunner asyncRunner, String workingFolderPath)
    {
        this(asyncRunner, Path.parse(workingFolderPath));
    }

    /**
     * Create a new FakeProcessFactory.
     */
    public FakeProcessFactory(AsyncRunner asyncRunner, Path workingFolderPath)
    {
        PreCondition.assertNotNull(asyncRunner, "parallelAsyncRunner");
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");
        PreCondition.assertTrue(workingFolderPath.isRooted(), "workingFolderPath.isRooted()");

        this.asyncRunner = asyncRunner;
        this.fakeProcessRuns = List.create();
        this.workingFolderPath = workingFolderPath;
    }

    /**
     * Create a new FakeProcessBuilderFactory.
     */
    public FakeProcessFactory(AsyncRunner asyncRunner, Folder workingFolder)
    {
        this(asyncRunner, workingFolder.getPath());
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
    public Result<ChildProcess> start(Path executablePath, Iterable<String> arguments, Path workingFolderPath, ByteReadStream redirectedInputStream, Action1<ByteReadStream> redirectedOutputStream, Action1<ByteReadStream> redirectedErrorStream, CharacterWriteStream verbose)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");

        return Result.create(() ->
        {
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

            ChildProcess result;
            if (match == null)
            {
                final String command = ProcessFactory.getCommand(executablePath, arguments, workingFolderPath);
                throw new NotFoundException("No fake process run found for " + Strings.escapeAndQuote(command) + ".");
            }
            else
            {
                final Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer> function = match.getFunction();
                if (function == null)
                {
                    result = BasicChildProcess.create(0);
                }
                else
                {
                    try
                    {
                        final ByteReadStream input = redirectedInputStream != null
                            ? redirectedInputStream
                            : InMemoryByteStream.create().endOfStream();
                        final Result<Void> inputTask = Result.success();

                        final InMemoryByteStream output = InMemoryByteStream.create();
                        final Result<Void> outputTask = redirectedOutputStream != null
                            ? this.asyncRunner.schedule(() -> redirectedOutputStream.run(output))
                            : Result.success();

                        final InMemoryByteStream error = InMemoryByteStream.create();
                        final Result<Void> errorTask = redirectedErrorStream != null
                            ? this.asyncRunner.schedule(() -> redirectedErrorStream.run(error))
                            : Result.success();

                        final Function0<Integer> processFunction = () ->
                        {
                            final int exitCode = function.run(input, output, error);
                            output.endOfStream();
                            error.endOfStream();
                            return exitCode;
                        };

                        result = BasicChildProcess.create(processFunction, inputTask, outputTask, errorTask);
                    }
                    catch (Throwable error)
                    {
                        throw Exceptions.asRuntime(error);
                    }
                }
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    @Override
    public Result<ProcessBuilder> getProcessBuilder(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return Result.success(new BasicProcessBuilder(this, executablePath, this.workingFolderPath));
    }
}
