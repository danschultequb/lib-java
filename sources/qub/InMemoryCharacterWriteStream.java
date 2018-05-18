package qub;

public class InMemoryCharacterWriteStream extends CharacterWriteStreamBase
{
    private final InMemoryByteStream byteStream;
    private final CharacterEncoding characterEncoding;

    public InMemoryCharacterWriteStream()
    {
        this(new InMemoryByteStream());
    }

    public InMemoryCharacterWriteStream(InMemoryByteStream byteStream)
    {
        this(byteStream, CharacterEncoding.UTF_8);
    }

    public InMemoryCharacterWriteStream(CharacterEncoding characterEncoding)
    {
        this(new InMemoryByteStream(), characterEncoding);
    }

    public InMemoryCharacterWriteStream(InMemoryByteStream byteStream, CharacterEncoding characterEncoding)
    {
        this.byteStream = byteStream;
        this.characterEncoding = characterEncoding;
    }

    public byte[] getBytes()
    {
        return byteStream.getBytes();
    }

    public String getText()
    {
        final CharacterEncoding characterEncoding = getCharacterEncoding();
        final byte[] bytes = getBytes();
        return characterEncoding.decode(bytes);
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return characterEncoding;
    }

    @Override
    public Result<Boolean> write(char toWrite)
    {
        final CharacterEncoding characterEncoding = getCharacterEncoding();
        final byte[] bytes = characterEncoding.encode(toWrite);
        return byteStream.write(bytes);
    }

    @Override
    public Result<Boolean> write(String toWrite, Object... formattedStringArguments)
    {
        if (CharacterWriteStreamBase.shouldFormat(toWrite, formattedStringArguments))
        {
            toWrite = String.format(toWrite, formattedStringArguments);
        }
        final CharacterEncoding characterEncoding = getCharacterEncoding();
        final byte[] bytes = characterEncoding.encode(toWrite);
        return byteStream.write(bytes);
    }

    @Override
    public ByteWriteStream asByteWriteStream()
    {
        return byteStream;
    }

    @Override
    public boolean isDisposed()
    {
        return byteStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return byteStream.dispose();
    }
}
