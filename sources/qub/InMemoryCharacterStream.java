package qub;

public class InMemoryCharacterStream extends InMemoryCharacterToByteStream
{
    public InMemoryCharacterStream()
    {
        this("");
    }

    public InMemoryCharacterStream(CharacterEncoding characterEncoding)
    {
        this("", characterEncoding);
    }

    public InMemoryCharacterStream(String text)
    {
        this(text, CharacterEncoding.UTF_8);
    }

    public InMemoryCharacterStream(String text, CharacterEncoding characterEncoding)
    {
        this(new InMemoryByteStream(Strings.isNullOrEmpty(text) ?null : characterEncoding.encode(text).await()), characterEncoding);
    }

    public InMemoryCharacterStream(InMemoryByteStream byteStream)
    {
        this(byteStream, CharacterEncoding.UTF_8);
    }

    public InMemoryCharacterStream(InMemoryByteStream byteStream, CharacterEncoding characterEncoding)
    {
        super(byteStream, characterEncoding);
    }
}
