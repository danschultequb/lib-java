package qub;

public class DESTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(DES.class, () ->
        {
            runner.test("initialPermutationBitNumbers", (Test test) ->
            {
                test.assertEqual(
                    new long[]
                    {
                        58, 50, 42, 34, 26, 18, 10,  2,
                        60, 52, 44, 36, 28, 20, 12,  4,
                        62, 54, 46, 38, 30, 22, 14,  6,
                        64, 56, 48, 40, 32, 24, 16,  8,
                        57, 49, 41, 33, 25, 17,  9,  1,
                        59, 51, 43, 35, 27, 19, 11,  3,
                        61, 53, 45, 37, 29, 21, 13,  5,
                        63, 55, 47, 39, 31, 23, 15,  7
                    },
                    DES.initialPermutationBitNumbers);
            });

            runner.test("initialPermutationInverseBitNumbers", (Test test) ->
            {
                test.assertEqual(
                    new long[]
                    {
                        40,  8, 48, 16, 56, 24, 64, 32,
                        39,  7, 47, 15, 55, 23, 63, 31,
                        38,  6, 46, 14, 54, 22, 62, 30,
                        37,  5, 45, 13, 53, 21, 61, 29,
                        36,  4, 44, 12, 52, 20, 60, 28,
                        35,  3, 43, 11, 51, 19, 59, 27,
                        34,  2, 42, 10, 50, 18, 58, 26,
                        33,  1, 41,  9, 49, 17, 57, 25
                    },
                    DES.initialPermutationInverseBitNumbers);
            });

            runner.test("eBitSelectionBitNumbersTable", (Test test) ->
            {
                test.assertEqual(
                    new long[]
                    {
                        32,  1,  2,  3,  4,  5,
                         4,  5,  6,  7,  8,  9,
                         8,  9, 10, 11, 12, 13,
                        12, 13, 14, 15, 16, 17,
                        16, 17, 18, 19, 20, 21,
                        20, 21, 22, 23, 24, 25,
                        24, 25, 26, 27, 28, 29,
                        28, 29, 30, 31, 32,  1
                    },
                    DES.eBitSelectionBitNumbersTable);
            });

            runner.test("s1", (Test test) ->
            {
                test.assertEqual(
                    new int[]
                    {
                        14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7,
                         0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8,
                         4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0,
                        15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13
                    },
                    DES.sFunctions[0]);
            });

            runner.test("s2", (Test test) ->
            {
                test.assertEqual(
                    new int[]
                    {
                        15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10,
                         3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5,
                         0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15,
                        13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9
                    },
                    DES.sFunctions[1]);
            });

            runner.test("s3", (Test test) ->
            {
                test.assertEqual(
                    new int[]
                    {
                        10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8,
                        13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1,
                        13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7,
                         1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12
                    },
                    DES.sFunctions[2]);
            });

            runner.test("s4", (Test test) ->
            {
                test.assertEqual(
                    new int[]
                    {
                         7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15,
                        13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9,
                        10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4,
                         3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14
                    },
                    DES.sFunctions[3]);
            });

            runner.testGroup("encrypt(BitArray,BitArray)", () ->
            {
                final Action3<String,String,String> encryptTest = (String message, String initializationVector, String expectedCiphertext) ->
                {
                    runner.test("with message " + Strings.quote(message) + " and initialization vector " + Strings.quote(initializationVector), (Test test) ->
                    {
                        final DES des = new DES();

                        final BitArray plaintextBits = BitArray.createFromHexString(message);
                        final BitArray initializationVectorBits = BitArray.createFromHexString(initializationVector);

                        final BitArray ciphertextBits = des.encrypt(initializationVectorBits, plaintextBits);
                        test.assertNotNull(ciphertextBits);
                        test.assertEqual(expectedCiphertext, ciphertextBits.toHexString());
                    });
                };

                encryptTest.run("8787878787878787", "0E329232EA6D0D73", "0000000000000000");
                encryptTest.run("0123456789ABCDEF", "133457799BBCDFF1", "85E813540F0AB405");
                encryptTest.run("596F7572206C6970", "0E329232EA6D0D73", "C0999FDDE378D7ED");
                encryptTest.run("732061726520736D", "0E329232EA6D0D73", "727DA00BCA5A84EE");
                encryptTest.run("6F6F746865722074", "0E329232EA6D0D73", "47F269A4D6438190");
                encryptTest.run("68616E2076617365", "0E329232EA6D0D73", "D9D52F78F5358499");
                encryptTest.run("6C696E650D0A0000", "0E329232EA6D0D73", "828AC9B453E0E653");
            });

            runner.testGroup("decrypt(BitArray,BitArray)", () ->
            {
                final Action3<String,String,String> decryptTest = (String ciphertext, String initializationVector, String expectedPlaintext) ->
                {
                    runner.test("with ciphertext " + Strings.quote(ciphertext) + " and initialization vector " + Strings.quote(initializationVector), (Test test) ->
                    {
                        final DES des = new DES();

                        final BitArray ciphertextBits = BitArray.createFromHexString(ciphertext);
                        final BitArray initializationVectorBits = BitArray.createFromHexString(initializationVector);

                        final BitArray plaintextBits = des.decrypt(initializationVectorBits, ciphertextBits);
                        test.assertNotNull(plaintextBits);
                        test.assertEqual(expectedPlaintext, plaintextBits.toHexString());
                    });
                };

                decryptTest.run("0000000000000000", "0E329232EA6D0D73", "8787878787878787");
                decryptTest.run("85E813540F0AB405", "133457799BBCDFF1", "0123456789ABCDEF");
                decryptTest.run("C0999FDDE378D7ED", "0E329232EA6D0D73", "596F7572206C6970");
                decryptTest.run("727DA00BCA5A84EE", "0E329232EA6D0D73", "732061726520736D");
                decryptTest.run("47F269A4D6438190", "0E329232EA6D0D73", "6F6F746865722074");
                decryptTest.run("D9D52F78F5358499", "0E329232EA6D0D73", "68616E2076617365");
                decryptTest.run("828AC9B453E0E653", "0E329232EA6D0D73", "6C696E650D0A0000");
            });
        });
    }
}
