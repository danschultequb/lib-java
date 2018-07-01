package qub;

/**
 * An encryption cipher that uses the DES cipher three times. See
 * https://en.wikipedia.org/wiki/Triple_DES for more information.
 */
public class TripleDES
{
    private static final DES des = new DES();

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
        PreCondition.assertOneOf(initializationVector.getBitCount(), new long[] { DES.blockSize * 2, DES.blockSize * 3 }, "initializationVector.getBitCount()");
        PreCondition.assertNotNull(plaintext, "plaintext");
        PreCondition.assertEqual(DES.blockSize, plaintext.getBitCount(), "plaintext.getBitCount()");

        final BitArray k1 = BitArray.fromBitCount(DES.blockSize);
        k1.copyFrom(initializationVector, 0, 0, DES.blockSize);

        final BitArray k2 = BitArray.fromBitCount(DES.blockSize);
        k2.copyFrom(initializationVector, DES.blockSize, 0, DES.blockSize);

        BitArray k3;
        if (initializationVector.getBitCount() == DES.blockSize * 2)
        {
            k3 = k1;
        }
        else
        {
            k3 = BitArray.fromBitCount(DES.blockSize);
            k3.copyFrom(initializationVector, DES.blockSize * 2, 0, DES.blockSize);
        }

        final BitArray result = des.encrypt(k3, des.decrypt(k2, des.encrypt(k1, plaintext)));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(DES.blockSize, result.getBitCount(), "result.getBitCount()");

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
        PreCondition.assertOneOf(initializationVector.getBitCount(), new long[] { DES.blockSize * 2, DES.blockSize * 3 }, "initializationVector.getBitCount()");
        PreCondition.assertNotNull(ciphertext, "ciphertext");
        PreCondition.assertEqual(DES.blockSize, ciphertext.getBitCount(), "ciphertext.getBitCount()");

        final BitArray k1 = BitArray.fromBitCount(DES.blockSize);
        k1.copyFrom(initializationVector, 0, 0, DES.blockSize);

        final BitArray k2 = BitArray.fromBitCount(DES.blockSize);
        k2.copyFrom(initializationVector, DES.blockSize, 0, DES.blockSize);

        BitArray k3;
        if (initializationVector.getBitCount() == DES.blockSize * 2)
        {
            k3 = k1;
        }
        else
        {
            k3 = BitArray.fromBitCount(DES.blockSize);
            k3.copyFrom(initializationVector, DES.blockSize * 2, 0, DES.blockSize);
        }

        final BitArray result = des.decrypt(k1, des.encrypt(k2, des.decrypt(k3, ciphertext)));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(DES.blockSize, result.getBitCount(), "result.getBitCount()");

        return result;
    }
}
