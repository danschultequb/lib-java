package qub;

public class InMemoryCharacterReadStream extends InputStreamReaderToCharacterReadStream
{
    public InMemoryCharacterReadStream()
    {
        this((AsyncRunner)null);
    }

    public InMemoryCharacterReadStream(AsyncRunner asyncRunner)
    {
        this("", asyncRunner);
    }

    public InMemoryCharacterReadStream(String text)
    {
        this(text, (AsyncRunner)null);
    }

    public InMemoryCharacterReadStream(String text, AsyncRunner asyncRunner)
    {
        this(text, CharacterEncoding.UTF_8, asyncRunner);
    }

    public InMemoryCharacterReadStream(String text, CharacterEncoding encoding)
    {
        this(text, encoding, null);
    }

    public InMemoryCharacterReadStream(String text, CharacterEncoding encoding, AsyncRunner asyncRunner)
    {
        this(new InMemoryByteReadStream(encoding.encode(text), asyncRunner).endOfStream(), encoding);
    }

    public InMemoryCharacterReadStream(InMemoryByteReadStream byteReadStream, CharacterEncoding encoding)
    {
        super(byteReadStream, encoding);
    }
}
