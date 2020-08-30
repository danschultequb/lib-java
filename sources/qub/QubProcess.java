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

        Process.run(() -> QubProcess.create(args), main);
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

        QubProcess.run(args, Process.getMainAction(main));
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

        QubProcess.run(arguments, Process.getMainAction(getParametersFunction, runAction));
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

        QubProcess.run(arguments, Process.getMainAction(getParametersFunction, runFunction));
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
    default Result<String> getVersion()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getVersion();
        });
    }

    /**
     * Get the QubProjectVersionFolder for the current process.
     * @return The QubProjectVersionFolder for the current process.
     */
    default Result<QubProjectVersionFolder> getQubProjectVersionFolder()
    {
        return QubProjectVersionFolder.getFromType(this.getFileSystem(), this.getMainClassFullName());
    }
}
