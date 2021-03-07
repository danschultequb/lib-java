package qub;

public interface Bits
{
    int minimum = 0;

    int maximum = 1;

    /**
     * The maximum number of bits that can be stored in an integer array.
     */
    long maximumIntegerBitArrayCount = ((long)Integers.maximum) * Integers.bitCount;

    /**
     * The maximum number of bits that can be stored in a long array.
     */
    long maximumLongBitArrayCount = ((long)Integers.maximum) * Longs.bitCount;

    /**
     * The allowed values for a bit.
     */
    Indexable<Integer> allowedValues = Indexable.create(0, 1);

    /**
     * Get the bit at the provided bitIndex in the provided value.
     * @param value The int value.
     * @param bitIndex The bitIndex (where 0 is the most significant bit digit).
     * @return The bit at the provided bitIndex.
     */
    static int getBit(byte value, int bitIndex)
    {
        PreCondition.assertIndexAccess(bitIndex, Bytes.bitCount, "bitIndex");

        final int result = (value >>> ((Bytes.bitCount - 1) - bitIndex)) & 0x1;

        PostCondition.assertOneOf(result, Bits.allowedValues, "result");

        return result;
    }
}
