package qub;

public class UnicodeCodePointToCharacterIterator implements Iterator<Character>
{
    private final UnicodeCodePointIterator unicodeCodePoints;
    private final Character[] currentCharacters;

    private UnicodeCodePointToCharacterIterator(UnicodeCodePointIterator unicodeCodePoints)
    {
        PreCondition.assertNotNull(unicodeCodePoints, "unicodeCodePoints");

        this.unicodeCodePoints = unicodeCodePoints;
        this.currentCharacters = new Character[2];
    }

    public static UnicodeCodePointToCharacterIterator create(UnicodeCodePointIterator unicodeCodePoints)
    {
        return new UnicodeCodePointToCharacterIterator(unicodeCodePoints);
    }

    /**
     * Set whether or not a Unicode byte order mark (BOM) will be returned if it is found.
     * @param returnByteOrderMark Whether or not a Unicode byte order mark (BOM) will be returned if
     *                            it is found.
     * @return This object for method chaining.
     */
    public UnicodeCodePointToCharacterIterator setReturnByteOrderMark(boolean returnByteOrderMark)
    {
        this.unicodeCodePoints.setReturnByteOrderMark(returnByteOrderMark);

        return this;
    }

    /**
     * Get whether or not a Unicode byte order mark (BOM) will be returned if it is found.
     * @return Whether or not a Unicode byte order mark (BOM) will be returned if it is found.
     */
    public boolean getReturnByteOrderMark()
    {
        return this.unicodeCodePoints.getReturnByteOrderMark();
    }

    /**
     * Get whether or not this iterator found a Unicode byte order mark (BOM) while decoding. If
     * this iterator hasn't iterated over its first code point yet, then this will return null.
     * @return Whether or not this iterator found a Unicode byte order mark (BOM) while decoding. If
     * this iterator hasn't iterated over its first code point yet, then this will return null.
     */
    public Boolean foundByteOrderMark()
    {
        return this.unicodeCodePoints.foundByteOrderMark();
    }

    @Override
    public boolean hasStarted()
    {
        return this.unicodeCodePoints.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return this.currentCharacters[0] != null || this.unicodeCodePoints.hasCurrent();
    }

    @Override
    public Character getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        if (this.currentCharacters[0] == null)
        {
            this.populateCurrentCharacters();
        }

        return this.currentCharacters[0];
    }

    @Override
    public boolean next()
    {
        if (this.currentCharacters[1] != null)
        {
            this.currentCharacters[0] = this.currentCharacters[1];
            this.currentCharacters[1] = null;
        }
        else if (this.unicodeCodePoints.next())
        {
            this.populateCurrentCharacters();
        }
        else
        {
            this.currentCharacters[0] = null;
        }
        return this.hasCurrent();
    }

    private void populateCurrentCharacters()
    {
        PreCondition.assertTrue(this.unicodeCodePoints.hasCurrent(), "this.unicodeCodePoints.hasCurrent()");

        final Integer currentUnicodeCodePoint = this.unicodeCodePoints.getCurrent();
        if (currentUnicodeCodePoint == null)
        {
            throw new IllegalArgumentException("Can't decode null unicode code point integer.");
        }
        else if (currentUnicodeCodePoint < 0x10000)
        {
            this.currentCharacters[0] = (char)currentUnicodeCodePoint.intValue();
        }
        else
        {
            this.currentCharacters[0] = (char)(((currentUnicodeCodePoint - 0x10000) >>> 10) + 0xD800);
            this.currentCharacters[1] = (char)((currentUnicodeCodePoint & 0x3FF) + 0xDC00);
        }
    }
}
