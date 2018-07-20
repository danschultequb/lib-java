package qub;

public class Shorts
{
    Shorts()
    {
    }

    /**
     * The number of bits in a short.
     */
    public static final int bitCount = 16;

    /**
     * The number of bytes in a short.
     */
    public static final int byteCount = 2;

    /**
     * The number of hex characters in a short.
     */
    public static final int hexCharCount = 4;

    public static String toHexString(short value)
    {
        return toHexString(value, false);
    }

    public static String toHexString(short value, boolean trimLeadingZeros)
    {
        final StringBuilder builder = new StringBuilder(hexCharCount);
        if (trimLeadingZeros)
        {
            do
            {
                builder.insert(0, Bytes.toHexChar(value & 0xF));
                value = (short)(value >>> 4);
            }
            while (value > 0);
        }
        else
        {
            for (int i = 0; i < hexCharCount; ++i)
            {
                builder.insert(0, Bytes.toHexChar(value & 0xF));
                value = (short)(value >>> 4);
            }
        }
        return builder.toString();
    }
}
