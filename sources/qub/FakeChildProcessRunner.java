package qub;

/**
 * A ProcessFactory that builds and runs fake processes.
 */
public class FakeChildProcessRunner implements ChildProcessRunner
{
    private final AsyncRunner parallelAsyncRunner;
    private final Folder currentFolder;
    private final List<FakeChildProcessRun> fakeProcessRuns;

    /**
     * Create a new FakeProcessFactory.
     */
    private FakeChildProcessRunner(AsyncRunner parallelAsyncRunner, Folder currentFolder)
    {
        PreCondition.assertNotNull(parallelAsyncRunner, "parallelAsyncRunner");
        PreCondition.assertNotNull(currentFolder, "currentFolder");

        this.parallelAsyncRunner = parallelAsyncRunner;
        this.currentFolder = currentFolder;
        this.fakeProcessRuns = List.create();
    }

    public static FakeChildProcessRunner create(AsyncRunner parallelAsyncRunner, Folder currentFolder)
    {
        return new FakeChildProcessRunner(parallelAsyncRunner, currentFolder);
    }

    public static FakeChildProcessRunner create(DesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        final AsyncRunner parallelAsyncRunner = process.getParallelAsyncRunner();
        final Folder currentFolder = process.getCurrentFolder();
        return FakeChildProcessRunner.create(parallelAsyncRunner, currentFolder);
    }

    @Override
    public Result<? extends ChildProcess> start(ChildProcessParameters parameters)
    {
        PreCondition.assertNotNull(parameters, "parameters");

        return Result.create(() ->
        {
            Path workingFolderPath = parameters.getWorkingFolderPath();
            if (workingFolderPath == null)
            {
                workingFolderPath = this.currentFolder.getPath();
            }

            final FileSystem fileSystem = this.currentFolder.getFileSystem();
            if (!fileSystem.folderExists(workingFolderPath).await())
            {
                throw new FolderNotFoundException(workingFolderPath);
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
                final String command = ChildProcessRunner.getCommand(executablePath, arguments, workingFolderPath);
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
                    final ByteReadStream inputStream = parameters.getInputStream();
                    final Action1<ByteReadStream> outputStreamHandler = parameters.getOutputStreamHandler();
                    final Action1<ByteReadStream> errorStreamHandler = parameters.getErrorStreamHandler();
                    try
                    {
                        final ByteReadStream input = inputStream != null
                            ? inputStream
                            : InMemoryByteStream.create().endOfStream();

                        final InMemoryByteStream output = InMemoryByteStream.create();
                        if (outputStreamHandler != null)
                        {
                            result.setOutputResult(this.parallelAsyncRunner.schedule(() -> outputStreamHandler.run(output)));
                        }

                        final InMemoryByteStream error = InMemoryByteStream.create();
                        if (errorStreamHandler != null)
                        {
                            result.setErrorResult(this.parallelAsyncRunner.schedule(() -> errorStreamHandler.run(error)));
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
