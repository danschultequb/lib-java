package qub;

public interface Bytes
{
    /**
     * The number of bits it takes to represent a byte.
     */
    int bitCount = 8;

    /**
     * The number of bytes it takes to represent a byte.
     */
    int byteCount = 1;

    /**
     * The number of hexidecimal characters it takes to represent a byte.
     */
    int hexCharCount = 2;

    /**
     * The minimum value that a signed byte can have.
     */
    byte minimum = java.lang.Byte.MIN_VALUE;

    /**
     * The maximum value that a signed byte can have.
     */
    byte maximum = java.lang.Byte.MAX_VALUE;

    /**
     * Convert the provided byte to an unsigned integer.
     * @param value The byte to convert.
     * @return The converted unsigned integer.
     */
    static int toUnsignedInt(byte value)
    {
        return 0xFF & value;
    }

    /**
     * Convert the provided by to an unsigned long integer.
     * @param value The byte to convert.
     * @return The converted unsigned long integer.
     */
    static long toUnsignedLong(byte value)
    {
        return 0xFF & value;
    }

    /**
     * Get the number of 1 bits at the front of the provided byte.
     * @param b The byte.
     * @return The number of 1 bits at the front of the provided byte.
     */
    static int getLeadingOneBits(byte b)
    {
        final int unsignedInt = Bytes.toUnsignedInt(b);
        return unsignedInt <= 0x7F ? 0 :
            unsignedInt <= 0xBF ? 1 :
            unsignedInt <= 0xDF ? 2 :
            unsignedInt <= 0xEF ? 3 :
            unsignedInt <= 0xF7 ? 4 :
            unsignedInt <= 0xFB ? 5 :
            unsignedInt <= 0xFD ? 6 :
            unsignedInt <= 0xFE ? 7 :
            8;
    }

    /**
     * Get the String representation of the provided byte value.
     * @param b The byte value.
     * @return The String representation of the provided byte value.
     */
    static String toString(byte b)
    {
        return java.lang.Byte.toString(b);
    }

    /**
     * Get the hexadecimal string representation of the provided byte.
     * @param b The byte.
     * @return The hexadecimal string representation of the provided byte.
     */
    static String toHexString(byte b)
    {
        return toHexString(b, true);
    }

    /**
     * Get the hexadecimal string representation of the provided byte.
     * @param b The byte.
     * @param includePrefix Whether or not to include the "0x" prefix at the beginning of the hex
     *                      string result.
     * @return The hexadecimal string representation of the provided byte.
     */
    static String toHexString(byte b, boolean includePrefix)
    {
        String result = (includePrefix ? "0x" : "");
        final int unsignedInt = Bytes.toUnsignedInt(b);

        final int firstHexDigitBits = ((unsignedInt >>> 4) & 0x0F);
        if (firstHexDigitBits != 0)
        {
            result += toHexChar(firstHexDigitBits);
        }

        final int secondHexDigitBits = (unsignedInt & 0x0F);
        result += toHexChar(secondHexDigitBits);

        return result;
    }

    /**
     * Get the hex character representation of the provided value. If the provided value is not
     * between 0 and 15 (inclusive), then the replacement character will be returned.
     * @param value The value to convert to a hex character.
     * @return The hex character associated with the provided value.
     */
    static char toHexChar(int value)
    {
        PreCondition.assertBetween(0, value, 15, "value");

        return (char)(value <= 9 ? '0' + value : 'A' + value - 10);
    }

    /**
     * Get the hex character representation of the provided value. If the provided value is not
     * between 0 and 15 (inclusive), then the replacement character will be returned.
     * @param value The value to convert to a hex character.
     * @return The hex character associated with the provided value.
     */
    static char toHexChar(long value)
    {
        PreCondition.assertBetween(0, value, 15, "value");

        return (char)(value <= 9 ? '0' + value : 'A' + value - 10);
    }

    static byte fromHexChar(char hexChar)
    {
        PreCondition.assertOneOf(hexChar, new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F' }, "hexChar");

        byte result;
        if ('0' <= hexChar && hexChar <= '9')
        {
            result = (byte)(hexChar - '0');
        }
        else if ('a' <= hexChar && hexChar <= 'f')
        {
            result = (byte)(10 + 'a' - hexChar);
        }
        else
        {
            result = (byte)(10 + 'A' - hexChar);
        }

        PostCondition.assertBetween(0, result, 15, "result");

        return result;
    }

    /**
     * Get the bit at the provided bitIndex in the provided value.
     * @param value The int value.
     * @param bitIndex The bitIndex (where 0 is the most significant bit digit).
     * @return The bit at the provided bitIndex.
     */
    static int getBit(byte value, int bitIndex)
    {
        return Bits.getBit(value, bitIndex);
    }
}
