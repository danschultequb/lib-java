package qub;

/**
 * A wrapper class around the byte[] primitive type.
 */
public class ByteArray implements Array<Byte>
{
    private final byte[] values;

    private ByteArray(byte[] values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values = values;
    }

    public static ByteArray createWithLength(int length)
    {
        PreCondition.assertGreaterThanOrEqualTo(length, 0, "length");

        return ByteArray.create(new byte[length]);
    }

    public static ByteArray create(byte... bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return new ByteArray(bytes);
    }

    public static ByteArray create(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertStartIndex(startIndex, bytes.length);
        PreCondition.assertLength(length, startIndex, bytes.length);

        byte[] resultBytes;
        if (bytes.length == length)
        {
            resultBytes = bytes;
        }
        else
        {
            resultBytes = new byte[length];
            Array.copy(bytes, startIndex, resultBytes, 0, length);
        }
        return new ByteArray(resultBytes);
    }

    /**
     * Create a new ByteArray from the provided integer values. Each integer value must be between
     * Bytes.minimum and Bytes.maximum.
     * @param bytes The integer values.
     * @return The new ByteArray.
     */
    public static Result<ByteArray> create(int... bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return ByteArray.create(bytes, 0, bytes.length);
    }

    public static Result<ByteArray> create(int[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertStartIndex(startIndex, bytes.length);
        PreCondition.assertLength(length, startIndex, bytes.length);

        return Result.create(() ->
        {
            final byte[] resultBytes = new byte[length];
            for (int writeIndex = 0; writeIndex < length; ++writeIndex)
            {
                final int readIndex = startIndex + writeIndex;
                final int intValue = bytes[readIndex];
                if (!Comparer.between(Bytes.minimum, intValue, Bytes.maximum))
                {
                    throw new ParseException(AssertionMessages.between(Bytes.minimum, intValue, Bytes.maximum, "The " + readIndex + "element"));
                }
                resultBytes[writeIndex] = (byte)intValue;
            }
            return new ByteArray(resultBytes);
        });
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
