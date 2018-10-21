package qub;

public class BasicCharacterWriteStream implements CharacterWriteStream
{
    private final ByteWriteStream byteWriteStream;
    private final CharacterEncoding characterEncoding;

    public BasicCharacterWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding)
    {
        this.byteWriteStream = byteWriteStream;
        this.characterEncoding = characterEncoding;
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return characterEncoding;
    }

    @Override
    public ByteWriteStream asByteWriteStream()
    {
        return byteWriteStream;
    }

    @Override
    public boolean isDisposed()
    {
        return byteWriteStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return byteWriteStream.dispose();
    }
}
