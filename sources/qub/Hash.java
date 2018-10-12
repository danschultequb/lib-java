package qub;

/**
 * A class that can be used to calculate hash codes of values.
 */
public class Hash
{
    Hash()
    {
    }

    /**
     * Get the hash code of the provided value.
     * @param value The value to get the hash code of.
     * @return The hash code of the provided value.
     */
    public static int getHashCode(Object value)
    {
        return value == null ? 0 : value.hashCode();
    }

    /**
     * Get the combined hash code of the provided values.
     * @param values The values to get the combined hash code of.
     * @return The combined hash code of the provided values.
     */
    public static int getHashCode(Object... values)
    {
        return getHashCode(Array.fromValues(values));
    }

    /**
     * Get the combined hash code of the provided values.
     * @param values The values to get the combined hash code of.
     * @return The combined hash code of the provided values.
     */
    public static int getHashCode(Iterable<?> values)
    {
        int result = 0;

        if (values != null)
        {
            int i = 0;
            for (final Object value : values)
            {
                result ^= Integers.rotateLeft(getHashCode(value) ^ i, i);
                ++i;
            }
        }

        return result;
    }
}
