package qub;

public class InMemoryLineWriteStream extends CharacterWriteStreamToLineWriteStream
{
    public InMemoryLineWriteStream()
    {
        this(new InMemoryCharacterWriteStream());
    }

    public InMemoryLineWriteStream(CharacterEncoding encoding)
    {
        this(new InMemoryCharacterWriteStream(encoding));
    }

    public InMemoryLineWriteStream(CharacterEncoding encoding, String lineSeparator)
    {
        this(new InMemoryCharacterWriteStream(encoding), lineSeparator);
    }

    public InMemoryLineWriteStream(InMemoryCharacterWriteStream characterWriteStream)
    {
        super(characterWriteStream);
    }

    public InMemoryLineWriteStream(InMemoryCharacterWriteStream characterWriteStream, String lineSeparator)
    {
        super(characterWriteStream, lineSeparator);
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
