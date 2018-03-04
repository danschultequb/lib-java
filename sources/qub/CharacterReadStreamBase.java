package qub;

public abstract class CharacterReadStreamBase extends IteratorBase<Character> implements CharacterReadStream
{
    @Override
    public char[] readCharacters(int charactersToRead)
    {
        return CharacterReadStreamBase.readCharacters(this, charactersToRead);
    }

    @Override
    public int readCharacters(char[] characters)
    {
        return CharacterReadStreamBase.readCharacters(this, characters);
    }

    @Override
    public int readCharacters(char[] characters, int startIndex, int length)
    {
        return CharacterReadStreamBase.readCharacters(this, characters, startIndex, length);
    }

    @Override
    public String readString(int charactersToRead)
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

    public static char[] readCharacters(CharacterReadStream characterReadStream, int charactersToRead)
    {
        char[] result = null;
        if (1 <= charactersToRead)
        {
            result = new char[charactersToRead];
            final int charactersRead = characterReadStream.readCharacters(result);
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

    public static int readCharacters(CharacterReadStream characterReadStream, char[] characters)
    {
        return characterReadStream.readCharacters(characters, 0, characters == null ? 0 : characters.length);
    }

    public static int readCharacters(CharacterReadStream characterReadStream, char[] characters, int startIndex, int length)
    {
        int charactersRead = 0;

        for (int i = 0; i < length; ++i)
        {
            final Character character = characterReadStream.readCharacter();
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

        return charactersRead;
    }

    public static String readString(CharacterReadStream characterReadStream, int charactersToRead)
    {
        final char[] characters = characterReadStream.readCharacters(charactersToRead);
        return characters == null ? null : new String(characters);
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
