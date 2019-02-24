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

    public Result<Boolean> write(char toWrite)
    {
        Result<Boolean> result;

        final CharacterWriteStream writeStream = getOutputAsCharacterWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }
        else
        {
            result = Result.successFalse();
        }

        return result;
    }

    public Result<Boolean> write(String toWrite, Object... formattedStringArguments)
    {
        Result<Boolean> result;

        final CharacterWriteStream writeStream = getOutputAsCharacterWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite, formattedStringArguments);
        }
        else
        {
            result = Result.successFalse();
        }

        return result;
    }

    public Result<Boolean> writeLine()
    {
        Result<Boolean> result;

        final LineWriteStream writeStream = getOutputAsLineWriteStream();
        if (writeStream != null)
        {
            result = writeStream.writeLine();
        }
        else
        {
            result = Result.successFalse();
        }

        return result;
    }

    public Result<Boolean> writeLine(String toWrite, Object... formattedStringArguments)
    {
        Result<Boolean> result;

        final LineWriteStream writeStream = getOutputAsLineWriteStream();
        if (writeStream != null)
        {
            result = writeStream.writeLine(toWrite, formattedStringArguments);
        }
        else
        {
            result = Result.successFalse();
        }

        return result;
    }

    public Result<Boolean> writeError(byte toWrite)
    {
        Result<Boolean> result = Result.successFalse();

        final ByteWriteStream writeStream = getErrorAsByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.writeByte(toWrite);
        }

        return result;
    }

    public Result<Integer> writeError(byte[] toWrite)
    {
        Result<Integer> result;

        final ByteWriteStream writeStream = getErrorAsByteWriteStream();
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

    public Result<Integer> writeError(byte[] toWrite, int startIndex, int length)
    {
        Result<Integer> result;

        final ByteWriteStream writeStream = getErrorAsByteWriteStream();
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

    public Result<Boolean> writeError(char toWrite)
    {
        Result<Boolean> result;

        final CharacterWriteStream writeStream = getErrorAsCharacterWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }
        else
        {
            result = Result.successFalse();
        }

        return result;
    }

    public Result<Boolean> writeError(String toWrite, Object... formattedStringArguments)
    {
        Result<Boolean> result;

        final CharacterWriteStream writeStream = getErrorAsCharacterWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite, formattedStringArguments);
        }
        else
        {
            result = Result.successFalse();
        }

        return result;
    }

    public Result<Boolean> writeErrorLine()
    {
        Result<Boolean> result;

        final LineWriteStream writeStream = getErrorAsLineWriteStream();
        if (writeStream != null)
        {
            result = writeStream.writeLine();
        }
        else
        {
            result = Result.successFalse();
        }

        return result;
    }

    public Result<Boolean> writeErrorLine(String toWrite, Object... formattedStringArguments)
    {
        Result<Boolean> result;

        final LineWriteStream writeStream = getErrorAsLineWriteStream();
        if (writeStream != null)
        {
            result = writeStream.writeLine(toWrite, formattedStringArguments);
        }
        else
        {
            result = Result.successFalse();
        }

        return result;
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
