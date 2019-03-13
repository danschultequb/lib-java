package qub;

public interface CharacterWriteStream extends Disposable
{
    CharacterEncoding getCharacterEncoding();

    default Result<Void> write(char toWrite)
    {
        return write(String.valueOf(toWrite));
    }

    default Result<Void> write(char[] toWrite)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return write(toWrite, 0, toWrite.length);
    }

    default Result<Void> write(char[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final CharacterEncoding characterEncoding = getCharacterEncoding();
        final ByteWriteStream byteWriteStream = asByteWriteStream();
        return characterEncoding.encode(toWrite, startIndex, length)
            .thenResult(byteWriteStream::writeAllBytes)
            .then(() -> {});
    }

    default Result<Void> write(String toWrite, Object... formattedStringArguments)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");

        toWrite = Strings.format(toWrite, formattedStringArguments);
        return write(toWrite.toCharArray());
    }

    /**
     * Write all of the bytes create the provided characterReadStream to this ByteWriteStream.
     * @param characterReadStream The ByteReadStream to read create.
     * @return Whether or not the writeByte was successful.
     */
    default Result<Void> writeAll(CharacterReadStream characterReadStream)
    {
        PreCondition.assertNotNull(characterReadStream, "characterReadStream");
        PreCondition.assertFalse(characterReadStream.isDisposed(), "characterReadStream.isDisposed()");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return Result.create(() ->
        {
            char[] buffer = new char[1024];
            while (true)
            {
                final Integer charactersRead = characterReadStream.readCharacters(buffer)
                    .catchError(EndOfStreamException.class)
                    .awaitError();
                if (charactersRead == null || charactersRead == -1)
                {
                    break;
                }
                else
                {
                    write(buffer, 0, charactersRead).awaitError();
                    if (charactersRead == buffer.length)
                    {
                        buffer = new char[buffer.length * 2];
                    }
                }
            }
        });
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
