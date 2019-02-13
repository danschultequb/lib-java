package qub;

/**
 * A set of functions that help when interacting with primitive arrays.
 */
public interface Arrays
{
    /**
     * Create an Array from the provided values.
     * @param values The values to initialize the array with.
     */
    static Array<Boolean> createFrom(boolean... values)
    {
        PreCondition.assertNotNull(values, "values");

        final Array<Boolean> result = new Array<>(values.length);
        for (int i = 0; i < values.length; ++i)
        {
            result.set(i, values[i]);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(result.getCount(), values.length, "result.getCount()");

        return result;
    }

    /**
     * Create a new ByteArray with the provided elements.
     * @param values The elements of the new ByteArray.
     * @return The new ByteArray.
     */
    static ByteArray createFrom(byte... values)
    {
        PreCondition.assertNotNull(values, "values");

        return ByteArray.createFrom(values);
    }

    /**
     * Create a new ByteArray with the provided elements.
     * @param values The elements of the new ByteArray.
     * @param startIndex The start index into the values.
     * @param length The number of bytes to copy.
     * @return The new ByteArray.
     */
    static ByteArray createFrom(byte[] values, int startIndex, int length)
    {
        return ByteArray.createFrom(values, startIndex, length);
    }

    /**
     * Create an Iterator for the provided values.
     * @param values The values to iterate.
     * @return The Iterator for the provided values.
     */
    static Iterator<Byte> iterate(byte... values)
    {
        PreCondition.assertNotNull(values, "values");

        return new ByteArrayIterator(values);
    }

    /**
     * Create an Iterator for the provided values.
     * @param values The values to iterate.
     * @return The Iterator for the provided values.
     */
    static Iterator<Byte> iterate(byte[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        return new ByteArrayIterator(values, startIndex, length);
    }
}
