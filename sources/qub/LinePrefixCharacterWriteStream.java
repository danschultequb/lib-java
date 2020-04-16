package qub;

/**
 * A CharacterWriteStream that writes a prefix to the beginning of each line.
 */
public interface LinePrefixCharacterWriteStream extends CharacterWriteStream
{
    static LinePrefixCharacterWriteStream create(CharacterWriteStream characterWriteStream)
    {
        PreCondition.assertNotNull(characterWriteStream, "characterWriteStream");

        return BasicLinePrefixCharacterWriteStream.create(characterWriteStream);
    }

    /**
     * Set the character that this stream will insert at the end of every line.
     * @param newLine The character that this stream will insert at the end of every line.
     * @return This object for method chaining.
     */
    default LinePrefixCharacterWriteStream setNewLine(char newLine)
    {
        return (LinePrefixCharacterWriteStream)CharacterWriteStream.super.setNewLine(newLine);
    }

    /**
     * Set the characters that this stream will insert at the end of every line.
     * @param newLine The characters that this stream will insert at the end of every line.
     * @return This object for method chaining.
     */
    default LinePrefixCharacterWriteStream setNewLine(char[] newLine)
    {
        return (LinePrefixCharacterWriteStream)CharacterWriteStream.super.setNewLine(newLine);
    }

    /**
     * Set the String that this stream will insert at the end of every line.
     * @param newLine The String that this stream will insert at the end of every line.
     * @return This object for method chaining.
     */
    LinePrefixCharacterWriteStream setNewLine(String newLine);

    /**
     * Get the current line prefix.
     * @return The current line prefix.
     */
    String getLinePrefix();

    /**
     * Set the line prefix function.
     * @param linePrefixFunction The function that will return the line prefix.
     * @return This object for method chaining.
     */
    LinePrefixCharacterWriteStream setLinePrefix(Function0<String> linePrefixFunction);

    /**
     * Set the line prefix.
     * @param linePrefix The line prefix.
     * @return This object for method chaining.
     */
    default LinePrefixCharacterWriteStream setLinePrefix(String linePrefix)
    {
        return this.setLinePrefix(() -> linePrefix);
    }
}
