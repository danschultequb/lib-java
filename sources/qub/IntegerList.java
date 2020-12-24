package qub;

/**
 * A list of integers. Internally this uses a primitive int[] to store bytes, so it should be more
 * efficient than using a generic List<Integer>.
 */
public class IntegerList implements List<Integer>
{
    private int[] integers;
    private int count;

    private IntegerList(int initialCapacity)
    {
        PreCondition.assertGreaterThanOrEqualTo(initialCapacity, 0, "initialCapacity");

        this.integers = new int[initialCapacity];
    }

    public static IntegerList createWithCapacity(int initialCapacity)
    {
        PreCondition.assertGreaterThanOrEqualTo(initialCapacity, 0, "initialCapacity");

        return new IntegerList(initialCapacity);
    }

    public static IntegerList create(int... values)
    {
        PreCondition.assertNotNull(values, "values");

        return IntegerList.createWithCapacity(values.length)
            .addAll(values);
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

    public IntegerList insert(int insertIndex, int value)
    {
        PreCondition.assertBetween(0, insertIndex, this.count, "insertIndex");

        if (this.count == this.integers.length)
        {
            final int[] newIntegers = new int[(this.integers.length * 2) + 1];
            if (1 <= insertIndex)
            {
                Array.copy(this.integers, 0, newIntegers, 0, insertIndex);
            }
            if (insertIndex <= count - 1)
            {
                Array.copy(this.integers, insertIndex, newIntegers, insertIndex + 1, this.count - insertIndex);
            }
            this.integers = newIntegers;
        }
        else if (insertIndex < this.count)
        {
            Array.shiftRight(this.integers, insertIndex, this.count - insertIndex);
        }
        this.integers[insertIndex] = value;
        ++this.count;

        return this;
    }

    @Override
    public IntegerList insert(int insertIndex, Integer value)
    {
        PreCondition.assertBetween(0, insertIndex, this.count, "insertIndex");
        PreCondition.assertNotNull(value, "value");

        return this.insert(insertIndex, value.intValue());
    }

    public IntegerList add(int value)
    {
        return this.insert(this.count, value);
    }

    public IntegerList add(Integer value)
    {
        return this.insert(this.count, value);
    }

    public IntegerList addAll(int... values)
    {
        PreCondition.assertNotNull(values, "values");

        if (values.length > 0)
        {
            for (final int value : values)
            {
                this.add(value);
            }
        }
        return this;
    }

    public IntegerList addAll(Integer... values)
    {
        PreCondition.assertNotNull(values, "values");

        if (values.length > 0)
        {
            for (final int value : values)
            {
                this.add(value);
            }
        }
        return this;
    }

    @Override
    public Integer removeAt(int index)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "this");
        PreCondition.assertBetween(0, index, this.count - 1, "index");

        final int result = this.integers[index];
        if (index != this.count - 1)
        {
            Array.shiftLeft(this.integers, index, this.count - index - 1);
        }
        --this.count;

        return result;
    }

    /**
     * Remove the first integers from this IntegerList as a IntegerArray.
     * @param valuesToRemove The number of integers to remove from this IntegerList.
     * @return The removed integers.
     */
    public IntegerArray removeFirstIntegers(int valuesToRemove)
    {
        PreCondition.assertGreaterThanOrEqualTo(valuesToRemove, 0, "valuesToRemove");
        PreCondition.assertNotNullAndNotEmpty(this, "this");

        final int[] integers = new int[valuesToRemove];
        this.removeFirstIntegers(integers);
        return IntegerArray.create(integers);
    }

    /**
     * Remove the first integers from this IntegerList and put them into the outputIntegers array. There must
     * be enough integers in this list to fill the provided array.
     * @param outputIntegers The array to put the integers into.
     */
    public int removeFirstIntegers(int[] outputIntegers)
    {
        PreCondition.assertNotNullAndNotEmpty(outputIntegers, "outputIntegers");
        PreCondition.assertNotNullAndNotEmpty(this, "this");

        return this.removeFirstIntegers(outputIntegers, 0, outputIntegers.length);
    }

    /**
     * Remove the first bytes from this IntegerList and put them into the outputIntegers array.
     * @param outputIntegers The array to put the bytes into.
     * @param startIndex The start index in the array to start putting the bytes to.
     * @param length The number of bytes to remove from the list and put into the array.
     */
    public int removeFirstIntegers(int[] outputIntegers, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputIntegers, "outputIntegers");
        PreCondition.assertStartIndex(startIndex, outputIntegers.length);
        PreCondition.assertLength(length, startIndex, outputIntegers.length);
        PreCondition.assertNotNullAndNotEmpty(this, "this");

        final int result = Math.minimum(this.count, length);
        if (result > 0)
        {
            Array.copy(this.integers, 0, outputIntegers, startIndex, result);
            if (result < this.count)
            {
                Array.copy(this.integers, result, this.integers, 0, this.count - result);
            }
            count -= length;
        }

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    @Override
    public IntegerArray removeFirst(int valuesToRemove)
    {
        return this.removeFirstIntegers(valuesToRemove);
    }

    public IntegerList set(int index, int value)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "this");
        PreCondition.assertBetween(0, index, this.count - 1, "index");

        this.integers[index] = value;

        return this;
    }

    @Override
    public IntegerList set(int index, Integer value)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "this");
        PreCondition.assertBetween(0, index, this.count - 1, "index");
        PreCondition.assertNotNull(value, "value");

        return this.set(index, value.intValue());
    }

    public int getAsInt(int index)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "this");
        PreCondition.assertBetween(0, index, this.count - 1, "index");

        return this.integers[index];
    }

    @Override
    public Integer get(int index)
    {
        return this.getAsInt(index);
    }

    @Override
    public Iterator<Integer> iterate()
    {
        return this.count == 0 ? Iterator.create() : new IntegerArrayIterator(this.integers, 0, this.count);
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
     * Get a int[] representation of this IntegerList.
     * @return The int[].
     */
    public int[] toIntArray()
    {
        return Array.clone(this.integers, 0, this.count);
    }
}
