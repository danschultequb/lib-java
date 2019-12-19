package qub;

/**
 * A Process object that exposes the platform functionality that a Java application can use.
 */
public interface IProcess extends Disposable
{
    /**
     * Create a new IProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new IProcess.
     */
    static IProcess create(String... commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return IProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new IProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new IProcess.
     */
    static IProcess create(Iterable<String> commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return IProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new IProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new IProcess.
     */
    static IProcess create(CommandLineArguments commandLineArguments)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        return IProcess.create(commandLineArguments, new ManualAsyncRunner());
    }

    /**
     * Create a new IProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new IProcess.
     */
    static IProcess create(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        return JavaProcess.create(commandLineArguments, mainAsyncRunner);
    }

    /**
     * Get the exit code that this process will return when it finishes.
     * @return The exit code that this process will return when it finishes.
     */
    int getExitCode();

    /**
     * Set the exit code that this process will return when it finishes.
     * @param exitCode The exit code that this process will return when it finishes.
     * @return This IProcess for method chaining.
     */
    IProcess setExitCode(int exitCode);

    /**
     * Add one to the current exit code.
     * @return This IProcess for method chaining.
     */
    IProcess incrementExitCode();

    AsyncScheduler getMainAsyncRunner();

    AsyncScheduler getParallelAsyncRunner();

    /**
     * Get the CommandLineArguments that were passed on the command line.
     * @return The CommandLineArguments that were passed on the command line.
     */
    CommandLineArguments getCommandLineArguments();

    /**
     * Create a CommandLineParameters object that can be used to create CommandLineParameter
     * objects. These CommandLineParameter objects can parse the CommandLineArguments that are
     * passed on the command line.
     * @return A new CommandLineParameters object.
     */
    default CommandLineParameters createCommandLineParameters()
    {
        return new CommandLineParameters()
            .setArguments(getCommandLineArguments());
    }

    /**
     * Get the ByteWriteStream that is assigned to this Console.
     * @return The ByteWriteStream that is assigned to this Console.
     */
    ByteWriteStream getOutputByteWriteStream();

    /**
     * Get the error ByteWriteStream that is assigned to this Console.
     * @return The error ByteWriteStream that is assigned to this Console.
     */
    ByteWriteStream getErrorByteWriteStream();

    /**
     * Get the ByteReadStream that is assigned to this Console.
     * @return The ByteReadStream that is assigned to this Console.
     */
    ByteReadStream getInputByteReadStream();

    CharacterReadStream getInputCharacterReadStream();

    CharacterWriteStream getOutputCharacterWriteStream();

    CharacterWriteStream getErrorCharacterWriteStream();

    IProcess setCharacterEncoding(CharacterEncoding characterEncoding);

    CharacterEncoding getCharacterEncoding();

    IProcess setLineSeparator(String lineSeparator);

    String getLineSeparator();

    /**
     * Set the ByteWriteStream that is assigned to this Console's output.
     * @param outputByteWriteStream The ByteWriteStream that is assigned to this Console's output.
     * @return This object for method chaining.
     */
    IProcess setOutputByteWriteStream(ByteWriteStream outputByteWriteStream);

    /**
     * Set the CharacterWriteStream that is assigned to this Console's output.
     * @param outputCharacterWriteStream The CharacterWriteStream that is assigned to this Console's
     *                                   output.
     * @return This object for method chaining.
     */
    IProcess setOutputCharacterWriteStream(CharacterWriteStream outputCharacterWriteStream);

    /**
     * Set the ByteWriteStream that is assigned to this Console's error.
     * @param errorByteWriteStream The ByteWriteStream that is assigned to this Console's error.
     * @return This object for method chaining.
     */
    IProcess setErrorByteWriteStream(ByteWriteStream errorByteWriteStream);

    /**
     * Set the CharacterWriteStream that is assigned to this Console's error.
     * @param errorCharacterWriteStream The CharacterWriteStream that is assigned to this Console's
     *                                  error.
     * @return This object for method chaining.
     */
    IProcess setErrorCharacterWriteStream(CharacterWriteStream errorCharacterWriteStream);

    /**
     * Set the ByteReadStream that is assigned to this Console's input.
     * @param inputByteReadStream The ByteReadStream that is assigned to this Console's input.
     * @return This object for method chaining.
     */
    IProcess setInputByteReadStream(ByteReadStream inputByteReadStream);

    /**
     * Set the CharacterReadStream that is assigned to this Console's input.
     * @param inputCharacterReadStream The CharacterReadStream that is assigned to this Console's
     *                                 input.
     * @return This object for method chaining.
     */
    IProcess setInputCharacterReadStream(CharacterReadStream inputCharacterReadStream);

    /**
     * Set the Random number generator assigned to this Console.
     * @param random The Random number generator assigned to this Console.
     */
    IProcess setRandom(Random random);

    /**
     * Get the Random number generator assigned to this Console.
     * @return The Random number generator assigned to this Console.
     */
    Random getRandom();

    /**
     * Get the FileSystem assigned to this Console.
     * @return The FileSystem assigned to this Console.
     */
    FileSystem getFileSystem();

    /**
     * Set the FileSystem that is assigned to this Console.
     * @param fileSystem The FileSystem that will be assigned to this Console.
     */
    IProcess setFileSystem(FileSystem fileSystem);

    Network getNetwork();

    IProcess setNetwork(Network network);

    String getCurrentFolderPathString();

    IProcess setCurrentFolderPathString(String currentFolderPathString);

    /**
     * Get the path to the folder that this Console is currently running in.
     * @return The path to the folder that this Console is currently running in.
     */
    default Path getCurrentFolderPath()
    {
        final String currentFolderPathString = this.getCurrentFolderPathString();
        return Strings.isNullOrEmpty(currentFolderPathString) ? null : Path.parse(currentFolderPathString);
    }

    /**
     * Set the path to the folder that this Console is currently running in.
     * @param currentFolderPath The folder to the path that this Console is currently running in.
     */
    default IProcess setCurrentFolderPath(Path currentFolderPath)
    {
        return this.setCurrentFolderPathString(currentFolderPath == null ? null : currentFolderPath.toString());
    }

    default Result<Folder> getCurrentFolder()
    {
        final FileSystem fileSystem = this.getFileSystem();
        return fileSystem == null
            ? Result.error(new IllegalArgumentException("No FileSystem has been set."))
            : fileSystem.getFolder(getCurrentFolderPath());
    }

    /**
     * Set the environment variables that will be used by this Application.
     * @param environmentVariables The environment variables that will be used by this application.
     */
    IProcess setEnvironmentVariables(EnvironmentVariables environmentVariables);

    /**
     * Get the environment variables for this application.
     * @return The environment variables for this application.
     */
    EnvironmentVariables getEnvironmentVariables();

    /**
     * Get the value of the provided environment variable.
     * @param variableName The name of the environment variable.
     */
    default Result<String> getEnvironmentVariable(String variableName)
    {
        PreCondition.assertNotNullAndNotEmpty(variableName, "variableName");

        return this.getEnvironmentVariables().get(variableName);
    }

    /**
     * Get the Synchronization factory for creating synchronization objects.
     * @return The Synchronization factory for creating synchronization objects.
     */
    Synchronization getSynchronization();

    IProcess setStopwatchCreator(Function0<Stopwatch> stopwatchCreator);

    Stopwatch getStopwatch();

    /**
     * Run the provided action and then write to output how long the action took.
     * @param action The action to run.
     */
    default void showDuration(Action0 action)
    {
        this.showDuration(true, action);
    }

    /**
     * Run the provided action and then write to output how long the action took.
     * @param shouldShowDuration Whether or not the duration will be written.
     * @param action The action to run.
     */
    default void showDuration(boolean shouldShowDuration, Action0 action)
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
     * Set the Clock object that this IProcess will use.
     * @param clock The Clock object that this IProcess will use.
     */
    IProcess setClock(Clock clock);

    /**
     * Get the Clock object that has been assigned to this Process.
     * @return The Clock object that has been assigned to this Process.
     */
    Clock getClock();

    /**
     * Set the displays Iterable that this IProcess will use.
     * @param displays The displays Iterable that this IProcess will use.
     */
    IProcess setDisplays(Iterable<Display> displays);

    /**
     * Get the displays that have been assigned to this Process.
     * @return The displays that have been assigned to this Process.
     */
    Iterable<Display> getDisplays();

    /**
     * Get the object that can be used to invoke external processes.
     * @return The object that can be used to invoke external processes.
     */
    ProcessFactory getProcessFactory();

    /**
     * Set the object that can be used to invoke external processes.
     * @param processFactory The object that can be used to invoke external processes.
     * @return This object for method chaining.
     */
    IProcess setProcessFactory(ProcessFactory processFactory);

    /**
     * Get the ProcessBuilder for the provided executable path.
     * @param executablePath The path to the executable.
     * @return The ProcessBuilder for the provided executable path.
     */
    default Result<ProcessBuilder> getProcessBuilder(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        return this.getProcessFactory().getProcessBuilder(executablePath);
    }

    /**
     * Get the ProcessBuilder for the provided executable path.
     * @param executablePath The path to the executable.
     * @return The ProcessBuilder for the provided executable path.
     */
    default Result<ProcessBuilder> getProcessBuilder(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return this.getProcessFactory().getProcessBuilder(executablePath);
    }

    /**
     * Get the ProcessBuilder for the provided executable file.
     * @param executableFile The file to executable.
     * @return The ProcessBuilder for the provided executable file.
     */
    default Result<ProcessBuilder> getProcessBuilder(File executableFile)
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
    DefaultApplicationLauncher getDefaultApplicationLauncher();

    /**
     * Set the DefaultApplicationLauncher that will be used to open files with their registered
     * default application.
     * @param defaultApplicationLauncher The DefaultApplicationLauncher that will be used to open files with their
     *                                   registered default application.
     * @return This object for method chaining.
     */
    IProcess setDefaultApplicationLauncher(DefaultApplicationLauncher defaultApplicationLauncher);

    /**
     * Open the provided file path with the registered default application.
     * @param filePathToOpen The file path to open.
     * @return The result of opening the file path.
     */
    default Result<Void> openFileWithDefaultApplication(String filePathToOpen)
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
    default Result<Void> openFileWithDefaultApplication(Path filePathToOpen)
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
    default Result<Void> openFileWithDefaultApplication(File file)
    {
        PreCondition.assertNotNull(file, "file");

        final DefaultApplicationLauncher launcher = this.getDefaultApplicationLauncher();
        return launcher.openFileWithDefaultApplication(file);
    }

    IProcess setSystemProperty(String systemPropertyName, String systemPropertyValue);

    /**
     * Get the System property with the provided name.
     * @param systemPropertyName The name of the System property to get.
     * @return The value of the System property.
     */
    Result<String> getSystemProperty(String systemPropertyName);

    /**
     * Get whether or not this application is running in a Windows environment.
     * @return Whether or not this application is running in a Windows environment.
     */
    default Result<Boolean> onWindows()
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
    default Result<String> getJVMClasspath()
    {
        return this.getSystemProperty("java.class.path");
    }

    /**
     * Set the java.class.path system property. This will not change the classpath of the running
     * JVM, but rather will only modify the system property.
     * @return This object for method chaining.
     */
    default IProcess setJVMClasspath(String jvmClasspath)
    {
        PreCondition.assertNotNull(jvmClasspath, "jvmClasspath");

        return this.setSystemProperty("java.class.path", jvmClasspath);
    }
}
