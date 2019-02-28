package qub;

/**
 * A Console platform that can be used to writeByte Console applications.
 */
public class Process implements Disposable
{
    private final CommandLine commandLine;
    private volatile int exitCode;

    private final Value<CharacterEncoding> characterEncoding;
    private final Value<String> lineSeparator;
    private final Value<Boolean> includeNewLines;

    private final Value<ByteWriteStream> outputWriteStream;
    private final Value<ByteWriteStream> errorWriteStream;

    private final Value<ByteReadStream> byteReadStream;
    private final Value<CharacterReadStream> characterReadStream;
    private final Value<LineReadStream> lineReadStream;

    private final Value<Random> random;
    private final Value<FileSystem> fileSystem;
    private final Value<Network> network;
    private final Value<String> currentFolderPathString;
    private final Value<Map<String,String>> environmentVariables;
    private final Value<Synchronization> synchronization;
    private final Value<Function0<Stopwatch>> stopwatchCreator;
    private final Value<Clock> clock;
    private final Value<Iterable<Display>> displays;

    private final List<Window> windows;

    private final AsyncRunner mainAsyncRunner;
    private volatile AsyncRunner parallelAsyncRunner;

    private volatile boolean disposed;

    /**
     * Create a new Process that Console applications can be written with.
     */
    public Process(String... commandLineArgumentStrings)
    {
        this(CommandLine.create(commandLineArgumentStrings));
    }

    public Process(Iterable<String> commandLineArgumentStrings)
    {
        this(CommandLine.create(commandLineArgumentStrings));
    }

    /**
     * Create a new Process that Console applications can be written with.
     */
    public Process(CommandLine commandLine)
    {
        this(commandLine, new ManualAsyncRunner());
    }

    Process(CommandLine commandLine, AsyncRunner mainAsyncRunner)
    {
        this.commandLine = commandLine;

        characterEncoding = Value.create();
        lineSeparator = Value.create();
        includeNewLines = Value.create();
        outputWriteStream = Value.create();
        errorWriteStream = Value.create();
        byteReadStream = Value.create();
        characterReadStream = Value.create();
        lineReadStream = Value.create();
        random = Value.create();
        fileSystem = Value.create();
        network = Value.create();
        currentFolderPathString = Value.create();
        environmentVariables = Value.create();
        synchronization = Value.create();
        stopwatchCreator = Value.create();
        clock = Value.create();
        displays = Value.create();

        windows = new ArrayList<>();

        this.mainAsyncRunner = mainAsyncRunner;
        mainAsyncRunner.setClockGetter(this::getClock);
        AsyncRunnerRegistry.setCurrentThreadAsyncRunner(mainAsyncRunner);
    }

    /**
     * Get the exit code that this process will return when it finishes.
     * @return The exit code that this process will return when it finishes.
     */
    public int getExitCode()
    {
        return exitCode;
    }

    /**
     * Set the exit code that this process will return when it finishes.
     * @param exitCode The exit code that this process will return when it finishes.
     * @return This Process for method chaining.
     */
    public Process setExitCode(int exitCode)
    {
        this.exitCode = exitCode;
        return this;
    }

    /**
     * Add one to the current exit code.
     * @return This Process for method chaining.
     */
    public Process incrementExitCode()
    {
        return setExitCode(getExitCode() + 1);
    }

    public AsyncRunner getMainAsyncRunner()
    {
        return mainAsyncRunner;
    }

    public AsyncRunner getParallelAsyncRunner()
    {
        if (parallelAsyncRunner == null)
        {
            parallelAsyncRunner = new ParallelAsyncRunner();
            parallelAsyncRunner.setClockGetter(this::getClock);
        }
        return parallelAsyncRunner;
    }

    public Indexable<String> getCommandLineArgumentStrings()
    {
        return commandLine.getArgumentStrings();
    }

    public CommandLine getCommandLine()
    {
        return commandLine;
    }

    public void setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        this.characterEncoding.set(characterEncoding);
    }

    public CharacterEncoding getCharacterEncoding()
    {
        if (!characterEncoding.hasValue())
        {
            characterEncoding.set(CharacterEncoding.US_ASCII);
        }
        return characterEncoding.get();
    }

    public void setLineSeparator(String lineSeparator)
    {
        this.lineSeparator.set(lineSeparator);
    }

    public String getLineSeparator()
    {
        if (!lineSeparator.hasValue())
        {
            lineSeparator.set(onWindows() ? "\r\n" : "\n");
        }
        return lineSeparator.get();
    }

    public void setIncludeNewLines(boolean includeNewLines)
    {
        this.includeNewLines.set(includeNewLines);
    }

    public boolean getIncludeNewLines()
    {
        if (!includeNewLines.hasValue())
        {
            includeNewLines.set(false);
        }
        return includeNewLines.get();
    }

    /**
     * Set the ByteWriteStream that is assigned to this Console's output.
     * @param writeStream The ByteWriteStream that is assigned to this Console's output.
     */
    public void setOutput(ByteWriteStream writeStream)
    {
        this.outputWriteStream.set(writeStream);
    }

    /**
     * Get the ByteWriteStream that is assigned to this Console.
     * @return The ByteWriteStream that is assigned to this Console.
     */
    public ByteWriteStream getOutputAsByteWriteStream()
    {
        if (!outputWriteStream.hasValue())
        {
            outputWriteStream.set(new OutputStreamToByteWriteStream(System.out));
        }
        return outputWriteStream.get();
    }

    public void setOutput(CharacterWriteStream writeStream)
    {
        setOutput(writeStream == null ? null : writeStream.asByteWriteStream());
    }

    public CharacterWriteStream getOutputAsCharacterWriteStream()
    {
        final ByteWriteStream outputByteWriteStream = getOutputAsByteWriteStream();
        return outputByteWriteStream == null ? null : outputByteWriteStream.asCharacterWriteStream(getCharacterEncoding());
    }

    public void setOutput(LineWriteStream writeStream)
    {
        setOutput(writeStream == null ? null : writeStream.asCharacterWriteStream());
    }

    public LineWriteStream getOutputAsLineWriteStream()
    {
        final ByteWriteStream outputByteWriteStream = getOutputAsByteWriteStream();
        return outputByteWriteStream == null ? null : outputByteWriteStream.asLineWriteStream(getCharacterEncoding(), getLineSeparator());
    }

    /**
     * Set the ByteWriteStream that is assigned to this Console's error.
     * @param writeStream The ByteWriteStream that is assigned to this Console's error.
     */
    public void setError(ByteWriteStream writeStream)
    {
        this.errorWriteStream.set(writeStream);
    }

    /**
     * Get the error ByteWriteStream that is assigned to this Console.
     * @return The error ByteWriteStream that is assigned to this Console.
     */
    public ByteWriteStream getErrorAsByteWriteStream()
    {
        if (!errorWriteStream.hasValue())
        {
            errorWriteStream.set(new OutputStreamToByteWriteStream(System.err));
        }
        return errorWriteStream.get();
    }

    public void setError(CharacterWriteStream writeStream)
    {
        setError(writeStream == null ? null : writeStream.asByteWriteStream());
    }

    public CharacterWriteStream getErrorAsCharacterWriteStream()
    {
        final ByteWriteStream errorByteWriteStream = getErrorAsByteWriteStream();
        return errorByteWriteStream == null ? null : errorByteWriteStream.asCharacterWriteStream(getCharacterEncoding());
    }

    public void setError(LineWriteStream writeStream)
    {
        setError(writeStream == null ? null : writeStream.asCharacterWriteStream());
    }

    public LineWriteStream getErrorAsLineWriteStream()
    {
        final ByteWriteStream errorByteWriteStream = getErrorAsByteWriteStream();
        return errorByteWriteStream == null ? null : errorByteWriteStream.asLineWriteStream(getCharacterEncoding(), getLineSeparator());
    }

    /**
     * Set the ByteReadStream that is assigned to this Console.
     * @param readStream The ByteReadStream that is assigned to this Console.
     */
    public void setInput(ByteReadStream readStream)
    {
        setInput(readStream == null ? null : readStream.asLineReadStream(getCharacterEncoding(), getIncludeNewLines()));
    }

    /**
     * Set the CharacterReadStream that is assigned to this Console.
     * @param readStream The CharacterReadStream that is assigned to this Console.
     */
    public void setInput(CharacterReadStream readStream)
    {
        setInput(readStream == null ? null : readStream.asLineReadStream(getIncludeNewLines()));
    }

    /**
     * Set the LineReadStream that is assigned to this Console.
     * @param readStream The LineReadStream that is assigned to this Console.
     */
    public void setInput(LineReadStream readStream)
    {
        lineReadStream.set(readStream);
        characterReadStream.set(readStream == null ? null : readStream.asCharacterReadStream());
        byteReadStream.set(readStream == null ? null : readStream.asByteReadStream());
    }

    /**
     * Get the ByteReadStream that is assigned to this Console.
     * @return The ByteReadStream that is assigned to this Console.
     */
    public ByteReadStream getInputAsByteReadStream()
    {
        if (!byteReadStream.hasValue())
        {
            setInput(new InputStreamToByteReadStream(System.in, getParallelAsyncRunner()));
        }
        return byteReadStream.get();
    }

    /**
     * Get the CharacterReadStream that is assigned to this Console.
     * @return The CharacterReadStream that is assigned to this Console.
     */
    public CharacterReadStream getInputAsCharacterReadStream()
    {
        if (!characterReadStream.hasValue())
        {
            getInputAsByteReadStream();
        }
        return characterReadStream.get();
    }

    /**
     * Get the LineReadStream that is assigned to this Console.
     * @return The LineReadStream that is assigned to this Console.
     */
    public LineReadStream getInputAsLineReadStream()
    {
        if (!lineReadStream.hasValue())
        {
            getInputAsByteReadStream();
        }
        return lineReadStream.get();
    }

    /**
     * Set the Random number generator assigned to this Console.
     * @param random The Random number generator assigned to this Console.
     */
    void setRandom(Random random)
    {
        this.random.set(random);
    }

    /**
     * Get the Random number generator assigned to this Console.
     * @return The Random number generator assigned to this Console.
     */
    public Random getRandom()
    {
        if (!random.hasValue())
        {
            random.set(new JavaRandom());
        }
        return random.get();
    }

    /**
     * Get the FileSystem assigned to this Console.
     * @return The FileSystem assigned to this Console.
     */
    public FileSystem getFileSystem()
    {
        if (!fileSystem.hasValue())
        {
            setFileSystem(new JavaFileSystem(getParallelAsyncRunner()));
        }
        return fileSystem.get();
    }

    /**
     * Set the FileSystem that is assigned to this Console.
     * @param fileSystem The FileSystem that will be assigned to this Console.
     */
    public void setFileSystem(FileSystem fileSystem)
    {
        this.fileSystem.set(fileSystem);
        if (fileSystem == null)
        {
            setCurrentFolderPathString(null);
        }
        else
        {
            currentFolderPathString.clear();
        }
    }

    public void setFileSystem(Function1<AsyncRunner,FileSystem> creator)
    {
        setFileSystem(creator.run(getParallelAsyncRunner()));
    }

    public Network getNetwork()
    {
        if (!network.hasValue())
        {
            setNetwork(new JavaNetwork(getParallelAsyncRunner()));
        }
        return network.get();
    }

    public void setNetwork(Network network)
    {
        this.network.set(network);
    }

    public void setNetwork(Function1<AsyncRunner,Network> creator)
    {
        setNetwork(creator == null ? null : creator.run(getParallelAsyncRunner()));
    }

    public String getCurrentFolderPathString()
    {
        if (!currentFolderPathString.hasValue())
        {
            currentFolderPathString.set(java.nio.file.Paths.get(".").toAbsolutePath().normalize().toString());
        }
        return currentFolderPathString.get();
    }

    public void setCurrentFolderPathString(String currentFolderPathString)
    {
        this.currentFolderPathString.set(currentFolderPathString);
    }

    /**
     * Get the path to the folder that this Console is currently running in.
     * @return The path to the folder that this Console is currently running in.
     */
    public Path getCurrentFolderPath()
    {
        final String currentFolderPathString = getCurrentFolderPathString();
        return Strings.isNullOrEmpty(currentFolderPathString) ? null : Path.parse(currentFolderPathString);
    }

    /**
     * Set the path to the folder that this Console is currently running in.
     * @param currentFolderPath The folder to the path that this Console is currently running in.
     */
    public void setCurrentFolderPath(Path currentFolderPath)
    {
        currentFolderPathString.set(currentFolderPath == null ? null : currentFolderPath.toString());
    }

    public Result<Folder> getCurrentFolder()
    {
        final FileSystem fileSystem = getFileSystem();
        return fileSystem == null
            ? Result.<Folder>error(new IllegalArgumentException("No FileSystem has been set."))
            : fileSystem.getFolder(getCurrentFolderPath());
    }

    /**
     * Set the environment variables that will be used by this Application.
     * @param environmentVariables The environment variables that will be used by this application.
     */
    public Process setEnvironmentVariables(Map<String,String> environmentVariables)
    {
        this.environmentVariables.set(environmentVariables);
        return this;
    }

    /**
     * Get the environment variables for this application.
     * @return The environment variables for this application.
     */
    public Map<String,String> getEnvironmentVariables()
    {
        if (!environmentVariables.hasValue())
        {
            final MutableMap<String,String> envVars = Map.create();
            for (final java.util.Map.Entry<String,String> entry : System.getenv().entrySet())
            {
                envVars.set(entry.getKey(), entry.getValue());
            }
            environmentVariables.set(envVars);
        }
        return environmentVariables.get();
    }

    /**
     * Get the value of the provided environment variable. If no environment variable exists with
     * the provided name, then null will be returned.
     * @param variableName The name of the environment variable.
     * @return The value of the environment variable, or null if the variable doesn't exist.
     */
    public String getEnvironmentVariable(String variableName)
    {
        String result = null;
        if (variableName != null && !variableName.isEmpty())
        {
            final String lowerVariableName = variableName.toLowerCase();
            final Map<String,String> environmentVariables = getEnvironmentVariables();
            final MapEntry<String,String> resultEntry = environmentVariables.first(new Function1<MapEntry<String, String>, Boolean>()
            {
                @Override
                public Boolean run(MapEntry<String, String> entry)
                {
                    return entry.getKey().toLowerCase().equals(lowerVariableName);
                }
            });
            result = resultEntry == null ? null : resultEntry.getValue();
        }
        return result;
    }

    /**
     * Get the Synchronization factory for creating synchronization objects.
     * @return The Synchronization factory for creating synchronization objects.
     */
    public Synchronization getSynchronization()
    {
        if (!synchronization.hasValue())
        {
            synchronization.set(new Synchronization());
        }
        return synchronization.get();
    }

    public void setStopwatchCreator(Function0<Stopwatch> stopwatchCreator)
    {
        this.stopwatchCreator.set(stopwatchCreator);
    }

    public Stopwatch getStopwatch()
    {
        if (!stopwatchCreator.hasValue())
        {
            stopwatchCreator.set(new Function0<Stopwatch>()
            {
                @Override
                public Stopwatch run()
                {
                    return new JavaStopwatch();
                }
            });
        }
        return stopwatchCreator.get() == null ? null : stopwatchCreator.get().run();
    }

    /**
     * Set the Clock object that this Process will use.
     * @param clock The Clock object that this Process will use.
     */
    public void setClock(Clock clock)
    {
        this.clock.set(clock);
    }

    /**
     * Get the Clock object that has been assigned to this Process.
     * @return The Clock object that has been assigned to this Process.
     */
    public Clock getClock()
    {
        if (!clock.hasValue())
        {
            clock.set(new JavaClock(getMainAsyncRunner(), getParallelAsyncRunner()));
        }
        return clock.get();
    }

    /**
     * Set the displays Iterable that this Process will use.
     * @param displays The displays Iterable that this Process will use.
     */
    public void setDisplays(Iterable<Display> displays)
    {
        this.displays.set(displays);
    }

    /**
     * Get the displays that have been assigned to this Process.
     * @return The displays that have been assigned to this Process.
     */
    public Iterable<Display> getDisplays()
    {
        if (!displays.hasValue())
        {
            final java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
            final int dpi = toolkit.getScreenResolution();

            final java.awt.GraphicsEnvironment graphicsEnvironment = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
            final java.awt.GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
            final List<Display> displayList = new ArrayList<>();

            if (graphicsDevices != null)
            {
                for (final java.awt.GraphicsDevice graphicsDevice : graphicsDevices)
                {
                    if (graphicsDevice != null)
                    {
                        final java.awt.GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
                        final java.awt.geom.AffineTransform defaultTransform = graphicsConfiguration.getDefaultTransform();
                        final double horizontalScale = defaultTransform.getScaleX();
                        final double verticalScale = defaultTransform.getScaleY();

                        final java.awt.DisplayMode displayMode = graphicsDevice.getDisplayMode();
                        displayList.add(new Display(displayMode.getWidth(), displayMode.getHeight(), dpi, dpi, horizontalScale, verticalScale));
                    }
                }
            }

            displays.set(displayList);
        }
        return displays.get();
    }

    /**
     * Create a new Window for this application. The Window will not be visible until
     * setVisible() is called.
     * @return The created Window.
     */
    public Window createWindow()
    {
        final Window result = new JavaWindow(getMainAsyncRunner(), getDisplays());
        windows.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get a ProcessBuilder that references the provided executablePath.
     * @param executablePath The path to the executable to run create the returned ProcessBuilder.
     * @return The ProcessBuilder.
     */
    public Result<ProcessBuilder> getProcessBuilder(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        return getProcessBuilder(Path.parse(executablePath));
    }

    private Result<File> getExecutableFile(final Path executablePath, boolean checkExtensions)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        Result<File> result;
        if (checkExtensions)
        {
            final File executableFile = getFileSystem().getFile(executablePath).getValue();
            final Result<Boolean> fileExistsResult = executableFile.exists();

            if (fileExistsResult.hasError())
            {
                result = Result.error(fileExistsResult.getError());
            }
            else if (!fileExistsResult.getValue())
            {
                result = Result.error(new FileNotFoundException(executablePath.toString()));
            }
            else
            {
                result = Result.success(executableFile);
            }
        }
        else
        {
            final Path executablePathWithoutExtension = executablePath.withoutFileExtension();

            final Path folderPath = executablePath.getParent();
            final Folder folder = getFileSystem().getFolder(folderPath).getValue();
            final Result<Iterable<File>> getFilesResult = folder.getFiles();
            if (getFilesResult.hasError())
            {
                result = Result.error(getFilesResult.getError());
            }
            else
            {
                result = null;

                final String[] executableExtensions = new String[] { "", ".exe", ".bat", ".cmd" };
                for (final String executableExtension : executableExtensions)
                {
                    final Path executablePathWithExtension = Strings.isNullOrEmpty(executableExtension)
                        ? executablePathWithoutExtension
                        : executablePathWithoutExtension.concatenate(executableExtension);
                    final File executableFile = getFilesResult.getValue().first((File file) -> executablePathWithExtension.equals(file.getPath()));
                    if (executableFile != null)
                    {
                        result = Result.success(executableFile);
                        break;
                    }
                }

                if (result == null)
                {
                    final File executableFile = getFilesResult.getValue().first((File file) -> executablePathWithoutExtension.equals(file.getPath().withoutFileExtension()));
                    if (executableFile != null)
                    {
                        result = Result.success(executableFile);
                    }
                    else
                    {
                        result = Result.error(new FileNotFoundException(executablePathWithoutExtension.toString()));
                    }
                }
            }
        }
        return result;
    }

    private Result<File> findExecutableFile(Path executablePath, boolean checkExtensions)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        Result<File> result;

        if (executablePath.isRooted())
        {
            result = getExecutableFile(executablePath, checkExtensions);
        }
        else
        {
            final Path currentFolderPath = getCurrentFolderPath();
            final Path currentFolderExecutablePath = currentFolderPath.concatenateSegment(executablePath);
            final Result<File> getExecutableFileResult = getExecutableFile(currentFolderExecutablePath, checkExtensions);

            if (!getExecutableFileResult.hasError())
            {
                result = getExecutableFileResult;
            }
            else
            {
                result = null;

                final String pathEnvironmentVariable = getEnvironmentVariable("path");
                if (!Strings.isNullOrEmpty(pathEnvironmentVariable))
                {
                    final Iterable<String> pathStrings = Iterable.create(pathEnvironmentVariable.split(";"));
                    for (final String pathString : pathStrings)
                    {
                        if (!Strings.isNullOrEmpty(pathString))
                        {
                            final Path path = Path.parse(pathString);
                            final Path resolvedExecutablePath = path.concatenateSegment(executablePath);
                            final Result<File> pathStringExecutableFileResult = getExecutableFile(resolvedExecutablePath, checkExtensions);
                            if (!pathStringExecutableFileResult.hasError())
                            {
                                result = pathStringExecutableFileResult;
                                break;
                            }
                        }
                    }
                }

                if (result == null)
                {
                    result = Result.error(new qub.FileNotFoundException(executablePath));
                }
            }
        }

        return result;
    }

    /**
     * Get a ProcessBuilder that references the provided executablePath.
     * @param executablePath The path to the executable to run create the returned ProcessBuilder.
     * @return The ProcessBuilder.
     */
    public Result<ProcessBuilder> getProcessBuilder(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        Result<File> executableFileResult = findExecutableFile(executablePath, true);
        if (executableFileResult.hasError())
        {
            executableFileResult = findExecutableFile(executablePath, false);
        }

        return !executableFileResult.hasError()
            ? Result.success(new ProcessBuilder(getParallelAsyncRunner(), executableFileResult.getValue()))
            : Result.error(executableFileResult.getError());
    }

    /**
     * Get whether or not this application is running in a Windows environment.
     * @return Whether or not this application is running in a Windows environment.
     */
    public boolean onWindows()
    {
        final String osName = System.getProperty("os.name");
        return osName.toLowerCase().contains("windows");
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (disposed)
        {
            result = Result.success(false);
        }
        else
        {
            disposed = true;

            Throwable error = null;
            for (final Window window : windows)
            {
                final Result<Boolean> windowDisposedResult = window.dispose();
                if (windowDisposedResult.hasError())
                {
                    error = ErrorIterable.create(error, windowDisposedResult.getError());
                }
            }

            if (mainAsyncRunner != null)
            {
                error = ErrorIterable.create(error, mainAsyncRunner.dispose().getError());
            }

            if (parallelAsyncRunner != null)
            {
                error = ErrorIterable.create(error, parallelAsyncRunner.dispose().getError());
            }

            result = error == null ? Result.success(disposed) : Result.error(error);
        }
        return result;
    }
}
