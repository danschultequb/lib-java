package qub;

public class InMemoryCharacterReadStream extends InputStreamReaderToCharacterReadStream
{
    public InMemoryCharacterReadStream()
    {
        this("");
    }

    public InMemoryCharacterReadStream(String text)
    {
        this(text, CharacterEncoding.UTF_8);
    }

    public InMemoryCharacterReadStream(String text, CharacterEncoding encoding)
    {
        this(new InMemoryByteReadStream(encoding.encode(text)), encoding);
    }

    public InMemoryCharacterReadStream(InMemoryByteReadStream byteReadStream, CharacterEncoding encoding)
    {
        super(byteReadStream, encoding);
    }
}
