package qub;

/**
 * A ProcessFactory that builds and runs fake processes.
 */
public class FakeChildProcessRunner implements ChildProcessRunner
{
    private final DesktopProcess process;
    private final List<FakeChildProcessRun> fakeProcessRuns;

    /**
     * Create a new FakeProcessFactory.
     */
    private FakeChildProcessRunner(DesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        this.process = process;
        this.fakeProcessRuns = List.create();
    }

    public static FakeChildProcessRunner create(DesktopProcess process)
    {
        return new FakeChildProcessRunner(process);
    }

    @Override
    public Result<? extends ChildProcess> start(ChildProcessParameters parameters)
    {
        PreCondition.assertNotNull(parameters, "parameters");

        return Result.create(() ->
        {
            Path currentFolderPath = parameters.getWorkingFolderPath();
            if (currentFolderPath == null)
            {
                currentFolderPath = this.process.getCurrentFolderPath();
            }

            final FileSystem fileSystem = this.process.getFileSystem();
            if (!fileSystem.folderExists(currentFolderPath).await())
            {
                throw new FolderNotFoundException(currentFolderPath);
            }

            final Path executablePath = parameters.getExecutablePath();
            final Iterable<String> arguments = parameters.getArguments();

            FakeChildProcessRun partialMatch = null;
            FakeChildProcessRun match = null;
            final int fakeProcessRunCount = this.fakeProcessRuns.getCount();
            for (int i = fakeProcessRunCount - 1; 0 <= i; --i)
            {
                final FakeChildProcessRun fakeProcessRun = this.fakeProcessRuns.get(i);

                if (executablePath.equals(fakeProcessRun.getExecutablePath()) && arguments.equals(fakeProcessRun.getArguments()))
                {
                    final Path runWorkingFolderPath = fakeProcessRun.getWorkingFolderPath();
                    if (currentFolderPath.equals(runWorkingFolderPath))
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
                final String command = ChildProcessRunner.getCommand(executablePath, arguments, currentFolderPath);
                throw new NotFoundException("No fake process run found for " + Strings.escapeAndQuote(command) + ".");
            }
            else
            {
                final Action1<FakeDesktopProcess> action = match.getAction();
                if (action == null)
                {
                    result.setProcessResult(0);
                }
                else
                {
                    final AsyncRunner parallelAsyncRunner = this.process.getParallelAsyncRunner();
                    final FakeDesktopProcess childProcess = FakeDesktopProcess.create(arguments)
                        .setChildProcessRunner(this.process::getChildProcessRunner)
                        .setClock(this.process::getClock)
                        .setCurrentFolderPath(currentFolderPath)
                        .setDefaultApplicationLauncher(this.process::getDefaultApplicationLauncher)
                        .setDisplays(this.process::getDisplays)
                        .setEnvironmentVariables(this.process::getEnvironmentVariables)
                        .setFileSystem(fileSystem)
                        .setNetwork(this.process::getNetwork)
                        .setRandom(this.process::getRandom)
                        .setSynchronization(this.process::getSynchronization)
                        .setSystemProperties(this.process::getSystemProperties)
                        .setTypeLoader(this.process::getTypeLoader);

                    final ByteReadStream inputStream = parameters.getInputStream();
                    if (inputStream != null)
                    {
                        childProcess.setInputReadStream(() ->
                        {
                            final InMemoryCharacterToByteStream childProcessInput = InMemoryCharacterToByteStream.create();
                            parallelAsyncRunner.schedule(() ->
                            {
                                childProcessInput.writeAll(inputStream).await();
                                childProcessInput.endOfStream();
                            });
                            return childProcessInput;
                        });

                    }

                    final Action1<ByteReadStream> outputStreamHandler = parameters.getOutputStreamHandler();
                    final InMemoryCharacterToByteStream childProcessOutput = childProcess.getOutputWriteStream();
                    if (outputStreamHandler != null)
                    {
                        result.setOutputResult(parallelAsyncRunner.schedule(() -> outputStreamHandler.run(childProcessOutput)));
                    }

                    final Action1<ByteReadStream> errorStreamHandler = parameters.getErrorStreamHandler();
                    final InMemoryCharacterToByteStream childProcessError = childProcess.getErrorWriteStream();
                    if (errorStreamHandler != null)
                    {
                        result.setErrorResult(parallelAsyncRunner.schedule(() -> errorStreamHandler.run(childProcessError)));
                    }

                    result.setProcessResult(parallelAsyncRunner.schedule(() ->
                    {
                        try
                        {
                            action.run(childProcess);
                        }
                        finally
                        {
                            childProcessOutput.endOfStream();
                            childProcessError.endOfStream();
                            result.setState(ProcessState.NotRunning);
                        }
                        return childProcess.getExitCode();
                    }));
                }
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
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
    public FakeChildProcessRunner add(FakeChildProcessRun fakeProcessRun)
    {
        PreCondition.assertNotNull(fakeProcessRun, "fakeProcessRun");

        this.fakeProcessRuns.add(fakeProcessRun);

        return this;
    }
}
