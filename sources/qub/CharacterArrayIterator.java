package qub;

public class CharacterArrayIterator implements Iterator<Character>
{
    private final char[] characters;
    private final int length;
    private boolean hasStarted;
    private int currentIndex;

    public CharacterArrayIterator(char[] characters)
    {
        this(characters, 0, characters.length);
    }

    public CharacterArrayIterator(char[] characters, int startIndex, int length)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertStartIndex(startIndex, characters.length);
        PreCondition.assertLength(length, startIndex, characters.length);

        this.characters = characters;
        this.currentIndex = startIndex;
        this.length = length;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return hasStarted && currentIndex < length;
    }

    @Override
    public Character getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return characters[currentIndex];
    }

    @Override
    public boolean next()
    {
        if (!hasStarted)
        {
            hasStarted = true;
        }
        else if (currentIndex < length)
        {
            ++currentIndex;
        }
        return currentIndex < length;
    }
}
