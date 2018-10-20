package qub;

public abstract class CharacterWriteStreamBase implements CharacterWriteStream
{
    public static boolean shouldFormat(String toWrite, Object... formattedStringArguments)
    {
        return toWrite != null && !toWrite.isEmpty() && formattedStringArguments != null && formattedStringArguments.length > 0;
    }

    @Override
    public LineWriteStream asLineWriteStream()
    {
        return CharacterWriteStreamBase.asLineWriteStream(this);
    }

    @Override
    public LineWriteStream asLineWriteStream(String lineSeparator)
    {
        return CharacterWriteStreamBase.asLineWriteStream(this, lineSeparator);
    }

    /**
     * Convert this CharacterWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and '\n' as its line separator.
     * @return A LineWriteStream that wraps around this CharacterWriteStream.
     */
    public static LineWriteStream asLineWriteStream(CharacterWriteStream characterWriteStream)
    {
        return characterWriteStream.asLineWriteStream("\n");
    }

    /**
     * Convert this CharacterWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and the provided line separator.
     * @param lineSeparator The separator to insert between lines.
     * @return A LineWriteStream that wraps around this CharacterWriteStream.
     */
    public static LineWriteStream asLineWriteStream(CharacterWriteStream characterWriteStream, String lineSeparator)
    {
        return new CharacterWriteStreamToLineWriteStream(characterWriteStream, lineSeparator);
    }
}
