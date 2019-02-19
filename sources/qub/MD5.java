package qub;

/**
 * A hash function that uses the MD5 hash algorithm to hash a sequence of bits.
 */
public class MD5
{
    private final static int BlockBitCount = 512;
    private final static int resultBitCount = 128;

    private static final int S11 =  7;
    private static final int S12 = 12;
    private static final int S13 = 17;
    private static final int S14 = 22;
    private static final int S21 =  5;
    private static final int S22 =  9;
    private static final int S23 = 14;
    private static final int S24 = 20;
    private static final int S31 =  4;
    private static final int S32 = 11;
    private static final int S33 = 16;
    private static final int S34 = 23;
    private static final int S41 =  6;
    private static final int S42 = 10;
    private static final int S43 = 15;
    private static final int S44 = 21;

    /**
     * Get the padding that should be added to the end of the message to hash.
     * @param bitCount The number of bits in the message to hash.
     * @return The bits to append to the end of the message to hash.
     */
    public BitArray getPaddingFromBitCount(long bitCount)
    {
        int resultBitCount = BlockBitCount - (int)(bitCount % BlockBitCount);
        if (resultBitCount <= Long.SIZE)
        {
            resultBitCount += BlockBitCount;
        }
        final BitArray result = new BitArray(resultBitCount);
        result.set(0, 1);

        final long littleEndianBitCount = ByteOrder.toLittleEndianLong(bitCount);
        result.setLastBitsFromLong(littleEndianBitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result.getCount(), 65, "result.getCount()");

        return result;
    }

    public BitArray hash(BitArray bits)
    {
        PreCondition.assertNotNull(bits, "bytes");

        final long bitCount = bits.getCount();
        final BitArray padding = getPaddingFromBitCount(bitCount);
        final BitArray paddedBits = bits.concatenate(padding);

        final Value<Integer> A = new Value<>(ByteOrder.toLittleEndianInteger(0x1234567));
        final Value<Integer> B = new Value<>(ByteOrder.toLittleEndianInteger(0x89abcdef));
        final Value<Integer> C = new Value<>(ByteOrder.toLittleEndianInteger(0xfedcba98));
        final Value<Integer> D = new Value<>(ByteOrder.toLittleEndianInteger(0x76543210));

        int[] X = new int[BlockBitCount / Integers.bitCount];

        // for (int i = 0; i < )

        final BitArray result = new BitArray(resultBitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(resultBitCount, result.getCount(), "result.getCount()");

        return result;
    }

    public BitArray hash(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        final BitArray result = new BitArray(resultBitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(resultBitCount, result.getCount(), "result.getCount()");

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

        final BitArray result = new BitArray(resultBitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(resultBitCount, result.getCount(), "result.getCount()");

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

    // void FF(Value<Integer> a, Value<Integer> b, Value<Integer> c, Value<Integer> d, int k, int s, int i)

    public BitArray hash(Iterable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        final BitArray result = new BitArray(resultBitCount);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(resultBitCount, result.getCount(), "result.getCount()");

        return result;
    }
}
