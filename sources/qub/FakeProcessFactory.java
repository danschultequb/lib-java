package qub;

/**
 * A ProcessFactory that builds and runs fake processes.
 */
public class FakeProcessFactory implements ProcessFactory
{
    private final AsyncRunner parallelAsyncRunner;
    private final List<FakeProcessRun> fakeProcessRuns;
    private final Folder workingFolder;

    /**
     * Create a new FakeProcessFactory.
     */
    private FakeProcessFactory(AsyncRunner parallelAsyncRunner, Folder workingFolder)
    {
        PreCondition.assertNotNull(parallelAsyncRunner, "parallelAsyncRunner");
        PreCondition.assertNotNull(workingFolder, "workingFolder");

        this.parallelAsyncRunner = parallelAsyncRunner;
        this.fakeProcessRuns = List.create();
        this.workingFolder = workingFolder;
    }

    public static FakeProcessFactory create(FakeDesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        return FakeProcessFactory.create(process.getParallelAsyncRunner(), process.getCurrentFolder());
    }

    public static FakeProcessFactory create(AsyncRunner parallelAsyncRunner, Folder workingFolder)
    {
        return new FakeProcessFactory(parallelAsyncRunner, workingFolder);
    }

    private static Path getWorkingFolderPath(Folder workingFolder)
    {
        PreCondition.assertNotNull(workingFolder, "workingFolder");

        return workingFolder.getPath();
    }

    /**
     * Get the path to the folder that executables will be "run" from.
     * @return The path to the folder that executables will be "run" from.
     */
    @Override
    public Path getWorkingFolderPath()
    {
        return this.workingFolder.getPath();
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
    public Result<FakeChildProcess> start(Path executablePath, Iterable<String> arguments, Path workingFolderPath, ByteReadStream redirectedInputStream, Action1<ByteReadStream> redirectedOutputStream, Action1<ByteReadStream> redirectedErrorStream, CharacterWriteStream verbose)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");

        return Result.create(() ->
        {
            final FileSystem fileSystem = this.workingFolder.getFileSystem();
            if (!fileSystem.folderExists(workingFolderPath).await())
            {
                throw new FolderNotFoundException(workingFolderPath);
            }

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

            final FakeChildProcess result = FakeChildProcess.create()
                .setState(ProcessState.Running);

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
                    result.setProcessResult(0);
                }
                else
                {
                    try
                    {
                        final ByteReadStream input = redirectedInputStream != null
                            ? redirectedInputStream
                            : InMemoryByteStream.create().endOfStream();

                        final InMemoryByteStream output = InMemoryByteStream.create();
                        if (redirectedOutputStream != null)
                        {
                            result.setOutputResult(this.parallelAsyncRunner.schedule(() -> redirectedOutputStream.run(output)));
                        }

                        final InMemoryByteStream error = InMemoryByteStream.create();
                        if (redirectedErrorStream != null)
                        {
                            result.setErrorResult(this.parallelAsyncRunner.schedule(() -> redirectedErrorStream.run(error)));
                        }

                        result.setProcessResult(this.parallelAsyncRunner.schedule(() ->
                        {
                            final int exitCode = function.run(input, output, error);
                            output.endOfStream();
                            error.endOfStream();
                            result.setState(ProcessState.NotRunning);
                            return exitCode;
                        }));
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

        return Result.success(new BasicProcessBuilder(this, executablePath, this.workingFolder.getPath()));
    }
}
