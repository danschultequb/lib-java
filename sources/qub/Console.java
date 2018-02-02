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

    public String readLine()
    {
        final LineReadStream lineReadStream = getInputAsLineReadStream();
        return lineReadStream.readLine();
    }

    public String readLine(boolean includeNewLine)
    {
        final LineReadStream lineReadStream = getInputAsLineReadStream();
        return lineReadStream.readLine(includeNewLine);
    }

    public boolean write(byte toWrite)
    {
        boolean result = false;

        final ByteWriteStream writeStream = getOutputAsByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public boolean write(byte[] toWrite)
    {
        boolean result = false;

        final ByteWriteStream writeStream = getOutputAsByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public boolean write(byte[] toWrite, int startIndex, int length)
    {
        boolean result = false;

        final ByteWriteStream writeStream = getOutputAsByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite, startIndex, length);
        }

        return result;
    }

    public boolean write(char toWrite)
    {
        boolean result = false;

        final CharacterWriteStream writeStream = getOutputAsCharacterWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public boolean write(String toWrite)
    {
        boolean result = false;

        final CharacterWriteStream writeStream = getOutputAsCharacterWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public boolean writeLine()
    {
        boolean result = false;

        final LineWriteStream writeStream = getOutputAsLineWriteStream();
        if (writeStream != null)
        {
            result = writeStream.writeLine();
        }

        return result;
    }

    public boolean writeLine(String toWrite)
    {
        boolean result = false;

        final LineWriteStream writeStream = getOutputAsLineWriteStream();
        if (writeStream != null)
        {
            writeStream.writeLine(toWrite);
        }

        return result;
    }

    public boolean writeError(byte toWrite)
    {
        boolean result = false;

        final ByteWriteStream writeStream = getErrorAsByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public boolean writeError(byte[] toWrite)
    {
        boolean result = false;

        final ByteWriteStream writeStream = getErrorAsByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public boolean writeError(byte[] toWrite, int startIndex, int length)
    {
        boolean result = false;

        final ByteWriteStream writeStream = getErrorAsByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite, startIndex, length);
        }

        return result;
    }

    public boolean writeError(char toWrite)
    {
        boolean result = false;

        final CharacterWriteStream writeStream = getErrorAsCharacterWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public boolean writeError(String toWrite)
    {
        boolean result = false;

        final CharacterWriteStream writeStream = getErrorAsCharacterWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public boolean writeErrorLine()
    {
        boolean result = false;

        final LineWriteStream writeStream = getErrorAsLineWriteStream();
        if (writeStream != null)
        {
            result = writeStream.writeLine();
        }

        return result;
    }

    public boolean writeErrorLine(String toWrite)
    {
        boolean result = false;

        final LineWriteStream writeStream = getErrorAsLineWriteStream();
        if (writeStream != null)
        {
            writeStream.writeLine(toWrite);
        }

        return result;
    }
}
