package qub;

public class BasicLinePrefixCharacterToByteWriteStream extends BasicLinePrefixCharacterWriteStream implements LinePrefixCharacterToByteWriteStream
{
    private final CharacterToByteWriteStream characterToByteWriteStream;

    private BasicLinePrefixCharacterToByteWriteStream(CharacterToByteWriteStream characterToByteWriteStream)
    {
        super(characterToByteWriteStream);

        this.characterToByteWriteStream = characterToByteWriteStream;
    }

    public static BasicLinePrefixCharacterToByteWriteStream create(ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        final CharacterToByteWriteStream characterToByteWriteStream = CharacterToByteWriteStream.create(byteWriteStream);
        return BasicLinePrefixCharacterToByteWriteStream.create(characterToByteWriteStream);
    }

    public static BasicLinePrefixCharacterToByteWriteStream create(CharacterToByteWriteStream characterToByteWriteStream)
    {
        PreCondition.assertNotNull(characterToByteWriteStream, "characterToByteWriteStream");

        return new BasicLinePrefixCharacterToByteWriteStream(characterToByteWriteStream);
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return this.characterToByteWriteStream.getCharacterEncoding();
    }

    @Override
    public BasicLinePrefixCharacterToByteWriteStream setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.characterToByteWriteStream.setCharacterEncoding(characterEncoding);

        return this;
    }

    /**
     * Set the line prefix function.
     * @param linePrefixFunction The function that will return the line prefix.
     * @return This object for method chaining.
     */
    public BasicLinePrefixCharacterToByteWriteStream setLinePrefix(Function0<String> linePrefixFunction)
    {
        return (BasicLinePrefixCharacterToByteWriteStream)super.setLinePrefix(linePrefixFunction);
    }

    @Override
    public BasicLinePrefixCharacterToByteWriteStream setNewLine(String newLine)
    {
        return (BasicLinePrefixCharacterToByteWriteStream)super.setNewLine(newLine);
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        return this.characterToByteWriteStream.write(toWrite);
    }

    @Override
    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        return this.characterToByteWriteStream.write(toWrite, startIndex, length);
    }
}
