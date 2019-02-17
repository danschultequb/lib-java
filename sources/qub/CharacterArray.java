package qub;

/**
 * A wrapper class around the char[] primitive type.
 */
public class CharacterArray extends Array<Character>
{
    private final char[] characters;

    public CharacterArray(int count)
    {
        this(new char[count]);
    }

    public CharacterArray(char[] characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        this.characters = characters;
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

    /**
     * Create a new CharacterArray with the provided elements.
     * @param values The elements of the new CharacterArray.
     * @param startIndex The start index into the values.
     * @param length The number of characters to copy.
     * @return The new CharacterArray.
     */
    public static CharacterArray create(char[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        final CharacterArray result = new CharacterArray(length);
        for (int i = 0; i < length; ++i)
        {
            result.set(i, values[startIndex + i]);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(length, result.getCount(), "length");

        return result;
    }
}
