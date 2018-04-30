package qub;

public class InMemoryLineReadStream extends CharacterReadStreamToLineReadStream
{
    public InMemoryLineReadStream(String text)
    {
        this(text, (AsyncRunner)null);
    }

    public InMemoryLineReadStream(String text, AsyncRunner asyncRunner)
    {
        super(new InMemoryCharacterReadStream(text, asyncRunner));
    }

    public InMemoryLineReadStream(String text, boolean includeNewLines)
    {
        this(text, includeNewLines, (AsyncRunner)null);
    }

    public InMemoryLineReadStream(String text, boolean includeNewLines, AsyncRunner asyncRunner)
    {
        super(new InMemoryCharacterReadStream(text, asyncRunner), includeNewLines);
    }
}
