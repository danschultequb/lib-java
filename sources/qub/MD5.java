package qub;

import java.security.MessageDigest;

/**
 * A hash function that uses the MD5 hash algorithm to hash a sequence of bits.
 */
public interface MD5
{
    static BitArray hash(BitArray bits)
    {
        PreCondition.assertNotNull(bits, "bytes");

        BitArray result;
        try
        {
            final java.security.MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bits.toByteArray());
            result = BitArray.createFromBytes(messageDigest.digest());
        }
        catch (Throwable error)
        {
            throw Exceptions.asRuntime(error);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static BitArray hash(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        final BitArray result = hash(BitArray.createFromBytes(bytes));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static Result<BitArray> hash(Iterator<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        final Result<BitArray> result = hash(bytes.toList());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static Result<BitArray> hash(Iterable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        final Result<BitArray> result = Array.toByteArray(bytes)
            .then((byte[] byteArray) -> MD5.hash(byteArray));

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
