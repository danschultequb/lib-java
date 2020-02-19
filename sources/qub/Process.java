package qub;

/**
 * A Process object that exposes the platform functionality that a Java application can use.
 */
public interface Process extends Disposable
{
    /**
     * Create a new Process object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new Process.
     */
    static Process create(String... commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return Process.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new Process object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new Process.
     */
    static Process create(Iterable<String> commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return Process.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new Process object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new Process.
     */
    static Process create(CommandLineArguments commandLineArguments)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        return Process.create(commandLineArguments, new ManualAsyncRunner());
    }

    /**
     * Create a new Process object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new Process.
     */
    static Process create(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        return JavaProcess.create(commandLineArguments, mainAsyncRunner);
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param args The String arguments provided.
     * @param main The main function that will be run.
     */
    static void run(String[] args, Action1<Process> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        Process.run(() -> Process.create(args), main);
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param main The main function that will be run.
     */
    static <TProcess extends Process> void run(Function0<TProcess> processCreator, Action1<TProcess> main)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(main, "main");

        final TProcess process = processCreator.run();
        try
        {
            main.run(process);
        }
        catch (Throwable error)
        {
            Exceptions.writeErrorString(process.getErrorCharacterWriteStream(), error).await();
            process.setExitCode(1);
        }
        finally
        {
            process.dispose().await();
            java.lang.System.exit(process.getExitCode());
        }
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param args The String arguments provided.
     * @param main The main function that will be run.
     */
    static void run(String[] args, Function1<Process,Integer> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        Process.run(() -> Process.create(args), Process.getMainAction(main));
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param main The main function that will be run.
     */
    static <TProcess extends Process> void run(Function0<TProcess> processCreator, Function1<TProcess,Integer> main)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(main, "main");

        Process.run(processCreator, Process.getMainAction(main));
    }

    static <TProcess extends Process> Action1<TProcess> getMainAction(Function1<TProcess,Integer> runFunction)
    {
        PreCondition.assertNotNull(runFunction, "runFunction");

        return (TProcess process) ->
        {
            process.setExitCode(runFunction.run(process));
        };
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param arguments The command line arguments to the application.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runAction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    static <TParameters> void run(String[] arguments, Function1<Process,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        Process.run(arguments, Process.getMainAction(getParametersFunction, runAction));
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runAction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    static <TProcess extends Process,TParameters> void run(Function0<TProcess> processCreator, Function1<TProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        Process.run(processCreator, Process.getMainAction(getParametersFunction, runAction));
    }

    static <TProcess extends Process,TParameters> Action1<TProcess> getMainAction(Function1<TProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        return (TProcess process) ->
        {
            final TParameters parameters = getParametersFunction.run(process);
            if (parameters != null)
            {
                process.showDuration(() ->
                {
                    runAction.run(parameters);
                });
            }
        };
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param arguments The command line arguments to the application.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runFunction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    static <TParameters> void run(String[] arguments, Function1<Process,TParameters> getParametersFunction, Function1<TParameters,Integer> runFunction)
    {
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runFunction, "runFunction");

        Process.run(arguments, Process.getMainAction(getParametersFunction, runFunction));
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runFunction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    static <TProcess extends Process,TParameters> void run(Function0<TProcess> processCreator, Function1<TProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runFunction)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runFunction, "runFunction");

        Process.run(processCreator, Process.getMainAction(getParametersFunction, runFunction));
    }

    static <TProcess extends Process,TParameters> Action1<TProcess> getMainAction(Function1<TProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runAction)
    {
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        return (TProcess process) ->
        {
            final TParameters parameters = getParametersFunction.run(process);
            if (parameters != null)
            {
                process.showDuration(() ->
                {
                    process.setExitCode(runAction.run(parameters));
                });
            }
        };
    }

    /**
     * Get the exit code that this process will return when it finishes.
     * @return The exit code that this process will return when it finishes.
     */
    int getExitCode();

    /**
     * Set the exit code that this process will return when it finishes.
     * @param exitCode The exit code that this process will return when it finishes.
     * @return This Process for method chaining.
     */
    Process setExitCode(int exitCode);

    /**
     * Add one to the current exit code.
     * @return This Process for method chaining.
     */
    Process incrementExitCode();

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
            .setArguments(this.getCommandLineArguments());
    }

    /**
     * Create a CommandLineActions object that can be used to create CommandLineAction objects. The
     * first positional argument will be used as the action (--action) parameter to determine which
     * action should be invoked.
     * @return A new CommandLineActions object.
     */
    default <TProcess extends Process> CommandLineActions<TProcess> createCommandLineActions()
    {
        return CommandLineActions.create();
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

    Process setCharacterEncoding(CharacterEncoding characterEncoding);

    CharacterEncoding getCharacterEncoding();

    Process setLineSeparator(String lineSeparator);

    String getLineSeparator();

    /**
     * Set the ByteWriteStream that is assigned to this Console's output.
     * @param outputByteWriteStream The ByteWriteStream that is assigned to this Console's output.
     * @return This object for method chaining.
     */
    Process setOutputByteWriteStream(ByteWriteStream outputByteWriteStream);

    /**
     * Set the CharacterWriteStream that is assigned to this Console's output.
     * @param outputCharacterWriteStream The CharacterWriteStream that is assigned to this Console's
     *                                   output.
     * @return This object for method chaining.
     */
    Process setOutputCharacterWriteStream(CharacterWriteStream outputCharacterWriteStream);

    /**
     * Set the ByteWriteStream that is assigned to this Console's error.
     * @param errorByteWriteStream The ByteWriteStream that is assigned to this Console's error.
     * @return This object for method chaining.
     */
    Process setErrorByteWriteStream(ByteWriteStream errorByteWriteStream);

    /**
     * Set the CharacterWriteStream that is assigned to this Console's error.
     * @param errorCharacterWriteStream The CharacterWriteStream that is assigned to this Console's
     *                                  error.
     * @return This object for method chaining.
     */
    Process setErrorCharacterWriteStream(CharacterWriteStream errorCharacterWriteStream);

    /**
     * Set the ByteReadStream that is assigned to this Console's input.
     * @param inputByteReadStream The ByteReadStream that is assigned to this Console's input.
     * @return This object for method chaining.
     */
    Process setInputByteReadStream(ByteReadStream inputByteReadStream);

    /**
     * Set the CharacterReadStream that is assigned to this Console's input.
     * @param inputCharacterReadStream The CharacterReadStream that is assigned to this Console's
     *                                 input.
     * @return This object for method chaining.
     */
    Process setInputCharacterReadStream(CharacterReadStream inputCharacterReadStream);

    /**
     * Set the Random number generator assigned to this Console.
     * @param random The Random number generator assigned to this Console.
     */
    Process setRandom(Random random);

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
    Process setFileSystem(FileSystem fileSystem);

    Network getNetwork();

    Process setNetwork(Network network);

    String getCurrentFolderPathString();

    Process setCurrentFolderPathString(String currentFolderPathString);

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
    default Process setCurrentFolderPath(Path currentFolderPath)
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
    Process setEnvironmentVariables(EnvironmentVariables environmentVariables);

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

    Process setStopwatchCreator(Function0<Stopwatch> stopwatchCreator);

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
     * Set the Clock object that this Process will use.
     * @param clock The Clock object that this Process will use.
     */
    Process setClock(Clock clock);

    /**
     * Get the Clock object that has been assigned to this Process.
     * @return The Clock object that has been assigned to this Process.
     */
    Clock getClock();

    /**
     * Set the displays Iterable that this Process will use.
     * @param displays The displays Iterable that this Process will use.
     */
    Process setDisplays(Iterable<Display> displays);

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
    Process setProcessFactory(ProcessFactory processFactory);

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
    Process setDefaultApplicationLauncher(DefaultApplicationLauncher defaultApplicationLauncher);

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

    Process setSystemProperty(String systemPropertyName, String systemPropertyValue);

    /**
     * Get the system properties of this process.
     * @return The system properties of this process.
     */
    Map<String,String> getSystemProperties();

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
    default Process setJVMClasspath(String jvmClasspath)
    {
        PreCondition.assertNotNull(jvmClasspath, "jvmClasspath");

        return this.setSystemProperty("java.class.path", jvmClasspath);
    }

    default String getMainClassFullName()
    {
        final String javaApplicationArguments = this.getSystemProperty("sun.java.command").await();
        final int firstSpaceIndex = javaApplicationArguments.indexOf(' ');
        final String result = firstSpaceIndex == -1 ? javaApplicationArguments : javaApplicationArguments.substring(0, firstSpaceIndex);

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    default Class<?> getMainClass()
    {
        final String mainClassFullName = this.getMainClassFullName();
        return Types.getClass(mainClassFullName).await();
    }
}
