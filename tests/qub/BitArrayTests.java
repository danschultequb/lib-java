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
        });
    }
}
