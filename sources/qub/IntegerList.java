package qub;

/**
 * A list of integers. Internally this uses a primitive int[] to store integers, so it should be more
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

        final IntegerList result = IntegerList.createWithCapacity(values.length);
        for (final int value : values)
        {
            result.add(value);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(values.length, result.getCount(), "result.getCount()");

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

    @Override
    public IntegerList addAll(Integer... values)
    {
        return (IntegerList)List.super.addAll(values);
    }

    @Override
    public IntegerList addAll(Iterator<? extends Integer> values)
    {
        return (IntegerList)List.super.addAll(values);
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

    @Override
    public Result<Iterable<Integer>> removeFirst(int valuesToRemove)
    {
        PreCondition.assertGreaterThanOrEqualTo(valuesToRemove, 0, "valuesToRemove");

        return Result.create(() ->
        {
            final int[] integers = new int[Math.minimum(this.count, valuesToRemove)];
            this.removeFirst(integers).await();
            return IntegerArray.create(integers);
        });
    }

    /**
     * Remove the first integers from this IntegerList and put them into the outputIntegers array. There must
     * be enough integers in this list to fill the provided array.
     * @param outputIntegers The array to put the integers into.
     */
    public Result<Integer> removeFirst(int[] outputIntegers)
    {
        PreCondition.assertNotNull(outputIntegers, "outputIntegers");

        return this.removeFirst(outputIntegers, 0, outputIntegers.length);
    }

    /**
     * Remove the first bytes from this IntegerList and put them into the outputIntegers array.
     * @param outputIntegers The array to put the bytes into.
     * @param startIndex The start index in the array to start putting the bytes to.
     * @param length The number of bytes to remove from the list and put into the array.
     */
    public Result<Integer> removeFirst(int[] outputIntegers, int startIndex, int length)
    {
        PreCondition.assertNotNull(outputIntegers, "outputIntegers");
        PreCondition.assertStartIndex(startIndex, outputIntegers.length);
        PreCondition.assertLength(length, startIndex, outputIntegers.length);

        return Result.create(() ->
        {
            final int result = Math.minimum(length, this.count);
            if (result > 0)
            {
                Array.copy(this.integers, 0, outputIntegers, startIndex, result);
                if (result < this.count)
                {
                    Array.copy(this.integers, result, this.integers, 0, this.count - result);
                }
                this.count -= result;
            }

            return result;
        });
    }

    @Override
    public IntegerList set(int index, Integer value)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "this");
        PreCondition.assertBetween(0, index, this.count - 1, "index");
        PreCondition.assertNotNull(value, "value");

        this.integers[index] = value.intValue();

        return this;
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
        return IntegerArrayIterator.create(this.integers, 0, this.count);
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
