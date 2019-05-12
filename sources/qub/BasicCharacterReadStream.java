package qub;

public class BasicCharacterReadStream implements CharacterReadStream
{
    private final ByteReadStream byteReadStream;
    private final CharacterEncoding characterEncoding;
    private Character current;

    public BasicCharacterReadStream(ByteReadStream byteReadStream, CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.byteReadStream = byteReadStream;
        this.characterEncoding = characterEncoding;
    }

    @Override
    public Result<Character> readCharacter()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final Result<Character> result = characterEncoding.decodeNextCharacter(byteReadStream)
            .then((Character c) -> current = c)
            .catchError(() -> current = null);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return characterEncoding;
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

    @Override
    public boolean hasStarted()
    {
        return byteReadStream.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return current != null;
    }

    @Override
    public Character getCurrent()
    {
        return current;
    }

    @Override
    public boolean next()
    {
        return readCharacter().await() != null;
    }
}
