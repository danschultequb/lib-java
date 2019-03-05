package qub;

public class BasicLineReadStream implements LineReadStream
{
    private final CharacterReadStream characterReadStream;
    private boolean includeNewLines;
    private String current;

    public BasicLineReadStream(CharacterReadStream readStream)
    {
        this(readStream, false);
    }

    public BasicLineReadStream(CharacterReadStream characterReadStream, boolean includeNewLines)
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
    public void setIncludeNewLines(boolean includeNewLines)
    {
        this.includeNewLines = includeNewLines;
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
    public Result<String> readLine()
    {
        return LineReadStream.super.readLine()
            .onValue((String line) ->
            {
                current = line;
            });
    }

    @Override
    public Result<Boolean> dispose()
    {
        current = null;
        return characterReadStream.dispose();
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return characterReadStream.getAsyncRunner();
    }
}
