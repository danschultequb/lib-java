package qub;

/**
 * A wrapper class around the long[] primitive type.
 */
public class LongArray implements Array<Long>
{
    private final long[] values;

    private LongArray(long[] values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values = values;
    }

    /**
     * Create a new IntegerArray from the provided values.
     * @param values The values to initialize the IntegerArray with.
     * @return The new IntegerArray.
     */
    public static LongArray create(long... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new LongArray(values);
    }

    @Override
    public int getCount()
    {
        return this.values.length;
    }

    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    public LongArray set(int index, long value)
    {
        PreCondition.assertIndexAccess(index, this.getCount(), "index");

        this.values[index] = value;

        return this;
    }

    @Override
    public LongArray set(int index, Long value)
    {
        PreCondition.assertIndexAccess(index, this.getCount(), "index");
        PreCondition.assertNotNull(value, "value");

        this.values[index] = value;

        return this;
    }

    @Override
    public Long get(int index)
    {
        PreCondition.assertIndexAccess(index, this.getCount(), "index");

        return this.values[index];
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
