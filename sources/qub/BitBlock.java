package qub;

/**
 * A contiguous block of bits.
 */
public class BitBlock
{
    private static final int bitsPerChunk = Integer.SIZE;
    private static final long maximumBitCount = ((long)Integer.MAX_VALUE) * bitsPerChunk;

    private final int[] chunks;
    private final long bitCount;

    private BitBlock(long bitCount)
    {
        this.bitCount = bitCount;
        this.chunks = new int[(int)Math.ceiling(bitCount / 32.0)];
    }

    /**
     * The number of bits contained in this Block.
     * @return The number of bits contained in this Block.
     * @postCondition result >= 1
     */
    public long getBitCount()
    {
        return bitCount;
    }

    /**
     * The number of chunks that it takes to contain these bits.
     * @return The number of bytes that it takes to contain these bits.
     * @postCondition result >= 1
     */
    public int getChunkCount()
    {
        return chunks.length;
    }

    /**
     * Get the bit at the provided bitIndex. Bit indexes start with the most significant (left-most)
     * bit at 0 and go to the least significant (right-most) bit at bitCount - 1.
     * @param bitIndex The index of the bit to get.
     * @return The bit's value at the provided bitIndex.
     * @preCondition 0 <= bitIndex <= getBitCount() - 1
     * @postCondition result == 0 || result == 1
     */
    public int getBit(long bitIndex)
    {
        PreCondition.assertBetween(0, bitIndex, getBitCount() - 1, "bitIndex");

        final int chunk = chunks[bitIndexToChunkIndex(bitIndex)];
        final int chunkBitOffset = bitIndexToChunkBitOffset(bitIndex);
        return (chunk >>> (bitsPerChunk - 1 - chunkBitOffset)) & 0x1;
    }

    /**
     * Set the bit at the provided bitIndex to the provided value. Bit indexes start with the most
     * significant (left-most) bit at 0 and go to the least significant (right-most) bit at
     * bitCount - 1.
     * @param bitIndex The index of the bit to get.
     * @param value The value to set. This can be either 0 or 1.
     * @preCondition 0 <= bitIndex <= getBitCount() - 1
     * @preCondition value == 0 || value == 1
     */
    public void setBit(long bitIndex, int value)
    {
        PreCondition.assertBetween(0, bitIndex, getBitCount() - 1, "bitIndex");
        PreCondition.assertTrue(value == 0 || value == 1, "value (" + value + ") must be either 0 or 1.");

        final int chunkIndex = bitIndexToChunkIndex(bitIndex);
        int chunk = chunks[chunkIndex];

        final int chunkBitOffset = bitIndexToChunkBitOffset(bitIndex);
        if (value == 0)
        {
            final int mask = getAllOnExceptMask(chunkBitOffset);
            chunk &= mask;
        }
        else
        {
            final int mask = getAllOffExceptMask(chunkBitOffset);
            chunk |= mask;
        }

        chunks[chunkIndex] = chunk;
    }

    /**
     * Set all of the bits in this block to be the provided value.
     * @param value The value to set all of the bits in this block to.
     * @preCondition value == 0 || value == 1
     */
    public void setAllBits(int value)
    {
        PreCondition.assertTrue(value == 0 || value == 1, "value (" + value + ") must be either 0 or 1.");

        final int chunkValue = (value == 0 ? 0x00000000 : 0xFFFFFFFF);
        for (int i = 0; i < getChunkCount(); ++i)
        {
            chunks[i] = chunkValue;
        }
    }

    /**
     * Get the String representation of
     * @return
     */
    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        for (long i = 0; i < getBitCount(); ++i)
        {
            builder.append(getBit(i));
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof BitBlock && equals((BitBlock)rhs);
    }

    /**
     * Get whether or not the bits in this BitBlock object are the same as the bits in the provided rhs
     * BitBlock object.
     * @param rhs The BitBlock object to compare against this BitBlock object.
     * @return whether or not the bits in this BitBlock object are the same as the bits in the provided
     * rhs BitBlock object.
     */
    public boolean equals(BitBlock rhs)
    {
        return rhs != null &&
           bitCount == rhs.bitCount &&
           Comparer.equal(chunks, rhs.chunks);
    }

    /**
     * Create a new block containing the provided number of bits.
     * @param bitCount The number of bits the created Block will contain.
     * @return A Block with the provided number of bits.
     * @preCondition 0 <= bitCount <= Block.maximumBitCount
     * @postCondition result != null
     * @postCondition result.getBitCount() == bitCount
     */
    public static BitBlock fromBitCount(long bitCount)
    {
        PreCondition.assertBetween(1, bitCount, maximumBitCount, "bitCount");

        return new BitBlock(bitCount);
    }

    private static int bitIndexToChunkIndex(long bitIndex)
    {
        return (int)(bitIndex / bitsPerChunk);
    }

    private static int bitIndexToChunkBitOffset(long bitIndex)
    {
        return (int)(bitIndex % bitsPerChunk);
    }


    private static final int[] allOnExceptMasks = new int[]
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
    private static int getAllOnExceptMask(int bitOffset)
    {
        return allOnExceptMasks[bitOffset];
    }

    private static final int[] allOffExceptMasks = new int[]
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
    private static int getAllOffExceptMask(int bitOffset)
    {
        return allOffExceptMasks[bitOffset];
    }
}
