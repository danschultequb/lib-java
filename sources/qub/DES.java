package qub;

/**
 * An implementation of the DES (Data Encryption Standard) algorithm. More details can be found at
 * https://en.wikipedia.org/wiki/Data_Encryption_Standard.
 */
public class DES
{
    public static final int blockSize = 64;
    private static final int iterationCount = 16;

    static final long[] initialPermutationBitNumbers = new long[]
    {
        58, 50, 42, 34, 26, 18, 10,  2,
        60, 52, 44, 36, 28, 20, 12,  4,
        62, 54, 46, 38, 30, 22, 14,  6,
        64, 56, 48, 40, 32, 24, 16,  8,
        57, 49, 41, 33, 25, 17,  9,  1,
        59, 51, 43, 35, 27, 19, 11,  3,
        61, 53, 45, 37, 29, 21, 13,  5,
        63, 55, 47, 39, 31, 23, 15,  7
    };

    static final long[] eBitSelectionBitNumbersTable = new long[]
    {
        32,  1,  2,  3,  4,  5,
         4,  5,  6,  7,  8,  9,
         8,  9, 10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32,  1
    };

    static final long[] initialPermutationInverseBitNumbers = new long[]
    {
        40,  8, 48, 16, 56, 24, 64, 32,
        39,  7, 47, 15, 55, 23, 63, 31,
        38,  6, 46, 14, 54, 22, 62, 30,
        37,  5, 45, 13, 53, 21, 61, 29,
        36,  4, 44, 12, 52, 20, 60, 28,
        35,  3, 43, 11, 51, 19, 59, 27,
        34,  2, 42, 10, 50, 18, 58, 26,
        33,  1, 41,  9, 49, 17, 57, 25
    };

    static final long[] permutedChoice1BitNumbers = new long[]
    {
        57, 49, 41, 33, 25, 17,  9,
         1, 58, 50, 42, 34, 26, 18,
        10,  2, 59, 51, 43, 35, 27,
        19, 11,  3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
         7, 62, 54, 46, 38, 30, 22,
        14,  6, 61, 53, 45, 37, 29,
        21, 13,  5, 28, 20, 12,  4
    };

    static final long[] permutedChoice2BitNumbers = new long[]
    {
        14, 17, 11, 24,  1,  5,
         3, 28, 15,  6, 21, 10,
        23, 19, 12,  4, 26,  8,
        16,  7, 27, 20, 13,  2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
    };

    static int[] iterationShiftCounts = new int[]
    {
        1, 1, 2, 2,
        2, 2, 2, 2,
        1, 2, 2, 2,
        2, 2, 2, 1
    };

    static int[][] sFunctions = new int[][]
    {
        // S1
        new int[]
        {
            14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7,
             0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8,
             4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0,
            15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13
        },
        // S2
        new int[]
        {
            15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10,
             3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5,
             0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15,
            13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9
        },
        // S3
        new int[]
        {
            10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8,
            13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1,
            13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7,
             1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12
        },
        // S4
        new int[]
        {
             7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15,
            13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9,
            10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4,
             3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14
        },
        // S5
        new int[]
        {
             2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9,
            14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6,
             4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14,
            11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3
        },
        // S6
        new int[]
        {
            12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11,
            10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8,
             9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6,
             4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13
        },
        // S7
        new int[]
        {
             4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1,
            13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6,
             1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2,
             6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12
        },
        // S8
        new int[]
        {
            13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7,
             1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2,
             7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8,
             2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11
        }
    };

    static long[] pFunction = new long[]
    {
        16,  7, 20, 21,
        29, 12, 28, 17,
         1, 15, 23, 26,
         5, 18, 31, 10,
         2,  8, 24, 14,
        32, 27,  3,  9,
        19, 13, 30,  6,
        22, 11,  4, 25
    };

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
        PreCondition.assertEqual(blockSize, initializationVector.getCount(), "initializationVector.getCount()");
        PreCondition.assertNotNull(plaintext, "plaintext");
        PreCondition.assertEqual(blockSize, plaintext.getCount(), "plaintext.getCount()");

        final BitArray[] kArray = getKArray(initializationVector);

        final BitArray[] lrArray = new BitArray[iterationCount + 1];
        lrArray[0] = plaintext.permuteByBitNumber(initialPermutationBitNumbers);

        for (int i = 1; i <= iterationCount; ++i)
        {
            final BitArray iterationK = kArray[i].permuteByBitNumber(permutedChoice2BitNumbers);
            final BitArray lrPrevious = lrArray[i - 1];
            lrArray[i] = getLRNextAfterIteration(lrPrevious, iterationK);
        }

        final BitArray result = getFinalResult(lrArray[iterationCount]);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(blockSize, result.getCount(), "result.getCount()");

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
        PreCondition.assertEqual(blockSize, initializationVector.getCount(), "initializationVector.getCount()");
        PreCondition.assertNotNull(ciphertext, "ciphertext");
        PreCondition.assertEqual(blockSize, ciphertext.getCount(), "ciphertext.getCount()");

        final BitArray[] kArray = getKArray(initializationVector);

        final BitArray[] lrArray = new BitArray[iterationCount + 1];
        lrArray[0] = ciphertext.permuteByBitNumber(initialPermutationBitNumbers);

        for (int i = 1; i <= iterationCount; ++i)
        {
            final BitArray iterationK = kArray[kArray.length - i].permuteByBitNumber(permutedChoice2BitNumbers);
            final BitArray lrPrevious = lrArray[i - 1];
            lrArray[i] = getLRNextAfterIteration(lrPrevious, iterationK);
        }

        final BitArray result = getFinalResult(lrArray[iterationCount]);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(64, result.getCount(), "result.getCount()");

        return result;
    }

    BitArray[] getKArray(BitArray initializationVector)
    {
        PreCondition.assertNotNull(initializationVector, "initializationVector");
        PreCondition.assertEqual(64, initializationVector.getCount(), "initializationVector.getCount()");

        final BitArray[] result = new BitArray[iterationCount + 1];
        result[0] = initializationVector.permuteByBitNumber(permutedChoice1BitNumbers);
        final long kSize = result[0].getCount();

        for (int i = 1; i <= iterationCount; ++i)
        {
            final int bitsToRotate = iterationShiftCounts[i - 1];
            result[i] = result[i - 1].clone();
            result[i].rotateLeft(bitsToRotate, 0, kSize / 2);
            result[i].rotateLeft(bitsToRotate, kSize / 2, kSize / 2);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(iterationCount + 1, result.length, "result.length");

        return result;
    }

    BitArray getIterationK(BitArray cAndD, int iterationNumber)
    {
        PreCondition.assertNotNull(cAndD, "cAndD");
        PreCondition.assertEqual(56, cAndD.getCount(), "cAndD.getCount()");
        PreCondition.assertBetween(1, iterationNumber, 16, "iterationNumber");

        final int iterationShiftCount = iterationShiftCounts[iterationNumber];
        cAndD.rotateLeft(iterationShiftCount, 0, 28);
        cAndD.rotateLeft(iterationShiftCount, 28, 28);
        final BitArray result = cAndD.permuteByBitNumber(permutedChoice2BitNumbers);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(48, result.getCount(), "result.getCount()");

        return result;
    }

    BitArray getPreOutput(BitArray lr)
    {
        PreCondition.assertNotNull(lr, "lr");
        PreCondition.assertEqual(blockSize, lr.getCount(), "lr.getCount()");

        // Swap L and R.
        final BitArray result = BitArray.create(blockSize);
        result.copyFrom(lr, blockSize / 2, 0, blockSize / 2);
        result.copyFrom(lr, 0, blockSize / 2, blockSize / 2);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(blockSize, result.getCount(), "result.getCount()");

        return result;
    }

    BitArray getLRNextAfterIteration(BitArray lrPrevious, BitArray iterationK)
    {
        PreCondition.assertNotNull(lrPrevious, "lrPrevious");
        PreCondition.assertEqual(blockSize, lrPrevious.getCount(), "lrPrevious.getCount()");
        PreCondition.assertNotNull(iterationK, "iterationK");
        PreCondition.assertEqual(48, iterationK.getCount(), "iterationK.getCount()");

        final long lrSize = lrPrevious.getCount();

        final BitArray lPrevious = BitArray.create(lrSize / 2);
        lPrevious.copyFrom(lrPrevious, 0, 0, lrSize / 2);

        final BitArray rPrevious = BitArray.create(lrSize / 2);
        rPrevious.copyFrom(lrPrevious, lrSize / 2, 0, lrSize / 2);

        // Rn copied to Ln-1
        final BitArray lNext = rPrevious.clone();

        // Rn through E
        final BitArray rAfterE = rPrevious.permuteByBitNumber(eBitSelectionBitNumbersTable);

        // Before S functions
        final BitArray sInput = rAfterE.xor(iterationK);

        // S functions
        final BitArray sOutput = BitArray.create(lrSize / 2);
        for (int sIndex = 0; sIndex < 8; ++sIndex)
        {
            final BitArray b = BitArray.create(6);
            b.copyFrom(sInput, sIndex * 6, 0, 6);

            final int row = (b.get(0) << 1) | b.get(5);
            final int column = b.toInteger(1, 4);
            final int sValueIndex = (row * 16) + column;
            final int sValue = sFunctions[sIndex][sValueIndex];

            sOutput.copyFrom(sValue, sIndex * 4, 4);
        }
        final BitArray sOutputAfterP = sOutput.permuteByBitNumber(pFunction);

        // Ln xor f(Rn, Kn)
        final BitArray rNext = lPrevious.xor(sOutputAfterP);

        final BitArray result = BitArray.create(lrSize);
        result.copyFrom(lNext, 0, 0, lrSize / 2);
        result.copyFrom(rNext, 0, lrSize / 2, lrSize / 2);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(blockSize, result.getCount(), "result.getCount()");

        return result;
    }

    public BitArray getFinalResult(BitArray lr)
    {
        PreCondition.assertNotNull(lr, "lr");
        PreCondition.assertEqual(blockSize, lr.getCount(), "lr.getCount()");

        final BitArray preoutput = getPreOutput(lr);
        final BitArray result = preoutput.permuteByBitNumber(initialPermutationInverseBitNumbers);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(blockSize, result.getCount(), "result.getCount()");

        return result;
    }
}
