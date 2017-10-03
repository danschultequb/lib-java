package qub;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * A Console platform that can be used to write Console applications.
 */
public class Console implements TextWriteStream, TextReadStream
{
    private final CommandLine commandLine;

    private final Value<TextWriteStream> writeStream;
    private final Value<TextReadStream> readStream;
    private final Value<Random> random;
    private final Value<FileSystem> fileSystem;
    private final Value<String> currentFolderPathString;

    /**
     * Create a new Console platform that Console applications can be written with.
     */
    public Console()
    {
        this(null);
    }

    /**
     * Create a new Console platform that Console applications can be written with.
     */
    public Console(String[] commandLineArgumentStrings)
    {
        this.commandLine = new CommandLine(commandLineArgumentStrings);

        writeStream = new Value<>();
        readStream = new Value<>();
        random = new Value<>();
        fileSystem = new Value<>();
        currentFolderPathString = new Value<>();
    }

    public String[] getCommandLineArgumentStrings()
    {
        return commandLine.getArgumentStrings();
    }

    public CommandLine getCommandLine()
    {
        return commandLine;
    }

    /**
     * Set the TextWriteStream that is assigned to this Console.
     * @param writeStream The TextWriteStream that is assigned to this Console.
     */
    public void setWriteStream(TextWriteStream writeStream)
    {
        this.writeStream.set(writeStream);
    }

    /**
     * Get the TextWriteStream that is assigned to this Console.
     * @return
     */
    TextWriteStream getWriteStream()
    {
        if (!writeStream.hasValue())
        {
            writeStream.set(new StandardOutputTextWriteStream());
        }
        return writeStream.get();
    }

    /**
     * Set the TextReadStream that is assigned to this Console.
     * @param readStream The TextReadStream that is assigned to this Console.
     */
    public void setReadStream(TextReadStream readStream)
    {
        this.readStream.set(readStream);
    }

    /**
     * Get the TextReadStream that is assigned to this Console.
     * @return The TextReadStream that is assigned to this Console.
     */
    TextReadStream getReadStream()
    {
        if (!readStream.hasValue())
        {
            readStream.set(new StandardInputTextReadStream());
        }
        return readStream.get();
    }

    @Override
    public boolean isOpen()
    {
        return true;
    }

    @Override
    public boolean close()
    {
        return false;
    }

    @Override
    public void write(byte toWrite)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite, int startIndex, int length)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite, startIndex, length);
        }
    }

    @Override
    public void write(String toWrite)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void writeLine()
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.writeLine();
        }
    }

    @Override
    public void writeLine(String toWrite)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.writeLine(toWrite);
        }
    }

    @Override
    public byte[] readBytes(int bytesToRead) throws IOException
    {
        byte[] result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readBytes(bytesToRead);
        }

        return result;
    }

    @Override
    public int readBytes(byte[] output) throws IOException
    {
        int result = -1;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readBytes(output);
        }

        return result;
    }

    @Override
    public int readBytes(byte[] output, int startIndex, int length) throws IOException
    {
        int result = -1;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readBytes(output, startIndex, length);
        }

        return result;
    }

    @Override
    public char[] readCharacters(int charactersToRead) throws IOException
    {
        char[] result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readCharacters(charactersToRead);
        }

        return result;
    }

    @Override
    public int readCharacters(char[] output) throws IOException
    {
        int result = -1;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readCharacters(output);
        }

        return result;
    }

    @Override
    public int readCharacters(char[] output, int startIndex, int length) throws IOException
    {
        int result = -1;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readCharacters(output, startIndex, length);
        }

        return result;
    }

    @Override
    public String readString(int stringLength) throws IOException
    {
        String result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readString(stringLength);
        }

        return result;
    }

    @Override
    public String readLine() throws IOException
    {
        String result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readLine();
        }

        return result;
    }

    @Override
    public String readLine(boolean includeNewLineInLine) throws IOException
    {
        String result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readLine(includeNewLineInLine);
        }

        return result;
    }

    /**
     * Set the Random number generator assigned to this Console.
     * @param random The Random number generator assigned to this Console.
     */
    void setRandom(Random random)
    {
        this.random.set(random);
    }

    /**
     * Get the Random number generator assigned to this Console.
     * @return The Random number generator assigned to this Console.
     */
    public Random getRandom()
    {
        if (!random.hasValue())
        {
            random.set(new JavaRandom());
        }
        return random.get();
    }

    /**
     * Get the FileSystem assigned to this Console.
     * @return The FileSystem assigned to this Console.
     */
    public FileSystem getFileSystem()
    {
        if (!fileSystem.hasValue())
        {
            fileSystem.set(new JavaFileSystem());
        }
        return fileSystem.get();
    }

    /**
     * Set the FileSystem that is assigned to this Console.
     * @param fileSystem The FileSystem that will be assigned to this Console.
     */
    public void setFileSystem(FileSystem fileSystem)
    {
        this.fileSystem.set(fileSystem);
        setCurrentFolderPathString(null);
    }

    public String getCurrentFolderPathString()
    {
        if (!currentFolderPathString.hasValue())
        {
            currentFolderPathString.set(Paths.get(".").toAbsolutePath().normalize().toString());
        }
        return currentFolderPathString.get();
    }

    public void setCurrentFolderPathString(String currentFolderPathString)
    {
        this.currentFolderPathString.set(currentFolderPathString);
    }

    /**
     * Get the path to the folder that this Console is currently running in.
     * @return The path to the folder that this Console is currently running in.
     */
    public Path getCurrentFolderPath()
    {
        final String currentFolderPathString = getCurrentFolderPathString();
        return Path.parse(currentFolderPathString);
    }

    /**
     * Set the path to the folder that this Console is currently running in.
     * @param currentFolderPath The folder to the path that this Console is currently running in.
     */
    public void setCurrentFolderPath(Path currentFolderPath)
    {
        currentFolderPathString.set(currentFolderPath == null ? null : currentFolderPath.toString());
    }

    public Folder getCurrentFolder()
    {
        final FileSystem fileSystem = getFileSystem();
        return fileSystem == null ? null : fileSystem.getFolder(getCurrentFolderPath());
    }
}
