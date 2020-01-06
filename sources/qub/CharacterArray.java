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
        PreCondition.assertNotNullAndNotEmpty(characters, "characters");
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
            Array.copy(characters, startIndex, resultArray, 0, length);
        }
        return new CharacterArray(resultArray);
    }

    @Override
    public int getCount()
    {
        return characters.length;
    }

    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    public void set(int index, char value)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        characters[index] = value;
    }

    @Override
    public void set(int index, Character value)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");
        PreCondition.assertNotNull(value, "value");

        characters[index] = value;
    }

    @Override
    public Character get(int index)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        return characters[index];
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
