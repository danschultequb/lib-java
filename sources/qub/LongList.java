package qub;

/**
 * A list of long integers. Internally this uses a primitive long[] to store longs, so it should be more
 * efficient than using a generic List<Long>.
 */
public class LongList implements List<Long>
{
    private long[] longs;
    private int count;

    private LongList(int initialCapacity)
    {
        PreCondition.assertGreaterThanOrEqualTo(initialCapacity, 0, "initialCapacity");

        this.longs = new long[initialCapacity];
    }

    public static LongList createWithCapacity(int initialCapacity)
    {
        PreCondition.assertGreaterThanOrEqualTo(initialCapacity, 0, "initialCapacity");

        return new LongList(initialCapacity);
    }

    public static LongList create(long... values)
    {
        PreCondition.assertNotNull(values, "values");

        final LongList result = LongList.createWithCapacity(values.length);
        for (final long value : values)
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

    public LongList insert(int insertIndex, long value)
    {
        PreCondition.assertBetween(0, insertIndex, this.count, "insertIndex");

        if (this.count == this.longs.length)
        {
            final long[] newLongs = new long[(this.longs.length * 2) + 1];
            if (1 <= insertIndex)
            {
                Array.copy(this.longs, 0, newLongs, 0, insertIndex);
            }
            if (insertIndex <= count - 1)
            {
                Array.copy(this.longs, insertIndex, newLongs, insertIndex + 1, this.count - insertIndex);
            }
            this.longs = newLongs;
        }
        else if (insertIndex < this.count)
        {
            Array.shiftRight(this.longs, insertIndex, this.count - insertIndex);
        }
        this.longs[insertIndex] = value;
        ++this.count;

        return this;
    }

    @Override
    public LongList insert(int insertIndex, Long value)
    {
        PreCondition.assertBetween(0, insertIndex, this.count, "insertIndex");
        PreCondition.assertNotNull(value, "value");

        return this.insert(insertIndex, value.longValue());
    }

    public LongList add(long value)
    {
        return this.insert(this.count, value);
    }

    public LongList add(Long value)
    {
        return this.insert(this.count, value);
    }

    @Override
    public LongList addAll(Long... values)
    {
        return (LongList)List.super.addAll(values);
    }

    @Override
    public Long removeAt(int index)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "this");
        PreCondition.assertBetween(0, index, this.count - 1, "index");

        final long result = this.longs[index];
        if (index != this.count - 1)
        {
            Array.shiftLeft(this.longs, index, this.count - index - 1);
        }
        --this.count;

        return result;
    }
    
    

    @Override
    public LongArray removeFirst(int valuesToRemove)
    {
        PreCondition.assertGreaterThanOrEqualTo(valuesToRemove, 0, "valuesToRemove");

        final long[] removedValues = new long[Math.minimum(this.count, valuesToRemove)];
        this.removeFirst(removedValues);
        return LongArray.create(removedValues);
    }

    /**
     * Remove the first integers from this LongList and put them into the outputLongs array. There must
     * be enough integers in this list to fill the provided array.
     * @param outputLongs The array to put the integers into.
     */
    public int removeFirst(long[] outputLongs)
    {
        PreCondition.assertNotNull(outputLongs, "outputLongs");

        return this.removeFirst(outputLongs, 0, outputLongs.length);
    }

    /**
     * Remove the first bytes from this LongList and put them into the outputLongs array.
     * @param outputLongs The array to put the bytes into.
     * @param startIndex The start index in the array to start putting the bytes to.
     * @param length The number of bytes to remove from the list and put into the array.
     */
    public int removeFirst(long[] outputLongs, int startIndex, int length)
    {
        PreCondition.assertNotNull(outputLongs, "outputLongs");
        PreCondition.assertStartIndex(startIndex, outputLongs.length);
        PreCondition.assertLength(length, startIndex, outputLongs.length);

        final int result = Math.minimum(this.count, length);
        if (result > 0)
        {
            Array.copy(this.longs, 0, outputLongs, startIndex, result);
            if (result < this.count)
            {
                Array.copy(this.longs, result, this.longs, 0, this.count - result);
            }
            this.count -= result;
        }

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public LongList set(int index, Integer value)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "this");
        PreCondition.assertBetween(0, index, this.count - 1, "index");
        PreCondition.assertNotNull(value, "value");

        return this.set(index, value.longValue());
    }

    @Override
    public LongList set(int index, Long value)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "this");
        PreCondition.assertBetween(0, index, this.count - 1, "index");
        PreCondition.assertNotNull(value, "value");

        this.longs[index] = value.longValue();

        return this;
    }

    public long getAsLong(int index)
    {
        PreCondition.assertNotNullAndNotEmpty(this, "this");
        PreCondition.assertBetween(0, index, this.count - 1, "index");

        return this.longs[index];
    }

    @Override
    public Long get(int index)
    {
        return this.getAsLong(index);
    }

    @Override
    public Iterator<Long> iterate()
    {
        return LongArrayIterator.create(this.longs, 0, this.count);
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
