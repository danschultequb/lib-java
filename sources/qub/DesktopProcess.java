package qub;

/**
 * A Process object that is running in a Desktop environment (Windows, Linux, Mac, etc.).
 */
public interface DesktopProcess extends Process
{
    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param args The String arguments provided.
     * @param main The main function that will be run.
     */
    static void run(String[] args, Action1<DesktopProcess> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        DesktopProcess.run(() -> RealDesktopProcess.create(args), main);
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param main The main function that will be run.
     */
    static void run(Function0<? extends DesktopProcess> processCreator, Action1<DesktopProcess> main)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(main, "main");

        final DesktopProcess process = processCreator.run();
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
    static void run(String[] args, Function1<DesktopProcess,Integer> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        DesktopProcess.run(() -> RealDesktopProcess.create(args), DesktopProcess.getMainAction(main));
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param main The main function that will be run.
     */
    static void run(Function0<? extends DesktopProcess> processCreator, Function1<DesktopProcess,Integer> main)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(main, "main");

        DesktopProcess.run(processCreator, DesktopProcess.getMainAction(main));
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
    static <TParameters> void run(String[] arguments, Function1<DesktopProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
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
    static <TParameters> void run(Function0<? extends DesktopProcess> processCreator, Function1<DesktopProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        DesktopProcess.run(processCreator, DesktopProcess.getMainAction(getParametersFunction, runAction));
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
    static <TParameters> void run(String[] arguments, Function1<DesktopProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runFunction)
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
    static <TParameters> void run(Function0<? extends DesktopProcess> processCreator, Function1<DesktopProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runFunction)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runFunction, "runFunction");

        DesktopProcess.run(processCreator, DesktopProcess.getMainAction(getParametersFunction, runFunction));
    }

    static Action1<DesktopProcess> getMainAction(Function1<DesktopProcess,Integer> runFunction)
    {
        PreCondition.assertNotNull(runFunction, "runFunction");

        return (DesktopProcess process) ->
        {
            process.setExitCode(runFunction.run(process));
        };
    }

    static <TParameters> Action1<DesktopProcess> getMainAction(Function1<DesktopProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        return (DesktopProcess process) ->
        {
            final TParameters parameters = getParametersFunction.run(process);
            if (parameters != null)
            {
                runAction.run(parameters);
            }
        };
    }

    static <TParameters> Action1<DesktopProcess> getMainAction(Function1<DesktopProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runAction)
    {
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        return (DesktopProcess process) ->
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
    long getProcessId();

    /**
     * Get the exit code that this process will return when it finishes.
     * @return The exit code that this process will return when it finishes.
     */
    int getExitCode();

    /**
     * Set the exit code that this process will return when it finishes.
     * @param exitCode The exit code that this process will return when it finishes.
     * @return This DesktopProcess for method chaining.
     */
    DesktopProcess setExitCode(int exitCode);

    /**
     * Add one to the current exit code.
     * @return This DesktopProcess for method chaining.
     */
    default DesktopProcess incrementExitCode()
    {
        return this.setExitCode(getExitCode() + 1);
    }

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
    default CommandLineActions createCommandLineActions()
    {
        return CommandLineActions.create();
    }

    /**
     * Get the object that can be used to invoke external processes.
     * @return The object that can be used to invoke external processes.
     */
    ProcessFactory getProcessFactory();

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
     * Get the full name of the main class of this application.
     * @return The full name of the main class of this application.
     */
    String getMainClassFullName();

    default Result<Class<?>> getMainClass()
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
    default VersionNumber getJavaVersion()
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
    default Result<QubFolder> getQubFolder()
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
    default Result<String> getPublisherName()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getPublisherName().await();
        });
    }

    default Result<QubPublisherFolder> getQubPublisherFolder()
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
    default Result<String> getProjectName()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getProjectName().await();
        });
    }

    default Result<QubProjectFolder> getQubProjectFolder()
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
    default Result<Folder> getQubProjectDataFolder()
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
    default Result<VersionNumber> getVersion()
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
    default Result<QubProjectVersionFolder> getQubProjectVersionFolder()
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
