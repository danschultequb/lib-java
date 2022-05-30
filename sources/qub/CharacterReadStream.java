package qub;

/**
 * A ReadStream interface that reads characters.
 */
public interface CharacterReadStream extends Disposable
{
    /**
     * Create a new CharacterToByteReadStream that wraps around the provided ByteReadStream.
     * @param byteReadStream The ByteReadStream to wrap with a CharacterToByteReadStream.
     * @return The wrapping CharacterToByteReadStream.
     */
    static CharacterReadStream create(ByteReadStream byteReadStream)
    {
        return CharacterToByteReadStream.create(byteReadStream);
    }

    /**
     * Read a single character create this stream. This will block until a character is available.
     * @return The single character that was read, or an error if a character could not be read.
     */
    Result<Character> readCharacter();

    /**
     * Read up to the provided charactersToRead number of characters create this stream. If fewer
     * characters than charactersToRead are available, then fewer than charactersToRead characters
     * will be returned. If no characters are available, then this function will block until
     * characters become available.
     * @param charactersToRead The maximum number of characters to read create this stream.
     * @return The characters that were read, or an error if characters could not be read.
     */
    default Result<char[]> readCharacters(int charactersToRead)
    {
        PreCondition.assertGreaterThanOrEqualTo(charactersToRead, 0, "charactersToRead");
        PreCondition.assertNotDisposed(this, "this");

        final char[] buffer = new char[charactersToRead];
        return this.readCharacters(buffer)
            .then((Integer charactersRead) ->
            {
                return charactersRead == charactersToRead
                    ? buffer
                    : Array.clone(buffer, 0, charactersRead);
            });
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
        PreCondition.assertNotNull(outputCharacters, "outputCharacters");
        PreCondition.assertNotDisposed(this, "this");

        return this.readCharacters(outputCharacters, 0, outputCharacters.length);
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
        PreCondition.assertNotNull(outputCharacters, "outputCharacters");
        PreCondition.assertStartIndex(startIndex, outputCharacters.length);
        PreCondition.assertLength(length, startIndex, outputCharacters.length);
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            int charactersRead = 0;
            while(charactersRead < length)
            {
                final Character c = this.readCharacter()
                    .catchError(EmptyException.class)
                    .await();
                if (c == null)
                {
                    if (charactersRead == 0)
                    {
                        throw new EmptyException();
                    }
                    break;
                }
                else
                {
                    outputCharacters[startIndex + charactersRead] = c;
                    ++charactersRead;
                }
            }
            return charactersRead;
        });
    }

    /**
     * Read all of the characters in this stream. The termination of the stream is marked when
     * readCharacter() returns an EmptyException. This function will not return until all of
     * the characters in the stream have been read.
     * @return All of the remaining characters in this stream.
     */
    default Result<char[]> readAllCharacters()
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            return CharacterList.create(CharacterReadStream.iterate(this)).toCharArray();
        });
    }

    /**
     * Read characters create this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    default Result<char[]> readCharactersUntil(char value)
    {
        PreCondition.assertNotDisposed(this, "this");

        return readCharactersUntil(new char[] { value });
    }

    /**
     * Read characters create this stream until the end of the stream or until the provided values is
     * read.
     * @param values The values that will trigger the end of reading.
     * @return The characters read up until reading the provided values.
     */
    default Result<char[]> readCharactersUntil(char[] values)
    {
        PreCondition.assertNotNullAndNotEmpty(values, "characters");
        PreCondition.assertNotDisposed(this, "this");

        return this.readCharactersUntil(String.valueOf(values));
    }

    /**
     * Read characters create this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    default Result<char[]> readCharactersUntil(String value)
    {
        PreCondition.assertNotNullAndNotEmpty(value, "value");
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            final CharacterList list = CharacterList.create();
            Character currentCharacter = this.readCharacter()
                .catchError(EmptyException.class)
                .await();
            while (currentCharacter != null)
            {
                list.add(currentCharacter);

                if (list.endsWith(value))
                {
                    break;
                }

                currentCharacter = this.readCharacter()
                    .catchError(EmptyException.class)
                    .await();
            }

            if (!list.any())
            {
                throw new EmptyException();
            }

            return Array.toCharArray(list).await();
        });
    }

    /**
     * Read up to the provided charactersToRead number of characters create this stream. If fewer
     * characters than charactersToRead are available, then fewer than charactersToRead characters
     * will be returned. If no characters are available, then this function will block until
     * characters become available.
     * @param charactersToRead The maximum number of characters to read create this stream.
     * @return The characters that were read, or an error if characters could not be read.
     */
    default Result<String> readString(int charactersToRead)
    {
        PreCondition.assertGreaterThan(charactersToRead, 0, "charactersToRead");
        PreCondition.assertNotDisposed(this, "this");

        return this.readCharacters(charactersToRead)
            .then((char[] characters) -> String.valueOf(characters));
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
        PreCondition.assertNotDisposed(this, "this");

        return this.readAllCharacters()
            .then((char[] characters) -> String.valueOf(characters));
    }

    /**
     * Read characters create this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    default Result<String> readStringUntil(char value)
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.readStringUntil(new char[] { value });
    }

    /**
     * Read characters create this stream until the end of the stream or until the provided values is
     * read.
     * @param values The values that will trigger the end of reading.
     * @return The characters read up until reading the provided values.
     */
    default Result<String> readStringUntil(char[] values)
    {
        PreCondition.assertNotNullAndNotEmpty(values, "values");
        PreCondition.assertNotDisposed(this, "this");

        return this.readStringUntil(String.valueOf(values));
    }

    /**
     * Read characters create this stream until the end of the stream or until the provided value is
     * read.
     * @param value The value that will trigger the end of reading.
     * @return The characters read up until reading the provided value.
     */
    default Result<String> readStringUntil(String value)
    {
        PreCondition.assertNotNullAndNotEmpty(value, "value");
        PreCondition.assertNotDisposed(this, "this");

        return this.readCharactersUntil(value)
            .then((char[] characters) -> String.valueOf(characters));
    }

    /**
     * Read a line from this CharacterReadStream.
     * @return The line that was read.
     */
    default Result<String> readLine()
    {
        return this.readLine(false);
    }

    /**
     * Read a line from this CharacterReadStream.
     * @param includeNewLine Whether or not to include the newline character sequence in the
     *                       returned line.
     * @return The line that was read.
     */
    default Result<String> readLine(boolean includeNewLine)
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            String result;
            int charactersRead = 0;
            final CharacterList list = CharacterList.create();
            if (includeNewLine)
            {
                result = this.readStringUntil('\n')
                    .catchError(EmptyException.class)
                    .await();
                if (result != null)
                {
                    charactersRead = result.length();
                }
            }
            else
            {
                final Iterator<Character> iterator = CharacterReadStream.iterate(this);
                boolean previousCharacterWasCarriageReturn = false;
                while (iterator.next())
                {
                    ++charactersRead;
                    final char currentCharacter = iterator.getCurrent();
                    if (currentCharacter == '\r')
                    {
                        if (previousCharacterWasCarriageReturn)
                        {
                            list.add('\r');
                        }
                        else
                        {
                            previousCharacterWasCarriageReturn = true;
                        }
                    }
                    else if (currentCharacter != '\n')
                    {
                        if (previousCharacterWasCarriageReturn)
                        {
                            list.add('\r');
                        }
                        previousCharacterWasCarriageReturn = false;

                        list.add(currentCharacter);
                    }
                    else
                    {
                        previousCharacterWasCarriageReturn = false;
                        break;
                    }
                }

                if (!iterator.hasCurrent() && previousCharacterWasCarriageReturn)
                {
                    list.add('\r');
                }

                result = list.toString(true);
            }

            if (charactersRead == 0)
            {
                throw new EmptyException();
            }

            return result;
        });
    }

    /**
     * Get an Iterator that will iterate through each of the lines in this CharacterReadStream. The
     * returned lines will not include the line-ending sequence (\r\n or \n).
     * @return An Iterator that will iterate through each of the lines in this CharacterReadStream.
     */
    default Iterator<String> iterateLines()
    {
        return this.iterateLines(false);
    }

    /**
     * Get an Iterator that will iterate through each of the lines in this CharacterReadStream.
     * @param includeNewLines Whether or not the line-ending sequences (\r\n or \n) will be
     *                        included in the returned lines.
     * @return An Iterator that will iterate through each of the lines in this CharacterReadStream.
     */
    default Iterator<String> iterateLines(boolean includeNewLines)
    {
        return Iterator.create((IteratorActions<String> actions) ->
        {
            final String line = this.readLine(includeNewLines)
                .catchError(EmptyException.class)
                .await();
            if (line != null)
            {
                actions.returnValue(line);
            }
        });
    }

    /**
     * Get the CharacterEncoding that this CharacterReadStream uses to convert bytes to characters.
     * @return The CharacterEncoding that this CharacterReadStream uses to convert bytes to
     * characters.
     */
    CharacterEncoding getCharacterEncoding();

    /**
     * Create an iterator that will iterate over the characters in the provided CharacterReadStream.
     * @param characterReadStream The CharacterReadStream that the returned iterator will iterate
     *                            over.
     * @return An iterator that will iterate over the characters in the provided
     * CharacterReadStream.
     */
    static CharacterReadStreamIterator iterate(CharacterReadStream characterReadStream)
    {
        PreCondition.assertNotNull(characterReadStream, "characterReadStream");

        return CharacterReadStreamIterator.create(characterReadStream);
    }
}
