package qub;

/**
 * A WriteStream that writes Characters.
 */
public interface CharacterWriteStream extends Disposable
{
    /**
     * Write a single character.
     * @param toWrite The character to write.
     * @return The number of characters that were written.
     */
    Result<Integer> write(char toWrite);

    /**
     * Write the encoded bytes of the character to the byteWriteStream using the characterEncoding.
     * @param toWrite The character to write.
     * @param characterEncoding The character encoding to use.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of characters that were written.
     */
    static Result<Integer> write(char toWrite, CharacterEncoding characterEncoding, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream");

        return characterEncoding.encode(toWrite, byteWriteStream)
            .thenResult(Result::successOne);
    }

    /**
     * Write the entire array of characters.
     * @param toWrite The characters to write.
     * @return The number of characters that were written.
     */
    default Result<Integer> write(char[] toWrite)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return write(toWrite, 0, toWrite.length);
    }

    /**
     * Write the subarray of characters starting at the provided startIndex and going for the
     * provided length.
     * @param toWrite The array of characters to get the subarray from.
     * @param startIndex The start index into the array of characters to begin writing from.
     * @param length The number of characters to write.
     * @return The number of characters that were written.
     */
    Result<Integer> write(char[] toWrite, int startIndex, int length);

    /**
     * Write the subarray of characters starting at the startIndex and going for length number of
     * characters.
     * @param toWrite The array of characters to get the subarray from.
     * @param startIndex The start index into the array of characters to begin writing from.
     * @param length The number of characters to write.
     * @param characterEncoding The CharacterEncoding to encode the characters with.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of characters that were written.
     */
    static Result<Integer> write(char[] toWrite, int startIndex, int length, CharacterEncoding characterEncoding, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        return characterEncoding.encode(toWrite, startIndex, length, byteWriteStream)
            .then(() -> length);
    }

    /**
     * Write the provided String of characters.
     * @param toWrite The String to write.
     * @param formattedStringArguments The formatted String arguments.
     * @return The number of characters that were written.
     */
    Result<Integer> write(String toWrite, Object... formattedStringArguments);

    /**
     * Write the provided String of characters.
     * @param toWrite The String to write.
     * @param formattedStringArguments The formatted String arguments.
     * @param characterEncoding The CharacterEncoding to encode the String with.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of characters that were written.
     */
    static Result<Integer> write(String toWrite, Object[] formattedStringArguments, CharacterEncoding characterEncoding, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        final String formattedString = Strings.format(toWrite, formattedStringArguments);
        return characterEncoding.encode(formattedString, byteWriteStream)
            .then(formattedString::length);
    }

    /**
     * Write an end of line character sequence.
     * @return The number of characters that were written.
     */
    Result<Integer> writeLine();

    /**
     * Write the provided String of characters followed by an end of line character sequence.
     * @param toWrite The text to write.
     * @param formattedStringArguments The formatted String arguments.
     * @return The number of characters that were written.
     */
    default Result<Integer> writeLine(String toWrite, Object... formattedStringArguments)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");

        return write(toWrite, formattedStringArguments)
            .thenResult((Integer firstCharactersWrittenCount) ->
            {
                return writeLine()
                    .then((Integer secondCharactersWrittenCount) ->
                    {
                        return firstCharactersWrittenCount + secondCharactersWrittenCount;
                    });
            });
    }

    /**
     * Write all of the bytes create the provided characterReadStream to this ByteWriteStream.
     * @param characterReadStream The ByteReadStream to read create.
     * @return Whether or not the writeByte was successful.
     */
    default Result<Long> writeAll(CharacterReadStream characterReadStream)
    {
        PreCondition.assertNotNull(characterReadStream, "characterReadStream");
        PreCondition.assertFalse(characterReadStream.isDisposed(), "characterReadStream.isDisposed()");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return Result.create(() ->
        {
            long result = 0;
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
                    result += write(buffer, 0, charactersRead).awaitError();
                    if (charactersRead == buffer.length)
                    {
                        buffer = new char[buffer.length * 2];
                    }
                }
            }
            return result;
        });
    }
}
