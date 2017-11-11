package qub;

public class InMemoryLineWriteStream extends CharacterWriteStreamToLineWriteStream
{
    public InMemoryLineWriteStream()
    {
        this(new InMemoryCharacterWriteStream());
    }

    public InMemoryLineWriteStream(InMemoryCharacterWriteStream characterWriteStream)
    {
        super(characterWriteStream);
    }

    @Override
    protected InMemoryCharacterWriteStream getCharacterWriteStream()
    {
        return (InMemoryCharacterWriteStream)super.getCharacterWriteStream();
    }

    public byte[] getBytes()
    {
        return getCharacterWriteStream().getBytes();
    }

    public String getText()
    {
        return getCharacterWriteStream().getText();
    }
}
