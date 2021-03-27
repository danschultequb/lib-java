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
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            final Character result = this.characterEncoding.iterateDecodedCharacters(this.byteReadStream).first();
            if (result == null)
            {
                throw new EndOfStreamException();
            }
            return result;
        });
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return this.characterEncoding;
    }

    @Override
    public CharacterToByteReadStream setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.characterEncoding = characterEncoding;

        return this;
    }

    @Override
    public boolean isDisposed()
    {
        return this.byteReadStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return this.byteReadStream.dispose();
    }

    @Override
    public Result<Byte> readByte()
    {
        return this.byteReadStream.readByte();
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return this.byteReadStream.readBytes(outputBytes, startIndex, length);
    }
}
