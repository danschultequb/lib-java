package qub;

/**
 * A CharacterToByteWriteStream that writes a prefix to the beginning of each line.
 */
public interface LinePrefixCharacterToByteWriteStream extends LinePrefixCharacterWriteStream, CharacterToByteWriteStream
{
    static LinePrefixCharacterToByteWriteStream create(ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return BasicLinePrefixCharacterToByteWriteStream.create(byteWriteStream);
    }

    static LinePrefixCharacterToByteWriteStream create(CharacterToByteWriteStream characterToByteWriteStream)
    {
        PreCondition.assertNotNull(characterToByteWriteStream, "characterToByteWriteStream");

        return BasicLinePrefixCharacterToByteWriteStream.create(characterToByteWriteStream);
    }

    /**
     * Set the encoding that this stream uses to convert characters to bytes.
     * @param characterEncoding The character encoding that this stream uses to convert characters
     *                          to bytes.
     * @return This object for method chaining.
     */
    LinePrefixCharacterToByteWriteStream setCharacterEncoding(CharacterEncoding characterEncoding);

    /**
     * Set the character that this stream will insert at the end of every line.
     * @param newLine The character that this stream will insert at the end of every line.
     * @return This object for method chaining.
     */
    default LinePrefixCharacterToByteWriteStream setNewLine(char newLine)
    {
        return (LinePrefixCharacterToByteWriteStream)CharacterToByteWriteStream.super.setNewLine(newLine);
    }

    /**
     * Set the characters that this stream will insert at the end of every line.
     * @param newLine The characters that this stream will insert at the end of every line.
     * @return This object for method chaining.
     */
    default LinePrefixCharacterToByteWriteStream setNewLine(char[] newLine)
    {
        return (LinePrefixCharacterToByteWriteStream)CharacterToByteWriteStream.super.setNewLine(newLine);
    }

    /**
     * Set the String that this stream will insert at the end of every line.
     * @param newLine The String that this stream will insert at the end of every line.
     * @return This object for method chaining.
     */
    LinePrefixCharacterToByteWriteStream setNewLine(String newLine);


    /**
     * Set the line prefix function.
     * @param linePrefixFunction The function that will return the line prefix.
     * @return This object for method chaining.
     */
    LinePrefixCharacterToByteWriteStream setLinePrefix(Function0<String> linePrefixFunction);

    /**
     * Set the line prefix.
     * @param linePrefix The line prefix.
     * @return This object for method chaining.
     */
    default LinePrefixCharacterToByteWriteStream setLinePrefix(String linePrefix)
    {
        PreCondition.assertNotNull(linePrefix, "linePrefix");

        return this.setLinePrefix(() -> linePrefix);
    }
}
