package qub;

/**
 * A list of characters. Internally this uses a primitive char[] to store characters, so it should
 * be more efficient than using a generic List&lt;Character&gt;.
 */
public class CharacterList implements List<Character>
{
    private static final int defaultCapacity = 64;

    private char[] characters;
    private int count;

    private CharacterList(char[] characters, int count)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertBetween(0, count, characters.length, "count");

        this.characters = characters;
        this.count = count;
    }

    /**
     * Create a new CharacterList from the provided characters.
     * @param characters The characters to initialize the new CharacterList with.
     * @return The new CharacterList.
     */
    public static CharacterList create(char... characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return new CharacterList(Array.clone(characters), characters.length);
    }

    /**
     * Create a new CharacterList from the provided characters.
     * @param characters The characters to initialize the new CharacterList with.
     * @return The new CharacterList.
     */
    public static CharacterList create(Iterable<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return CharacterList.create(characters.iterate());
    }

    /**
     * Create a new CharacterList from the provided characters.
     * @param characters The characters to initialize the new CharacterList with.
     * @return The new CharacterList.
     */
    public static CharacterList create(Iterator<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        final CharacterList result = CharacterList.create();
        result.addAll(characters);

        PostCondition.assertNotNull(characters, "characters");

        return result;
    }

    @Override
    public boolean any()
    {
        return count > 0;
    }

    @Override
    public int getCount()
    {
        return count;
    }

    public CharacterList insert(int insertIndex, char value)
    {
        PreCondition.assertIndexAccess(insertIndex, count + 1, "insertIndex");

        if (count == characters.length)
        {
            final char[] newCharacters = new char[(characters.length * 2) + 1];
            if (1 <= insertIndex)
            {
                Array.copy(characters, 0, newCharacters, 0, insertIndex);
            }
            if (insertIndex <= count - 1)
            {
                Array.copy(characters, insertIndex, newCharacters, insertIndex + 1, count - insertIndex);
            }
            characters = newCharacters;
        }
        else if (insertIndex < count)
        {
            Array.shiftRight(characters, insertIndex, count - insertIndex);
        }
        characters[insertIndex] = value;
        ++count;

        return this;
    }

    @Override
    public CharacterList insert(int insertIndex, Character value)
    {
        PreCondition.assertIndexAccess(insertIndex, count + 1, "insertIndex");
        PreCondition.assertNotNull(value, "value");

        return this.insert(insertIndex, value.charValue());
    }

    public CharacterList add(char value)
    {
        return this.insert(count, value);
    }

    /**
     * Add all of the characters in the provided String to this CharacterList.
     * @param values The characters to add.
     */
    public CharacterList addAll(String values)
    {
        PreCondition.assertNotNull(values, "values");

        return this.addAll(values.toCharArray());
    }

    /**
     * Add all of the characters in the provided String to this CharacterList.
     * @param values The characters to add.
     */
    public CharacterList addAll(java.lang.StringBuilder values)
    {
        PreCondition.assertNotNull(values, "values");

        return this.addAll(values.toString());
    }

    /**
     * Add all of the provided characters to this CharacterList.
     * @param values The characters to add.
     */
    public CharacterList addAll(char... values)
    {
        if (values != null && values.length > 0)
        {
            for (final char value : values)
            {
                this.add(value);
            }
        }
        return this;
    }

    @Override
    public Character removeAt(int index)
    {
        PreCondition.assertIndexAccess(index, count, "index");

        final char result = characters[index];
        if (index < count - 1)
        {
            Array.shiftLeft(characters, index, count - index - 1);
        }
        --count;

        return result;
    }

    /**
     * Remove the first characters from this CharacterList as a CharacterArray.
     * @param valuesToRemove The number of characters to remove from this CharacterList.
     * @return The removed characters.
     */
    public CharacterArray removeFirstCharacters(int valuesToRemove)
    {
        PreCondition.assertGreaterThanOrEqualTo(valuesToRemove, 1, "valuesToRemove");
        PreCondition.assertNotNullAndNotEmpty(this, "list");
        PreCondition.assertLength(valuesToRemove, 0, getCount());

        final char[] characters = new char[valuesToRemove];
        this.removeFirstCharacters(characters);
        return CharacterArray.create(characters);
    }

    /**
     * Remove the first characters from this CharacterList and put them into the outputCharacters
     * array. There must be enough characters in this list to fill the provided array.
     * @param outputCharacters The array to put the characters into.
     */
    public void removeFirstCharacters(char[] outputCharacters)
    {
        PreCondition.assertNotNullAndNotEmpty(outputCharacters, "outputCharacters");
        PreCondition.assertNotNullAndNotEmpty(this, "list");
        PreCondition.assertLength(outputCharacters.length, 0, getCount());

        removeFirstCharacters(outputCharacters, 0, outputCharacters.length);
    }

    /**
     * Remove the first characters from this CharacterList and put them into the outputCharacters
     * array.
     * @param outputCharacters The array to put the characters into.
     * @param startIndex The start index in the array to start putting the characters to.
     * @param length The number of characters to remove from the list and put into the array.
     */
    public void removeFirstCharacters(char[] outputCharacters, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputCharacters, "outputCharacters");
        PreCondition.assertStartIndex(startIndex, outputCharacters.length);
        PreCondition.assertLength(length, startIndex, outputCharacters.length);
        PreCondition.assertNotNullAndNotEmpty(this, "list");
        PreCondition.assertLength(length, 0, getCount());

        Array.copy(characters, 0, outputCharacters, startIndex, length);
        final int bytesInList = getCount();
        if (length < bytesInList)
        {
            Array.copy(characters, length, this.characters, 0, bytesInList - length);
        }
        count -= length;
    }

    @Override
    public Iterable<Character> removeFirst(int valuesToRemove)
    {
        return removeFirstCharacters(valuesToRemove);
    }

    public CharacterList set(int index, char value)
    {
        PreCondition.assertIndexAccess(index, count, "index");

        characters[index] = value;

        return this;
    }

    @Override
    public CharacterList set(int index, Character value)
    {
        PreCondition.assertIndexAccess(index, count, "index");
        PreCondition.assertNotNull(value, "value");

        return this.set(index, value.charValue());
    }

    @Override
    public Character get(int index)
    {
        PreCondition.assertIndexAccess(index, count, "index");

        return characters[index];
    }

    @Override
    public Iterator<Character> iterate()
    {
        return count == 0 ? Iterator.empty() : new CharacterArrayIterator(characters, 0, count);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return toString(false);
    }

    /**
     * Get the String representation of the characters in this CharacterList.
     * @param asString Whether or not to display the characters as a concatenated String or as an
     *                 Iterable.
     * @return The String representation of the characters in this CharacterList.
     */
    public String toString(boolean asString)
    {
        return asString
            ? String.valueOf(characters, 0, count)
            : Iterable.toString(this);
    }
}
