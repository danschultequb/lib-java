package qub;

public interface CharacterWriteStream extends Stream
{
    CharacterEncoding getCharacterEncoding();

    /**
     * Write the provided byte to this ByteWriteStream.
     * @param toWrite The byte to write to this stream.
     */
    boolean write(byte toWrite);

    /**
     * Write the provided bytes to this ByteWriteStream.
     * @param toWrite The bytes to write to this stream.
     */
    boolean write(byte[] toWrite);

    /**
     * Write the provided subsection of bytes to this ByteWriteStream.
     * @param toWrite The array of bytes that contains the bytes to write to this stream.
     * @param startIndex The start index of the subsection inside toWrite to write.
     * @param length The number of bytes to write.
     */
    boolean write(byte[] toWrite, int startIndex, int length);

    boolean write(char toWrite);

    boolean write(String toWrite);

    /**
     * Convert this CharacterWriteStream to a ByteWriteStream.
     * @return The converted ByteWriteStream.
     */
    ByteWriteStream asByteWriteStream();

    /**
     * Convert this CharacterWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and '\n' as its line separator.
     * @return A LineWriteStream that wraps around this CharacterWriteStream.
     */
    LineWriteStream asLineWriteStream();

    /**
     * Convert this CharacterWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and the provided line separator.
     * @param lineSeparator The separator to insert between lines.
     * @return A LineWriteStream that wraps around this CharacterWriteStream.
     */
    LineWriteStream asLineWriteStream(String lineSeparator);
}
