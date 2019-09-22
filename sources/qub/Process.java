package qub;

/**
 * A Console platform that can be used to writeByte Console applications.
 */
public class Process implements Disposable
{
    private final CommandLineArguments commandLineArguments;
    private volatile int exitCode;

    private final Value<ByteWriteStream> outputByteWriteStream;
    private final Value<ByteWriteStream> errorByteWriteStream;
    private final Value<ByteReadStream> inputByteReadStream;
    private final Value<CharacterWriteStream> outputCharacterWriteStream;
    private final Value<CharacterWriteStream> errorCharacterWriteStream;
    private final Value<CharacterReadStream> inputCharacterReadStream;
    private final Value<CharacterEncoding> characterEncoding;
    private final Value<String> lineSeparator;

    private final Value<Random> random;
    private final Value<FileSystem> fileSystem;
    private final Value<Network> network;
    private final Value<String> currentFolderPathString;
    private final Value<EnvironmentVariables> environmentVariables;
    private final Value<Synchronization> synchronization;
    private final Value<Function0<Stopwatch>> stopwatchCreator;
    private final Value<Clock> clock;
    private final Value<Iterable<Display>> displays;
    private final Value<ProcessRunner> processRunner;

    private final AsyncScheduler mainAsyncRunner;
    private final AsyncScheduler parallelAsyncRunner;

    private volatile boolean disposed;

    /**
     * Create a new Process that Console applications can be written with.
     */
    public Process(String... commandLineArgumentStrings)
    {
        this(CommandLineArguments.create(commandLineArgumentStrings));
    }

    public Process(Iterable<String> commandLineArgumentStrings)
    {
        this(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new Process that Console applications can be written with.
     */
    public Process(CommandLineArguments commandLineArguments)
    {
        this(commandLineArguments, new ManualAsyncRunner());
    }

    Process(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        this.commandLineArguments = commandLineArguments;

        outputByteWriteStream = Value.create();
        errorByteWriteStream = Value.create();
        inputByteReadStream = Value.create();
        outputCharacterWriteStream = Value.create();
        errorCharacterWriteStream = Value.create();
        inputCharacterReadStream = Value.create();
        characterEncoding = Value.create();
        lineSeparator = Value.create();

        random = Value.create();
        fileSystem = Value.create();
        network = Value.create();
        currentFolderPathString = Value.create();
        environmentVariables = Value.create();
        synchronization = Value.create();
        stopwatchCreator = Value.create();
        clock = Value.create();
        displays = Value.create();
        processRunner = Value.create();

        this.mainAsyncRunner = mainAsyncRunner;
        CurrentThread.setAsyncRunner(mainAsyncRunner);

        this.parallelAsyncRunner = new ParallelAsyncRunner();
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

    public AsyncScheduler getMainAsyncRunner()
    {
        return mainAsyncRunner;
    }

    public AsyncScheduler getParallelAsyncRunner()
    {
        return parallelAsyncRunner;
    }

    public CommandLineArguments getCommandLineArguments()
    {
        return commandLineArguments;
    }

    public CommandLineParameters createCommandLineParameters()
    {
        return this.createCommandLineParameters(null);
    }

    public CommandLineParameters createCommandLineParameters(String applicationName)
    {
        return new CommandLineParameters(applicationName)
            .setArguments(getCommandLineArguments());
    }

    /**
     * Get the ByteWriteStream that is assigned to this Console.
     * @return The ByteWriteStream that is assigned to this Console.
     */
    public ByteWriteStream getOutputByteWriteStream()
    {
        if (!outputByteWriteStream.hasValue())
        {
            outputByteWriteStream.set(new OutputStreamToByteWriteStream(System.out));
        }
        return outputByteWriteStream.get();
    }

    /**
     * Get the error ByteWriteStream that is assigned to this Console.
     * @return The error ByteWriteStream that is assigned to this Console.
     */
    public ByteWriteStream getErrorByteWriteStream()
    {
        if (!errorByteWriteStream.hasValue())
        {
            errorByteWriteStream.set(new OutputStreamToByteWriteStream(System.err));
        }
        return errorByteWriteStream.get();
    }

    /**
     * Get the ByteReadStream that is assigned to this Console.
     * @return The ByteReadStream that is assigned to this Console.
     */
    public ByteReadStream getInputByteReadStream()
    {
        if (!inputByteReadStream.hasValue())
        {
            setInputByteReadStream(new InputStreamToByteReadStream(System.in));
        }
        return inputByteReadStream.get();
    }

    public CharacterReadStream getInputCharacterReadStream()
    {
        if (!inputCharacterReadStream.hasValue())
        {
            final ByteReadStream inputByteReadStream = getInputByteReadStream();
            final CharacterEncoding characterEncoding = getCharacterEncoding();
            inputCharacterReadStream.set(inputByteReadStream.asCharacterReadStream(characterEncoding));
        }
        return inputCharacterReadStream.get();
    }

    public CharacterWriteStream getOutputCharacterWriteStream()
    {
        if (!outputCharacterWriteStream.hasValue())
        {
            final ByteWriteStream outputByteWriteStream = getOutputByteWriteStream();
            final CharacterEncoding characterEncoding = getCharacterEncoding();
            final String lineSeparator = getLineSeparator();
            outputCharacterWriteStream.set(outputByteWriteStream.asCharacterWriteStream(characterEncoding, lineSeparator));
        }
        return outputCharacterWriteStream.get();
    }

    public CharacterWriteStream getErrorCharacterWriteStream()
    {
        if (!errorCharacterWriteStream.hasValue())
        {
            final ByteWriteStream errorByteWriteStream = getErrorByteWriteStream();
            final CharacterEncoding characterEncoding = getCharacterEncoding();
            final String lineSeparator = getLineSeparator();
            errorCharacterWriteStream.set(errorByteWriteStream.asCharacterWriteStream(characterEncoding, lineSeparator));
        }
        return errorCharacterWriteStream.get();
    }

    public Process setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        this.characterEncoding.set(characterEncoding);
        outputCharacterWriteStream.clear();
        errorCharacterWriteStream.clear();
        inputCharacterReadStream.clear();
        return this;
    }

    public CharacterEncoding getCharacterEncoding()
    {
        if (!characterEncoding.hasValue())
        {
            characterEncoding.set(CharacterEncoding.UTF_8);
        }
        return characterEncoding.get();
    }

    public Process setLineSeparator(String lineSeparator)
    {
        this.lineSeparator.set(lineSeparator);
        outputCharacterWriteStream.clear();
        errorCharacterWriteStream.clear();
        inputCharacterReadStream.clear();
        return this;
    }

    public String getLineSeparator()
    {
        if (!lineSeparator.hasValue())
        {
            lineSeparator.set(onWindows() ? "\r\n" : "\n");
        }
        return lineSeparator.get();
    }

    /**
     * Set the ByteWriteStream that is assigned to this Console's output.
     * @param outputByteWriteStream The ByteWriteStream that is assigned to this Console's output.
     * @return This object for method chaining.
     */
    public Process setOutputByteWriteStream(ByteWriteStream outputByteWriteStream)
    {
        this.outputByteWriteStream.set(outputByteWriteStream);
        outputCharacterWriteStream.clear();
        return this;
    }

    /**
     * Set the CharacterWriteStream that is assigned to this Console's output.
     * @param outputCharacterWriteStream The CharacterWriteStream that is assigned to this Console's
     *                                   output.
     * @return This object for method chaining.
     */
    public Process setOutputCharacterWriteStream(CharacterWriteStream outputCharacterWriteStream)
    {
        this.outputByteWriteStream.clear();
        this.outputCharacterWriteStream.set(outputCharacterWriteStream);
        return this;
    }

    /**
     * Set the ByteWriteStream that is assigned to this Console's error.
     * @param errorByteWriteStream The ByteWriteStream that is assigned to this Console's error.
     * @return This object for method chaining.
     */
    public Process setErrorByteWriteStream(ByteWriteStream errorByteWriteStream)
    {
        this.errorByteWriteStream.set(errorByteWriteStream);
        errorCharacterWriteStream.clear();
        return this;
    }

    /**
     * Set the CharacterWriteStream that is assigned to this Console's error.
     * @param errorCharacterWriteStream The CharacterWriteStream that is assigned to this Console's
     *                                  error.
     * @return This object for method chaining.
     */
    public Process setErrorCharacterWriteStream(CharacterWriteStream errorCharacterWriteStream)
    {
        this.errorByteWriteStream.clear();
        this.errorCharacterWriteStream.set(errorCharacterWriteStream);
        return this;
    }

    /**
     * Set the ByteReadStream that is assigned to this Console's input.
     * @param inputByteReadStream The ByteReadStream that is assigned to this Console's input.
     * @return This object for method chaining.
     */
    public Process setInputByteReadStream(ByteReadStream inputByteReadStream)
    {
        this.inputByteReadStream.set(inputByteReadStream);
        inputCharacterReadStream.clear();
        return this;
    }

    /**
     * Set the CharacterReadStream that is assigned to this Console's input.
     * @param inputCharacterReadStream The CharacterReadStream that is assigned to this Console's
     *                                 input.
     * @return This object for method chaining.
     */
    public Process setInputCharacterReadStream(CharacterReadStream inputCharacterReadStream)
    {
        this.inputByteReadStream.set(null);
        this.inputCharacterReadStream.set(inputCharacterReadStream);
        return this;
    }

    /**
     * Set the Random number generator assigned to this Console.
     * @param random The Random number generator assigned to this Console.
     */
    public Process setRandom(Random random)
    {
        this.random.set(random);
        return this;
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
            setFileSystem(new JavaFileSystem());
        }
        return fileSystem.get();
    }

    /**
     * Set the FileSystem that is assigned to this Console.
     * @param fileSystem The FileSystem that will be assigned to this Console.
     */
    public Process setFileSystem(FileSystem fileSystem)
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
        return this;
    }

    public Network getNetwork()
    {
        if (!network.hasValue())
        {
            setNetwork(new JavaNetwork(getClock()));
        }
        return network.get();
    }

    public Process setNetwork(Network network)
    {
        this.network.set(network);
        return this;
    }

    public String getCurrentFolderPathString()
    {
        if (!currentFolderPathString.hasValue())
        {
            currentFolderPathString.set(java.nio.file.Paths.get(".").toAbsolutePath().normalize().toString());
        }
        return currentFolderPathString.get();
    }

    public Process setCurrentFolderPathString(String currentFolderPathString)
    {
        this.currentFolderPathString.set(currentFolderPathString);
        return this;
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
    public Process setCurrentFolderPath(Path currentFolderPath)
    {
        currentFolderPathString.set(currentFolderPath == null ? null : currentFolderPath.toString());
        return this;
    }

    public Result<Folder> getCurrentFolder()
    {
        final FileSystem fileSystem = getFileSystem();
        return fileSystem == null
            ? Result.error(new IllegalArgumentException("No FileSystem has been set."))
            : fileSystem.getFolder(getCurrentFolderPath());
    }

    /**
     * Set the environment variables that will be used by this Application.
     * @param environmentVariables The environment variables that will be used by this application.
     */
    public Process setEnvironmentVariables(EnvironmentVariables environmentVariables)
    {
        this.environmentVariables.set(environmentVariables);
        return this;
    }

    /**
     * Get the environment variables for this application.
     * @return The environment variables for this application.
     */
    public EnvironmentVariables getEnvironmentVariables()
    {
        if (!environmentVariables.hasValue())
        {
            final EnvironmentVariables environmentVariables = new EnvironmentVariables();
            for (final java.util.Map.Entry<String,String> entry : System.getenv().entrySet())
            {
                environmentVariables.set(entry.getKey(), entry.getValue());
            }
            this.environmentVariables.set(environmentVariables);
        }
        return environmentVariables.get();
    }

    /**
     * Get the value of the provided environment variable.
     * @param variableName The name of the environment variable.
     */
    public Result<String> getEnvironmentVariable(String variableName)
    {
        PreCondition.assertNotNullAndNotEmpty(variableName, "variableName");

        return this.getEnvironmentVariables().get(variableName);
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

    public Process setStopwatchCreator(Function0<Stopwatch> stopwatchCreator)
    {
        this.stopwatchCreator.set(stopwatchCreator);
        return this;
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
    public Process setClock(Clock clock)
    {
        this.clock.set(clock);
        return this;
    }

    /**
     * Get the Clock object that has been assigned to this Process.
     * @return The Clock object that has been assigned to this Process.
     */
    public Clock getClock()
    {
        if (!clock.hasValue())
        {
            clock.set(new JavaClock(getParallelAsyncRunner()));
        }
        return clock.get();
    }

    /**
     * Set the displays Iterable that this Process will use.
     * @param displays The displays Iterable that this Process will use.
     */
    public Process setDisplays(Iterable<Display> displays)
    {
        this.displays.set(displays);
        return this;
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
     * Set the ProcessRunner that will be used to start external processes.
     * @param processRunner The ProcessRunner that will be used to start external processes.
     * @return This object for method chaining.
     */
    public Process setProcessRunner(ProcessRunner processRunner)
    {
        PreCondition.assertNotNull(processRunner, "processRunner");

        this.processRunner.set(processRunner);

        return this;
    }

    /**
     * Get the ProcessRunner that can be used to start external processes.
     * @return The ProcessRunner that can be used to start external processes.
     */
    public ProcessRunner getProcessRunner()
    {
        if (!processRunner.hasValue())
        {
            processRunner.set(new RealProcessRunner(getParallelAsyncRunner()));
        }
        return processRunner.get();
    }

    /**
     * Get the ProcessBuilder for the provided executable path.
     * @param executablePath The path to the executable.
     * @return The ProcessBuilder for the provided executable path.
     */
    public Result<ProcessBuilder> getProcessBuilder(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        final Result<ProcessBuilder> result = this.getProcessBuilder(Path.parse(executablePath));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the ProcessBuilder for the provided executable path.
     * @param executablePath The path to the executable.
     * @return The ProcessBuilder for the provided executable path.
     */
    public Result<ProcessBuilder> getProcessBuilder(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return Result.create(() ->
        {
            final File executableFile = findExecutableFile(executablePath, true)
                .catchError(FileNotFoundException.class, () -> findExecutableFile(executablePath, false).await())
                .catchError(FileNotFoundException.class)
                .await();
            return new ProcessBuilder(this.getProcessRunner(), executableFile, getCurrentFolder().await());
        });
    }


    public Result<File> getExecutableFile(final Path executableFilePath, boolean checkExtensions)
    {
        PreCondition.assertNotNull(executableFilePath, "executableFilePath");
        PreCondition.assertTrue(executableFilePath.isRooted(), "executableFilePath.isRooted()");

        return Result.create(() ->
        {
            File result = null;
            final FileSystem fileSystem = this.getFileSystem();
            if (checkExtensions)
            {
                if (fileSystem.fileExists(executableFilePath).await())
                {
                    result = fileSystem.getFile(executableFilePath).await();
                }
                else
                {
                    throw new FileNotFoundException(executableFilePath);
                }
            }
            else
            {
                final Path executablePathWithoutExtension = executableFilePath.withoutFileExtension();

                final Path folderPath = executableFilePath.getParent().await();
                final Folder folder = fileSystem.getFolder(folderPath).await();
                final Iterable<File> files = folder.getFiles().await();

                final String[] executableExtensions = new String[] { "", ".exe", ".bat", ".cmd" };
                for (final String executableExtension : executableExtensions)
                {
                    final Path executablePathWithExtension = Strings.isNullOrEmpty(executableExtension)
                        ? executablePathWithoutExtension
                        : executablePathWithoutExtension.concatenate(executableExtension);
                    result = files.first((File file) -> executablePathWithExtension.equals(file.getPath()));
                    if (result != null)
                    {
                        break;
                    }
                }

                if (result == null)
                {
                    result = files.first((File file) -> executablePathWithoutExtension.equals(file.getPath().withoutFileExtension()));
                    if (result == null)
                    {
                        throw new FileNotFoundException(executablePathWithoutExtension);
                    }
                }
            }
            return result;
        });
    }

    /**
     * Find the file to execute based on the provided executablePath. If the executablePath is not
     * rooted, then this function will check if a file exists by resolving the executablePath
     * against the current folder. If no file exists from that resolved path, then this function
     * will resolve the executablePath against each of the segments in the PATH environment
     * variable.
     * @param executablePath The path to the executable to run.
     * @param checkExtensions Whether or not file extensions should be considered when comparing
     *                        files. If this is true, the executablePath "program.exe" would not
     *                        match "program.cmd", but it would match if checkExtensions is false.
     * @return The file to execute.
     */
    public Result<File> findExecutableFile(Path executablePath, boolean checkExtensions)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return Result.create(() ->
        {
            File result;

            if (executablePath.isRooted())
            {
                result = this.getExecutableFile(executablePath, checkExtensions).await();
            }
            else
            {
                final Path currentFolderPath = this.getCurrentFolder().await().getPath();
                final Path currentFolderExecutablePath = currentFolderPath.concatenateSegment(executablePath);

                result = getExecutableFile(currentFolderExecutablePath, checkExtensions)
                    .catchError(FileNotFoundException.class)
                    .await();

                if (result == null)
                {
                    final String pathEnvironmentVariable = this.getEnvironmentVariable("path").await();
                    if (!Strings.isNullOrEmpty(pathEnvironmentVariable))
                    {
                        final Iterable<String> pathStrings = Iterable.create(pathEnvironmentVariable.split(";"));
                        for (final String pathString : pathStrings)
                        {
                            if (!Strings.isNullOrEmpty(pathString))
                            {
                                final Path path = Path.parse(pathString);
                                final Path resolvedExecutablePath = path.concatenateSegment(executablePath);
                                result = getExecutableFile(resolvedExecutablePath, checkExtensions).catchError().await();
                                if (result != null)
                                {
                                    break;
                                }
                            }
                        }
                    }

                    if (result == null)
                    {
                        throw new qub.FileNotFoundException(executablePath);
                    }
                }
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
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
            result = Result.successFalse();
        }
        else
        {
            disposed = true;
            result = Result.successTrue();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
