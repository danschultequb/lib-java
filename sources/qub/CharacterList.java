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
        return this.count > 0;
    }

    @Override
    public int getCount()
    {
        return this.count;
    }

    /**
     * Insert the provided value into this {@link CharacterList} at the provided insertIndex.
     * @param insertIndex The index at which to insert the provided value.
     * @param value The value to insert into this {@link CharacterList};
     * @return This object for method chaining.
     */
    public CharacterList insert(int insertIndex, char value)
    {
        PreCondition.assertIndexAccess(insertIndex, this.count + 1, "insertIndex");

        if (this.count == this.characters.length)
        {
            final char[] newCharacters = new char[(this.characters.length * 2) + 1];
            if (1 <= insertIndex)
            {
                Array.copy(this.characters, 0, newCharacters, 0, insertIndex);
            }
            if (insertIndex <= this.count - 1)
            {
                Array.copy(this.characters, insertIndex, newCharacters, insertIndex + 1, this.count - insertIndex);
            }
            this.characters = newCharacters;
        }
        else if (insertIndex < this.count)
        {
            Array.shiftRight(this.characters, insertIndex, this.count - insertIndex);
        }
        this.characters[insertIndex] = value;
        this.count++;

        return this;
    }

    @Override
    public CharacterList insert(int insertIndex, Character value)
    {
        PreCondition.assertIndexAccess(insertIndex, this.count + 1, "insertIndex");
        PreCondition.assertNotNull(value, "value");

        return this.insert(insertIndex, value.charValue());
    }

    /**
     * Insert all the characters in the provided {@link String} to this {@link CharacterList} at the provided index.
     * @param insertIndex The index to insert the characters into.
     * @param values The characters to add.
     * @return This object for method chaining.
     */
    public CharacterList insertAll(int insertIndex, char... values)
    {
        PreCondition.assertIndexAccess(insertIndex, this.count + 1, "insertIndex");
        PreCondition.assertNotNull(values, "values");

        for (int i = 0; i < values.length; i++)
        {
            this.insert(insertIndex + i, values[i]);
        }

        return this;
    }

    /**
     * Insert all the characters in the provided {@link String} to this {@link CharacterList} at the provided index.
     * @param insertIndex The index to insert the characters into.
     * @param values The characters to add.
     * @return This object for method chaining.
     */
    public CharacterList insertAll(int insertIndex, String values)
    {
        PreCondition.assertIndexAccess(insertIndex, this.count + 1, "insertIndex");
        PreCondition.assertNotNull(values, "values");

        return this.insertAll(insertIndex, values.toCharArray());
    }

    /**
     * Add the provided value onto the end of this {@link CharacterList}.
     * @param value The value to add.
     * @return This object for method chaining.
     */
    public CharacterList add(char value)
    {
        return this.insert(this.count, value);
    }

    /**
     * Add all the characters in the provided {@link String} to this {@link CharacterList}.
     * @param values The characters to add.
     * @return This object for method chaining.
     */
    public CharacterList addAll(String values)
    {
        PreCondition.assertNotNull(values, "values");

        return this.addAll(values.toCharArray());
    }

    /**
     * Add all the provided characters to this {@link CharacterList}.
     * @param values The characters to add.
     * @return This object for method chaining.
     */
    public CharacterList addAll(char... values)
    {
        PreCondition.assertNotNull(values, "values");

        for (final char value : values)
        {
            this.add(value);
        }
        return this;
    }

    @Override
    public Character removeAt(int index)
    {
        PreCondition.assertIndexAccess(index, this.count, "index");

        final char result = this.characters[index];
        if (index < this.count - 1)
        {
            Array.shiftLeft(this.characters, index, this.count - index - 1);
        }
        this.count--;

        return result;
    }

    @Override
    public Result<CharacterArray> removeFirst(int valuesToRemove)
    {
        PreCondition.assertGreaterThanOrEqualTo(valuesToRemove, 0, "valuesToRemove");

        return Result.create(() ->
        {
            final char[] characters = new char[valuesToRemove];
            final int charactersRemoved = this.removeFirst(characters).await();
            return CharacterArray.create(characters, 0, charactersRemoved);
        });
    }

    /**
     * Remove the first characters from this {@link CharacterList} and put them into the
     * outputCharacters array.
     * @param outputCharacters The char[] to put the characters into.
     * @return The number of characters that were removed and added to the provided
     * outputCharacters.
     */
    public Result<Integer> removeFirst(char[] outputCharacters)
    {
        PreCondition.assertNotNull(outputCharacters, "outputCharacters");

        return this.removeFirst(outputCharacters, 0, outputCharacters.length);
    }

    /**
     * Remove the first characters from this {@link CharacterList} and put them into the
     * outputCharacters array.
     * @param outputCharacters The array to put the characters into.
     * @param startIndex The start index in the array to start putting the characters to.
     * @param length The number of characters to remove from the list and put into the array.
     * @return The number of characters that were removed.
     * @exception EmptyException if length is greater than 0 and this {@link CharacterList} is
     * empty.
     */
    public Result<Integer> removeFirst(char[] outputCharacters, int startIndex, int length)
    {
        PreCondition.assertNotNull(outputCharacters, "outputCharacters");
        PreCondition.assertStartIndex(startIndex, outputCharacters.length);
        PreCondition.assertLength(length, startIndex, outputCharacters.length);

        return Result.create(() ->
        {
            int result = 0;

            if (length >= 1)
            {
                if (!this.any())
                {
                    throw new EmptyException();
                }

                result = Math.minimum(this.count, length);
                Array.copy(this.characters, 0, outputCharacters, startIndex, result);
                if (result < this.count)
                {
                    Array.copy(this.characters, result, this.characters, 0, this.count - result);
                }
                this.count -= result;
            }

            PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

            return result;
        });
    }

    @Override
    public CharacterList clear()
    {
        this.count = 0;

        return this;
    }

    public CharacterList set(int index, char value)
    {
        PreCondition.assertIndexAccess(index, count, "index");

        this.characters[index] = value;

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

    public boolean endsWith(char[] values)
    {
        PreCondition.assertNotNull(values, "values");

        return this.endsWith(CharacterArray.create(values));
    }

    public boolean endsWith(String value)
    {
        PreCondition.assertNotNull(value, "value");

        return this.endsWith(Strings.iterable(value));
    }

    @Override
    public Iterator<Character> iterate()
    {
        return this.count == 0 ? Iterator.create() : new CharacterArrayIterator(characters, 0, count);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return this.toString(true);
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
            ? String.valueOf(this.characters, 0, this.count)
            : Iterable.toString(this);
    }

    /**
     * Get the characters of this CharacterList as a char[].
     * @return The characters of this CharacterList as a char[].
     */
    public char[] toCharArray()
    {
        return Array.clone(this.characters, 0, this.count);
    }
}
