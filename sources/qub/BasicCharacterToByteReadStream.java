package qub;

public class BasicCharacterToByteReadStream implements CharacterToByteReadStream
{
    private final ByteReadStream byteReadStream;
    private CharacterEncoding characterEncoding;

    protected BasicCharacterToByteReadStream(ByteReadStream byteReadStream, CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.byteReadStream = byteReadStream;
        this.characterEncoding = characterEncoding;
    }

    public static BasicCharacterToByteReadStream create(ByteReadStream byteReadStream, CharacterEncoding characterEncoding)
    {
        return new BasicCharacterToByteReadStream(byteReadStream, characterEncoding);
    }

    @Override
    public Result<Character> readCharacter()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return characterEncoding.decodeNextCharacter(byteReadStream);
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return characterEncoding;
    }

    @Override
    public CharacterToByteReadStream setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.characterEncoding = characterEncoding;

        return this;
    }

    @Override
    public ByteReadStream getByteReadStream()
    {
        return this.byteReadStream;
    }

    @Override
    public ByteReadStream asByteReadStream()
    {
        return byteReadStream;
    }

    @Override
    public boolean isDisposed()
    {
        return byteReadStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return byteReadStream.dispose();
    }
}
