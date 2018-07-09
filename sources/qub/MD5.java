package qub;

/**
 * A hash function that uses the MD5 hash algorithm to hash a sequence of bits.
 */
public class MD5
{
    public BitArray hash(BitArray bits)
    {
        PreCondition.assertNotNull(bits, "bytes");

        final BitArray result = BitArray.fromBitCount(128);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(128, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    public BitArray hash(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        final BitArray result = BitArray.fromBitCount(128);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(128, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    public BitArray hash(Iterator<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        final BitArray result = BitArray.fromBitCount(128);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(128, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    public BitArray hash(Iterable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        final BitArray result = BitArray.fromBitCount(128);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(128, result.getBitCount(), "result.getBitCount()");

        return result;
    }
}
