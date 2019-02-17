package qub;

/**
 * A wrapper class around the boolean[] primitive type.
 */
public class BooleanArray extends Array<Boolean>
{
    private final boolean[] values;

    public BooleanArray(int count)
    {
        this.values = new boolean[count];
    }

    public BooleanArray(boolean[] values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values = values;
    }

    public BooleanArray(boolean[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        if (values.length == length)
        {
            this.values = values;
        }
        else
        {
            this.values = new boolean[length];
            for (int i = 0; i < length; ++i)
            {
                this.values[i] = values[startIndex + i];
            }
        }
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
