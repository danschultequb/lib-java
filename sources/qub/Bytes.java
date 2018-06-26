package qub;

public class Bytes
{
    Bytes()
    {
    }

    public static int toUnsignedInt(byte value)
    {
        return 0xFF & value;
    }

    /**
     * Get the number of 1 bits at the front of the provided byte.
     * @param b The byte.
     * @return The number of 1 bits at the front of the provided byte.
     */
    public static int getSignificantBitCount(byte b)
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
     * Get the hexadecimal string representation of the provided byte.
     * @param b The byte.
     * @return The hexadecimal string representation of the provided byte.
     */
    public static String toHexString(byte b)
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
    public static String toHexString(byte b, boolean includePrefix)
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
    public static char toHexChar(int value)
    {
        char result = UTF8CharacterEncoding.replacementCharacter;
        if (0 <= value)
        {
            if (value <= 9)
            {
                result = (char)('0' + value);
            }
            else if (value <= 15)
            {
                result = (char)('A' + value - 10);
            }
        }
        return result;
    }

    public static byte fromHexChar(char hexChar)
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
}
