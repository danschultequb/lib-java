package qub;

/**
 * A Console that can be used to writeByte Console applications.
 */
public class Console extends Process
{
    /**
     * Create a new Console platform that Console applications can be written with.
     */
    public Console()
    {
        this(CommandLineArguments.create());
    }

    /**
     * Create a new Console platform that Console applications can be written with.
     */
    public Console(CommandLineArguments commandLineArguments)
    {
        super(commandLineArguments);
    }

    public Result<String> readLine()
    {
        return getInputCharacterReadStream().readLine();
    }

    public Result<Integer> write(char toWrite)
    {
        return getOutputCharacterWriteStream().write(toWrite);
    }

    public Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        return getOutputCharacterWriteStream().write(toWrite, formattedStringArguments);
    }

    public Result<Integer> writeLine()
    {
        return getOutputCharacterWriteStream().writeLine();
    }

    public Result<Integer> writeLine(String toWrite, Object... formattedStringArguments)
    {
        return getOutputCharacterWriteStream().writeLine(toWrite, formattedStringArguments);
    }

    public Result<Integer> writeError(char toWrite)
    {
        return getErrorCharacterWriteStream().write(toWrite);
    }

    public Result<Integer> writeError(String toWrite, Object... formattedStringArguments)
    {
        return getErrorCharacterWriteStream().write(toWrite, formattedStringArguments);
    }

    public Result<Integer> writeErrorLine()
    {
        return getErrorCharacterWriteStream().writeLine();
    }

    public Result<Integer> writeErrorLine(String toWrite, Object... formattedStringArguments)
    {
        return getErrorCharacterWriteStream().writeLine(toWrite, formattedStringArguments);
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param args The String arguments provided.
     * @param main The main function that will be run.
     */
    public static void run(String[] args, Action1<Console> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        final Console console = new Console(CommandLineArguments.create(args));
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
    public static <TParameters> void run(String[] arguments, Function1<Console,TParameters> getParametersFunction, Action1<TParameters> runAction)
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
    public static <TParameters> void run(Console console, Function1<Console,TParameters> getParametersFunction, Action1<TParameters> runAction)
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
