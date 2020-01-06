package qub;

/**
 * A wrapper class around the int[] primitive type.
 */
public class IntegerArray implements Array<Integer>
{
    private final int[] values;

    private IntegerArray(int[] values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values = values;
    }

    /**
     * Create a new IntegerArray from the provided values.
     * @param values The values to initialize the IntegerArray with.
     * @return The new IntegerArray.
     */
    public static IntegerArray create(int... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new IntegerArray(values);
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
