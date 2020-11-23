package qub;

public class LogCharacterWriteStreams implements Disposable, Indexable<CharacterWriteStream>
{
    private final CharacterWriteStream logStream;
    private final Indexable<CharacterWriteStream> combinedStreams;

    private LogCharacterWriteStreams(CharacterWriteStream logStream, Indexable<CharacterWriteStream> combinedStreams)
    {
        PreCondition.assertNotNull(logStream, "logStream");
        PreCondition.assertNotDisposed(logStream, "logStream");
        PreCondition.assertNotNullAndNotEmpty(combinedStreams, "combinedStreams");

        this.logStream = logStream;
        this.combinedStreams = combinedStreams;
    }

    public static LogCharacterWriteStreams create(CharacterWriteStream logStream, Indexable<CharacterWriteStream> combinedStreams)
    {
        return new LogCharacterWriteStreams(logStream, combinedStreams);
    }

    @Override
    public CharacterWriteStream get(int index)
    {
        return this.combinedStreams.get(index);
    }

    @Override
    public Iterator<CharacterWriteStream> iterate()
    {
        return this.combinedStreams.iterate();
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
