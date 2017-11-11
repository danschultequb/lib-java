package qub;

public class InMemoryLineReadStream extends CharacterReadStreamToLineReadStream
{
    public InMemoryLineReadStream(String text)
    {
        super(new InMemoryCharacterReadStream(text));
    }
}
