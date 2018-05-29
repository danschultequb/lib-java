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
        this(text, CharacterEncoding.US_ASCII, asyncRunner);
    }

    public InMemoryCharacterReadStream(String text, CharacterEncoding encoding)
    {
        this(text, encoding, null);
    }

    public InMemoryCharacterReadStream(String text, CharacterEncoding encoding, AsyncRunner asyncRunner)
    {
        this(new InMemoryByteStream(encoding.encode(text).getValue(), asyncRunner).endOfStream(), encoding);
    }

    public InMemoryCharacterReadStream(InMemoryByteStream byteReadStream, CharacterEncoding encoding)
    {
        super(byteReadStream, encoding);
    }
}
