package qub;

/**
 * An iterator that converts Java characters to Unicode Code Point integers.
 */
public class CharacterToUnicodeCodePointIterator implements Iterator<Integer>
{
    private final Iterator<Character> characters;
    private Integer current;

    private CharacterToUnicodeCodePointIterator(Iterator<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        this.characters = characters;
    }

    /**
     * Create a new CharacterToUnicodeCodePointIterator from the provided characters.
     * @param characters The characters to convert.
     * @return The new CharacterToUnicodeCodePointIterator.
     */
    public static CharacterToUnicodeCodePointIterator create(Iterator<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return new CharacterToUnicodeCodePointIterator(characters);
    }

    @Override
    public boolean hasStarted()
    {
        return this.characters.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return this.current != null || this.characters.hasCurrent();
    }

    @Override
    public Integer getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        if (this.current == null)
        {
            this.populateCurrent();
        }
        final Integer result = this.current;

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public boolean next()
    {
        final boolean result = this.characters.next();
        if (result)
        {
            this.populateCurrent();
        }
        else
        {
            this.current = null;
        }
        return result;
    }

    private void populateCurrent()
    {
        PreCondition.assertTrue(this.characters.hasCurrent(), "this.characters.hasCurrent()");

        Integer currentCodePoint;
        final Character firstCharacter = this.characters.getCurrent();
        if (firstCharacter == null)
        {
            throw new IllegalArgumentException("Can't encode a null character.");
        }
        else if (UTF16CharacterEncoding.isHighSurrogate(firstCharacter))
        {
            final boolean hasLowSurrogate = this.characters.next();
            if (!hasLowSurrogate)
            {
                throw new IllegalArgumentException("Missing low-surrogate character (between 0xDC00 and 0xDFFF) after high-surrogate character (between 0xD800 and 0xDBFF).");
            }
            else
            {
                final Character secondCharacter = this.characters.getCurrent();
                if (secondCharacter == null)
                {
                    throw new IllegalArgumentException("Can't encode a null character.");
                }
                else if (!UTF16CharacterEncoding.isLowSurrogate(secondCharacter))
                {
                    throw new IllegalArgumentException("Expected low-surrogate character (between 0xDC00 and 0xDFFF) after high surrogate character (between 0xD800 and 0xDBFF), but found 0x" + Integers.toHexString(secondCharacter, true) + " instead.");
                }
                else
                {
                    currentCodePoint = 0x10000 + ((firstCharacter - 0xD800) << 10) + (secondCharacter - 0xDC00);
                }
            }
        }
        else if (UTF16CharacterEncoding.isLowSurrogate(firstCharacter))
        {
            throw new IllegalArgumentException("Expected to find a non-surrogate character (not between 0xD800 and 0xDFFF) or high-surrogate character (between 0xD800 and 0xDBFF, but found a low surrogate character instead (0x" + Integers.toHexString((int)firstCharacter, true) + ").");
        }
        else
        {
            currentCodePoint = (int)firstCharacter;
        }
        this.current = currentCodePoint;
    }
}
