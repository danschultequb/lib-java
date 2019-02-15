package qub;

/**
 * A wrapper class around the char[] primitive type.
 */
public class CharacterArray implements MutableIndexable<Character>
{
    private final char[] characters;

    private CharacterArray(char[] characters)
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

    @Override
    public Iterator<Character> iterate()
    {
        return new CharacterArrayIterator(characters);
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

    /**
     * Create a new CharacterArray with the provided number of elements.
     * @param count The number of elements.
     * @return The new CharacterArray.
     */
    public static CharacterArray createWithLength(int count)
    {
        PreCondition.assertGreaterThanOrEqualTo(count, 0, "count");

        return new CharacterArray(new char[count]);
    }

    /**
     * Create a new CharacterArray with the provided elements.
     * @param values The elements of the new CharacterArray.
     * @return The new CharacterArray.
     */
    public static CharacterArray create(char... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new CharacterArray(values);
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

        final CharacterArray result = CharacterArray.createWithLength(length);
        for (int i = 0; i < length; ++i)
        {
            result.set(i, values[startIndex + i]);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(length, result.getCount(), "length");

        return result;
    }
}
