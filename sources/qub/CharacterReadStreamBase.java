package qub;

public abstract class CharacterReadStreamBase implements CharacterReadStream
{
    @Override
    public abstract Result<Character> readCharacter();

    @Override
    public AsyncFunction<Result<Character>> readCharacterAsync()
    {
        return CharacterReadStreamBase.readCharacterAsync(this);
    }

    @Override
    public Result<char[]> readCharacters(int charactersToRead)
    {
        return CharacterReadStreamBase.readCharacters(this, charactersToRead);
    }

    @Override
    public AsyncFunction<Result<char[]>> readCharactersAsync(int charactersToRead)
    {
        return CharacterReadStreamBase.readCharactersAsync(this, charactersToRead);
    }

    @Override
    public Result<Integer> readCharacters(char[] characters)
    {
        return CharacterReadStreamBase.readCharacters(this, characters);
    }

    @Override
    public AsyncFunction<Result<Integer>> readCharactersAsync(char[] characters)
    {
        return CharacterReadStreamBase.readCharactersAsync(this, characters);
    }

    @Override
    public Result<Integer> readCharacters(char[] characters, int startIndex, int length)
    {
        return CharacterReadStreamBase.readCharacters(this, characters, startIndex, length);
    }

    @Override
    public AsyncFunction<Result<Integer>> readCharactersAsync(char[] characters, int startIndex, int length)
    {
        return CharacterReadStreamBase.readCharactersAsync(this, characters, startIndex, length);
    }

    @Override
    public Result<char[]> readCharactersUntil(char character)
    {
        return CharacterReadStreamBase.readCharactersUntil(this, character);
    }

    @Override
    public AsyncFunction<Result<char[]>> readCharactersUntilAsync(char character)
    {
        return CharacterReadStreamBase.readCharactersUntilAsync(this, character);
    }

    @Override
    public Result<char[]> readCharactersUntil(char[] characters)
    {
        return CharacterReadStreamBase.readCharactersUntil(this, characters);
    }

    @Override
    public AsyncFunction<Result<char[]>> readCharactersUntilAsync(char[] characters)
    {
        return CharacterReadStreamBase.readCharactersUntilAsync(this, characters);
    }

    @Override
    public Result<char[]> readCharactersUntil(String text)
    {
        return CharacterReadStreamBase.readCharactersUntil(this, text);
    }

    @Override
    public AsyncFunction<Result<char[]>> readCharactersUntilAsync(String text)
    {
        return CharacterReadStreamBase.readCharactersUntilAsync(this, text);
    }

    @Override
    public Result<String> readString(int charactersToRead)
    {
        return CharacterReadStreamBase.readString(this, charactersToRead);
    }

    @Override
    public AsyncFunction<Result<String>> readStringAsync(int charactersToRead)
    {
        return CharacterReadStreamBase.readStringAsync(this, charactersToRead);
    }

    @Override
    public Result<String> readStringUntil(char character)
    {
        return CharacterReadStreamBase.readStringUntil(this, character);
    }

    @Override
    public AsyncFunction<Result<String>> readStringUntilAsync(char character)
    {
        return CharacterReadStreamBase.readStringUntilAsync(this, character);
    }

    @Override
    public Result<String> readStringUntil(char[] characters)
    {
        return CharacterReadStreamBase.readStringUntil(this, characters);
    }

    @Override
    public AsyncFunction<Result<String>> readStringUntilAsync(char[] characters)
    {
        return CharacterReadStreamBase.readStringUntilAsync(this, characters);
    }

    @Override
    public Result<String> readStringUntil(String text)
    {
        return CharacterReadStreamBase.readStringUntil(this, text);
    }

    @Override
    public AsyncFunction<Result<String>> readStringUntilAsync(String text)
    {
        return CharacterReadStreamBase.readStringUntilAsync(this, text);
    }

    @Override
    public LineReadStream asLineReadStream()
    {
        return CharacterReadStreamBase.asLineReadStream(this);
    }

    @Override
    public LineReadStream asLineReadStream(boolean includeNewLines)
    {
        return CharacterReadStreamBase.asLineReadStream(this, includeNewLines);
    }

    public static AsyncFunction<Result<Character>> readCharacterAsync(final CharacterReadStream characterReadStream)
    {
        AsyncFunction<Result<Character>> result = CharacterReadStreamBase.validateCharacterReadStreamAsync(characterReadStream);
        if (result == null)
        {
            result = async(characterReadStream, new Function0<Result<Character>>()
            {
                @Override
                public Result<Character> run()
                {
                    return characterReadStream.readCharacter();
                }
            });
        }
        return result;
    }

    public static Result<char[]> readCharacters(CharacterReadStream characterReadStream, int charactersToRead)
    {
        Result<char[]> result = CharacterReadStreamBase.validateCharacterReadStream(characterReadStream);
        if (result == null)
        {
            result = Result.greaterThan(charactersToRead, 0, "charactersToRead");
            if (result == null)
            {
                char[] buffer = new char[charactersToRead];
                final Result<Integer> readCharactersResult = characterReadStream.readCharacters(buffer);
                if (readCharactersResult.hasError())
                {
                    result = Result.error(readCharactersResult.getError());
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
            }
        }
        return result;
    }

    public static AsyncFunction<Result<char[]>> readCharactersAsync(final CharacterReadStream characterReadStream, final int charactersToRead)
    {
        AsyncFunction<Result<char[]>> result = CharacterReadStreamBase.validateCharacterReadStreamAsync(characterReadStream);
        if (result == null)
        {
            result = async(characterReadStream, new Function0<Result<char[]>>()
            {
                @Override
                public Result<char[]> run()
                {
                    return characterReadStream.readCharacters(charactersToRead);
                }
            });
        }
        return result;
    }

    public static Result<Integer> readCharacters(CharacterReadStream characterReadStream, char[] outputCharacters)
    {
        Result<Integer> result = CharacterReadStreamBase.validateCharacterReadStream(characterReadStream);
        if (result == null)
        {
            result = CharacterReadStreamBase.validateOutputCharacters(outputCharacters);
            if (result == null)
            {
                result = characterReadStream.readCharacters(outputCharacters, 0, outputCharacters.length);
            }
        }
        return result;
    }

    public static AsyncFunction<Result<Integer>> readCharactersAsync(final CharacterReadStream characterReadStream, final char[] outputCharacters)
    {
        AsyncFunction<Result<Integer>> result = CharacterReadStreamBase.validateCharacterReadStreamAsync(characterReadStream);
        if (result == null)
        {
            result = CharacterReadStreamBase.validateOutputCharactersAsync(outputCharacters);
            if (result == null)
            {
                result = async(characterReadStream, new Function0<Result<Integer>>()
                {
                    @Override
                    public Result<Integer> run()
                    {
                        return characterReadStream.readCharacters(outputCharacters);
                    }
                });
            }
        }
        return result;
    }

    public static Result<Integer> readCharacters(CharacterReadStream characterReadStream, char[] outputCharacters, int startIndex, int length)
    {
        Result<Integer> result = CharacterReadStreamBase.validateCharacterReadStream(characterReadStream);
        if (result == null)
        {
            result = CharacterReadStreamBase.validateOutputCharacters(outputCharacters);
            if (result == null)
            {
                result = Result.between(0, startIndex, outputCharacters.length - 1, "startIndex");
                if (result == null)
                {
                    result = Result.between(1, length, outputCharacters.length - startIndex, "length");
                    if (result == null)
                    {
                        int charactersRead = 0;

                        Result<Character> readCharacterResult;
                        Character character;
                        for (int i = 0; i < length; ++i)
                        {
                            readCharacterResult = characterReadStream.readCharacter();
                            character = readCharacterResult.getValue();
                            if (character == null)
                            {
                                break;
                            }
                            else
                            {
                                outputCharacters[startIndex + i] = character;
                                ++charactersRead;
                            }
                        }

                        result = Result.success(charactersRead == 0 ? null : charactersRead);
                    }
                }
            }
        }
        return result;
    }

    public static AsyncFunction<Result<Integer>> readCharactersAsync(final CharacterReadStream characterReadStream, final char[] outputCharacters, final int startIndex, final int length)
    {
        AsyncFunction<Result<Integer>> result = CharacterReadStreamBase.validateCharacterReadStreamAsync(characterReadStream);
        if (result == null)
        {
            result = CharacterReadStreamBase.validateOutputCharactersAsync(outputCharacters);
            if (result == null)
            {
                result = async(characterReadStream, new Function0<Result<Integer>>()
                {
                    @Override
                    public Result<Integer> run()
                    {
                        return characterReadStream.readCharacters(outputCharacters, startIndex, length);
                    }
                });
            }
        }
        return result;
    }

    public static Result<char[]> readCharactersUntil(CharacterReadStream characterReadStream, char value)
    {
        Result<char[]> result = CharacterReadStreamBase.validateCharacterReadStream(characterReadStream);
        if (result == null)
        {
            final CharacterEncoding characterEncoding = characterReadStream.getCharacterEncoding();
            final Result<byte[]> encodedBytes = characterEncoding.encode(value);
            if (encodedBytes.hasError())
            {
                result = Result.error(encodedBytes.getError());
            }
            else
            {
                final ByteReadStream byteReadStream = characterReadStream.asByteReadStream();
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
        }
        return result;
    }

    public static AsyncFunction<Result<char[]>> readCharactersUntilAsync(final CharacterReadStream characterReadStream, final char value)
    {
        AsyncFunction<Result<char[]>> result = CharacterReadStreamBase.validateCharacterReadStreamAsync(characterReadStream);
        if (result == null)
        {
            result = async(characterReadStream, new Function0<Result<char[]>>()
            {
                @Override
                public Result<char[]> run()
                {
                    return characterReadStream.readCharactersUntil(value);
                }
            });
        }
        return result;
    }

    public static Result<char[]> readCharactersUntil(CharacterReadStream characterReadStream, char[] values)
    {
        Result<char[]> result = CharacterReadStreamBase.validateCharacterReadStream(characterReadStream);
        if (result == null)
        {
            final CharacterEncoding characterEncoding = characterReadStream.getCharacterEncoding();
            final Result<byte[]> encodedBytes = characterEncoding.encode(values);
            if (encodedBytes.hasError())
            {
                result = Result.error(encodedBytes.getError());
            }
            else
            {
                final ByteReadStream byteReadStream = characterReadStream.asByteReadStream();
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
        }
        return result;
    }

    public static AsyncFunction<Result<char[]>> readCharactersUntilAsync(final CharacterReadStream characterReadStream, final char[] values)
    {
        AsyncFunction<Result<char[]>> result = CharacterReadStreamBase.validateCharacterReadStreamAsync(characterReadStream);
        if (result == null)
        {
            result = async(characterReadStream, new Function0<Result<char[]>>()
            {
                @Override
                public Result<char[]> run()
                {
                    return characterReadStream.readCharactersUntil(values);
                }
            });
        }
        return result;
    }

    public static Result<char[]> readCharactersUntil(CharacterReadStream characterReadStream, String text)
    {
        Result<char[]> result = CharacterReadStreamBase.validateCharacterReadStream(characterReadStream);
        if (result == null)
        {
            final CharacterEncoding characterEncoding = characterReadStream.getCharacterEncoding();
            final Result<byte[]> encodedBytes = characterEncoding.encode(text);
            if (encodedBytes.hasError())
            {
                result = Result.error(encodedBytes.getError());
            }
            else
            {
                final ByteReadStream byteReadStream = characterReadStream.asByteReadStream();
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
        }
        return result;
    }

    public static AsyncFunction<Result<char[]>> readCharactersUntilAsync(final CharacterReadStream characterReadStream, final String text)
    {
        AsyncFunction<Result<char[]>> result = CharacterReadStreamBase.validateCharacterReadStreamAsync(characterReadStream);
        if (result == null)
        {
            result = async(characterReadStream, new Function0<Result<char[]>>()
            {
                @Override
                public Result<char[]> run()
                {
                    return characterReadStream.readCharactersUntil(text);
                }
            });
        }
        return result;
    }

    public static Result<String> readString(CharacterReadStream characterReadStream, int charactersToRead)
    {
        Result<String> result = CharacterReadStreamBase.validateCharacterReadStream(characterReadStream);
        if (result == null)
        {
            result = Result.greaterThan(charactersToRead, 0, "charactersToRead");
            if (result == null)
            {
                final Result<char[]> readCharactersResult = characterReadStream.readCharacters(charactersToRead);
                final char[] characters = readCharactersResult.getValue();
                final String resultString = characters == null ? null : new String(characters);
                result = Result.done(resultString, readCharactersResult.getError());
            }
        }
        return result;
    }

    public static AsyncFunction<Result<String>> readStringAsync(final CharacterReadStream characterReadStream, final int charactersToRead)
    {
        AsyncFunction<Result<String>> result = CharacterReadStreamBase.validateCharacterReadStreamAsync(characterReadStream);
        if (result == null)
        {
            result = async(characterReadStream, new Function0<Result<String>>()
            {
                @Override
                public Result<String> run()
                {
                    return characterReadStream.readString(charactersToRead);
                }
            });
        }
        return result;
    }

    public static Result<String> readStringUntil(CharacterReadStream characterReadStream, char value)
    {
        Result<String> result;
        final Result<char[]> charactersResult = characterReadStream.readCharactersUntil(value);
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

    public static AsyncFunction<Result<String>> readStringUntilAsync(final CharacterReadStream characterReadStream, final char value)
    {
        AsyncFunction<Result<String>> result = CharacterReadStreamBase.validateCharacterReadStreamAsync(characterReadStream);
        if (result == null)
        {
            result = async(characterReadStream, new Function0<Result<String>>()
            {
                @Override
                public Result<String> run()
                {
                    return characterReadStream.readStringUntil(value);
                }
            });
        }
        return result;
    }

    public static Result<String> readStringUntil(CharacterReadStream characterReadStream, char[] values)
    {
        Result<String> result;
        final Result<char[]> charactersResult = characterReadStream.readCharactersUntil(values);
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

    public static AsyncFunction<Result<String>> readStringUntilAsync(final CharacterReadStream characterReadStream, final char[] values)
    {
        AsyncFunction<Result<String>> result = CharacterReadStreamBase.validateCharacterReadStreamAsync(characterReadStream);
        if (result == null)
        {
            result = async(characterReadStream, new Function0<Result<String>>()
            {
                @Override
                public Result<String> run()
                {
                    return characterReadStream.readStringUntil(values);
                }
            });
        }
        return result;
    }

    public static Result<String> readStringUntil(CharacterReadStream characterReadStream, String text)
    {
        Result<String> result;
        final Result<char[]> charactersResult = characterReadStream.readCharactersUntil(text);
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

    public static AsyncFunction<Result<String>> readStringUntilAsync(final CharacterReadStream characterReadStream, final String text)
    {
        AsyncFunction<Result<String>> result = CharacterReadStreamBase.validateCharacterReadStreamAsync(characterReadStream);
        if (result == null)
        {
            result = async(characterReadStream, new Function0<Result<String>>()
            {
                @Override
                public Result<String> run()
                {
                    return characterReadStream.readStringUntil(text);
                }
            });
        }
        return result;
    }

    public static LineReadStream asLineReadStream(CharacterReadStream characterReadStream)
    {
        return new BasicLineReadStream(characterReadStream);
    }

    public static LineReadStream asLineReadStream(CharacterReadStream characterReadStream, boolean includeNewLines)
    {
        return new BasicLineReadStream(characterReadStream, includeNewLines);
    }

    public static <T> Result<T> validateCharacterReadStream(CharacterReadStream characterReadStream)
    {
        Result<T> result = Result.notNull(characterReadStream, "characterReadStream");
        if (result == null)
        {
            result = Result.equal(false, characterReadStream.isDisposed(), "characterReadStream.isDisposed()");
        }
        return result;
    }

    public static <T> AsyncFunction<Result<T>> validateCharacterReadStreamAsync(CharacterReadStream characterReadStream)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final Result<T> result = CharacterReadStreamBase.validateCharacterReadStream(characterReadStream);
        return result == null ? null : currentAsyncRunner.done(result);
    }

    public static <T> Result<T> validateOutputCharacters(char[] outputCharacters)
    {
        Result<T> result = Result.notNull(outputCharacters, "outputCharacters");
        if (result == null)
        {
            result = Result.greaterThan(outputCharacters.length, 0, "outputCharacters.length");
        }
        return result;
    }

    public static <T> AsyncFunction<Result<T>> validateOutputCharactersAsync(char[] outputCharacters)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final Result<T> result = CharacterReadStreamBase.validateOutputCharacters(outputCharacters);
        return result == null ? null : currentAsyncRunner.done(result);
    }

    private static <T> AsyncFunction<Result<T>> async(CharacterReadStream characterReadStream, Function0<Result<T>> function)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final AsyncRunner streamAsyncRunner = characterReadStream.getAsyncRunner();

        AsyncFunction<Result<T>> result;
        if (streamAsyncRunner == null)
        {
            result = currentAsyncRunner.error(new IllegalArgumentException("Cannot invoke CharacterReadStream asynchronous functions when the CharacterReadStream hasn't been assigned an AsyncRunner."));
        }
        else
        {
            result = streamAsyncRunner.schedule(function)
                .thenOn(currentAsyncRunner);
        }
        return result;
    }
}
