package qub;

public class Longs
{
    Longs()
    {
    }

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

    public static String toHexString(long value)
    {
        return toHexString(value, false);
    }

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
}
