package qub;

/**
 * A WriteStream that writes Characters.
 */
public interface CharacterWriteStream extends Disposable
{
    /**
     * Create a new ByteWriteStreamToCharacterWriteStream that wraps around the provided
     * ByteWriteStream. The default CharacterEncoding of the returned stream will be UTF-8.
     * @param byteWriteStream The ByteWriteStream to wrap as a CharacterWriteStream.
     * @return The new ByteWriteStreamToCharacterWriteStream.
     */
    static CharacterToByteWriteStream create(ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return CharacterToByteWriteStream.create(byteWriteStream);
    }

    /**
     * Get the String that this stream will insert at the end of every line.
     * @return The String that this stream will insert at the end of every line.
     */
    String getNewLine();

    /**
     * Set the character that this stream will insert at the end of every line.
     * @param newLine The character that this stream will insert at the end of every line.
     * @return This object for method chaining.
     */
    default CharacterWriteStream setNewLine(char newLine)
    {
        return this.setNewLine(Characters.toString(newLine));
    }

    /**
     * Set the characters that this stream will insert at the end of every line.
     * @param newLine The characters that this stream will insert at the end of every line.
     * @return This object for method chaining.
     */
    default CharacterWriteStream setNewLine(char[] newLine)
    {
        return this.setNewLine(newLine == null ? null : java.lang.String.valueOf(newLine));
    }

    /**
     * Set the String that this stream will insert at the end of every line.
     * @param newLine The String that this stream will insert at the end of every line.
     * @return This object for method chaining.
     */
    CharacterWriteStream setNewLine(String newLine);

    /**
     * Write a single character.
     * @param toWrite The character to write.
     * @return The number of characters that were written.
     */
    Result<Integer> write(char toWrite);

    /**
     * Write the entire array of characters.
     * @param toWrite The characters to write.
     * @return The number of characters that were written.
     */
    default Result<Integer> write(char[] toWrite)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertNotDisposed(this, "this");

        return this.write(toWrite, 0, toWrite.length);
    }

    /**
     * Write the subarray of characters starting at the provided startIndex and going for the
     * provided length.
     * @param toWrite The array of characters to get the subarray from.
     * @param startIndex The start index into the array of characters to begin writing from.
     * @param length The number of characters to write.
     * @return The number of characters that were written.
     */
    default Result<Integer> write(char[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            final int endIndex = startIndex + length;
            for (int i = startIndex; i < endIndex; ++i)
            {
                this.write(toWrite[i]).await();
            }
            return length;
        });
    }

    /**
     * Write the provided String of characters.
     * @param toWrite The String to write.
     * @param formattedStringArguments The formatted String arguments.
     * @return The number of characters that were written.
     */
    Result<Integer> write(String toWrite, Object... formattedStringArguments);

    /**
     * Write an end of line character sequence.
     * @return The number of characters that were written.
     */
    default Result<Integer> writeLine()
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            int result = 0;

            final String newLine = this.getNewLine();
            if (newLine != null)
            {
                result = this.write(newLine).await();
            }

            return result;
        });
    }

    /**
     * Write the provided String of characters followed by an end of line character sequence.
     * @param toWrite The text to write.
     * @param formattedStringArguments The formatted String arguments.
     * @return The number of characters that were written.
     */
    default Result<Integer> writeLine(String toWrite, Object... formattedStringArguments)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");

        return Result.create(() ->
        {
            int result = 0;
            result += this.write(toWrite, formattedStringArguments).await();
            result += this.writeLine().await();
            return result;
        });
    }
}
