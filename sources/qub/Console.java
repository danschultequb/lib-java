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
        super();
    }

    /**
     * Create a new Console platform that Console applications can be written with.
     */
    public Console(String[] commandLineArgumentStrings)
    {
        super(commandLineArgumentStrings);
    }

    public Console(Iterable<String> commandLineArgumentStrings)
    {
        super(commandLineArgumentStrings);
    }

    public Console(CommandLine commandLine)
    {
        super(commandLine);
    }

    public Result<String> readLine()
    {
        final LineReadStream lineReadStream = getInputAsLineReadStream();
        return lineReadStream.readLine();
    }

    public Result<Boolean> write(byte toWrite)
    {
        Result<Boolean> result = Result.successFalse();

        final ByteWriteStream writeStream = getOutputAsByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.writeByte(toWrite);
        }

        return result;
    }

    public Result<Integer> write(byte[] toWrite)
    {
        Result<Integer> result;

        final ByteWriteStream writeStream = getOutputAsByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.writeBytes(toWrite);
        }
        else
        {
            result = Result.success(null);
        }

        return result;
    }

    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        Result<Integer> result;

        final ByteWriteStream writeStream = getOutputAsByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.writeBytes(toWrite, startIndex, length);
        }
        else
        {
            result = Result.success(null);
        }

        return result;
    }

    public Result<Void> write(char toWrite)
    {
        final CharacterWriteStream writeStream = getOutputAsCharacterWriteStream();
        return writeStream == null
            ? Result.success()
            : writeStream.write(toWrite);
    }

    public Result<Void> write(String toWrite, Object... formattedStringArguments)
    {
        final CharacterWriteStream writeStream = getOutputAsCharacterWriteStream();
        return writeStream == null
            ? Result.success()
            : writeStream.write(toWrite, formattedStringArguments);
    }

    public Result<Void> writeLine()
    {
        final LineWriteStream writeStream = getOutputAsLineWriteStream();
        return writeStream == null
            ? Result.success()
            : writeStream.writeLine();
    }

    public Result<Void> writeLine(String toWrite, Object... formattedStringArguments)
    {
        final LineWriteStream writeStream = getOutputAsLineWriteStream();
        return writeStream == null
            ? Result.success()
            : writeStream.writeLine(toWrite, formattedStringArguments);
    }

    public Result<Boolean> writeError(byte toWrite)
    {
        final ByteWriteStream writeStream = getErrorAsByteWriteStream();
        return writeStream == null
            ? Result.success()
            : writeStream.writeByte(toWrite);
    }

    public Result<Integer> writeError(byte[] toWrite)
    {
        final ByteWriteStream writeStream = getErrorAsByteWriteStream();
        return writeStream == null
            ? Result.success()
            : writeStream.writeBytes(toWrite);
    }

    public Result<Integer> writeError(byte[] toWrite, int startIndex, int length)
    {
        final ByteWriteStream writeStream = getErrorAsByteWriteStream();
        return writeStream == null
            ? Result.success()
            : writeStream.writeBytes(toWrite, startIndex, length);
    }

    public Result<Void> writeError(char toWrite)
    {
        final CharacterWriteStream writeStream = getErrorAsCharacterWriteStream();
        return writeStream == null
            ? Result.success()
            : writeStream.write(toWrite);
    }

    public Result<Void> writeError(String toWrite, Object... formattedStringArguments)
    {
        final CharacterWriteStream writeStream = getErrorAsCharacterWriteStream();
        return writeStream == null
            ? Result.success()
            : writeStream.write(toWrite, formattedStringArguments);
    }

    public Result<Void> writeErrorLine()
    {
        final LineWriteStream writeStream = getErrorAsLineWriteStream();
        return writeStream == null
            ? Result.success()
            : writeStream.writeLine();
    }

    public Result<Void> writeErrorLine(String toWrite, Object... formattedStringArguments)
    {
        final LineWriteStream writeStream = getErrorAsLineWriteStream();
        return writeStream == null
            ? Result.success()
            : writeStream.writeLine(toWrite, formattedStringArguments);
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
            console.writeLine("Unhandled exception: " + error);
        }
        finally
        {
            console.dispose().await();
            java.lang.System.exit(console.getExitCode());
        }
    }
}
