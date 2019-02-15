package qub;

/**
 * A wrapper class around the int[] primitive type.
 */
public class IntegerArray implements MutableIndexable<Integer>
{
    private final int[] values;

    private IntegerArray(int[] values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values = values;
    }

    @Override
    public int getCount()
    {
        return values.length;
    }

    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    public void set(int index, char value)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        values[index] = value;
    }

    @Override
    public void set(int index, Integer value)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");
        PreCondition.assertNotNull(value, "value");

        values[index] = value;
    }

    @Override
    public Integer get(int index)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        return values[index];
    }

    @Override
    public Iterator<Integer> iterate()
    {
        return new IntegerArrayIterator(values);
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
     * Create a new IntegerArray with the provided number of elements.
     * @param count The number of elements.
     * @return The new IntegerArray.
     */
    public static IntegerArray createWithLength(int count)
    {
        PreCondition.assertGreaterThanOrEqualTo(count, 0, "count");

        return new IntegerArray(new int[count]);
    }

    /**
     * Create a new IntegerArray with the provided elements.
     * @param values The elements of the new IntegerArray.
     * @return The new IntegerArray.
     */
    public static IntegerArray create(int... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new IntegerArray(values);
    }

    /**
     * Create a new CharacterArray with the provided elements.
     * @param values The elements of the new CharacterArray.
     * @param startIndex The start index into the values.
     * @param length The number of values to copy.
     * @return The new CharacterArray.
     */
    public static IntegerArray create(int[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        final IntegerArray result = IntegerArray.createWithLength(length);
        for (int i = 0; i < length; ++i)
        {
            result.set(i, values[startIndex + i]);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(length, result.getCount(), "length");

        return result;
    }
}
