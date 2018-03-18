package qub;

public class CharacterWriteStreamToLineWriteStream extends LineWriteStreamBase
{
    private final CharacterWriteStream characterWriteStream;
    private final String lineSeparator;

    CharacterWriteStreamToLineWriteStream(CharacterWriteStream characterWriteStream)
    {
        this(characterWriteStream, "\n");
    }

    CharacterWriteStreamToLineWriteStream(CharacterWriteStream characterWriteStream, String lineSeparator)
    {
        this.characterWriteStream = characterWriteStream;
        this.lineSeparator = lineSeparator;
    }

    protected CharacterWriteStream getCharacterWriteStream()
    {
        return characterWriteStream;
    }

    @Override
    public String getLineSeparator()
    {
        return lineSeparator;
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream()
    {
        return characterWriteStream;
    }

    @Override
    public boolean isDisposed()
    {
        return characterWriteStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return characterWriteStream.dispose();
    }
}
