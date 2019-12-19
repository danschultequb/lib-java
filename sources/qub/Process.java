package qub;

/**
 * A Process object that exposes the platform functionality that a Java application can use.
 */
public interface Process extends IProcess
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

        final Process process = Process.create(args);
        try
        {
            main.run(process);
        }
        catch (Throwable error)
        {
            Exceptions.writeErrorString(process.getErrorCharacterWriteStream(), error).await();
        }
        finally
        {
            process.dispose().await();
            java.lang.System.exit(process.getExitCode());
        }
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

        Process.run(arguments, (Process process) -> Process.run(process, getParametersFunction, runAction));
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param process The Process object that contains the command line arguments.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runAction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    static <TParameters> void run(Process process, Function1<Process,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(process, "process");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        final TParameters parameters = getParametersFunction.run(process);
        if (parameters != null)
        {
            process.showDuration(() ->
            {
                runAction.run(parameters);
            });
        }
    }
}
