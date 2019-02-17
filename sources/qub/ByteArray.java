package qub;

/**
 * A wrapper class around the byte[] primitive type.
 */
public class ByteArray extends Array<Byte>
{
    private final byte[] values;

    public ByteArray(int count)
    {
        this.values = new byte[count];
    }

    public ByteArray(byte[] values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values = values;
    }

    public ByteArray(byte[] values, int startIndex, int length)
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
            this.values = new byte[length];
            for (int i = 0; i < length; ++i)
            {
                this.values[i] = values[startIndex + i];
            }
        }
    }

    public ByteArray(int[] values)
    {
        PreCondition.assertNotNull(values, "bytes");

        this.values = new byte[values.length];
        for (int i = 0; i < values.length; ++i)
        {
            PreCondition.assertByte(values[i], "The " + i + " element");
            this.values[i] = (byte)values[i];
        }
    }

    public ByteArray(int[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        this.values = new byte[length];
        for (int i = 0; i < length; ++i)
        {
            final int value = values[i];
            PreCondition.assertByte(value, "The " + i + " element");
            this.values[i] = (byte)value;
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
    public void set(int index, byte value)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        values[index] = value;
    }

    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    public void set(int index, int value)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");
        PreCondition.assertByte(value, "value");

        values[index] = (byte)value;
    }

    @Override
    public void set(int index, Byte value)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");
        PreCondition.assertNotNull(value, "value");

        values[index] = value;
    }

    @Override
    public Byte get(int index)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        return values[index];
    }

    /**
     * Get a byte[] representation of this ByteArray.
     * @return The byte[].
     */
    public byte[] toByteArray()
    {
        return values;
    }
}
