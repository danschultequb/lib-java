package qub;

public class LogCharacterWriteStreams
{
    private final File logFile;
    private final CharacterWriteStream logStream;
    private final Indexable<CharacterWriteStream> combinedStreams;

    private LogCharacterWriteStreams(File logFile, CharacterWriteStream logStream, Indexable<CharacterWriteStream> combinedStreams)
    {
        PreCondition.assertNotNull(logFile, "logFile");
        PreCondition.assertNotNull(logStream, "logStream");
        PreCondition.assertNotDisposed(logStream, "logStream");
        PreCondition.assertNotNullAndNotEmpty(combinedStreams, "combinedStreams");

        this.logFile = logFile;
        this.logStream = logStream;
        this.combinedStreams = combinedStreams;
    }

    public static LogCharacterWriteStreams create(File logFile, CharacterWriteStream logStream, Indexable<CharacterWriteStream> combinedStreams)
    {
        return new LogCharacterWriteStreams(logFile, logStream, combinedStreams);
    }

    public File getLogFile()
    {
        return this.logFile;
    }

    public CharacterWriteStream getLogStream()
    {
        return this.logStream;
    }

    public CharacterWriteStream getCombinedStream(int index)
    {
        return this.combinedStreams.get(index);
    }

    public int getCombinedStreamsCount()
    {
        return this.combinedStreams.getCount();
    }
}
