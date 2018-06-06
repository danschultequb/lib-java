package qub;

public abstract class CharacterReadStreamBase extends IteratorBase<Character> implements CharacterReadStream
{
    @Override
    public AsyncFunction<Result<Boolean>> disposeAsync()
    {
        return AsyncDisposableBase.disposeAsync(this);
    }

    @Override
    public void close()
    {
        DisposableBase.close(this);
    }

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
    public Result<String> readLine()
    {
        return CharacterReadStreamBase.readLine(this);
    }

    @Override
    public AsyncFunction<Result<String>> readLineAsync()
    {
        return CharacterReadStreamBase.readLineAsync(this);
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
            result = Result.greaterThan(0, charactersToRead, "charactersToRead");
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

    public static Result<String> readString(CharacterReadStream characterReadStream, int charactersToRead)
    {
        Result<String> result = CharacterReadStreamBase.validateCharacterReadStream(characterReadStream);
        if (result == null)
        {
            result = Result.greaterThan(0, charactersToRead, "charactersToRead");
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

    public static Result<String> readLine(CharacterReadStream characterReadStream)
    {
        Result<String> result = CharacterReadStreamBase.validateCharacterReadStream(characterReadStream);
        if (result == null)
        {
            final StringBuilder builder = new StringBuilder();
            Throwable error = null;
            do
            {
                final Result<Character> character = characterReadStream.readCharacter();
                if (character.hasError())
                {
                    error = character.getError();
                }

                if (character.getValue() != null)
                {
                    builder.append(character.getValue());
                }
                else
                {
                    break;
                }
            }
            while(error == null && builder.charAt(builder.length() - 1) != '\n');

            final int builderLength = builder.length();
            if (builderLength >= 1)
            {
                if (builder.charAt(builderLength - 1) == '\n')
                {
                    if (builderLength >= 2 && builder.charAt(builderLength - 2) == '\r')
                    {
                        builder.setLength(builderLength - 2);
                    }
                    else
                    {
                        builder.setLength(builderLength - 1);
                    }
                }
            }

            result = Result.done(builderLength == 0 ? null : builder.toString(), error);
        }
        return result;
    }

    public static AsyncFunction<Result<String>> readLineAsync(final CharacterReadStream characterReadStream)
    {
        AsyncFunction<Result<String>> result = CharacterReadStreamBase.validateCharacterReadStreamAsync(characterReadStream);
        if (result == null)
        {
            result = async(characterReadStream, new Function0<Result<String>>()
            {
                @Override
                public Result<String> run()
                {
                    return characterReadStream.readLine();
                }
            });
        }
        return result;
    }

    public static LineReadStream asLineReadStream(CharacterReadStream characterReadStream)
    {
        return new CharacterReadStreamToLineReadStream(characterReadStream);
    }

    public static LineReadStream asLineReadStream(CharacterReadStream characterReadStream, boolean includeNewLines)
    {
        return new CharacterReadStreamToLineReadStream(characterReadStream, includeNewLines);
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
            result = Result.greaterThan(0, outputCharacters.length, "outputCharacters.length");
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
