package qub;

public class ByteWriteStreamToCharacterWriteStream extends CharacterWriteStreamBase
{
    private final ByteWriteStream byteWriteStream;
    private final CharacterEncoding characterEncoding;

    ByteWriteStreamToCharacterWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding)
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
    public boolean isDisposed()
    {
        return byteWriteStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return byteWriteStream.dispose();
    }

    @Override
    public Result<Boolean> write(char character)
    {
        Result<Boolean> result;
        final Result<byte[]> characterBytes = characterEncoding.encode(character);
        if (characterBytes.hasError())
        {
            result = Result.error(characterBytes.getError());
        }
        else
        {
            result = byteWriteStream.write(characterBytes.getValue());
        }
        return result;
    }

    @Override
    public Result<Boolean> write(String toWrite, Object... formattedStringArguments)
    {
        Result<Boolean> result = Result.notNullAndNotEmpty(toWrite, "toWrite");
        if (result == null)
        {
            if (CharacterWriteStreamBase.shouldFormat(toWrite, formattedStringArguments))
            {
                toWrite = String.format(toWrite, formattedStringArguments);
            }

            result = Result.notNullAndNotEmpty(toWrite, "toWrite after formatting arguments");
            if (result == null)
            {
                final byte[] stringBytes = characterEncoding.encode(toWrite).getValue();
                result = byteWriteStream.write(stringBytes);
            }
        }
        return result;
    }

    @Override
    public ByteWriteStream asByteWriteStream()
    {
        return byteWriteStream;
    }
}
