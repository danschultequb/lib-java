package qub;

public class Longs
{
    Longs()
    {
    }

    /**
     * The smallest number that a long can hold.
     */
    public static final long minimumValue = Long.MIN_VALUE;

    /**
     * The largest number that a long can hold.
     */
    public static final long maximumValue = Long.MAX_VALUE;

    /**
     * The number of bits in a short.
     */
    public static final int bitCount = 64;

    /**
     * The number of bytes in a short.
     */
    public static final int byteCount = 8;

    /**
     * The number of hex characters in a short.
     */
    public static final int hexCharCount = 16;

    /**
     * Get the string representation of the provided long.
     * @param value The value to convert to a String.
     * @return The string representation of the provided long.
     */
    public static String toString(long value)
    {
        return java.lang.Long.toString(value);
    }

    /**
     * Get the string representation of the provided long.
     * @param value The value to convert to a String.
     * @return The string representation of the provided long.
     */
    public static String toString(java.lang.Long value)
    {
        PreCondition.assertNotNull(value, "value");

        return java.lang.Long.toString(value);
    }

    /**
     * Get the hex string for the provided long.
     * @param value The value to convert to a hex string.
     * @return The hex string for the provided long.
     */
    public static String toHexString(long value)
    {
        return toHexString(value, false);
    }

    /**
     * Get the hex string for the provided long.
     * @param value The value to convert to a hex string.
     * @param trimLeadingZeros Whether or not leading zeros will be trimmed away.
     * @return The hex string for the provided long.
     */
    public static String toHexString(long value, boolean trimLeadingZeros)
    {
        final StringBuilder builder = new StringBuilder(hexCharCount);
        if (trimLeadingZeros)
        {
            do
            {
                builder.insert(0, Bytes.toHexChar(value & 0xF));
                value = (value >>> 4);
            }
            while (value > 0);
        }
        else
        {
            for (int i = 0; i < hexCharCount; ++i)
            {
                builder.insert(0, Bytes.toHexChar(value & 0xF));
                value = (value >>> 4);
            }
        }
        return builder.toString();
    }

    public static Result<Long> parse(String value)
    {
        PreCondition.assertNotNullAndNotEmpty(value, "value");

        Result<Long> result;
        try
        {
            result = Result.success(Long.parseLong(value));
        }
        catch (Throwable error)
        {
            result = Result.error(error);
        }
        return result;
    }
}
