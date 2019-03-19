package qub;

/**
 * A wrapper class around the char[] primitive type.
 */
public class CharacterArray extends Array<Character>
{
    private final char[] characters;

    public CharacterArray(int count)
    {
        PreCondition.assertGreaterThanOrEqualTo(count, 0, "count");

        this.characters = new char[count];
    }

    public CharacterArray(char[] characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        this.characters = characters;
    }

    public CharacterArray(char[] characters, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(characters, "characters");
        PreCondition.assertStartIndex(startIndex, characters.length);
        PreCondition.assertLength(length, startIndex, characters.length);

        if (characters.length == length)
        {
            this.characters = characters;
        }
        else
        {
            this.characters = new char[length];
            Array.copy(characters, startIndex, this.characters, 0, length);
        }
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
}
