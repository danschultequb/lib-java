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
    public BitBlock encrypt(BitBlock initializationVector, BitBlock plaintext)
    {
        return null;
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
    public BitBlock decrypt(BitBlock initializationVector, BitBlock ciphertext)
    {
        return null;
    }
}
