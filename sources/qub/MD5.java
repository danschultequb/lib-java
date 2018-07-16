package qub;

/**
 * A hash function that uses the MD5 hash algorithm to hash a sequence of bits.
 */
public class MD5
{
    private final static int blockBitCount = 512;
    private final static int resultBitCount = 128;

    /**
     * Get the padding that should be added to the end of the message to hash.
     * @param bitCount The number of bits in the message to hash.
     * @return The bits to append to the end of the message to hash.
     */
    public BitArray getPaddingFromBitCount(long bitCount)
    {
        int resultBitCount = blockBitCount - (int)(bitCount % blockBitCount);
        if (resultBitCount <= Long.SIZE)
        {
            resultBitCount += blockBitCount;
        }
        final BitArray result = BitArray.fromBitCount(resultBitCount);
        result.setBit(0, 1);
        result.setLastBitsFromLong(bitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result.getBitCount(), 65, "result.getBitCount()");

        return result;
    }

    public BitArray hash(BitArray bits)
    {
        PreCondition.assertNotNull(bits, "bytes");

        final long bitCount = bits.getBitCount();
        final BitArray padding = getPaddingFromBitCount(bitCount);
        final BitArray paddedBits = bits.concatenate(padding);

        final int[] state = new int[]
        {
            0x67452301,
            0xefcdab89,
            0x98badcfe,
            0x10325476
        };

        int[] X = new int[blockBitCount / Integer.SIZE];
        // for (int i = 0; i < )

        final BitArray result = BitArray.fromBitCount(resultBitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(resultBitCount, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    public BitArray hash(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        final BitArray result = BitArray.fromBitCount(resultBitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(resultBitCount, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    public BitArray hash(Iterator<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        long bitsRead = 0;

        int A = 0x01234567;
        int B = 0x89abcdef;
        int C = 0xfedcba98;
        int D = 0x76543210;

        final BitArray result = BitArray.fromBitCount(resultBitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(resultBitCount, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    int F(int X, int Y, int Z)
    {
        return (X & Y) | (~X & Z);
    }

    int G(int X, int Y, int Z)
    {
        return (X & Z) | (Y & ~Z);
    }

    int H(int X, int Y, int Z)
    {
        return X ^ Y ^ Z;
    }

    int I(int X, int Y, int Z)
    {
        return Y ^ (X | ~Z);
    }

    public BitArray hash(Iterable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        final BitArray result = BitArray.fromBitCount(resultBitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(resultBitCount, result.getBitCount(), "result.getBitCount()");

        return result;
    }
}
