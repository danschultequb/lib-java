package qub;

/**
 * A ReadStream interface that reads characters.
 */
public interface CharacterReadStream extends AsyncDisposable, Iterator<Character>
{
    /**
     * Read a single character from this stream. This will block until a character is available.
     * @return The single character that was read, or an error if a character could not be read.
     */
    Result<Character> readCharacter();

    /**
     * Read a single character from this stream. This will not resolve until a character is
     * available.
     * @return The single character that was read, or an error if a character could not be read.
     */
    default AsyncFunction<Result<Character>> readCharacterAsync()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(this::readCharacter);
    }

    /**
     * Read up to the provided charactersToRead number of characters from this stream. If fewer
     * characters than charactersToRead are available, then fewer than charactersToRead characters
     * will be returned. If no characters are available, then this function will block until
     * characters become available.
     * @param charactersToRead The maximum number of characters to read from this stream.
     * @return The characters that were read, or an error if characters could not be read.
     */
    default Result<char[]> readCharacters(int charactersToRead)
    {
        PreCondition.assertGreaterThan(charactersToRead, 0, "charactersToRead");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        Result<char[]> result;

        char[] buffer = new char[charactersToRead];
        final Result<Integer> readCharactersResult = readCharacters(buffer);
        if (readCharactersResult.hasError())
        {
            result = readCharactersResult.convertError();
        }
        else
        {
            final Integer charactersRead = readCharactersResult.getValue();
            if (charactersRead == null)
            {
                buffer = null;
            }
            else if (charactersRead < charactersToRead)
            {
                buffer = Array.clone(buffer, 0, charactersRead);
            }
            result = Result.success(buffer);
        }

        return result;
    }

    /**
     * Read up to the provided charactersToRead number of characters from this stream. If fewer
     * characters than charactersToRead are available, then fewer than charactersToRead characters
     * will be returned. If no characters are available, then this function will not resolve until
     * characters become available.
     * @param charactersToRead The maximum number of characters to read from this stream.
     * @return The characters that were read, or an error if characters could not be read.
     */
    default AsyncFunction<Result<char[]>> readCharactersAsync(int charactersToRead)
    {
        PreCondition.assertGreaterThan(charactersToRead, 0, "charactersToRead");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readCharacters(charactersToRead));
    }

    /**
     * Read available characters into the provided char[] and return the number of characters that
     * were read. If no characters are available, then this function will not return until
     * characters become available.
     * @param outputCharacters The character array to read characters into.
     * @return The number of characters that were read, or an error if characters could not be read.
     */
    default Result<Integer> readCharacters(char[] outputCharacters)
    {
        PreCondition.assertNotNullAndNotEmpty(outputCharacters, "outputCharacters");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return readCharacters(outputCharacters, 0, outputCharacters.length);
    }

    /**
     * Read available characters into the provided char[] and return the number of characters that
     * were read. If no characters are available, then this function will not resolve until
     * characters become available.
     * @param outputCharacters The character array to read characters into.
     * @return The number of characters that were read, or an error if characters could not be read.
     */
    default AsyncFunction<Result<Integer>> readCharactersAsync(char[] outputCharacters)
    {
        PreCondition.assertNotNullAndNotEmpty(outputCharacters, "outputCharacters");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readCharacters(outputCharacters));
    }

    /**
     * Read up to length available characters into the provided character array at the provided startIndex and
     * return the number of characters that were read. If no characters are available, then this function will
     * not return until characters become available.
     * @param outputCharacters The character array to read characters into.
     * @param startIndex The start index in in outputCharacters to start writing characters to.
     * @param length The maximum number of characters to read.
     * @return The number of characters that were read.
     */
    default Result<Integer> readCharacters(char[] outputCharacters, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputCharacters, "outputCharacters");
        PreCondition.assertBetween(0, startIndex, outputCharacters.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, outputCharacters.length - startIndex, "length");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        int charactersRead = 0;
        Throwable error = null;

        while (error == null && charactersRead < length)
        {
            final Result<Character> readCharacterResult = readCharacter();
            if (readCharacterResult.hasError())
            {
                error = readCharacterResult.getError();
            }

            final Character character = readCharacterResult.getValue();
            if (character == null)
            {
                break;
            }
            else
            {
                outputCharacters[startIndex + charactersRead] = character;
                ++charactersRead;
            }
        }

        return error == null ? Result.success(charactersRead == 0 ? null : charactersRead) : Result.error(error);
    }

    /**
     * Read up to length available characters into the provided character array at the provided startIndex and
     * return the number of characters that were read. If no characters are available, then this function will
     * not resolve until characters become available.
     * @param outputCharacters The character array to read characters into.
     * @param startIndex The start index in in outputCharacters to start writing characters to.
     * @param length The maximum number of characters to read.
     * @return The number of characters that were read.
     */
    default AsyncFunction<Result<Integer>> readCharactersAsync(char[] outputCharacters, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputCharacters, "characters");
        PreCondition.assertBetween(0, startIndex, outputCharacters.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, outputCharacters.length - startIndex, "length");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readCharacters(outputCharacters, startIndex, length));
    }

    /**
     * Read all of the characters in this stream. The termination of the stream is marked when
     * readCharacter() returns a null character. This function will not return until all of the
     * characters in the stream have been read.
     * @return All of the characters in this stream, null if the end of the stream has been reached,
     * or an error if characters could not be read.
     */
    default Result<char[]> readAllCharacters()
    {
        final List<char[]> readCharacterArrays = new ArrayList<>();
        char[] buffer = new char[1024];

        while (true)
        {
            final Result<Integer> readCharacterResult = readCharacters(buffer);

            if (readCharacterResult.hasError())
            {
                break;
            }
            else
            {
                final Integer bytesRead = readCharacterResult.getValue();
                if (bytesRead == null || bytesRead == -1)
                {
                    break;
                }
                else
                {
                    readCharacterArrays.add(Array.clone(buffer, 0, bytesRead));

                    if (buffer.length == bytesRead)
                    {
                        buffer = new char[buffer.length * 2];
                    }
                }
            }
        }

        return Result.success(Array.mergeCharacters(readCharacterArrays));
    }

    /**
     * Read all of the characters in this stream. The termination of the stream is marked when
     * readCharacter() returns a null character. This function will not return until all of the
     * characters in the stream have been read.
     * @return All of the characters in this stream, null if the end of the stream has been reached,
     * or an error if characters could not be read.
     */
    default AsyncFunction<Result<char[]>> readAllCharactersAsync()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(this::readAllCharacters);
    }

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    default Result<char[]> readCharactersUntil(char value)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return readCharactersUntil(new char[] { value });
    }

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    default AsyncFunction<Result<char[]>> readCharactersUntilAsync(char value)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readCharactersUntil(value));
    }

    /**
     * Read characters from this stream until the end of the stream or until the provided values is
     * read.
     * @param values The values that will trigger the end of reading.
     * @return The characters read up until reading the provided values.
     */
    default Result<char[]> readCharactersUntil(char[] values)
    {
        PreCondition.assertNotNullAndNotEmpty(values, "characters");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return readCharactersUntil(String.valueOf(values));
    }

    /**
     * Read characters from this stream until the end of the stream or until the provided values is
     * read.
     * @param values The values that will trigger the end of reading.
     * @return The characters read up until reading the provided values.
     */
    default AsyncFunction<Result<char[]>> readCharactersUntilAsync(char[] values)
    {
        PreCondition.assertNotNullAndNotEmpty(values, "values");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readCharactersUntil(values));
    }

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    default Result<char[]> readCharactersUntil(String value)
    {
        PreCondition.assertNotNullAndNotEmpty(value, "value");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        Result<char[]> result;

        final CharacterEncoding characterEncoding = getCharacterEncoding();
        final Result<byte[]> encodedBytes = characterEncoding.encode(value);
        if (encodedBytes.hasError())
        {
            result = Result.error(encodedBytes.getError());
        }
        else
        {
            final ByteReadStream byteReadStream = asByteReadStream();
            final Result<byte[]> readBytes = byteReadStream.readBytesUntil(encodedBytes.getValue());
            if (readBytes.hasError())
            {
                result = Result.error(readBytes.getError());
            }
            else
            {
                result = characterEncoding.decode(readBytes.getValue());
            }
        }

        return result;
    }

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    default AsyncFunction<Result<char[]>> readCharactersUntilAsync(String value)
    {
        PreCondition.assertNotNullAndNotEmpty(value, "value");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readCharactersUntil(value));
    }

    /**
     * Read up to the provided charactersToRead number of characters from this stream. If fewer
     * characters than charactersToRead are available, then fewer than charactersToRead characters
     * will be returned. If no characters are available, then this function will block until
     * characters become available.
     * @param charactersToRead The maximum number of characters to read from this stream.
     * @return The characters that were read, or an error if characters could not be read.
     */
    default Result<String> readString(int charactersToRead)
    {
        PreCondition.assertGreaterThan(charactersToRead, 0, "charactersToRead");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return readCharacters(charactersToRead)
            .then((char[] characters) -> characters == null ? null : String.valueOf(characters));
    }

    /**
     * Read all of the characters in this stream. The termination of the stream is marked when
     * readCharacter() returns a null character. This function will not return until all of the
     * characters in the stream have been read.
     * @return All of the characters in this stream, null if the end of the stream has been reached,
     * or an error if characters could not be read.
     */
    default Result<String> readEntireString()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return readAllCharacters()
            .then((char[] characters) -> String.valueOf(characters));
    }

    /**
     * Read all of the characters in this stream. The termination of the stream is marked when
     * readCharacter() returns a null character. This function will not return until all of the
     * characters in the stream have been read.
     * @return All of the characters in this stream, null if the end of the stream has been reached,
     * or an error if characters could not be read.
     */
    default AsyncFunction<Result<String>> readEntireStringAsync()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(this::readEntireString);
    }

    /**
     * Read up to the provided charactersToRead number of characters from this stream. If fewer
     * characters than charactersToRead are available, then fewer than charactersToRead characters
     * will be returned. If no characters are available, then this function will not resolve until
     * characters become available.
     * @param charactersToRead The maximum number of characters to read from this stream.
     * @return The characters that were read, or an error if characters could not be read.
     */
    default AsyncFunction<Result<String>> readStringAsync(int charactersToRead)
    {
        PreCondition.assertGreaterThan(charactersToRead, 0, "charactersToRead");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readString(charactersToRead));
    }

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    default Result<String> readStringUntil(char value)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return readStringUntil(new char[] { value });
    }

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    default AsyncFunction<Result<String>> readStringUntilAsync(char value)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readStringUntil(value));
    }

    /**
     * Read characters from this stream until the end of the stream or until the provided values is
     * read.
     * @param values The values that will trigger the end of reading.
     * @return The characters read up until reading the provided values.
     */
    default Result<String> readStringUntil(char[] values)
    {
        PreCondition.assertNotNullAndNotEmpty(values, "values");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return readStringUntil(String.valueOf(values));
    }

    /**
     * Read characters from this stream until the end of the stream or until the provided values is
     * read.
     * @param values The values that will trigger the end of reading.
     * @return The characters read up until reading the provided values.
     */
    default AsyncFunction<Result<String>> readStringUntilAsync(char[] values)
    {
        PreCondition.assertNotNullAndNotEmpty(values, "values");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readStringUntil(values));
    }

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    default Result<String> readStringUntil(String value)
    {
        PreCondition.assertNotNullAndNotEmpty(value, "value");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        Result<String> result;
        final Result<char[]> charactersResult = readCharactersUntil(value);
        if (charactersResult.hasError())
        {
            result = Result.error(charactersResult.getError());
        }
        else
        {
            result = Result.success(String.valueOf(charactersResult.getValue()));
        }
        return result;
    }

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    default AsyncFunction<Result<String>> readStringUntilAsync(String value)
    {
        PreCondition.assertNotNullAndNotEmpty(value, "value");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readStringUntil(value));
    }

    /**
     * Get the CharacterEncoding that this CharacterReadStream uses to convert bytes to characters.
     * @return The CharacterEncoding that this CharacterReadStream uses to convert bytes to
     * characters.
     */
    CharacterEncoding getCharacterEncoding();

    /**
     * Convert this CharacterReadStream to a ByteReadStream.
     * @return The converted ByteReadStream.
     */
    ByteReadStream asByteReadStream();

    /**
     * Convert this CharacterReadStream to a LineReadStream.
     * @return The converted LineReadStream.
     */
    default LineReadStream asLineReadStream()
    {
        return new BasicLineReadStream(this);
    }

    /**
     * Convert this CharacterReadStream to a LineReadStream.
     * @param includeNewLines Whether or not to include the line terminating sequences in the read
     *                        lines.
     * @return The converted LineReadStream.
     */
    default LineReadStream asLineReadStream(boolean includeNewLines)
    {
        return new BasicLineReadStream(this, includeNewLines);
    }
}
