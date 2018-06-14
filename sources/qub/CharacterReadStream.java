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
    AsyncFunction<Result<Character>> readCharacterAsync();

    /**
     * Read up to the provided charactersToRead number of characters from this stream. If fewer
     * characters than charactersToRead are available, then fewer than charactersToRead characters
     * will be returned. If no characters are available, then this function will block until
     * characters become available.
     * @param charactersToRead The maximum number of characters to read from this stream.
     * @return The characters that were read, or an error if characters could not be read.
     */
    Result<char[]> readCharacters(int charactersToRead);

    /**
     * Read up to the provided charactersToRead number of characters from this stream. If fewer
     * characters than charactersToRead are available, then fewer than charactersToRead characters
     * will be returned. If no characters are available, then this function will not resolve until
     * characters become available.
     * @param charactersToRead The maximum number of characters to read from this stream.
     * @return The characters that were read, or an error if characters could not be read.
     */
    AsyncFunction<Result<char[]>> readCharactersAsync(int charactersToRead);

    /**
     * Read available characters into the provided char[] and return the number of characters that
     * were read. If no characters are available, then this function will not return until
     * characters become available.
     * @param outputCharacters The character array to read characters into.
     * @return The number of characters that were read, or an error if characters could not be read.
     */
    Result<Integer> readCharacters(char[] outputCharacters);

    /**
     * Read available characters into the provided char[] and return the number of characters that
     * were read. If no characters are available, then this function will not resolve until
     * characters become available.
     * @param outputCharacters The character array to read characters into.
     * @return The number of characters that were read, or an error if characters could not be read.
     */
    AsyncFunction<Result<Integer>> readCharactersAsync(char[] outputCharacters);

    /**
     * Read up to length available characters into the provided character array at the provided startIndex and
     * return the number of characters that were read. If no characters are available, then this function will
     * not return until characters become available.
     * @param outputCharacters The character array to read characters into.
     * @param startIndex The start index in in outputCharacters to start writing characters to.
     * @param length The maximum number of characters to read.
     * @return The number of characters that were read.
     */
    Result<Integer> readCharacters(char[] outputCharacters, int startIndex, int length);

    /**
     * Read up to length available characters into the provided character array at the provided startIndex and
     * return the number of characters that were read. If no characters are available, then this function will
     * not resolve until characters become available.
     * @param outputCharacters The character array to read characters into.
     * @param startIndex The start index in in outputCharacters to start writing characters to.
     * @param length The maximum number of characters to read.
     * @return The number of characters that were read.
     */
    AsyncFunction<Result<Integer>> readCharactersAsync(char[] outputCharacters, int startIndex, int length);

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    Result<char[]> readCharactersUntil(char value);

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    AsyncFunction<Result<char[]>> readCharactersUntilAsync(char value);

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    Result<char[]> readCharactersUntil(String value);

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    AsyncFunction<Result<char[]>> readCharactersUntilAsync(String value);

    /**
     * Read characters from this stream until the end of the stream or until the provided values is
     * read.
     * @param values The values that will trigger the end of reading.
     * @return The characters read up until reading the provided values.
     */
    Result<char[]> readCharactersUntil(char[] values);

    /**
     * Read characters from this stream until the end of the stream or until the provided values is
     * read.
     * @param values The values that will trigger the end of reading.
     * @return The characters read up until reading the provided values.
     */
    AsyncFunction<Result<char[]>> readCharactersUntilAsync(char[] values);

    /**
     * Read up to the provided charactersToRead number of characters from this stream. If fewer
     * characters than charactersToRead are available, then fewer than charactersToRead characters
     * will be returned. If no characters are available, then this function will block until
     * characters become available.
     * @param charactersToRead The maximum number of characters to read from this stream.
     * @return The characters that were read, or an error if characters could not be read.
     */
    Result<String> readString(int charactersToRead);

    /**
     * Read up to the provided charactersToRead number of characters from this stream. If fewer
     * characters than charactersToRead are available, then fewer than charactersToRead characters
     * will be returned. If no characters are available, then this function will not resolve until
     * characters become available.
     * @param charactersToRead The maximum number of characters to read from this stream.
     * @return The characters that were read, or an error if characters could not be read.
     */
    AsyncFunction<Result<String>> readStringAsync(int charactersToRead);

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    Result<String> readStringUntil(char value);

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    AsyncFunction<Result<String>> readStringUntilAsync(char value);

    /**
     * Read characters from this stream until the end of the stream or until the provided values is
     * read.
     * @param values The values that will trigger the end of reading.
     * @return The characters read up until reading the provided values.
     */
    Result<String> readStringUntil(char[] values);

    /**
     * Read characters from this stream until the end of the stream or until the provided values is
     * read.
     * @param values The values that will trigger the end of reading.
     * @return The characters read up until reading the provided values.
     */
    AsyncFunction<Result<String>> readStringUntilAsync(char[] values);

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    Result<String> readStringUntil(String value);

    /**
     * Read characters from this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    AsyncFunction<Result<String>> readStringUntilAsync(String value);

    CharacterEncoding getCharacterEncoding();

    ByteReadStream asByteReadStream();

    LineReadStream asLineReadStream();

    LineReadStream asLineReadStream(boolean includeNewLines);
}
