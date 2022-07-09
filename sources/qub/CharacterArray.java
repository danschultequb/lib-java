package qub;

/**
 * A wrapper class around the char[] primitive type.
 */
public class CharacterArray implements Array<Character>
{
    private final char[] characters;

    private CharacterArray(char[] characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        this.characters = characters;
    }

    public static CharacterArray create(char... characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return new CharacterArray(characters);
    }

    public static CharacterArray create(char[] characters, int startIndex, int length)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertStartIndex(startIndex, characters.length);
        PreCondition.assertLength(length, startIndex, characters.length);

        char[] resultArray;
        if (characters.length == length)
        {
            resultArray = characters;
        }
        else
        {
            resultArray = new char[length];
            if (length > 0)
            {
                Array.copy(characters, startIndex, resultArray, 0, length);
            }
        }
        return new CharacterArray(resultArray);
    }

    @Override
    public int getCount()
    {
        return this.characters.length;
    }

    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    public CharacterArray set(int index, char value)
    {
        PreCondition.assertIndexAccess(index, this.getCount(), "index");

        this.characters[index] = value;

        return this;
    }

    @Override
    public CharacterArray set(int index, Character value)
    {
        PreCondition.assertIndexAccess(index, this.getCount(), "index");
        PreCondition.assertNotNull(value, "value");

        this.characters[index] = value;

        return this;
    }

    @Override
    public Character get(int index)
    {
        PreCondition.assertIndexAccess(index, this.getCount(), "index");

        return this.characters[index];
    }

    /**
     * Get whether this {@link CharacterArray} contains the provided value.
     * @param value The value to look for in this {@link CharacterArray}.
     */
    public boolean contains(char value)
    {
        boolean result = false;

        for (final char c : this.characters)
        {
            if (c == value)
            {
                result = true;
                break;
            }
        }

        return result;
    }

    @Override
    public boolean contains(Character value)
    {
        return value != null && this.contains(value.charValue());
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }
}
