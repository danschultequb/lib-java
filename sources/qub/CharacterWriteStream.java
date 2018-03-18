package qub;

public interface CharacterWriteStream extends Disposable
{
    CharacterEncoding getCharacterEncoding();

    boolean write(char toWrite);

    boolean write(String toWrite, Object... formattedStringArguments);

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
