package qub;

import java.io.IOException;

public interface CharacterWriteStream extends Stream
{
    CharacterEncoding getCharacterEncoding();

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
    default LineWriteStream asLineWriteStream()
    {
        return asLineWriteStream("\n");
    }

    /**
     * Convert this CharacterWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and the provided line separator.
     * @param lineSeparator The separator to insert between lines.
     * @return A LineWriteStream that wraps around this CharacterWriteStream.
     */
    default LineWriteStream asLineWriteStream(String lineSeparator)
    {
        return new CharacterWriteStreamToLineWriteStream(this, lineSeparator);
    }
}
