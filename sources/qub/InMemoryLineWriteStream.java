package qub;

public class InMemoryLineWriteStream extends CharacterWriteStreamToLineWriteStream
{
    private final InMemoryCharacterStream characterStream;

    public InMemoryLineWriteStream()
    {
        this(new InMemoryCharacterStream());
    }

    public InMemoryLineWriteStream(CharacterEncoding characterEncoding)
    {
        this(new InMemoryCharacterStream(characterEncoding));
    }

    public InMemoryLineWriteStream(CharacterEncoding characterEncoding, String lineSeparator)
    {
        this(new InMemoryCharacterStream(characterEncoding), lineSeparator);
    }

    public InMemoryLineWriteStream(InMemoryCharacterStream characterStream)
    {
        super(characterStream);

        this.characterStream = characterStream;
    }

    public InMemoryLineWriteStream(InMemoryCharacterStream characterStream, String lineSeparator)
    {
        super(characterStream, lineSeparator);

        this.characterStream = characterStream;
    }

    public byte[] getBytes()
    {
        return characterStream.getBytes();
    }

    public Result<String> getText()
    {
        return characterStream.getText();
    }
}
