package qub;

public class BasicCharacterReadStream extends CharacterReadStreamBase
{
    private final ByteReadStream byteReadStream;
    private final CharacterEncoding characterEncoding;
    private Character current;

    public BasicCharacterReadStream(ByteReadStream byteReadStream, CharacterEncoding characterEncoding)
    {
        this.byteReadStream = byteReadStream;
        this.characterEncoding = characterEncoding;
    }

    @Override
    public Result<Character> readCharacter()
    {
        Result<Character> result = CharacterReadStreamBase.validateCharacterReadStream(this);
        if (result == null)
        {
            result = characterEncoding.decodeNextCharacter(byteReadStream);
            current = result.getValue();
        }
        return result;
    }

    @Override
    public CharacterEncoding getEncoding()
    {
        return characterEncoding;
    }

    @Override
    public ByteReadStream asByteReadStream()
    {
        return byteReadStream;
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return byteReadStream.getAsyncRunner();
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
        return readCharacter().getValue() != null;
    }
}
