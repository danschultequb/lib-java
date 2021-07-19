package qub;

public interface Integers
{
    /**
     * The number of bits in an int/Integer.
     */
    int bitCount = 32;

    /**
     * The number of bytes in an int/Integer.
     */
    int byteCount = 4;

    /**
     * The number of hex characters in an int/Integer.
     */
    int hexCharCount = 8;

    int maximum = java.lang.Integer.MAX_VALUE;

    int minimum = java.lang.Integer.MIN_VALUE;

    /**
     * Get whether or not the provided value is zero or null.
     * @param value The value to check.
     * @return Whether or not the provided value is zero or null.
     */
    static boolean isZeroOrNull(int value)
    {
        return value == 0;
    }

    /**
     * Get whether or not the provided value is zero or null.
     * @param value The value to check.
     * @return Whether or not the provided value is zero or null.
     */
    static boolean isZeroOrNull(Integer value)
    {
        return value == null || value == 0;
    }

    static boolean equal(int lhs, int rhs)
    {
        return lhs == rhs;
    }

    static boolean equal(Integer lhs, Integer rhs)
    {
        return Comparer.equal(lhs, rhs);
    }

    static boolean lessThan(int lhs, int rhs)
    {
        return lhs < rhs;
    }

    static boolean lessThan(Integer lhs, Integer rhs)
    {
        return compare(lhs, rhs) == Comparison.LessThan;
    }

    static boolean lessThanOrEqualTo(Integer lhs, Integer rhs)
    {
        return compare(lhs, rhs) != Comparison.GreaterThan;
    }

    /**
     * Compare the two provided ints.
     * @param lhs The first int to compare.
     * @param rhs The second int to compare.
     * @return The comparison of the two ints.
     */
    static Comparison compare(int lhs, int rhs)
    {
        return Comparison.create(lhs - rhs);
    }

    /**
     * Compare the two provided ints.
     * @param lhs The first int to compare.
     * @param rhs The second int to compare.
     * @return The comparison of the two ints.
     */
    static Comparison compare(int lhs, Integer rhs)
    {
        return rhs == null ? Comparison.GreaterThan : Integers.compare(lhs, rhs.intValue());
    }

    /**
     * Compare the two provided ints.
     * @param lhs The first int to compare.
     * @param rhs The second int to compare.
     * @return The comparison of the two ints.
     */
    static Comparison compare(Integer lhs, int rhs)
    {
        return lhs == null ? Comparison.LessThan : Integers.compare(lhs.intValue(), rhs);
    }

    /**
     * Compare the two provided Integers.
     * @param lhs The first Integer to compare.
     * @param rhs The second Integer to compare.
     * @return The comparison of the two Integers.
     */
    static Comparison compare(Integer lhs, Integer rhs)
    {
        return lhs == rhs ? Comparison.Equal :
                lhs == null ? Comparison.LessThan :
                rhs == null ? Comparison.GreaterThan :
                Integers.compare(lhs.intValue(), rhs.intValue());
    }

    /**
     * Get the sum of the provided values.
     * @param values The values to get the sum of.
     * @return The sum of the provided values.
     */
    static int sum(java.lang.Integer... values)
    {
        PreCondition.assertNotNull(values, "values");

        return Integers.sum(Iterable.create(values));
    }

    /**
     * Get the sum of the provided values.
     * @param values The values to get the sum of.
     * @return The sum of the provided values.
     */
    static int sum(Iterable<Integer> values)
    {
        PreCondition.assertNotNull(values, "values");

        return Integers.sum(values.iterate());
    }

    /**
     * Get the sum of the provided values.
     * @param values The values to get the sum of.
     * @return The sum of the provided values.
     */
    static int sum(Iterator<Integer> values)
    {
        PreCondition.assertNotNull(values, "values");

        int result = 0;
        int index = 0;
        for (final Integer value : values)
        {
            PreCondition.assertNotNull(value, "values[" + index + "]");

            result += value;
            ++index;
        }

        return result;
    }

    /**
     * Rotate the bits in the value to the left.
     * @param value The value to rotate.
     * @return The rotated value.
     */
    static int rotateLeft(int value)
    {
        return rotateLeft(value, 1);
    }

    /**
     * Rotate the bits in the value to the left.
     * @param value The value to rotate.
     * @param places The number of bit places to rotate to the left.
     * @return The rotated value.
     */
    static int rotateLeft(int value, int places)
    {
        int result = value;

        if (value != 0 && value != -1)
        {
            final int leftShift = Math.modulo(places, Integer.SIZE);
            if (leftShift != 0)
            {
                final int rightShift = Integer.SIZE - leftShift;
                result = (value << leftShift) | (value >>> rightShift);
            }
        }

        return result;
    }

    /**
     * Rotate the bits in the value to the right.
     * @param value The value to rotate.
     * @return The rotated value.
     */
    static int rotateRight(int value)
    {
        return rotateRight(value, 1);
    }

    /**
     * Rotate the bits in the value to the right.
     * @param value The value to rotate.
     * @param places The number of bit places to rotate to the right.
     * @return The rotated value.
     */
    static int rotateRight(int value, int places)
    {
        return rotateLeft(value, -places);
    }

    /**
     * Get the String representation of the provided value.
     * @param value The value.
     * @return The String representation of the provided value.
     */
    static String toString(int value)
    {
        return java.lang.Integer.toString(value);
    }

    /**
     * Get the String representation of the provided value.
     * @param value The value.
     * @return The String representation of the provided value.
     */
    static String toString(java.lang.Integer value)
    {
        PreCondition.assertNotNull(value, "value");

        return java.lang.Integer.toString(value);
    }

    static String toHexString(int value)
    {
        return Integers.toHexString(value, false);
    }

    static String toHexString(int value, boolean trimLeadingZeros)
    {
        final StringBuilder builder = new StringBuilder(hexCharCount);
        if (trimLeadingZeros)
        {
            do
            {
                builder.insert(0, Bytes.toHexChar(value & 0xF));
                value = value >>> 4;
            }
            while (value > 0);
        }
        else
        {
            for (int i = 0; i < hexCharCount; ++i)
            {
                builder.insert(0, Bytes.toHexChar(value & 0xF));
                value = value >>> 4;
            }
        }
        return builder.toString();
    }

    static int fromHexString(String hexString)
    {
        PreCondition.assertNotNullAndNotEmpty(hexString, "hexString");
        PreCondition.assertBetween(1, hexString.length(), hexCharCount, "hexString.length()");

        int result = 0;
        for (int i = 0; i < hexString.length(); ++i)
        {
            result = result << 4;

            final char c = hexString.charAt(i);
            result |= fromHexChar(c);
        }

        return result;
    }

    static int fromHexChar(char hexChar)
    {
        PreCondition.assertOneOf(hexChar, new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F' }, "hexChar");

        int result;
        if ('0' <= hexChar && hexChar <= '9')
        {
            result = (hexChar - '0');
        }
        else
        {
            final char base = ('a' <= hexChar && hexChar <= 'f' ? 'a' : 'A');
            result = 10 + hexChar - base;
        }

        PostCondition.assertBetween(0, result, 15, "result");

        return result;
    }

    /**
     * Parse an Integer from the provided text.
     * @param text The text to parse.
     * @return The parsed Integer.
     */
    static Result<Integer> parse(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        return Result.create(() -> java.lang.Integer.parseInt(text));
    }

    /**
     * Get the bit at the provided bitIndex in the provided value.
     * @param value The int value.
     * @param bitIndex The bitIndex (where 0 is the most significant bit digit).
     * @return The bit at the provided bitIndex.
     */
    static int getBit(int value, int bitIndex)
    {
        PreCondition.assertBetween(0, bitIndex, Integers.bitCount - 1, "bitIndex");

        return (value >>> ((Integers.bitCount - 1) - bitIndex)) & 0x1;
    }

    static int getBits(int value, int startIndex, int length)
    {
        PreCondition.assertBetween(0, startIndex, Integers.bitCount - 1, "startIndex");
        PreCondition.assertBetween(1, length, Integers.bitCount - startIndex, "length");

        final int result = 0;

        return result;
    }

    /**
     * Set the bit at the provided bitIndex for the provided value. Bit indexes start with the most
     * significant (left-most) bit at 0 and go to the least significant (right-most) bit at
     * bitCount - 1.
     * @param value The int value to set the bit in.
     * @param bitIndex The index of the bit to get.
     * @param bit The value to set. This can be either 0 or 1.
     * @preCondition 0 <= bitIndex <= Integer.SIZE - 1
     * @preCondition bit == 0 || bit == 1
     */
    static int setBit(int value, int bitIndex, int bit)
    {
        PreCondition.assertBetween(0, bitIndex, Integer.SIZE - 1, "bitIndex");
        PreCondition.assertOneOf(bit, new int[] { 0, 1 }, "value");

        if (bit == 0)
        {
            value &= getAllOnExceptMask(bitIndex);
        }
        else
        {
            value |= getAllOffExceptMask(bitIndex);
        }

        return value;
    }

    int[] allOnExceptMasks = new int[]
    {
        0x7FFFFFFF,
        0xBFFFFFFF,
        0xDFFFFFFF,
        0xEFFFFFFF,
        0xF7FFFFFF,
        0xFBFFFFFF,
        0xFDFFFFFF,
        0xFEFFFFFF,
        0xFF7FFFFF,
        0xFFBFFFFF,
        0xFFDFFFFF,
        0xFFEFFFFF,
        0xFFF7FFFF,
        0xFFFBFFFF,
        0xFFFDFFFF,
        0xFFFEFFFF,
        0xFFFF7FFF,
        0xFFFFBFFF,
        0xFFFFDFFF,
        0xFFFFEFFF,
        0xFFFFF7FF,
        0xFFFFFBFF,
        0xFFFFFDFF,
        0xFFFFFEFF,
        0xFFFFFF7F,
        0xFFFFFFBF,
        0xFFFFFFDF,
        0xFFFFFFEF,
        0xFFFFFFF7,
        0xFFFFFFFB,
        0xFFFFFFFD,
        0xFFFFFFFE
    };
    static int getAllOnExceptMask(int bitOffset)
    {
        PreCondition.assertBetween(0, bitOffset, Integer.SIZE - 1, "bitOffset");

        return allOnExceptMasks[bitOffset];
    }

    int[] allOffExceptMasks = new int[]
    {
        0x80000000,
        0x40000000,
        0x20000000,
        0x10000000,
        0x08000000,
        0x04000000,
        0x02000000,
        0x01000000,
        0x00800000,
        0x00400000,
        0x00200000,
        0x00100000,
        0x00080000,
        0x00040000,
        0x00020000,
        0x00010000,
        0x00008000,
        0x00004000,
        0x00002000,
        0x00001000,
        0x00000800,
        0x00000400,
        0x00000200,
        0x00000100,
        0x00000080,
        0x00000040,
        0x00000020,
        0x00000010,
        0x00000008,
        0x00000004,
        0x00000002,
        0x00000001
    };
    static int getAllOffExceptMask(int bitOffset)
    {
        PreCondition.assertBetween(0, bitOffset, Integer.SIZE - 1, "bitOffset");

        return allOffExceptMasks[bitOffset];
    }
}
