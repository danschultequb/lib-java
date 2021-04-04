package qub;

public class LogStreams implements Disposable
{
    private final File logFile;
    private final CharacterToByteWriteStream logStream;
    private final CharacterToByteWriteStream output;
    private final CharacterToByteWriteStream error;
    private final VerboseCharacterToByteWriteStream verbose;

    private LogStreams(File logFile, CharacterToByteWriteStream logStream, CharacterToByteWriteStream output, CharacterToByteWriteStream error, VerboseCharacterToByteWriteStream verbose)
    {
        PreCondition.assertNotNull(logFile, "logFile");
        PreCondition.assertNotNull(logStream, "logStream");

        this.logFile = logFile;
        this.logStream = logStream;
        this.output = output;
        this.error = error;
        this.verbose = verbose;
    }

    public static LogStreams create(File logFile, CharacterToByteWriteStream logStream, CharacterToByteWriteStream output, CharacterToByteWriteStream error, VerboseCharacterToByteWriteStream verbose)
    {
        return new LogStreams(logFile, logStream, output, error, verbose);
    }

    public File getLogFile()
    {
        return this.logFile;
    }

    public CharacterToByteWriteStream getLogStream()
    {
        return this.logStream;
    }

    public CharacterToByteWriteStream getOutput()
    {
        return this.output;
    }

    public CharacterToByteWriteStream getError()
    {
        return this.error;
    }

    public VerboseCharacterToByteWriteStream getVerbose()
    {
        return this.verbose;
    }

    @Override
    public boolean isDisposed()
    {
        return this.logStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return this.logStream.dispose();
    }
}
