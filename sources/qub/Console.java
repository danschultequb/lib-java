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
        this(CommandLine.create());
    }

    /**
     * Create a new Console platform that Console applications can be written with.
     */
    public Console(String[] commandLineArgumentStrings)
    {
        this(CommandLine.create(commandLineArgumentStrings));
    }

    public Console(Iterable<String> commandLineArgumentStrings)
    {
        this(CommandLine.create(commandLineArgumentStrings));
    }

    public Console(CommandLine commandLine)
    {
        super(commandLine);
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

        final Console console = new Console(args);
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
}
