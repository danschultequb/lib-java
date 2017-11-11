package qub;

public abstract class ByteWriteStreamBase implements ByteWriteStream
{
    @Override
    public CharacterWriteStream asCharacterWriteStream()
    {
        return asCharacterWriteStream(this);
    }

    @Override
    public LineWriteStream asLineWriteStream()
    {
        return asLineWriteStream(this);
    }

    @Override
    public LineWriteStream asLineWriteStream(CharacterEncoding encoding)
    {
        return asLineWriteStream(this, encoding);
    }

    @Override
    public LineWriteStream asLineWriteStream(String lineSeparator)
    {
        return asLineWriteStream(this, lineSeparator);
    }

    @Override
    public LineWriteStream asLineWriteStream(CharacterEncoding encoding, String lineSeparator)
    {
        return asLineWriteStream(this, encoding, lineSeparator);
    }

    public static CharacterWriteStream asCharacterWriteStream(ByteWriteStream byteWriteStream)
    {
        return asCharacterWriteStream(byteWriteStream, CharacterEncoding.UTF_8);
    }

    public static CharacterWriteStream asCharacterWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding encoding)
    {
        return byteWriteStream == null ? null : byteWriteStream.asCharacterWriteStream(encoding);
    }

    public static LineWriteStream asLineWriteStream(ByteWriteStream byteWriteStream)
    {
        final CharacterWriteStream characterWriteStream = asCharacterWriteStream(byteWriteStream);
        return characterWriteStream == null ? null : characterWriteStream.asLineWriteStream();
    }

    public static LineWriteStream asLineWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding encoding)
    {
        final CharacterWriteStream characterWriteStream = asCharacterWriteStream(byteWriteStream, encoding);
        return characterWriteStream == null ? null : characterWriteStream.asLineWriteStream();
    }

    public static LineWriteStream asLineWriteStream(ByteWriteStream byteWriteStream, String lineSeparator)
    {
        final CharacterWriteStream characterWriteStream = asCharacterWriteStream(byteWriteStream);
        return characterWriteStream == null ? null : characterWriteStream.asLineWriteStream(lineSeparator);
    }

    public static LineWriteStream asLineWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding encoding, String lineSeparator)
    {
        final CharacterWriteStream characterWriteStream = asCharacterWriteStream(byteWriteStream, encoding);
        return characterWriteStream == null ? null : characterWriteStream.asLineWriteStream(lineSeparator);
    }
}
