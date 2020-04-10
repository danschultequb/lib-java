package qub;

/**
 * A CharacterWriteStream that wraps a ByteWriteStream so all characters written to this stream will
 * be encoded and written to the inner ByteWriteStream.
 */
public class BasicCharacterToByteWriteStream implements CharacterToByteWriteStream
{
    private final ByteWriteStream byteWriteStream;
    private CharacterEncoding characterEncoding;
    private String newLine;

    public BasicCharacterToByteWriteStream(ByteWriteStream byteWriteStream)
    {
        this(byteWriteStream, CharacterEncoding.UTF_8);
    }

    public BasicCharacterToByteWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.byteWriteStream = byteWriteStream;
        this.characterEncoding = characterEncoding;
        this.newLine = "\n";
    }

    @Override
    public ByteWriteStream getByteWriteStream()
    {
        return this.byteWriteStream;
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return this.characterEncoding;
    }

    @Override
    public BasicCharacterToByteWriteStream setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.characterEncoding = characterEncoding;

        return this;
    }

    @Override
    public String getNewLine()
    {
        return this.newLine;
    }

    @Override
    public CharacterToByteWriteStream setNewLine(String newLine)
    {
        PreCondition.assertNotNull(newLine, "newLine");

        this.newLine = newLine;

        return this;
    }
}
