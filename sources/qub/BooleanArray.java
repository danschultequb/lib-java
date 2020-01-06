package qub;

/**
 * A wrapper class around the boolean[] primitive type.
 */
public class BooleanArray implements Array<Boolean>
{
    private final boolean[] values;

    private BooleanArray(boolean[] values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values = values;
    }

    /**
     * Create a new BooleanArray with the provided capacity.
     * @param initialCapacity The initial capacity of the new BooleanArray.
     * @return A new BooleanArray with the provided capacity.
     */
    public static BooleanArray create(int initialCapacity)
    {
        PreCondition.assertGreaterThanOrEqualTo(initialCapacity, 0, "initialCapacity");

        return BooleanArray.create(new boolean[initialCapacity]);
    }

    public static BooleanArray create(boolean... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new BooleanArray(values);
    }

    public static BooleanArray create(boolean[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        boolean[] resultValues;
        if (values.length == length)
        {
            resultValues = values;
        }
        else
        {
            resultValues = new boolean[length];
            for (int i = 0; i < length; ++i)
            {
                resultValues[i] = values[startIndex + i];
            }
        }

        return new BooleanArray(resultValues);
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
    public void set(int index, boolean value)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        values[index] = value;
    }

    @Override
    public void set(int index, Boolean value)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");
        PreCondition.assertNotNull(value, "value");

        values[index] = value;
    }

    @Override
    public Boolean get(int index)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        return values[index];
    }

    /**
     * Get a boolean[] representation of this BooleanArray.
     * @return The boolean[].
     */
    public boolean[] toBooleanArray()
    {
        return values;
    }
}
