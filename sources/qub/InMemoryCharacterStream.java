package qub;

public class InMemoryCharacterStream extends BasicCharacterReadStream implements CharacterWriteStream
{
    private final InMemoryByteStream byteStream;

    public InMemoryCharacterStream()
    {
        this((AsyncRunner)null);
    }

    public InMemoryCharacterStream(CharacterEncoding characterEncoding)
    {
        this(characterEncoding, null);
    }

    public InMemoryCharacterStream(CharacterEncoding characterEncoding, AsyncRunner asyncRunner)
    {
        this("", characterEncoding, asyncRunner);
    }

    public InMemoryCharacterStream(AsyncRunner asyncRunner)
    {
        this("", asyncRunner);
    }

    public InMemoryCharacterStream(String text)
    {
        this(text, (AsyncRunner)null);
    }

    public InMemoryCharacterStream(String text, AsyncRunner asyncRunner)
    {
        this(text, CharacterEncoding.UTF_8, asyncRunner);
    }

    public InMemoryCharacterStream(String text, CharacterEncoding characterEncoding)
    {
        this(text, characterEncoding, null);
    }

    public InMemoryCharacterStream(String text, CharacterEncoding characterEncoding, AsyncRunner asyncRunner)
    {
        this(new InMemoryByteStream(characterEncoding.encode(text).getValue(), asyncRunner), characterEncoding);
    }

    public InMemoryCharacterStream(InMemoryByteStream byteStream)
    {
        this(byteStream, CharacterEncoding.UTF_8);
    }

    public InMemoryCharacterStream(InMemoryByteStream byteStream, CharacterEncoding characterEncoding)
    {
        super(byteStream, characterEncoding);

        this.byteStream = byteStream;
    }

    public byte[] getBytes()
    {
        return byteStream.getBytes();
    }

    public Result<String> getText()
    {
        final byte[] bytes = getBytes();
        return bytes == null || bytes.length == 0 ? Result.success("") : getCharacterEncoding().decodeAsString(getBytes());
    }

    @Override
    public ByteWriteStream asByteWriteStream()
    {
        return byteStream;
    }

    public InMemoryCharacterStream endOfStream()
    {
        byteStream.endOfStream();
        return this;
    }
}
