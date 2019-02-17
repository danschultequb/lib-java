package qub;

/**
 * A wrapper class around the int[] primitive type.
 */
public class IntegerArray extends Array<Integer>
{
    private final int[] values;

    public IntegerArray(int count)
    {
        this(new int[count]);
    }

    public IntegerArray(int... values)
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

        final IntegerArray result = new IntegerArray(length);
        for (int i = 0; i < length; ++i)
        {
            result.set(i, values[startIndex + i]);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(length, result.getCount(), "length");

        return result;
    }
}
