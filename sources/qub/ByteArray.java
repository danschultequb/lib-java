package qub;

/**
 * A wrapper class around the byte[] primitive type.
 */
public class ByteArray implements MutableIndexable<Byte>
{
    private final byte[] bytes;

    private ByteArray(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        this.bytes = bytes;
    }

    @Override
    public int getCount()
    {
        return bytes.length;
    }

    /**
     * Set the value at the provided index.
     * @param index The index to set.
     * @param value The value to set at the provided index.
     */
    public void set(int index, byte value)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        bytes[index] = value;
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

        bytes[index] = (byte)value;
    }

    @Override
    public void set(int index, Byte value)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");
        PreCondition.assertNotNull(value, "value");

        bytes[index] = value;
    }

    @Override
    public Byte get(int index)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        return bytes[index];
    }

    @Override
    public Iterator<Byte> iterate()
    {
        return new ByteArrayIterator(bytes);
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
     * Create a new ByteArray with the provided number of elements.
     * @param count The number of elements.
     * @return The new ByteArray.
     */
    public static ByteArray create(int count)
    {
        PreCondition.assertGreaterThanOrEqualTo(count, 0, "count");

        return new ByteArray(new byte[count]);
    }

    /**
     * Create a new ByteArray with the provided elements.
     * @param values The elements of the new ByteArray.
     * @return The new ByteArray.
     */
    public static ByteArray createFrom(byte... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new ByteArray(values);
    }

    /**
     * Create a new ByteArray with the provided elements.
     * @param values The elements of the new ByteArray.
     * @return The new ByteArray.
     */
    public static ByteArray createFrom(int... values)
    {
        PreCondition.assertNotNull(values, "values");

        final ByteArray result = ByteArray.create(values.length);
        for (int i = 0; i < values.length; ++i)
        {
            result.set(i, values[i]);
        }

        return result;
    }

    /**
     * Create a new ByteArray with the provided elements.
     * @param values The elements of the new ByteArray.
     * @param startIndex The start index into the values.
     * @param length The number of bytes to copy.
     * @return The new ByteArray.
     */
    public static ByteArray createFrom(byte[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        final ByteArray result = ByteArray.create(length);
        for (int i = 0; i < length; ++i)
        {
            result.set(i, values[startIndex + i]);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(length, result.getCount(), "length");

        return result;
    }
}
