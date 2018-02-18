package qub;

public interface CharacterReadStream extends Stream, Iterator<Character>
{
    Character readCharacter();

    default char[] readCharacters(int charactersToRead)
    {
        char[] result = null;
        if (1 <= charactersToRead)
        {
            result = new char[charactersToRead];
            final int charactersRead = readCharacters(result);
            if (charactersRead < 0)
            {
                result = null;
            }
            else if (charactersRead < charactersToRead)
            {
                result = Array.clone(result, 0, charactersRead);
            }
        }
        return result;
    }

    default int readCharacters(char[] characters)
    {
        return readCharacters(characters, 0, characters == null ? 0 : characters.length);
    }

    int readCharacters(char[] characters, int startIndex, int length);

    default String readString(int charactersToRead)
    {
        final char[] characters = readCharacters(charactersToRead);
        return characters == null ? null : new String(characters);
    }

    CharacterEncoding getEncoding();

    ByteReadStream asByteReadStream();

    default LineReadStream asLineReadStream()
    {
        return new CharacterReadStreamToLineReadStream(this);
    }

    default LineReadStream asLineReadStream(boolean includeNewLines)
    {
        return new CharacterReadStreamToLineReadStream(this, includeNewLines);
    }
}
