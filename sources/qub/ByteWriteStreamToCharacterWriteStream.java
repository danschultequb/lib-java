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
    public boolean isOpen()
    {
        return byteWriteStream.isOpen();
    }

    @Override
    public void close()
    {
        byteWriteStream.close();
    }

    @Override
    public boolean write(char character)
    {
        final byte[] characterBytes = characterEncoding.encode(character);
        return byteWriteStream.write(characterBytes);
    }

    @Override
    public boolean write(String toWrite, Object... formattedStringArguments)
    {
        if (CharacterWriteStreamBase.shouldFormat(toWrite, formattedStringArguments))
        {
            toWrite = String.format(toWrite, formattedStringArguments);
        }
        final byte[] stringBytes = characterEncoding.encode(toWrite);
        return byteWriteStream.write(stringBytes);
    }

    @Override
    public ByteWriteStream asByteWriteStream()
    {
        return byteWriteStream;
    }
}
