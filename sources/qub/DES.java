package qub;

/**
 * An implementation of the DES (Data Encryption Standard) algorithm. More details can be found at
 * https://en.wikipedia.org/wiki/Data_Encryption_Standard.
 */
public class DES
{
    /**
     * Encrypt the provided plaintext using DES and the provided initialization vector.
     * @param initializationVector The initialization vector (IV) to use to encrypt the provided
     *                             plaintext. This must be either 56 or 64 bits (7 or 8 bytes) long.
     *                             If 64 bits (8 bytes) are provided, then bits 8, 16, 24, 32, 40,
     *                             48, 56, and 64 are ignored.
     * @param plaintext The plaintext to encrypt. This block must be 64 bits (8 bytes) long.
     * @return The encrypted Block. This Block will be 64 bits (8 bytes) long.
     */
    public BitArray encrypt(BitArray initializationVector, BitArray plaintext)
    {
        PreCondition.assertNotNull(initializationVector, "initializationVector");
        PreCondition.assertTrue(initializationVector.getBitCount() == 56 || initializationVector.getBitCount() == 64, "initializationVector.getBitCount() must be either 56 or 64.");
        PreCondition.assertNotNull(plaintext, "plaintext");
        PreCondition.assertEqual(64, plaintext.getBitCount(), "plaintext.getBitCount()");

        final BitArray result = null;

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(64, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    /**
     * Decrypt the provided ciphertext using DES and the provided initialization vector.
     * @param initializationVector The initialization vector (IV) to use to decrypt the provided
     *                             ciphertext. This must be either 56 or 64 bits (7 or 8 bytes)
     *                             long. If 64 bits (8 bytes) are provided, then bits 8, 16, 24, 32,
     *                             40, 48, 56, and 64 are ignored.
     * @param ciphertext The ciphertext to encrypt. This block must be 64 bits (8 bytes) long.
     * @return The encrypted block. This block will be 64 bits (8 bytes) long.
     */
    public BitArray decrypt(BitArray initializationVector, BitArray ciphertext)
    {
        PreCondition.assertNotNull(initializationVector, "initializationVector");
        PreCondition.assertTrue(initializationVector.getBitCount() == 56 || initializationVector.getBitCount() == 64, "initializationVector.getBitCount() must be either 56 or 64.");
        PreCondition.assertNotNull(ciphertext, "ciphertext");
        PreCondition.assertEqual(64, ciphertext.getBitCount(), "ciphertext.getBitCount()");

        final BitArray result = null;

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(64, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    BitArray initializationVectorToKey(BitArray initializationVector)
    {
        PreCondition.assertNotNull(initializationVector, "initializationVector");
        PreCondition.assertTrue(initializationVector.getBitCount() == 56 || initializationVector.getBitCount() == 64, "initializationVector.getBitCount() must be either 56 or 64.");

        BitArray result;
        if (initializationVector.getBitCount() == 64)
        {
            result = initializationVector.clone();
        }
        else
        {
            result = BitArray.fromBitCount(64);
            for (int i = 0; i < 8; ++i)
            {
                result.copyFrom(initializationVector, i * 7, i * 8, 7);
            }
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(64, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    /**
     * Convert the provided key into the first key permutation (permuted choice 1).
     * @param key The key to permute.
     * @return The first permuted key.
     * @preCondition key != null
     * @preCondition key.getBitCount() == 64
     * @postCondition result != null
     * @postCondition result.getBitCount() == 56
     */
    BitArray permutedChoice1(BitArray key)
    {
        PreCondition.assertNotNull(key, "key");
        PreCondition.assertEqual(64, key.getBitCount(), "key.getBitCount()");

        final BitArray result = BitArray.fromBitCount(56);

        // Row 1
        result.setBit(bitNumberToBitIndex(1), key.getBit(bitNumberToBitIndex(57)));
        result.setBit(bitNumberToBitIndex(2), key.getBit(bitNumberToBitIndex(49)));
        result.setBit(bitNumberToBitIndex(3), key.getBit(bitNumberToBitIndex(41)));
        result.setBit(bitNumberToBitIndex(4), key.getBit(bitNumberToBitIndex(33)));
        result.setBit(bitNumberToBitIndex(5), key.getBit(bitNumberToBitIndex(25)));
        result.setBit(bitNumberToBitIndex(6), key.getBit(bitNumberToBitIndex(17)));
        result.setBit(bitNumberToBitIndex(7), key.getBit(bitNumberToBitIndex(9)));

        // Row 2
        result.setBit(bitNumberToBitIndex(8), key.getBit(bitNumberToBitIndex(1)));
        result.setBit(bitNumberToBitIndex(9), key.getBit(bitNumberToBitIndex(58)));
        result.setBit(bitNumberToBitIndex(10), key.getBit(bitNumberToBitIndex(50)));
        result.setBit(bitNumberToBitIndex(11), key.getBit(bitNumberToBitIndex(42)));
        result.setBit(bitNumberToBitIndex(12), key.getBit(bitNumberToBitIndex(34)));
        result.setBit(bitNumberToBitIndex(13), key.getBit(bitNumberToBitIndex(26)));
        result.setBit(bitNumberToBitIndex(14), key.getBit(bitNumberToBitIndex(18)));

        // Row 3
        result.setBit(bitNumberToBitIndex(15), key.getBit(bitNumberToBitIndex(10)));
        result.setBit(bitNumberToBitIndex(16), key.getBit(bitNumberToBitIndex(2)));
        result.setBit(bitNumberToBitIndex(17), key.getBit(bitNumberToBitIndex(59)));
        result.setBit(bitNumberToBitIndex(18), key.getBit(bitNumberToBitIndex(51)));
        result.setBit(bitNumberToBitIndex(19), key.getBit(bitNumberToBitIndex(43)));
        result.setBit(bitNumberToBitIndex(20), key.getBit(bitNumberToBitIndex(35)));
        result.setBit(bitNumberToBitIndex(21), key.getBit(bitNumberToBitIndex(27)));

        // Row 4
        result.setBit(bitNumberToBitIndex(22), key.getBit(bitNumberToBitIndex(19)));
        result.setBit(bitNumberToBitIndex(23), key.getBit(bitNumberToBitIndex(11)));
        result.setBit(bitNumberToBitIndex(24), key.getBit(bitNumberToBitIndex(3)));
        result.setBit(bitNumberToBitIndex(25), key.getBit(bitNumberToBitIndex(60)));
        result.setBit(bitNumberToBitIndex(26), key.getBit(bitNumberToBitIndex(52)));
        result.setBit(bitNumberToBitIndex(27), key.getBit(bitNumberToBitIndex(44)));
        result.setBit(bitNumberToBitIndex(28), key.getBit(bitNumberToBitIndex(36)));

        // Row 5
        result.setBit(bitNumberToBitIndex(29), key.getBit(bitNumberToBitIndex(63)));
        result.setBit(bitNumberToBitIndex(30), key.getBit(bitNumberToBitIndex(55)));
        result.setBit(bitNumberToBitIndex(31), key.getBit(bitNumberToBitIndex(47)));
        result.setBit(bitNumberToBitIndex(32), key.getBit(bitNumberToBitIndex(39)));
        result.setBit(bitNumberToBitIndex(33), key.getBit(bitNumberToBitIndex(31)));
        result.setBit(bitNumberToBitIndex(34), key.getBit(bitNumberToBitIndex(23)));
        result.setBit(bitNumberToBitIndex(35), key.getBit(bitNumberToBitIndex(15)));

        // Row 6
        result.setBit(bitNumberToBitIndex(36), key.getBit(bitNumberToBitIndex(7)));
        result.setBit(bitNumberToBitIndex(37), key.getBit(bitNumberToBitIndex(62)));
        result.setBit(bitNumberToBitIndex(38), key.getBit(bitNumberToBitIndex(54)));
        result.setBit(bitNumberToBitIndex(39), key.getBit(bitNumberToBitIndex(46)));
        result.setBit(bitNumberToBitIndex(40), key.getBit(bitNumberToBitIndex(38)));
        result.setBit(bitNumberToBitIndex(41), key.getBit(bitNumberToBitIndex(30)));
        result.setBit(bitNumberToBitIndex(42), key.getBit(bitNumberToBitIndex(22)));

        // Row 7
        result.setBit(bitNumberToBitIndex(43), key.getBit(bitNumberToBitIndex(14)));
        result.setBit(bitNumberToBitIndex(44), key.getBit(bitNumberToBitIndex(6)));
        result.setBit(bitNumberToBitIndex(45), key.getBit(bitNumberToBitIndex(61)));
        result.setBit(bitNumberToBitIndex(46), key.getBit(bitNumberToBitIndex(53)));
        result.setBit(bitNumberToBitIndex(47), key.getBit(bitNumberToBitIndex(45)));
        result.setBit(bitNumberToBitIndex(48), key.getBit(bitNumberToBitIndex(37)));
        result.setBit(bitNumberToBitIndex(49), key.getBit(bitNumberToBitIndex(29)));

        // Row 8
        result.setBit(bitNumberToBitIndex(50), key.getBit(bitNumberToBitIndex(21)));
        result.setBit(bitNumberToBitIndex(51), key.getBit(bitNumberToBitIndex(13)));
        result.setBit(bitNumberToBitIndex(52), key.getBit(bitNumberToBitIndex(5)));
        result.setBit(bitNumberToBitIndex(53), key.getBit(bitNumberToBitIndex(28)));
        result.setBit(bitNumberToBitIndex(54), key.getBit(bitNumberToBitIndex(20)));
        result.setBit(bitNumberToBitIndex(55), key.getBit(bitNumberToBitIndex(12)));
        result.setBit(bitNumberToBitIndex(56), key.getBit(bitNumberToBitIndex(4)));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(56, result.getBitCount(), "result.getBitCount()");

        return result;
    }

    private static int bitNumberToBitIndex(int bitNumber)
    {
        return bitNumber - 1;
    }
}
