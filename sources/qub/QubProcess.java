package qub;

/**
 * An interface for a process/application that runs from the Qub folder.
 */
public interface QubProcess extends Process
{
    /**
     * Create a new QubProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new QubProcess.
     */
    static QubProcess create(String... commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return QubProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new QubProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new QubProcess.
     */
    static QubProcess create(Iterable<String> commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return QubProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new QubProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new QubProcess.
     */
    static QubProcess create(CommandLineArguments commandLineArguments)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        return QubProcess.create(commandLineArguments, new ManualAsyncRunner());
    }

    /**
     * Create a new QubProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new QubProcess.
     */
    static QubProcess create(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
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
    static void run(String[] args, Action1<QubProcess> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        final QubProcess process = QubProcess.create(args);
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
    static void run(String[] args, Function1<QubProcess,Integer> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        QubProcess.run(args, QubProcess.getMainAction(main));
    }

    static Action1<QubProcess> getMainAction(Function1<QubProcess,Integer> runFunction)
    {
        PreCondition.assertNotNull(runFunction, "runFunction");

        return (QubProcess process) ->
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
    static <TParameters> void run(String[] arguments, Function1<QubProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        QubProcess.run(arguments, QubProcess.getMainAction(getParametersFunction, runAction));
    }

    static <TParameters> Action1<QubProcess> getMainAction(Function1<QubProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        return (QubProcess process) ->
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
    static <TParameters> void run(String[] arguments, Function1<QubProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runFunction)
    {
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runFunction, "runFunction");

        QubProcess.run(arguments, QubProcess.getMainAction(getParametersFunction, runFunction));
    }

    static <TParameters> Action1<QubProcess> getMainAction(Function1<QubProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runAction)
    {
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        return (QubProcess process) ->
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
     * Get the QubProjectVersionFolder for the current process.
     * @return The QubProjectVersionFolder for the current process.
     */
    default Result<QubProjectVersionFolder> getQubProjectVersionFolder()
    {
        return Result.create(() ->
        {
            final String javaApplicationArguments = this.getSystemProperty("sun.java.command").await();
            final int firstSpaceIndex = javaApplicationArguments.indexOf(' ');
            final String mainClassFullName = firstSpaceIndex == -1 ? javaApplicationArguments : javaApplicationArguments.substring(0, firstSpaceIndex);
            final Class<?> mainClass = Types.getClass(mainClassFullName).await();
            final String mainClassContainerPathString = mainClass.getProtectionDomain().getCodeSource().getLocation().toString().substring("file:/".length());

            final FileSystem fileSystem = this.getFileSystem();
            Folder mainClassContainerFolder;

            final File mainClassContainerFile = fileSystem.getFile(mainClassContainerPathString).await();
            if (mainClassContainerFile.exists().await())
            {
                mainClassContainerFolder = mainClassContainerFile.getParentFolder().await();
            }
            else
            {
                mainClassContainerFolder = fileSystem.getFolder(mainClassContainerPathString).await();
            }

            final QubProjectVersionFolder result = QubProjectVersionFolder.create(mainClassContainerFolder);

            PostCondition.assertNotNull(result, "result");

            return result;
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

    /**
     * Get the version of the current process's project.
     * @return The version of the current process's project.
     */
    default Result<String> getVersion()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getVersion();
        });
    }

    /**
     * Get the data file system that is associated with the current process's project.
     * @return The data file system that is associated with the current process's project.
     */
    default Result<FileSystem> getProjectData()
    {
        return Result.create(() ->
        {
            final QubProjectVersionFolder projectVersionFolder = this.getQubProjectVersionFolder().await();
            final QubProjectFolder projectFolder = projectVersionFolder.getProjectFolder().await();
            final Folder projectDataFolder = projectFolder.getFolder("data").await();
            projectDataFolder.create()
                .catchError(FolderAlreadyExistsException.class)
                .await();
            return FolderFileSystem.get(projectDataFolder);
        });
    }
}
