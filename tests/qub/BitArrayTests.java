package qub;

public class BitArrayTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(BitArray.class, () ->
        {
            runner.testGroup("fromBitCount(long)", () ->
            {
                runner.test("with -1 bitCount", (Test test) ->
                {
                    test.assertThrows(
                        () -> BitArray.fromBitCount(-1),
                        new PreConditionFailure("bitCount (-1) must be between 1 and 68719476704."));
                });

                runner.test("with 0 bitCount", (Test test) ->
                {
                    test.assertThrows(
                        () -> BitArray.fromBitCount(0),
                        new PreConditionFailure("bitCount (0) must be between 1 and 68719476704."));
                });

                runner.test("with 1 bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(1);
                    test.assertNotNull(block);
                    test.assertEqual(1, block.getBitCount());
                    test.assertEqual(1, block.getChunkCount());
                    test.assertEqual(0, block.getBit(0));
                    for (long i = 0; i < block.getBitCount(); ++i)
                    {
                        test.assertEqual(0, block.getBit(i));
                    }
                });

                runner.test("with 7 bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(7);
                    test.assertNotNull(block);
                    test.assertEqual(7, block.getBitCount());
                    test.assertEqual(1, block.getChunkCount());
                    for (long i = 0; i < block.getBitCount(); ++i)
                    {
                        test.assertEqual(0, block.getBit(i));
                    }
                });

                runner.test("with 8 bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(8);
                    test.assertNotNull(block);
                    test.assertEqual(8, block.getBitCount());
                    test.assertEqual(1, block.getChunkCount());
                    for (long i = 0; i < block.getBitCount(); ++i)
                    {
                        test.assertEqual(0, block.getBit(i));
                    }
                });

                runner.test("with 9 bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(9);
                    test.assertNotNull(block);
                    test.assertEqual(9, block.getBitCount());
                    test.assertEqual(1, block.getChunkCount());
                    for (long i = 0; i < block.getBitCount(); ++i)
                    {
                        test.assertEqual(0, block.getBit(i));
                    }
                });

                runner.test("with 100 bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(100);
                    test.assertNotNull(block);
                    test.assertEqual(100, block.getBitCount());
                    test.assertEqual(4, block.getChunkCount());
                    for (long i = 0; i < block.getBitCount(); ++i)
                    {
                        test.assertEqual(0, block.getBit(i));
                    }
                });

                runner.test("with 68719476705 bitCount", (Test test) ->
                {
                    test.assertThrows(
                        () -> BitArray.fromBitCount(68719476705L),
                        new PreConditionFailure("bitCount (68719476705) must be between 1 and 68719476704."));
                });

                runner.test("with 68719476706 bitCount", (Test test) ->
                {
                    test.assertThrows(
                        () -> BitArray.fromBitCount(68719476706L),
                        new PreConditionFailure("bitCount (68719476706) must be between 1 and 68719476704."));
                });
            });

            runner.testGroup("getBit(long)", () ->
            {
                runner.test("with negative bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(10);
                    test.assertThrows(
                        () -> block.getBit(-1),
                        new PreConditionFailure("bitIndex (-1) must be between 0 and 9."));
                });

                runner.test("with 0 bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(10);
                    test.assertEqual(0, block.getBit(0));
                });

                runner.test("with 1 bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(10);
                    test.assertEqual(0, block.getBit(0));
                });

                runner.test("with bitIndex assertEqual to bitCount - 1", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(10);
                    test.assertEqual(0, block.getBit(9));
                });

                runner.test("with bitIndex assertEqual to bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(10);
                    test.assertThrows(
                        () -> block.getBit(10),
                        new PreConditionFailure("bitIndex (10) must be between 0 and 9."));
                });

                runner.test("with bitIndex assertEqual to bitCount + 1", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(10);
                    test.assertThrows(
                        () -> block.getBit(11),
                        new PreConditionFailure("bitIndex (11) must be between 0 and 9."));
                });
            });

            runner.testGroup("setBit(long,int)", () ->
            {
                runner.test("with negative bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(10);
                    test.assertThrows(
                        () -> block.setBit(-1, 1),
                        new PreConditionFailure("bitIndex (-1) must be between 0 and 9."));
                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertEqual(0, block.getBit(i), "Expected bit at index " + i + " to be 0.");
                    }
                });

                runner.test("with 0 bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(10);
                    block.setBit(0, 1);
                    for (int i = 0; i < 10; ++i)
                    {
                        final int expected = i == 0 ? 1 : 0;
                        test.assertEqual(expected, block.getBit(i), "Expected bit at index " + i + " to be " + expected + ".");
                    }
                });

                runner.test("with 1 bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(10);
                    block.setBit(1, 1);
                    for (int i = 0; i < 10; ++i)
                    {
                        final int expected = i == 1 ? 1 : 0;
                        test.assertEqual(expected, block.getBit(i), "Expected bit at index " + i + " to be " + expected + ".");
                    }
                });

                runner.test("with bitIndex equal to bitCount - 1", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(10);
                    block.setBit(9, 1);
                    for (int i = 0; i < 10; ++i)
                    {
                        final int expected = i == 9 ? 1 : 0;
                        test.assertEqual(expected, block.getBit(i), "Expected bit at index " + i + " to be " + expected + ".");
                    }
                });

                runner.test("with bitIndex equal to bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(10);
                    test.assertThrows(
                        () -> block.setBit(10, 1),
                        new PreConditionFailure("bitIndex (10) must be between 0 and 9."));
                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertEqual(0, block.getBit(i), "Expected bit at index " + i + " to be 0.");
                    }
                });

                runner.test("with bitIndex equal to bitCount + 1", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(10);
                    test.assertThrows(
                        () -> block.setBit(11, 1),
                        new PreConditionFailure("bitIndex (11) must be between 0 and 9."));
                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertEqual(0, block.getBit(i), "Expected bit at index " + i + " to be 0.");
                    }
                });
            });

            runner.testGroup("setAllBits(int)", () ->
            {
                runner.test("with -1", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(3);
                    test.assertThrows(
                        () -> block.setAllBits(-1),
                        new PreConditionFailure("value (-1) must be either 0 or 1."));
                    test.assertEqual("000", block.toString());
                });

                runner.test("with 0", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(3);
                    block.setBit(1, 1);
                    block.setAllBits(0);
                    test.assertEqual("000", block.toString());
                });

                runner.test("with 1", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(3);
                    block.setAllBits(1);
                    test.assertEqual("111", block.toString());
                });
            });

            runner.testGroup("rotateLeft()", () ->
            {
                runner.test("with \"1\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.rotateLeft();
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"101101\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateLeft();
                    test.assertEqual("011011", bits.toBitString());
                });
            });

            runner.testGroup("rotateLeft(int)", () ->
            {
                runner.test("with \"1\" and -2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.rotateLeft(-2);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and -1", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.rotateLeft(-1);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and 0", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.rotateLeft(0);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and 1", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.rotateLeft(1);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.rotateLeft(2);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"101101\" and -2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateLeft(-2);
                    test.assertEqual("011011", bits.toBitString());
                });

                runner.test("with \"101101\" and -1", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateLeft(-1);
                    test.assertEqual("110110", bits.toBitString());
                });

                runner.test("with \"101101\" and 0", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateLeft(0);
                    test.assertEqual("101101", bits.toBitString());
                });

                runner.test("with \"101101\" and 1", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateLeft(1);
                    test.assertEqual("011011", bits.toBitString());
                });

                runner.test("with \"101101\" and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateLeft(2);
                    test.assertEqual("110110", bits.toBitString());
                });

                runner.test("with \"101101\" and 57", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateLeft(57);
                    test.assertEqual("101101", bits.toBitString());
                });
            });

            runner.testGroup("rotateRight()", () ->
            {
                runner.test("with \"1\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.rotateRight();
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"101101\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateRight();
                    test.assertEqual("110110", bits.toBitString());
                });
            });

            runner.testGroup("rotateRight(int)", () ->
            {
                runner.test("with \"1\" and -2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.rotateRight(-2);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and -1", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.rotateRight(-1);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and 0", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.rotateRight(0);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and 1", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.rotateRight(1);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.rotateRight(2);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"101101\" and -2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateRight(-2);
                    test.assertEqual("110110", bits.toBitString());
                });

                runner.test("with \"101101\" and -1", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateRight(-1);
                    test.assertEqual("011011", bits.toBitString());
                });

                runner.test("with \"101101\" and 0", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateRight(0);
                    test.assertEqual("101101", bits.toBitString());
                });

                runner.test("with \"101101\" and 1", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateRight(1);
                    test.assertEqual("110110", bits.toBitString());
                });

                runner.test("with \"101101\" and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateRight(2);
                    test.assertEqual("011011", bits.toBitString());
                });

                runner.test("with \"101101\" and 57", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101101");
                    bits.rotateRight(57);
                    test.assertEqual("101101", bits.toBitString());
                });
            });

            runner.testGroup("shiftLeft()", () ->
            {
                runner.test("with 0", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("0");
                    bits.shiftLeft();
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 1", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.shiftLeft();
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 101100111000", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101100111000");
                    bits.shiftLeft();
                    test.assertEqual("011001110000", bits.toBitString());
                });
            });

            runner.testGroup("shiftLeft(long)", () ->
            {
                runner.test("with 0 and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("0");
                    bits.shiftLeft(2);
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 1 and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.shiftLeft(2);
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 101100111000", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101100111000");
                    bits.shiftLeft(2);
                    test.assertEqual("110011100000", bits.toBitString());
                });
            });

            runner.testGroup("shiftLeft(long,long,long)", () ->
            {
                runner.test("with 101100111000, 2, 3, and 4", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101100111000");
                    bits.shiftLeft(2, 3, 4);
                    test.assertEqual("101010011000", bits.toBitString());
                });
            });

            runner.testGroup("shiftRight()", () ->
            {
                runner.test("with 0", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("0");
                    bits.shiftRight();
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 1", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.shiftRight();
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 101100111000", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101100111000");
                    bits.shiftRight();
                    test.assertEqual("010110011100", bits.toBitString());
                });
            });

            runner.testGroup("shiftRight(long)", () ->
            {
                runner.test("with 0 and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("0");
                    bits.shiftRight(2);
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 1 and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    bits.shiftRight(2);
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 101100111000 and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101100111000");
                    bits.shiftRight(2);
                    test.assertEqual("001011001110", bits.toBitString());
                });
            });

            runner.testGroup("shiftRight(long,long,long)", () ->
            {
                runner.test("with 101100111000, 2, 3, and 4", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("101100111000");
                    bits.shiftRight(2, 3, 4);
                    test.assertEqual("101001011000", bits.toBitString());
                });
            });

            runner.testGroup("xor(BitArray)", () ->
            {
                runner.test("with \"11010\" and null", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("11010");
                    test.assertThrows(() -> bits.xor(null));
                });

                runner.test("with \"11010\" and \"0101\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("11010");
                    final BitArray rhs = BitArray.fromBitString("0101");
                    test.assertThrows(() -> bits.xor(rhs));
                });

                runner.test("with \"11010\" and \"01010\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("11010");
                    final BitArray rhs =  BitArray.fromBitString("01010");
                    test.assertEqual(BitArray.fromBitString("10000"), bits.xor(rhs));
                    test.assertEqual(BitArray.fromBitString("10000"), rhs.xor(bits));
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with no block on", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    test.assertEqual("00000", block.toString());
                });

                runner.test("with first bit on", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    block.setBit(0, 1);
                    test.assertEqual("10000", block.toString());
                });

                runner.test("with last bit on", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    block.setBit(4, 1);
                    test.assertEqual("00001", block.toString());
                });

                runner.test("with all block on", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(6);
                    block.setAllBits(1);
                    test.assertEqual("111111", block.toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    test.assertFalse(block.equals((Object)null));
                });

                runner.test("with String", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    test.assertFalse(block.equals((Object)"00000"));
                });

                runner.test("with same", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    test.assertTrue(block.equals((Object)block));
                });

                runner.test("with equal", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    test.assertTrue(block.equals((Object)BitArray.fromBitCount(5)));
                });

                runner.test("with different bit count but same block", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    final BitArray otherBits = BitArray.fromBitCount(6);
                    test.assertFalse(block.equals((Object)otherBits));
                });

                runner.test("with equal bit count but different block", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    final BitArray otherBits = BitArray.fromBitCount(5);
                    otherBits.setBit(0, 1);
                    test.assertFalse(block.equals((Object)otherBits));
                });
            });

            runner.testGroup("equals(BitArray)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    test.assertFalse(block.equals((BitArray)null));
                });

                runner.test("with same", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    test.assertTrue(block.equals(block));
                });

                runner.test("with equal", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    test.assertTrue(block.equals(BitArray.fromBitCount(5)));
                });

                runner.test("with different bit count but same block", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    final BitArray otherBits = BitArray.fromBitCount(6);
                    test.assertFalse(block.equals(otherBits));
                });

                runner.test("with equal bit count but different block", (Test test) ->
                {
                    final BitArray block = BitArray.fromBitCount(5);
                    final BitArray otherBits = BitArray.fromBitCount(5);
                    otherBits.setBit(0, 1);
                    test.assertFalse(block.equals(otherBits));
                });
            });

            runner.testGroup("fromBitString(String)", () ->
            {
                runner.test("with \"0\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("0");
                    test.assertEqual("0", bits.toString());
                });

                runner.test("with \"1\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1");
                    test.assertEqual("1", bits.toString());
                });

                runner.test("with \"1100\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1100");
                    test.assertEqual("1100", bits.toString());
                });
            });

            runner.testGroup("fromHexString(String)", () ->
            {
                runner.test("with \"0\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromHexString("0");
                    test.assertEqual("0000", bits.toBitString());
                });

                runner.test("with \"1\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromHexString("1");
                    test.assertEqual("0001", bits.toBitString());
                });

                runner.test("with \"8\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromHexString("8");
                    test.assertEqual("1000", bits.toBitString());
                });

                runner.test("with \"a\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromHexString("a");
                    test.assertEqual("1010", bits.toBitString());
                });

                runner.test("with \"A\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromHexString("A");
                    test.assertEqual("1010", bits.toBitString());
                });

                runner.test("with \"f\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromHexString("f");
                    test.assertEqual("1111", bits.toBitString());
                });

                runner.test("with \"F\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromHexString("F");
                    test.assertEqual("1111", bits.toBitString());
                });

                runner.test("with \"10\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromHexString("10");
                    test.assertEqual("00010000", bits.toBitString());
                });

                runner.test("with \"0123456789ABCDEF\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromHexString("0123456789ABCDEF");
                    test.assertEqual("0000000100100011010001010110011110001001101010111100110111101111", bits.toBitString());
                });

                runner.test("with \"133457799BBCDFF1\"", (Test test) ->
                {
                    final BitArray bits = BitArray.fromHexString("133457799BBCDFF1");
                    test.assertEqual("0001001100110100010101110111100110011011101111001101111111110001", bits.toBitString());
                });
            });

            runner.testGroup("fromByteArray(byte[])", () ->
            {
                runner.test("with []", (Test test) ->
                {
                    final BitArray bits = BitArray.fromByteArray(new byte[0]);
                    test.assertEqual("", bits.toBitString());
                });

                runner.test("with [0]", (Test test) ->
                {
                    final BitArray bits = BitArray.fromByteArray(new byte[] { 0 });
                    test.assertEqual("00", bits.toHexString());
                });

                runner.test("with [-1]", (Test test) ->
                {
                    final BitArray bits = BitArray.fromByteArray(new byte[] { -1 });
                    test.assertEqual("FF", bits.toHexString());
                });

                runner.test("with [-1, 0]", (Test test) ->
                {
                    final BitArray bits = BitArray.fromByteArray(new byte[] { -1, 0 });
                    test.assertEqual("FF00", bits.toHexString());
                });

                runner.test("with [-1, 0, -2, 1]", (Test test) ->
                {
                    final BitArray bits = BitArray.fromByteArray(new byte[] { -1, 0, -2, 1 });
                    test.assertEqual("FF00FE01", bits.toHexString());
                });

                runner.test("with [-1, 0, -1, 0, -2, 1, -2, 1, -3]", (Test test) ->
                {
                    final BitArray bits = BitArray.fromByteArray(new byte[] { -1, 0, -1, 0, -2, 1, -2, 1, -3 });
                    test.assertEqual("FF00FF00FE01FE01FD", bits.toHexString());
                });
            });

            runner.testGroup("toInteger()", () ->
            {
                for (int i = 1; i <= 32; ++i)
                {
                    final int bitCount = i;
                    runner.test("with " + Strings.escapeAndQuote(Strings.repeat("0", bitCount)), (Test test) ->
                    {
                        test.assertEqual(0, BitArray.fromBitString(Strings.repeat("0", bitCount)).toInteger());
                    });
                }

                runner.test("with " + Strings.escapeAndQuote(Strings.repeat("0", 33)), (Test test) ->
                {
                    test.assertThrows(() -> BitArray.fromBitString(Strings.repeat("0", 33)).toInteger());
                });

                runner.test("with \"1\"", (Test test) ->
                {
                    test.assertEqual(1, BitArray.fromBitString("1").toInteger());
                });

                runner.test("with \"11\"", (Test test) ->
                {
                    test.assertEqual(3, BitArray.fromBitString("11").toInteger());
                });

                runner.test("with \"111\"", (Test test) ->
                {
                    test.assertEqual(7, BitArray.fromBitString("111").toInteger());
                });

                runner.test("with \"1111\"", (Test test) ->
                {
                    test.assertEqual(15, BitArray.fromBitString("1111").toInteger());
                });

                runner.test("with \"10101\"", (Test test) ->
                {
                    test.assertEqual(21, BitArray.fromBitString("10101").toInteger());
                });
            });

            runner.testGroup("toInteger(int,int)", () ->
            {
                runner.test("with 1000, 0, and 1", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("1000");
                    test.assertEqual(1, bits.toInteger(0, 1));
                });

                runner.test("with 110010, 1, and 4", (Test test) ->
                {
                    final BitArray bits = BitArray.fromBitString("110010");
                    test.assertEqual(9, bits.toInteger(1, 4));
                });
            });

            runner.testGroup("toHexString()", () ->
            {
                runner.test("with 0", (Test test) ->
                {
                    test.assertEqual("0", BitArray.fromBitString("0").toHexString());
                });

                runner.test("with 1", (Test test) ->
                {
                    test.assertEqual("8", BitArray.fromBitString("1").toHexString());
                });

                runner.test("with 00", (Test test) ->
                {
                    test.assertEqual("0", BitArray.fromBitString("00").toHexString());
                });

                runner.test("with 10", (Test test) ->
                {
                    test.assertEqual("8", BitArray.fromBitString("10").toHexString());
                });

                runner.test("with 11", (Test test) ->
                {
                    test.assertEqual("C", BitArray.fromBitString("11").toHexString());
                });

                runner.test("with 110", (Test test) ->
                {
                    test.assertEqual("C", BitArray.fromBitString("11").toHexString());
                });

                runner.test("with 1100", (Test test) ->
                {
                    test.assertEqual("C", BitArray.fromBitString("1100").toHexString());
                });

                runner.test("with 11000", (Test test) ->
                {
                    test.assertEqual("C0", BitArray.fromBitString("11000").toHexString());
                });

                runner.test("with 8787878787878787", (Test test) ->
                {
                    test.assertEqual("8787878787878787", BitArray.fromHexString("8787878787878787").toHexString());
                });
            });
        });
    }
}
