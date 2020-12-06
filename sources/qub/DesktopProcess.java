package qub;

/**
 * A Process object that is running in a Desktop environment (Windows, Linux, Mac, etc.).
 */
public class DesktopProcess extends ProcessBase
{
    private final CommandLineArguments commandLineArguments;
    private int exitCode;
    private final LongValue processId;
    private final Value<ProcessFactory> processFactory;
    private final Value<String> mainClassFullName;

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new JavaProcess.
     */
    public static DesktopProcess create(String... commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return DesktopProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new JavaProcess.
     */
    public static DesktopProcess create(Iterable<String> commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return DesktopProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new JavaProcess.
     */
    public static DesktopProcess create(CommandLineArguments commandLineArguments)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        return DesktopProcess.create(commandLineArguments, new ManualAsyncRunner());
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new JavaProcess.
     */
    public static DesktopProcess create(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        return new DesktopProcess(commandLineArguments, mainAsyncRunner);
    }

    protected DesktopProcess(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        super(mainAsyncRunner);

        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        this.commandLineArguments = commandLineArguments;
        this.processId = LongValue.create();
        this.processFactory = Value.create();
        this.mainClassFullName = Value.create();
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param args The String arguments provided.
     * @param main The main function that will be run.
     */
    public static void run(String[] args, Action1<DesktopProcess> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        DesktopProcess.run(() -> DesktopProcess.create(args), main);
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param main The main function that will be run.
     */
    public static <TProcess extends DesktopProcess> void run(Function0<TProcess> processCreator, Action1<TProcess> main)
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
            Exceptions.writeErrorString(process.getErrorWriteStream(), error).await();
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
    public static void run(String[] args, Function1<DesktopProcess,Integer> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        DesktopProcess.run(() -> DesktopProcess.create(args), DesktopProcess.getMainAction(main));
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param main The main function that will be run.
     */
    public static <TProcess extends DesktopProcess> void run(Function0<TProcess> processCreator, Function1<TProcess,Integer> main)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(main, "main");

        DesktopProcess.run(processCreator, DesktopProcess.getMainAction(main));
    }

    public static <TProcess extends DesktopProcess> Action1<TProcess> getMainAction(Function1<TProcess,Integer> runFunction)
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
    public static <TParameters> void run(String[] arguments, Function1<DesktopProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        DesktopProcess.run(arguments, DesktopProcess.getMainAction(getParametersFunction, runAction));
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
    public static <TProcess extends DesktopProcess,TParameters> void run(Function0<TProcess> processCreator, Function1<TProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        DesktopProcess.run(processCreator, DesktopProcess.getMainAction(getParametersFunction, runAction));
    }

    public static <TProcess extends DesktopProcess,TParameters> Action1<TProcess> getMainAction(Function1<TProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        return (TProcess process) ->
        {
            final TParameters parameters = getParametersFunction.run(process);
            if (parameters != null)
            {
                runAction.run(parameters);
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
    public static <TParameters> void run(String[] arguments, Function1<DesktopProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runFunction)
    {
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runFunction, "runFunction");

        DesktopProcess.run(arguments, DesktopProcess.getMainAction(getParametersFunction, runFunction));
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
    public static <TProcess extends DesktopProcess,TParameters> void run(Function0<TProcess> processCreator, Function1<TProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runFunction)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runFunction, "runFunction");

        DesktopProcess.run(processCreator, DesktopProcess.getMainAction(getParametersFunction, runFunction));
    }

    public static <TProcess extends DesktopProcess,TParameters> Action1<TProcess> getMainAction(Function1<TProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runAction)
    {
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        return (TProcess process) ->
        {
            final TParameters parameters = getParametersFunction.run(process);
            if (parameters != null)
            {
                process.setExitCode(runAction.run(parameters));
            }
        };
    }

    /**
     * Get the id of this process.
     * @return The id of this process.
     */
    public long getProcessId()
    {
        return this.processId.getOrSet(this.createDefaultProcessId());
    }

    protected long createDefaultProcessId()
    {
        return java.lang.ProcessHandle.current().pid();
    }

    /**
     * Set the process ID of this DesktopProcess.
     * @param processId The process ID of this process.
     * @return This object for method chaining.
     */
    protected DesktopProcess setProcessId(long processId)
    {
        this.processId.set(processId);

        return this;
    }

    /**
     * Get the exit code that this process will return when it finishes.
     * @return The exit code that this process will return when it finishes.
     */
    public int getExitCode()
    {
        return this.exitCode;
    }

    /**
     * Set the exit code that this process will return when it finishes.
     * @param exitCode The exit code that this process will return when it finishes.
     * @return This DesktopProcess for method chaining.
     */
    public DesktopProcess setExitCode(int exitCode)
    {
        this.exitCode = exitCode;
        return this;
    }

    /**
     * Add one to the current exit code.
     * @return This DesktopProcess for method chaining.
     */
    public DesktopProcess incrementExitCode()
    {
        return this.setExitCode(getExitCode() + 1);
    }

    /**
     * Get the CommandLineArguments that were passed on the command line.
     * @return The CommandLineArguments that were passed on the command line.
     */
    public CommandLineArguments getCommandLineArguments()
    {
        return this.commandLineArguments;
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
            .setArguments(this.getCommandLineArguments());
    }

    /**
     * Create a CommandLineActions object that can be used to create CommandLineAction objects. The
     * first positional argument will be used as the action (--action) parameter to determine which
     * action should be invoked.
     * @return A new CommandLineActions object.
     */
    public <TProcess extends DesktopProcess> CommandLineActions<TProcess> createCommandLineActions()
    {
        return CommandLineActions.create();
    }

    /**
     * Get the object that can be used to invoke external processes.
     * @return The object that can be used to invoke external processes.
     */
    public ProcessFactory getProcessFactory()
    {
        return this.processFactory.getOrSet(this::createDefaultProcessFactory);
    }

    protected ProcessFactory createDefaultProcessFactory()
    {
        final AsyncRunner parallelAsyncRunner = this.getParallelAsyncRunner();
        final EnvironmentVariables environmentVariables = this.getEnvironmentVariables();
        final Folder currentFolder = this.getCurrentFolder();
        return new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder);
    }

    /**
     * Set the object that can be used to invoke external processes.
     * @param processFactory The object that can be used to invoke external processes.
     * @return This object for method chaining.
     */
    protected ProcessBase setProcessFactory(ProcessFactory processFactory)
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

    @Override
    protected DesktopProcess setSystemProperty(String systemPropertyName, String systemPropertyValue)
    {
        return (DesktopProcess)super.setSystemProperty(systemPropertyName, systemPropertyValue);
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

    /**
     * Set the java.class.path system property. This will not change the classpath of the running
     * JVM, but rather will only modify the system property.
     * @return This object for method chaining.
     */
    protected DesktopProcess setJVMClasspath(String jvmClasspath)
    {
        PreCondition.assertNotNull(jvmClasspath, "jvmClasspath");

        return this.setSystemProperty("java.class.path", jvmClasspath);
    }

    /**
     * Get the full name of the main class of this application.
     * @return The full name of the main class of this application.
     */
    public String getMainClassFullName()
    {
        return this.mainClassFullName.getOrSet(this::createDefaultMainClassFullName);
    }

    protected String createDefaultMainClassFullName()
    {
        final String javaApplicationArguments = this.getSystemProperty("sun.java.command").await();
        final int firstSpaceIndex = javaApplicationArguments.indexOf(' ');
        final String result = firstSpaceIndex == -1 ? javaApplicationArguments : javaApplicationArguments.substring(0, firstSpaceIndex);

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    protected DesktopProcess setMainClassFullName(String mainClassFullName)
    {
        PreCondition.assertNotNullAndNotEmpty(mainClassFullName, "mainClassFullName");

        this.mainClassFullName.set(mainClassFullName);

        return this;
    }

    public Result<Class<?>> getMainClass()
    {
        return Result.create(() ->
        {
            final String mainClassFullName = this.getMainClassFullName();
            final TypeLoader typeLoader = this.getTypeLoader();
            return typeLoader.getType(mainClassFullName).await();
        });
    }

    /**
     * Get the version of Java that is running this application.
     * @return The version of Java that is running this application.
     */
    public VersionNumber getJavaVersion()
    {
        final String javaVersionString = this.getSystemProperty("java.version").await();
        final VersionNumber result = VersionNumber.parse(javaVersionString).await();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the Qub folder that contains the main binaries for this process.
     * @return The Qub folder that contains the main binaries for this process.
     */
    public Result<QubFolder> getQubFolder()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getQubFolder().await();
        });
    }

    /**
     * Get the name of the current process's publisher.
     * @return The name of the current process's publisher.
     */
    public Result<String> getPublisherName()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getPublisherName().await();
        });
    }

    public Result<QubPublisherFolder> getQubPublisherFolder()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getPublisherFolder().await();
        });
    }

    /**
     * Get the name of the current process's project.
     * @return The name of the current process's project.
     */
    public Result<String> getProjectName()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getProjectName().await();
        });
    }

    public Result<QubProjectFolder> getQubProjectFolder()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getProjectFolder().await();
        });
    }

    /**
     * Get the data folder that is associated with the current process's project.
     * @return The data folder that is associated with the current process's project.
     */
    public Result<Folder> getQubProjectDataFolder()
    {
        return Result.create(() ->
        {
            final QubProjectFolder projectFolder = this.getQubProjectFolder().await();
            return projectFolder.getProjectDataFolder().await();
        });
    }

    /**
     * Get the version of the current process's project.
     * @return The version of the current process's project.
     */
    public Result<VersionNumber> getVersion()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getVersion().await();
        });
    }

    /**
     * Get the QubProjectVersionFolder for the current process.
     * @return The QubProjectVersionFolder for the current process.
     */
    public Result<QubProjectVersionFolder> getQubProjectVersionFolder()
    {
        return Result.create(() ->
        {
            QubProjectVersionFolder result;

            final String mainClassFullName = this.getMainClassFullName();
            final TypeLoader typeLoader = this.getTypeLoader();
            final Path typeContainerPath = typeLoader.getTypeContainerPath(mainClassFullName).await();
            final FileSystem fileSystem = this.getFileSystem();
            final File projectVersionFile = fileSystem.getFile(typeContainerPath).await();
            if (projectVersionFile.exists().await())
            {
                result = QubProjectVersionFolder.get(projectVersionFile.getParentFolder().await());
            }
            else
            {
                final Folder projectVersionFolder = fileSystem.getFolder(typeContainerPath).await();
                if (projectVersionFolder.exists().await() || typeContainerPath.endsWith('/') || typeContainerPath.endsWith('\\'))
                {
                    result = QubProjectVersionFolder.get(projectVersionFolder);
                }
                else
                {
                    result = QubProjectVersionFolder.get(projectVersionFile.getParentFolder().await());
                }
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }
}
