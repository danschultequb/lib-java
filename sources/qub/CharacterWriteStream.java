package qub;

public interface CharacterWriteStream extends Disposable
{
    CharacterEncoding getCharacterEncoding();

    default Result<Boolean> write(char toWrite)
    {
        return write(String.valueOf(toWrite));
    }

    default Result<Boolean> write(String toWrite, Object... formattedStringArguments)
    {
        Result<Boolean> result;

        toWrite = Strings.format(toWrite, formattedStringArguments);

        final Result<byte[]> encodedBytes = getCharacterEncoding().encode(toWrite);
        if (encodedBytes.hasError())
        {
            result = Result.done(false, encodedBytes.getError());
        }
        else
        {
            result = asByteWriteStream().write(encodedBytes.getValue());
        }
        return result;
    }

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
