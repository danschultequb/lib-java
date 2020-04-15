package qub;

public class LinePrefixCharacterToByteWriteStream extends LinePrefixCharacterWriteStream implements CharacterToByteWriteStream
{
    private final CharacterToByteWriteStream characterToByteWriteStream;

    private LinePrefixCharacterToByteWriteStream(CharacterToByteWriteStream characterToByteWriteStream)
    {
        super(characterToByteWriteStream);

        this.characterToByteWriteStream = characterToByteWriteStream;
    }

    public static LinePrefixCharacterToByteWriteStream create(ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        final CharacterToByteWriteStream characterToByteWriteStream = CharacterToByteWriteStream.create(byteWriteStream);
        return LinePrefixCharacterToByteWriteStream.create(characterToByteWriteStream);
    }

    public static LinePrefixCharacterToByteWriteStream create(CharacterToByteWriteStream characterToByteWriteStream)
    {
        PreCondition.assertNotNull(characterToByteWriteStream, "characterToByteWriteStream");

        return new LinePrefixCharacterToByteWriteStream(characterToByteWriteStream);
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return this.characterToByteWriteStream.getCharacterEncoding();
    }

    @Override
    public LinePrefixCharacterToByteWriteStream setCharacterEncoding(CharacterEncoding characterEncoding)
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
    public LinePrefixCharacterToByteWriteStream setLinePrefix(Function0<String> linePrefixFunction)
    {
        return (LinePrefixCharacterToByteWriteStream)super.setLinePrefix(linePrefixFunction);
    }

    /**
     * Set the line prefix.
     * @param linePrefix The line prefix.
     * @return This object for method chaining.
     */
    public LinePrefixCharacterToByteWriteStream setLinePrefix(String linePrefix)
    {
        return (LinePrefixCharacterToByteWriteStream)super.setLinePrefix(linePrefix);
    }

    @Override
    public LinePrefixCharacterToByteWriteStream setNewLine(char newLine)
    {
        return (LinePrefixCharacterToByteWriteStream)super.setNewLine(newLine);
    }

    @Override
    public LinePrefixCharacterToByteWriteStream setNewLine(char[] newLine)
    {
        return (LinePrefixCharacterToByteWriteStream)super.setNewLine(newLine);
    }

    @Override
    public LinePrefixCharacterToByteWriteStream setNewLine(String newLine)
    {
        return (LinePrefixCharacterToByteWriteStream)super.setNewLine(newLine);
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
