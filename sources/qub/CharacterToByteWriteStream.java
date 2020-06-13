package qub;

/**
 * A CharacterWriteStream that encodes its written characters and then writes the encoded characters
 * to a ByteWriteStream.
 */
public interface CharacterToByteWriteStream extends CharacterWriteStream, ByteWriteStream
{
    /**
     * Create a new ByteWriteStreamToCharacterWriteStream that wraps around the provided
     * ByteWriteStream. The default CharacterEncoding of the returned stream will be UTF-8.
     * @param byteWriteStream The ByteWriteStream to wrap as a CharacterWriteStream.
     * @return The new ByteWriteStreamToCharacterWriteStream.
     */
    static BasicCharacterToByteWriteStream create(ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return new BasicCharacterToByteWriteStream(byteWriteStream);
    }

    /**
     * Get the character encoding that this stream uses to convert characters to bytes.
     * @return The character encoding that this stream uses to convert characters to bytes.
     */
    CharacterEncoding getCharacterEncoding();

    /**
     * Set the encoding that this stream uses to convert characters to bytes.
     * @param characterEncoding The character encoding that this stream uses to convert characters
     *                          to bytes.
     * @return This object for method chaining.
     */
    CharacterToByteWriteStream setCharacterEncoding(CharacterEncoding characterEncoding);

    @Override
    default CharacterToByteWriteStream setNewLine(char newLine)
    {
        return this.setNewLine(java.lang.String.valueOf(newLine));
    }

    @Override
    default CharacterToByteWriteStream setNewLine(char[] newLine)
    {
        return this.setNewLine(newLine == null ? null : java.lang.String.valueOf(newLine));
    }

    /**
     * Set the String that this stream will insert at the end of every line.
     * @param newLine The String that this stream will insert at the end of every line.
     * @return This object for method chaining.
     */
    CharacterToByteWriteStream setNewLine(String newLine);

    @Override
    default Result<Integer> writeLine()
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.write(this.getNewLine());
    }
}
