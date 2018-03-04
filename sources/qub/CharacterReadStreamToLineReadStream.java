package qub;

public class CharacterReadStreamToLineReadStream extends LineReadStreamBase
{
    private final CharacterReadStream characterReadStream;
    private final boolean includeNewLines;
    private String current;

    public CharacterReadStreamToLineReadStream(CharacterReadStream readStream)
    {
        this(readStream, false);
    }

    public CharacterReadStreamToLineReadStream(CharacterReadStream characterReadStream, boolean includeNewLines)
    {
        this.characterReadStream = characterReadStream;
        this.includeNewLines = includeNewLines;
    }

    @Override
    public boolean getIncludeNewLines()
    {
        return includeNewLines;
    }

    @Override
    public void close()
    {
        current = null;

        characterReadStream.close();
    }

    @Override
    public CharacterReadStream asCharacterReadStream()
    {
        return characterReadStream;
    }

    @Override
    public boolean hasCurrent()
    {
        return current != null;
    }

    @Override
    public String getCurrent()
    {
        return current;
    }

    @Override
    public String readLine(boolean includeNewLines)
    {
        current = super.readLine(includeNewLines);
        return current;
    }
}
