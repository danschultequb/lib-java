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
    private final Value<ProcessFactory> processFactory;
    private final Value<DefaultApplicationLauncher> defaultApplicationLauncher;

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
        processFactory = Value.create();
        this.defaultApplicationLauncher = Value.create();

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

    /**
     * Get the CommandLineArguments that were passed on the command line.
     * @return The CommandLineArguments that were passed on the command line.
     */
    public CommandLineArguments getCommandLineArguments()
    {
        return commandLineArguments;
    }

    /**
     * Create a CommandLineParameters object that can be used to create CommandLineParameter
     * objects. These CommandLineParameter objects can parse the CommandLineArguments that are
     * passed on the command line.
     * @return A new CommandLineParameters object.
     */
    public CommandLineParameters createCommandLineParameters()
    {
        return new CommandLineParameters()
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
            lineSeparator.set(this.onWindows().await() ? "\r\n" : "\n");
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
     * Run the provided action and then write to output how long the action took.
     * @param action The action to run.
     */
    public void showDuration(Action0 action)
    {
        this.showDuration(true, action);
    }

    /**
     * Run the provided action and then write to output how long the action took.
     * @param shouldShowDuration Whether or not the duration will be written.
     * @param action The action to run.
     */
    public void showDuration(boolean shouldShowDuration, Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        Stopwatch stopwatch = null;
        if (shouldShowDuration)
        {
            stopwatch = this.getStopwatch();
            stopwatch.start();
        }
        try
        {
            action.run();
        }
        finally
        {
            if (shouldShowDuration)
            {
                final Duration compilationDuration = stopwatch.stop().toSeconds();
                final CharacterWriteStream output = this.getOutputCharacterWriteStream();
                output.writeLine("Done (" + compilationDuration.toString("0.0") + ")").await();
            }
        }
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
     * Get the object that can be used to invoke external processes.
     * @return The object that can be used to invoke external processes.
     */
    public ProcessFactory getProcessFactory()
    {
        if (!this.processFactory.hasValue())
        {
            final AsyncRunner parallelAsyncRunner = this.getParallelAsyncRunner();
            final EnvironmentVariables environmentVariables = this.getEnvironmentVariables();
            final Folder currentFolder = this.getCurrentFolder().await();
            this.processFactory.set(new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder));
        }
        return this.processFactory.get();
    }

    /**
     * Set the object that can be used to invoke external processes.
     * @param processFactory The object that can be used to invoke external processes.
     * @return This object for method chaining.
     */
    public Process setProcessFactory(ProcessFactory processFactory)
    {
        PreCondition.assertNotNull(processFactory, "processFactory");

        this.processFactory.set(processFactory);

        return this;
    }

    /**
     * Get the ProcessBuilder for the provided executable path.
     * @param executablePath The path to the executable.
     * @return The ProcessBuilder for the provided executable path.
     */
    public Result<ProcessBuilder> getProcessBuilder(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        return this.getProcessFactory().getProcessBuilder(executablePath);
    }

    /**
     * Get the ProcessBuilder for the provided executable path.
     * @param executablePath The path to the executable.
     * @return The ProcessBuilder for the provided executable path.
     */
    public Result<ProcessBuilder> getProcessBuilder(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return this.getProcessFactory().getProcessBuilder(executablePath);
    }

    /**
     * Get the ProcessBuilder for the provided executable file.
     * @param executableFile The file to executable.
     * @return The ProcessBuilder for the provided executable file.
     */
    public Result<ProcessBuilder> getProcessBuilder(File executableFile)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");

        return this.getProcessFactory().getProcessBuilder(executableFile);
    }

    /**
     * Get the DefaultApplicationLauncher that will be used to open files with their registered
     * default application.
     * @return The DefaultApplicationLauncher that will be used to open files with their registered
     * default application.
     */
    public DefaultApplicationLauncher getDefaultApplicationLauncher()
    {
        if (!this.defaultApplicationLauncher.hasValue())
        {
            this.defaultApplicationLauncher.set(new RealDefaultApplicationLauncher());
        }
        return this.defaultApplicationLauncher.get();
    }

    /**
     * Set the DefaultApplicationLauncher that will be used to open files with their registered
     * default application.
     * @param defaultApplicationLauncher The DefaultApplicationLauncher that will be used to open files with their
     *                                   registered default application.
     * @return This object for method chaining.
     */
    public Process setDefaultApplicationLauncher(DefaultApplicationLauncher defaultApplicationLauncher)
    {
        PreCondition.assertNotNull(defaultApplicationLauncher, "defaultApplicationLauncher");

        this.defaultApplicationLauncher.set(defaultApplicationLauncher);

        return this;
    }

    /**
     * Open the provided file path with the registered default application.
     * @param filePathToOpen The file path to open.
     * @return The result of opening the file path.
     */
    public Result<Void> openFileWithDefaultApplication(String filePathToOpen)
    {
        PreCondition.assertNotNullAndNotEmpty(filePathToOpen, "file");

        final DefaultApplicationLauncher launcher = this.getDefaultApplicationLauncher();
        return launcher.openFileWithDefaultApplication(filePathToOpen);
    }

    /**
     * Open the provided file path with the registered default application.
     * @param filePathToOpen The file path to open.
     * @return The result of opening the file path.
     */
    public Result<Void> openFileWithDefaultApplication(Path filePathToOpen)
    {
        PreCondition.assertNotNull(filePathToOpen, "file");

        final DefaultApplicationLauncher launcher = this.getDefaultApplicationLauncher();
        return launcher.openFileWithDefaultApplication(filePathToOpen);
    }

    /**
     * Open the provided file with the registered default application.
     * @param file The file to open.
     * @return The result of opening the file.
     */
    public Result<Void> openFileWithDefaultApplication(File file)
    {
        PreCondition.assertNotNull(file, "file");

        final DefaultApplicationLauncher launcher = this.getDefaultApplicationLauncher();
        return launcher.openFileWithDefaultApplication(file);
    }

    /**
     * Get the System property with the provided name.
     * @param systemPropertyName The name of the System property to get.
     * @return The value of the System property.
     */
    public Result<String> getSystemProperty(String systemPropertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(systemPropertyName, "systemPropertyName");

        final String systemPropertyValue = java.lang.System.getProperty(systemPropertyName);
        return systemPropertyValue == null
            ? Result.error(new NotFoundException("No system property found with the name " + Strings.escapeAndQuote(systemPropertyName) + "."))
            : Result.success(systemPropertyValue);
    }

    /**
     * Get whether or not this application is running in a Windows environment.
     * @return Whether or not this application is running in a Windows environment.
     */
    public Result<Boolean> onWindows()
    {
        return Result.create(() ->
        {
            final String osName = this.getSystemProperty("os.name").await();
            return osName.toLowerCase().contains("windows");
        });
    }

    /**
     * Get the classpath that was provided to this application's JVM when it was started.
     * @return The classpath that was provided to this application's JVM when it was started.
     */
    public Result<String> getJVMClasspath()
    {
        return this.getSystemProperty("java.class.path");
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
