package qub;

public class InMemoryLineReadStream extends CharacterReadStreamToLineReadStream
{
    public InMemoryLineReadStream(String text)
    {
        super(new InMemoryCharacterReadStream(text));
    }

    public InMemoryLineReadStream(String text, boolean includeNewLines)
    {
        super(new InMemoryCharacterReadStream(text), includeNewLines);
    }
}
