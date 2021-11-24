package qub;

/**
 * A {@link CharacterToByteReadStream} type that ignores requests to dispose.
 */

public class IgnoreDisposeCharacterToByteReadStream extends CharacterToByteReadStreamDecorator<IgnoreDisposeCharacterToByteReadStream>
{
    protected IgnoreDisposeCharacterToByteReadStream(CharacterToByteReadStream innerStream)
    {
        super(innerStream);
    }

    public static IgnoreDisposeCharacterToByteReadStream create(CharacterToByteReadStream innerStream)
    {
        return new IgnoreDisposeCharacterToByteReadStream(innerStream);
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.success(false);
    }
}
