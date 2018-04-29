package qub;

/**
 * A Console that can be used to write Console applications.
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
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public Result<Boolean> write(byte[] toWrite)
    {
        Result<Boolean> result;

        final ByteWriteStream writeStream = getOutputAsByteWriteStream();
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

    public Result<Boolean> write(byte[] toWrite, int startIndex, int length)
    {
        Result<Boolean> result;

        final ByteWriteStream writeStream = getOutputAsByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite, startIndex, length);
        }
        else
        {
            result = Result.successFalse();
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
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public Result<Boolean> writeError(byte[] toWrite)
    {
        Result<Boolean> result;

        final ByteWriteStream writeStream = getErrorAsByteWriteStream();
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

    public Result<Boolean> writeError(byte[] toWrite, int startIndex, int length)
    {
        Result<Boolean> result;

        final ByteWriteStream writeStream = getErrorAsByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite, startIndex, length);
        }
        else
        {
            result = Result.successFalse();
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
}
