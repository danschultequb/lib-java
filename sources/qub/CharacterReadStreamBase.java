package qub;

public abstract class CharacterReadStreamBase extends IteratorBase<Character> implements CharacterReadStream
{
    @Override
    public AsyncFunction<Result<Boolean>> disposeAsync()
    {
        return AsyncDisposableBase.disposeAsync(this);
    }

    @Override
    public void close() throws Exception
    {
        DisposableBase.close(this);
    }

    @Override
    public Result<char[]> readCharacters(int charactersToRead)
    {
        return CharacterReadStreamBase.readCharacters(this, charactersToRead);
    }

    @Override
    public Result<Integer> readCharacters(char[] characters)
    {
        return CharacterReadStreamBase.readCharacters(this, characters);
    }

    @Override
    public Result<Integer> readCharacters(char[] characters, int startIndex, int length)
    {
        return CharacterReadStreamBase.readCharacters(this, characters, startIndex, length);
    }

    @Override
    public Result<String> readString(int charactersToRead)
    {
        return CharacterReadStreamBase.readString(this, charactersToRead);
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

    public static Result<char[]> readCharacters(CharacterReadStream characterReadStream, int charactersToRead)
    {
        Result<char[]> result = Result.notNull(characterReadStream, "characterReadStream");
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

    public static Result<Integer> readCharacters(CharacterReadStream characterReadStream, char[] characters)
    {
        Result<Integer> result = Result.notNull(characterReadStream, "characterReadStream");
        if (result == null)
        {
            result = Result.notNull(characters, "characters");
            if (result == null)
            {
                result = Result.greaterThan(0, characters.length, "characters.length");
                if (result == null)
                {
                    result = characterReadStream.readCharacters(characters, 0, characters.length);
                }
            }
        }
        return result;
    }

    public static Result<Integer> readCharacters(CharacterReadStream characterReadStream, char[] characters, int startIndex, int length)
    {
        Result<Integer> result = Result.notNull(characterReadStream, "characterReadStream");
        if (result == null)
        {
            result = Result.notNull(characters, "characters");
            if (result == null)
            {
                result = Result.between(0, startIndex, characters.length - 1, "startIndex");
                if (result == null)
                {
                    result = Result.between(1, length, characters.length - startIndex, "length");
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
                                characters[startIndex + i] = character;
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

    public static Result<String> readString(CharacterReadStream characterReadStream, int charactersToRead)
    {
        Result<String> result = Result.notNull(characterReadStream, "characterReadStream");
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

    public static LineReadStream asLineReadStream(CharacterReadStream characterReadStream)
    {
        return new CharacterReadStreamToLineReadStream(characterReadStream);
    }

    public static LineReadStream asLineReadStream(CharacterReadStream characterReadStream, boolean includeNewLines)
    {
        return new CharacterReadStreamToLineReadStream(characterReadStream, includeNewLines);
    }
}
