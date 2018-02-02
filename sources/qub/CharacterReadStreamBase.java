package qub;

public abstract class CharacterReadStreamBase extends IteratorBase<Character> implements CharacterReadStream
{
    private final CharacterEncoding encoding;

    protected CharacterReadStreamBase(CharacterEncoding encoding)
    {
        this.encoding = encoding;
    }

    @Override
    public CharacterEncoding getEncoding()
    {
        return encoding;
    }

    @Override
    public char[] readCharacters(int charactersToRead)
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

    @Override
    public String readString(int charactersToRead)
    {
        final char[] characters = readCharacters(charactersToRead);
        return characters == null ? null : new String(characters);
    }

    @Override
    public LineReadStream asLineReadStream()
    {
        return asLineReadStream(this);
    }

    @Override
    public LineReadStream asLineReadStream(boolean includeNewLines)
    {
        return asLineReadStream(this, includeNewLines);
    }

    public static LineReadStream asLineReadStream(CharacterReadStream characterReadStream)
    {
        return characterReadStream == null ? null : new CharacterReadStreamToLineReadStream(characterReadStream);
    }

    public static LineReadStream asLineReadStream(CharacterReadStream characterReadStream, boolean includeNewLines)
    {
        return characterReadStream == null ? null : new CharacterReadStreamToLineReadStream(characterReadStream, includeNewLines);
    }
}
