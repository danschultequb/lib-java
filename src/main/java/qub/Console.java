package qub;

import java.nio.file.Paths;

/**
 * A Console platform that can be used to write Console applications.
 */
public class Console
{
    private final CommandLine commandLine;

    private final Value<CharacterEncoding> characterEncoding;
    private final Value<String> lineSeparator;
    private final Value<Boolean> includeNewLines;
    private final Value<ByteWriteStream> writeStream;
    private final Value<ByteReadStream> byteReadStream;
    private final Value<CharacterReadStream> characterReadStream;
    private final Value<LineReadStream> lineReadStream;
    private final Value<Random> random;
    private final Value<FileSystem> fileSystem;
    private final Value<String> currentFolderPathString;

    private final AsyncRunner mainRunner;
    private final AsyncRunner parallelRunner;

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

        characterEncoding = new Value<>();
        lineSeparator = new Value<>();
        includeNewLines = new Value<>();
        writeStream = new Value<>();
        byteReadStream = new Value<>();
        characterReadStream = new Value<>();
        lineReadStream = new Value<>();
        random = new Value<>();
        fileSystem = new Value<>();
        currentFolderPathString = new Value<>();

        mainRunner = new CurrentThreadAsyncRunner();
        AsyncRunnerRegistry.setCurrentThreadAsyncRunner(mainRunner);

        parallelRunner = new ParallelAsyncRunner();
    }

    public String[] getCommandLineArgumentStrings()
    {
        return commandLine.getArgumentStrings();
    }

    public CommandLine getCommandLine()
    {
        return commandLine;
    }

    public void setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        this.characterEncoding.set(characterEncoding);
    }

    public String readLine()
    {
        final LineReadStream lineReadStream = asLineReadStream();
        return lineReadStream.readLine();
    }

    public String readLine(boolean includeNewLine)
    {
        final LineReadStream lineReadStream = asLineReadStream();
        return lineReadStream.readLine(includeNewLine);
    }

    public CharacterEncoding getCharacterEncoding()
    {
        if (!characterEncoding.hasValue())
        {
            characterEncoding.set(CharacterEncoding.UTF_8);
        }
        return characterEncoding.get();
    }

    public void setLineSeparator(String lineSeparator)
    {
        this.lineSeparator.set(lineSeparator);
    }

    public String getLineSeparator()
    {
        if (!lineSeparator.hasValue())
        {
            lineSeparator.set("\n");
        }
        return lineSeparator.get();
    }

    public void setIncludeNewLines(boolean includeNewLines)
    {
        this.includeNewLines.set(includeNewLines);
    }

    public boolean getIncludeNewLines()
    {
        if (!includeNewLines.hasValue())
        {
            includeNewLines.set(false);
        }
        return includeNewLines.get();
    }

    /**
     * Set the TextWriteStream that is assigned to this Console.
     * @param writeStream The TextWriteStream that is assigned to this Console.
     */
    public void setByteWriteStream(ByteWriteStream writeStream)
    {
        this.writeStream.set(writeStream);
    }

    /**
     * Get the TextWriteStream that is assigned to this Console.
     * @return
     */
    public ByteWriteStream asByteWriteStream()
    {
        if (!writeStream.hasValue())
        {
            writeStream.set(new OutputStreamToByteWriteStream(System.out));
        }
        return writeStream.get();
    }

    public void setCharacterWriteStream(CharacterWriteStream writeStream)
    {
        setByteWriteStream(writeStream == null ? null : writeStream.asByteWriteStream());
    }

    public CharacterWriteStream asCharacterWriteStream()
    {
        return ByteWriteStreamBase.asCharacterWriteStream(asByteWriteStream(), getCharacterEncoding());
    }

    public void setLineWriteStream(LineWriteStream writeStream)
    {
        setCharacterWriteStream(writeStream == null ? null : writeStream.asCharacterWriteStream());
    }

    public LineWriteStream asLineWriteStream()
    {
        return ByteWriteStreamBase.asLineWriteStream(asByteWriteStream(), getCharacterEncoding(), getLineSeparator());
    }

    /**
     * Set the ByteReadStream that is assigned to this Console.
     * @param readStream The ByteReadStream that is assigned to this Console.
     */
    public void setByteReadStream(ByteReadStream readStream)
    {
        setLineReadStream(ByteReadStreamBase.asLineReadStream(readStream, getCharacterEncoding(), getIncludeNewLines()));
    }

    /**
     * Set the CharacterReadStream that is assigned to this Console.
     * @param readStream The CharacterReadStream that is assigned to this Console.
     */
    public void setCharacterReadStream(CharacterReadStream readStream)
    {
        setLineReadStream(CharacterReadStreamBase.asLineReadStream(readStream, getIncludeNewLines()));
    }

    /**
     * Set the LineReadStream that is assigned to this Console.
     * @param readStream The LineReadStream that is assigned to this Console.
     */
    public void setLineReadStream(LineReadStream readStream)
    {
        lineReadStream.set(readStream);
        characterReadStream.set(readStream == null ? null : readStream.asCharacterReadStream());
        byteReadStream.set(readStream == null ? null : readStream.asByteReadStream());
    }

    /**
     * Get the TextReadStream that is assigned to this Console.
     * @return The TextReadStream that is assigned to this Console.
     */
    public ByteReadStream asByteReadStream()
    {
        if (!byteReadStream.hasValue())
        {
            setByteReadStream(new InputStreamToByteReadStream(System.in));
        }
        return byteReadStream.get();
    }

    public CharacterReadStream asCharacterReadStream()
    {
        if (!characterReadStream.hasValue())
        {
            asByteReadStream();
        }
        return characterReadStream.get();
    }

    public LineReadStream asLineReadStream()
    {
        if (!lineReadStream.hasValue())
        {
            asByteReadStream();
        }
        return lineReadStream.get();
    }

    public boolean write(byte toWrite)
    {
        boolean result = false;

        final ByteWriteStream writeStream = asByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public boolean write(byte[] toWrite)
    {
        boolean result = false;

        final ByteWriteStream writeStream = asByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public boolean write(byte[] toWrite, int startIndex, int length)
    {
        boolean result = false;

        final ByteWriteStream writeStream = asByteWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite, startIndex, length);
        }

        return result;
    }

    public boolean write(char toWrite)
    {
        boolean result = false;

        final CharacterWriteStream writeStream = asCharacterWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public boolean write(String toWrite)
    {
        boolean result = false;

        final CharacterWriteStream writeStream = asCharacterWriteStream();
        if (writeStream != null)
        {
            result = writeStream.write(toWrite);
        }

        return result;
    }

    public boolean writeLine()
    {
        boolean result = false;

        final LineWriteStream writeStream = asLineWriteStream();
        if (writeStream != null)
        {
            result = writeStream.writeLine();
        }

        return result;
    }

    public boolean writeLine(String toWrite)
    {
        boolean result = false;

        final LineWriteStream writeStream = asLineWriteStream();
        if (writeStream != null)
        {
            writeStream.writeLine(toWrite);
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
            setFileSystem(new JavaFileSystem());
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
        if (fileSystem == null)
        {
            setCurrentFolderPathString(null);
        }
        else
        {
            fileSystem.setAsyncRunner(parallelRunner);
            currentFolderPathString.clear();
        }
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
