package qub;

public abstract class CharacterWriteStreamBase implements CharacterWriteStream
{
    private final ByteWriteStream byteWriteStream;
    private final CharacterEncoding characterEncoding;

    protected CharacterWriteStreamBase(ByteWriteStream byteWriteStream, CharacterEncoding encoding)
    {
        this.byteWriteStream = byteWriteStream;
        this.characterEncoding = encoding;
    }

    protected ByteWriteStream getByteWriteStream()
    {
        return byteWriteStream;
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
        try
        {
            byteWriteStream.close();
        }
        catch (Exception ignored)
        {
        }
    }

    @Override
    public boolean write(byte toWrite)
    {
        boolean result = false;
        try
        {
            result = byteWriteStream.write(toWrite);
        }
        catch (Exception ignored)
        {
        }
        return result;
    }

    @Override
    public boolean write(byte[] toWrite)
    {
        boolean result = false;
        try
        {
            result = byteWriteStream.write(toWrite);
        }
        catch (Exception ignored)
        {
        }
        return result;
    }

    @Override
    public boolean write(byte[] toWrite, int startIndex, int length)
    {
        boolean result = false;
        try
        {
            result = byteWriteStream.write(toWrite, startIndex, length);
        }
        catch (Exception ignored)
        {
        }
        return result;
    }

    @Override
    public ByteWriteStream asByteWriteStream()
    {
        return byteWriteStream;
    }

    @Override
    public LineWriteStream asLineWriteStream()
    {
        return asLineWriteStream(this);
    }

    @Override
    public LineWriteStream asLineWriteStream(String lineSeparator)
    {
        return asLineWriteStream(this, lineSeparator);
    }

    public static LineWriteStream asLineWriteStream(CharacterWriteStream characterWriteStream)
    {
        return characterWriteStream == null ? null : new CharacterWriteStreamToLineWriteStream(characterWriteStream);
    }

    public static LineWriteStream asLineWriteStream(CharacterWriteStream characterWriteStream, String lineSeparator)
    {
        return characterWriteStream == null ? null : new CharacterWriteStreamToLineWriteStream(characterWriteStream, lineSeparator);
    }
}
