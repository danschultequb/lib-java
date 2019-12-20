package qub;

/**
 * A Console object that can be used in console applications.
 */
public interface Console extends Process
{
    /**
     * Create a new Console object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new Console.
     */
    static Console create(String... commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return Console.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new Console object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new Console.
     */
    static Console create(Iterable<String> commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return Console.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new Console object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new Console.
     */
    static Console create(CommandLineArguments commandLineArguments)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        return Console.create(commandLineArguments, new ManualAsyncRunner());
    }

    /**
     * Create a new Console object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new Console.
     */
    static Console create(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        return JavaProcess.create(commandLineArguments, mainAsyncRunner);
    }

    default Result<String> readLine()
    {
        return this.getInputCharacterReadStream().readLine();
    }

    default Result<Integer> write(char toWrite)
    {
        return this.getOutputCharacterWriteStream().write(toWrite);
    }

    default Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        return this.getOutputCharacterWriteStream().write(toWrite, formattedStringArguments);
    }

    default Result<Integer> writeLine()
    {
        return this.getOutputCharacterWriteStream().writeLine();
    }

    default Result<Integer> writeLine(String toWrite, Object... formattedStringArguments)
    {
        return this.getOutputCharacterWriteStream().writeLine(toWrite, formattedStringArguments);
    }

    default Result<Integer> writeError(char toWrite)
    {
        return this.getErrorCharacterWriteStream().write(toWrite);
    }

    default Result<Integer> writeError(String toWrite, Object... formattedStringArguments)
    {
        return this.getErrorCharacterWriteStream().write(toWrite, formattedStringArguments);
    }

    default Result<Integer> writeErrorLine()
    {
        return this.getErrorCharacterWriteStream().writeLine();
    }

    default Result<Integer> writeErrorLine(String toWrite, Object... formattedStringArguments)
    {
        return this.getErrorCharacterWriteStream().writeLine(toWrite, formattedStringArguments);
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param args The String arguments provided.
     * @param main The main function that will be run.
     */
    static void run(String[] args, Action1<Console> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        final Console console = Console.create(args);
        try
        {
            main.run(console);
        }
        catch (Throwable error)
        {
            Exceptions.writeErrorString(console.getErrorCharacterWriteStream(), error).await();
        }
        finally
        {
            console.dispose().await();
            java.lang.System.exit(console.getExitCode());
        }
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param arguments The command line arguments to the application.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Console's command line arguments.
     * @param runAction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    static <TParameters> void run(String[] arguments, Function1<Console,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        Console.run(arguments, (Console console) -> Console.run(console, getParametersFunction, runAction));
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param console The Console object that contains the command line arguments.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Console's command line arguments.
     * @param runAction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    static <TParameters> void run(Console console, Function1<Console,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(console, "console");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        final TParameters parameters = getParametersFunction.run(console);
        if (parameters != null)
        {
            console.showDuration(() ->
            {
                runAction.run(parameters);
            });
        }
    }
}
