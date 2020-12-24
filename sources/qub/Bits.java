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
}
