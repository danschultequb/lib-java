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
     * Get the ByteWriteStream that the encoded characters will be written to.
     * @return The ByteWriteStream that the encoded characters will be written to.
     */
    ByteWriteStream getByteWriteStream();

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
        PreCondition.assertNotNull(newLine, "newLine");

        return this.setNewLine(java.lang.String.valueOf(newLine));
    }

    /**
     * Set the String that this stream will insert at the end of every line.
     * @param newLine The String that this stream will insert at the end of every line.
     * @return This object for method chaining.
     */
    CharacterToByteWriteStream setNewLine(String newLine);

    @Override
    default Result<Integer> write(char toWrite)
    {
        PreCondition.assertNotDisposed(this);

        return Result.create(() ->
        {
            this.getCharacterEncoding().encode(toWrite, this.getByteWriteStream()).await();
            return 1;
        });
    }

    @Override
    default Result<Integer> write(char[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);
        PreCondition.assertNotDisposed(this);

        return Result.create(() ->
        {
            this.getCharacterEncoding().encode(toWrite, startIndex, length, this.getByteWriteStream()).await();
            return length;
        });
    }

    @Override
    default Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertNotDisposed(this);

        return Result.create(() ->
        {
            final String formattedString = Strings.format(toWrite, formattedStringArguments);
            this.getCharacterEncoding().encode(formattedString, this.getByteWriteStream()).await();
            return formattedString.length();
        });
    }

    @Override
    default Result<Integer> writeLine()
    {
        PreCondition.assertNotDisposed(this);

        return this.write(this.getNewLine());
    }

    /**
     * Write the provided byte to this ByteWriteStream.
     * @param toWrite The byte to writeByte to this stream.
     * @return The number of bytes that were written.
     */
    default Result<Integer> write(byte toWrite)
    {
        return this.getByteWriteStream().write(toWrite);
    }

    /**
     * Attempt to write the provided subsection of bytes to this ByteWriteStream. It is possible
     * that not all of the bytes will be written. If you want to ensure that all of the bytes will
     * be written, then use writeAll() instead.
     * @param toWrite The array of bytes that contains the bytes to writeByte to this stream.
     * @param startIndex The start index of the subsection inside toWrite to writeByte.
     * @param length The number of bytes to write.
     * @return The number of bytes that were written.
     */
    default Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        return this.getByteWriteStream().write(toWrite, startIndex, length);
    }

    @Override
    default boolean isDisposed()
    {
        return this.getByteWriteStream().isDisposed();
    }

    @Override
    default Result<Boolean> dispose()
    {
        return this.getByteWriteStream().dispose();
    }
}
