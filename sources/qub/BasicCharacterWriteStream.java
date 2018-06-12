package qub;

public class BasicCharacterWriteStream extends CharacterWriteStreamBase
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
    public Result<Boolean> write(char toWrite)
    {
        return BasicCharacterWriteStream.writeCharacter(byteWriteStream, characterEncoding, toWrite);
    }

    @Override
    public Result<Boolean> write(String toWrite, Object... formattedStringArguments)
    {
        return BasicCharacterWriteStream.writeString(byteWriteStream, characterEncoding, toWrite, formattedStringArguments);
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

    public static Result<Boolean> writeCharacter(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding, char character)
    {
        Result<Boolean> result;
        final Result<byte[]> encodedBytes = characterEncoding.encode(character);
        if (encodedBytes.hasError())
        {
            result = Result.done(false, encodedBytes.getError());
        }
        else
        {
            result = byteWriteStream.write(encodedBytes.getValue());
        }
        return result;
    }

    public static Result<Boolean> writeString(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding, String text, Object... formattedStringArguments)
    {
        Result<Boolean> result;
        if (CharacterWriteStreamBase.shouldFormat(text, formattedStringArguments))
        {
            text = String.format(text, formattedStringArguments);
        }
        final Result<byte[]> encodedBytes = characterEncoding.encode(text);
        if (encodedBytes.hasError())
        {
            result = Result.done(false, encodedBytes.getError());
        }
        else
        {
            result = byteWriteStream.write(encodedBytes.getValue());
        }
        return result;
    }
}
