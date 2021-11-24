package qub;

/**
 * A {@link CharacterToByteWriteStream} type that ignores requests to dispose.
 */
public class IgnoreDisposeCharacterToByteWriteStream extends CharacterToByteWriteStreamDecorator<IgnoreDisposeCharacterToByteWriteStream>
{
    protected IgnoreDisposeCharacterToByteWriteStream(CharacterToByteWriteStream innerStream)
    {
        super(innerStream);
    }

    public static IgnoreDisposeCharacterToByteWriteStream create(CharacterToByteWriteStream innerStream)
    {
        return new IgnoreDisposeCharacterToByteWriteStream(innerStream);
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.success(false);
    }
}
