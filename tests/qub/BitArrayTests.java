package qub;

public interface BitArrayTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BitArray.class, () ->
        {
            runner.testGroup("clone()", () ->
            {
                runner.test("with " + Strings.escapeAndQuote(""), (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("");
                    final BitArray clone = bits.clone();
                    test.assertNotSame(bits, clone);
                    test.assertEqual(bits, clone);
                });

                runner.test("with " + Strings.escapeAndQuote("1"), (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    final BitArray clone = bits.clone();
                    test.assertNotSame(bits, clone);
                    test.assertEqual(bits, clone);

                    bits.set(0, 0);
                    test.assertEqual("0", bits.toBitString());
                    test.assertEqual("1", clone.toBitString());
                });
            });

            runner.testGroup("create(long)", () ->
            {
                runner.test("with -1 bitCount", (Test test) ->
                {
                    test.assertThrows(
                        () -> BitArray.create(-1),
                        new PreConditionFailure("bitCount (-1) must be between 0 and 68719476704."));
                });

                runner.test("with 0 bitCount", (Test test) ->
                {
                    final BitArray bits = BitArray.create(0);
                    test.assertNotNull(bits);
                    test.assertEqual(0, bits.getCount());
                });

                runner.test("with 1 bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.create(1);
                    test.assertNotNull(block);
                    test.assertEqual(1, block.getCount());
                    test.assertEqual(1, block.getBitChunkCount());
                    test.assertEqual(0, block.get(0));
                    for (long i = 0; i < block.getCount(); ++i)
                    {
                        test.assertEqual(0, block.getBit(i));
                    }
                });

                runner.test("with 7 bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.create(7);
                    test.assertNotNull(block);
                    test.assertEqual(7, block.getCount());
                    test.assertEqual(1, block.getBitChunkCount());
                    for (long i = 0; i < block.getCount(); ++i)
                    {
                        test.assertEqual(0, block.getBit(i));
                    }
                });

                runner.test("with 8 bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.create(8);
                    test.assertNotNull(block);
                    test.assertEqual(8, block.getCount());
                    test.assertEqual(1, block.getBitChunkCount());
                    for (long i = 0; i < block.getCount(); ++i)
                    {
                        test.assertEqual(0, block.getBit(i));
                    }
                });

                runner.test("with 9 bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.create(9);
                    test.assertNotNull(block);
                    test.assertEqual(9, block.getCount());
                    test.assertEqual(1, block.getBitChunkCount());
                    for (long i = 0; i < block.getCount(); ++i)
                    {
                        test.assertEqual(0, block.getBit(i));
                    }
                });

                runner.test("with 100 bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.create(100);
                    test.assertNotNull(block);
                    test.assertEqual(100, block.getCount());
                    test.assertEqual(4, block.getBitChunkCount());
                    for (long i = 0; i < block.getCount(); ++i)
                    {
                        test.assertEqual(0, block.getBit(i));
                    }
                });

                runner.test("with 68719476705 bitCount", (Test test) ->
                {
                    test.assertThrows(
                        () -> BitArray.create(68719476705L),
                        new PreConditionFailure("bitCount (68719476705) must be between 0 and 68719476704."));
                });

                runner.test("with 68719476706 bitCount", (Test test) ->
                {
                    test.assertThrows(
                        () -> BitArray.create(68719476706L),
                        new PreConditionFailure("bitCount (68719476706) must be between 0 and 68719476704."));
                });
            });

            runner.testGroup("get(int)", () ->
            {
                runner.test("with negative bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertThrows(
                        () -> block.get(-1),
                        new PreConditionFailure("index (-1) must be between 0 and 9."));
                });

                runner.test("with 0 bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertEqual(0, block.get(0));
                });

                runner.test("with 1 bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertEqual(0, block.get(0));
                });

                runner.test("with bitIndex assertEqual to bitCount - 1", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertEqual(0, block.get(9));
                });

                runner.test("with bitIndex assertEqual to bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertThrows(
                        () -> block.get(10),
                        new PreConditionFailure("index (10) must be between 0 and 9."));
                });

                runner.test("with bitIndex assertEqual to bitCount + 1", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertThrows(
                        () -> block.get(11),
                        new PreConditionFailure("index (11) must be between 0 and 9."));
                });
            });

            runner.testGroup("set(int,Integer)", () ->
            {
                runner.test("with negative bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertThrows(
                        () -> block.set(-1, Integer.valueOf(1)),
                        new PreConditionFailure("index (-1) must be between 0 and 9."));
                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertEqual(0, block.get(i), "Expected bit at index " + i + " to be 0.");
                    }
                });

                runner.test("with 0 bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    block.set(0, Integer.valueOf(1));
                    for (int i = 0; i < 10; ++i)
                    {
                        final int expected = i == 0 ? 1 : 0;
                        test.assertEqual(expected, block.get(i), "Expected bit at index " + i + " to be " + expected + ".");
                    }
                });

                runner.test("with 1 bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    block.set(1, Integer.valueOf(1));
                    for (int i = 0; i < 10; ++i)
                    {
                        final int expected = i == 1 ? 1 : 0;
                        test.assertEqual(expected, block.get(i), "Expected bit at index " + i + " to be " + expected + ".");
                    }
                });

                runner.test("with bitIndex equal to bitCount - 1", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    block.set(9, Integer.valueOf(1));
                    for (int i = 0; i < 10; ++i)
                    {
                        final int expected = i == 9 ? 1 : 0;
                        test.assertEqual(expected, block.get(i), "Expected bit at index " + i + " to be " + expected + ".");
                    }
                });

                runner.test("with bitIndex equal to bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertThrows(
                        () -> block.set(10, Integer.valueOf(1)),
                        new PreConditionFailure("index (10) must be between 0 and 9."));
                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertEqual(0, block.get(i), "Expected bit at index " + i + " to be 0.");
                    }
                });

                runner.test("with bitIndex equal to bitCount + 1", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertThrows(
                        () -> block.set(11, Integer.valueOf(1)),
                        new PreConditionFailure("index (11) must be between 0 and 9."));
                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertEqual(0, block.get(i), "Expected bit at index " + i + " to be 0.");
                    }
                });
            });

            runner.testGroup("set(int,int)", () ->
            {
                runner.test("with negative bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertThrows(
                        () -> block.set(-1, 1),
                        new PreConditionFailure("index (-1) must be between 0 and 9."));
                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertEqual(0, block.get(i), "Expected bit at index " + i + " to be 0.");
                    }
                });

                runner.test("with 0 bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    block.set(0, 1);
                    for (int i = 0; i < 10; ++i)
                    {
                        final int expected = i == 0 ? 1 : 0;
                        test.assertEqual(expected, block.get(i), "Expected bit at index " + i + " to be " + expected + ".");
                    }
                });

                runner.test("with 1 bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    block.set(1, 1);
                    for (int i = 0; i < 10; ++i)
                    {
                        final int expected = i == 1 ? 1 : 0;
                        test.assertEqual(expected, block.get(i), "Expected bit at index " + i + " to be " + expected + ".");
                    }
                });

                runner.test("with bitIndex equal to bitCount - 1", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    block.set(9, 1);
                    for (int i = 0; i < 10; ++i)
                    {
                        final int expected = i == 9 ? 1 : 0;
                        test.assertEqual(expected, block.get(i), "Expected bit at index " + i + " to be " + expected + ".");
                    }
                });

                runner.test("with bitIndex equal to bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertThrows(
                        () -> block.set(10, 1),
                        new PreConditionFailure("index (10) must be between 0 and 9."));
                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertEqual(0, block.get(i), "Expected bit at index " + i + " to be 0.");
                    }
                });

                runner.test("with bitIndex equal to bitCount + 1", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertThrows(
                        () -> block.set(11, 1),
                        new PreConditionFailure("index (11) must be between 0 and 9."));
                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertEqual(0, block.get(i), "Expected bit at index " + i + " to be 0.");
                    }
                });
            });

            runner.testGroup("setBit(long,int)", () ->
            {
                runner.test("with negative bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertThrows(
                        () -> block.setBit(-1, 1),
                        new PreConditionFailure("index (-1) must be between 0 and 9."));
                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertEqual(0, block.get(i), "Expected bit at index " + i + " to be 0.");
                    }
                });

                runner.test("with 0 bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    block.setBit(0, 1);
                    for (int i = 0; i < 10; ++i)
                    {
                        final int expected = i == 0 ? 1 : 0;
                        test.assertEqual(expected, block.get(i), "Expected bit at index " + i + " to be " + expected + ".");
                    }
                });

                runner.test("with 1 bitIndex", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    block.setBit(1, 1);
                    for (int i = 0; i < 10; ++i)
                    {
                        final int expected = i == 1 ? 1 : 0;
                        test.assertEqual(expected, block.get(i), "Expected bit at index " + i + " to be " + expected + ".");
                    }
                });

                runner.test("with bitIndex equal to bitCount - 1", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    block.setBit(9, 1);
                    for (int i = 0; i < 10; ++i)
                    {
                        final int expected = i == 9 ? 1 : 0;
                        test.assertEqual(expected, block.get(i), "Expected bit at index " + i + " to be " + expected + ".");
                    }
                });

                runner.test("with bitIndex equal to bitCount", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertThrows(
                        () -> block.setBit(10, 1),
                        new PreConditionFailure("index (10) must be between 0 and 9."));
                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertEqual(0, block.get(i), "Expected bit at index " + i + " to be 0.");
                    }
                });

                runner.test("with bitIndex equal to bitCount + 1", (Test test) ->
                {
                    final BitArray block = BitArray.create(10);
                    test.assertThrows(
                        () -> block.setBit(11, 1),
                        new PreConditionFailure("index (11) must be between 0 and 9."));
                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertEqual(0, block.get(i), "Expected bit at index " + i + " to be 0.");
                    }
                });
            });

            runner.testGroup("setAllBits(int)", () ->
            {
                runner.test("with -1", (Test test) ->
                {
                    final BitArray block = BitArray.create(3);
                    test.assertThrows(
                        () -> block.setAllBits(-1),
                        new PreConditionFailure("value (-1) must be 0 or 1."));
                    test.assertEqual("000", block.toString());
                });

                runner.test("with 0", (Test test) ->
                {
                    final BitArray block = BitArray.create(3);
                    block.set(1, 1);
                    block.setAllBits(0);
                    test.assertEqual("000", block.toString());
                });

                runner.test("with 1", (Test test) ->
                {
                    final BitArray block = BitArray.create(3);
                    block.setAllBits(1);
                    test.assertEqual("111", block.toString());
                });
            });

            runner.testGroup("setLastBitsFromLong(long)", () ->
            {
                runner.test("with less than 64 bits", (Test test) ->
                {
                    final BitArray bits = BitArray.create(63);
                    test.assertThrows(() -> bits.setLastBitsFromLong(1),
                        new PreConditionFailure("getCount() (63) must be greater than or equal to 64."));
                    test.assertEqual(BitArray.create(63), bits);
                });

                runner.test("with 64-bit BitArray and 1", (Test test) ->
                {
                    final BitArray bits = BitArray.create(64);
                    bits.setLastBitsFromLong(1);
                    test.assertEqual(BitArray.createFromHexString("0000000000000001"), bits);
                });

                runner.test("with 72-bit BitArray and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.create(72);
                    bits.setLastBitsFromLong(2);
                    test.assertEqual(BitArray.createFromHexString("000000000000000002"), bits);
                });

                runner.test("with 80-bit BitArray and 16", (Test test) ->
                {
                    final BitArray bits = BitArray.create(80);
                    bits.setLastBitsFromLong(16);
                    test.assertEqual(BitArray.createFromHexString("00000000000000000010"), bits);
                });
            });

            runner.testGroup("rotateLeft()", () ->
            {
                runner.test("with \"1\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.rotateLeft();
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"101101\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateLeft();
                    test.assertEqual("011011", bits.toBitString());
                });
            });

            runner.testGroup("rotateLeft(int)", () ->
            {
                runner.test("with \"1\" and -2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.rotateLeft(-2);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and -1", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.rotateLeft(-1);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and 0", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.rotateLeft(0);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and 1", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.rotateLeft(1);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.rotateLeft(2);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"101101\" and -2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateLeft(-2);
                    test.assertEqual("011011", bits.toBitString());
                });

                runner.test("with \"101101\" and -1", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateLeft(-1);
                    test.assertEqual("110110", bits.toBitString());
                });

                runner.test("with \"101101\" and 0", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateLeft(0);
                    test.assertEqual("101101", bits.toBitString());
                });

                runner.test("with \"101101\" and 1", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateLeft(1);
                    test.assertEqual("011011", bits.toBitString());
                });

                runner.test("with \"101101\" and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateLeft(2);
                    test.assertEqual("110110", bits.toBitString());
                });

                runner.test("with \"101101\" and 57", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateLeft(57);
                    test.assertEqual("101101", bits.toBitString());
                });
            });

            runner.testGroup("rotateRight()", () ->
            {
                runner.test("with \"1\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.rotateRight();
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"101101\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateRight();
                    test.assertEqual("110110", bits.toBitString());
                });
            });

            runner.testGroup("rotateRight(int)", () ->
            {
                runner.test("with \"1\" and -2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.rotateRight(-2);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and -1", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.rotateRight(-1);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and 0", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.rotateRight(0);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and 1", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.rotateRight(1);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"1\" and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.rotateRight(2);
                    test.assertEqual("1", bits.toBitString());
                });

                runner.test("with \"101101\" and -2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateRight(-2);
                    test.assertEqual("110110", bits.toBitString());
                });

                runner.test("with \"101101\" and -1", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateRight(-1);
                    test.assertEqual("011011", bits.toBitString());
                });

                runner.test("with \"101101\" and 0", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateRight(0);
                    test.assertEqual("101101", bits.toBitString());
                });

                runner.test("with \"101101\" and 1", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateRight(1);
                    test.assertEqual("110110", bits.toBitString());
                });

                runner.test("with \"101101\" and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateRight(2);
                    test.assertEqual("011011", bits.toBitString());
                });

                runner.test("with \"101101\" and 57", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101101");
                    bits.rotateRight(57);
                    test.assertEqual("101101", bits.toBitString());
                });
            });

            runner.testGroup("shiftLeft()", () ->
            {
                runner.test("with 0", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("0");
                    bits.shiftLeft();
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 1", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.shiftLeft();
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 101100111000", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101100111000");
                    bits.shiftLeft();
                    test.assertEqual("011001110000", bits.toBitString());
                });
            });

            runner.testGroup("shiftLeft(long)", () ->
            {
                runner.test("with 0 and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("0");
                    bits.shiftLeft(2);
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 1 and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.shiftLeft(2);
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 101100111000", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101100111000");
                    bits.shiftLeft(2);
                    test.assertEqual("110011100000", bits.toBitString());
                });
            });

            runner.testGroup("shiftLeft(long,long,long)", () ->
            {
                runner.test("with 101100111000, 2, 3, and 4", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101100111000");
                    bits.shiftLeft(2, 3, 4);
                    test.assertEqual("101010011000", bits.toBitString());
                });
            });

            runner.testGroup("shiftRight()", () ->
            {
                runner.test("with 0", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("0");
                    bits.shiftRight();
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 1", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.shiftRight();
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 101100111000", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101100111000");
                    bits.shiftRight();
                    test.assertEqual("010110011100", bits.toBitString());
                });
            });

            runner.testGroup("shiftRight(long)", () ->
            {
                runner.test("with 0 and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("0");
                    bits.shiftRight(2);
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 1 and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    bits.shiftRight(2);
                    test.assertEqual("0", bits.toBitString());
                });

                runner.test("with 101100111000 and 2", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101100111000");
                    bits.shiftRight(2);
                    test.assertEqual("001011001110", bits.toBitString());
                });
            });

            runner.testGroup("shiftRight(long,long,long)", () ->
            {
                runner.test("with 101100111000, 2, 3, and 4", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101100111000");
                    bits.shiftRight(2, 3, 4);
                    test.assertEqual("101001011000", bits.toBitString());
                });
            });

            runner.testGroup("xor(BitArray)", () ->
            {
                runner.test("with \"11010\" and null", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("11010");
                    test.assertThrows(() -> bits.xor(null),
                        new PreConditionFailure("rhs cannot be null."));
                });

                runner.test("with \"11010\" and \"0101\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("11010");
                    final BitArray rhs = BitArray.createFromBitString("0101");
                    test.assertThrows(() -> bits.xor(rhs),
                        new PreConditionFailure("rhs.getCount() (4) must be 5."));
                });

                runner.test("with \"11010\" and \"01010\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("11010");
                    final BitArray rhs =  BitArray.createFromBitString("01010");
                    test.assertEqual(BitArray.createFromBitString("10000"), bits.xor(rhs));
                    test.assertEqual(BitArray.createFromBitString("10000"), rhs.xor(bits));
                });
            });

            runner.testGroup("permuteByBitIndex(long[])", () ->
            {
                runner.test("with bit indexes than reference indexes greater than bits in the BitArray", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("");
                    test.assertThrows(() -> bits.permuteByBitIndex(new long[] { 0 }),
                        new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                });

                runner.test("with empty bit indexes and empty BitArray", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("");
                    final BitArray permutedBits = bits.permuteByBitIndex(new long[0]);
                    test.assertEqual("", bits.toBitString());
                    test.assertEqual("", permutedBits.toBitString());
                });

                runner.test("with equal number of bit indexes and bits in the BitArray", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("010011");
                    final BitArray permutedBits = bits.permuteByBitIndex(new long[] { 1, 4, 5, 0, 2, 3 });
                    test.assertEqual("010011", bits.toBitString());
                    test.assertEqual("111000", permutedBits.toBitString());
                });

                runner.test("with fewer number of bit indexes than bits in the BitArray", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("010011");
                    final BitArray permutedBits = bits.permuteByBitIndex(new long[] { 5, 0 });
                    test.assertEqual("010011", bits.toBitString());
                    test.assertEqual("10", permutedBits.toBitString());
                });

                runner.test("with greater number of bit indexes than bits in the BitArray", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("010");
                    final BitArray permutedBits = bits.permuteByBitIndex(new long[] { 1, 2, 1, 2, 1 });
                    test.assertEqual("010", bits.toBitString());
                    test.assertEqual("10101", permutedBits.toBitString());
                });
            });

            runner.testGroup("permuteByBitNumber(long[])", () ->
            {
                runner.test("with bit numbers than reference negative indexes in the BitArray", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("");
                    test.assertThrows(() -> bits.permuteByBitNumber(new long[] { 0 }),
                        new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                });

                runner.test("with bit numbers that reference indexes greater than bits in the BitArray", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("");
                    test.assertThrows(() -> bits.permuteByBitNumber(new long[] { 1 }),
                        new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                });

                runner.test("with empty bit numbers and empty BitArray", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("");
                    final BitArray permuteByBitNumberResult = bits.permuteByBitNumber(new long[0]);
                    test.assertNotSame(bits, permuteByBitNumberResult);
                    test.assertEqual("", permuteByBitNumberResult.toBitString());
                });

                runner.test("with equal number of bit numbers and bits in the BitArray", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("010011");
                    final BitArray permutedBits = bits.permuteByBitNumber(new long[] { 2, 5, 6, 1, 3, 4 });
                    test.assertEqual("010011", bits.toBitString());
                    test.assertEqual("111000", permutedBits.toBitString());
                });

                runner.test("with fewer number of bit numbers than bits in the BitArray", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("010011");
                    final BitArray permutedBits = bits.permuteByBitNumber(new long[] { 6, 1 });
                    test.assertEqual("010011", bits.toBitString());
                    test.assertEqual("10", permutedBits.toBitString());
                });

                runner.test("with greater number of bit numbers than bits in the BitArray", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("010");
                    final BitArray permutedBits = bits.permuteByBitNumber(new long[] { 2, 3, 2, 3, 2 });
                    test.assertEqual("010", bits.toBitString());
                    test.assertEqual("10101", permutedBits.toBitString());
                });
            });

            runner.testGroup("concatenate(BitArray)", () ->
            {
                runner.test("with 101 and null", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101");
                    final BitArray rhs = null;
                    test.assertThrows(() -> bits.concatenate(rhs),
                        new PreConditionFailure("rhs cannot be null."));
                });

                runner.test("with 101 and empty", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101");
                    final BitArray rhs = BitArray.createFromBitString("");
                    final BitArray result = bits.concatenate(rhs);
                    test.assertEqual(bits, result);
                    test.assertNotSame(bits, result);
                });

                runner.test("with 101 and 01", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("101");
                    final BitArray rhs = BitArray.createFromBitString("01");
                    final BitArray result = bits.concatenate(rhs);
                    test.assertEqual(BitArray.createFromBitString("10101"), result);
                });

                runner.test("with empty and 10", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("");
                    final BitArray rhs = BitArray.createFromBitString("10");
                    final BitArray result = bits.concatenate(rhs);
                    test.assertEqual(BitArray.createFromBitString("10"), result);
                });
            });

            runner.testGroup("iterate()", () ->
            {
                final Action2<String,int[]> iterateTest = (String bitString, int[] expectedBits) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(bitString), (Test test) ->
                    {
                        final BitArray bits = BitArray.createFromBitString(bitString);

                        final Iterator<Integer> iterator = bits.iterate();
                        test.assertNotNull(iterator);
                        test.assertFalse(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                        test.assertThrows(iterator::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));

                        for (int expectedBit : expectedBits)
                        {
                            test.assertTrue(iterator.next());
                            test.assertTrue(iterator.hasStarted());
                            test.assertTrue(iterator.hasCurrent());
                            test.assertEqual(expectedBit, iterator.getCurrent());
                        }

                        for (int i = 0; i < 2; ++i)
                        {
                            test.assertFalse(iterator.next());
                            test.assertTrue(iterator.hasStarted());
                            test.assertFalse(iterator.hasCurrent());
                            test.assertThrows(iterator::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
                        }
                    });
                };

                iterateTest.run("", new int[0]);
                iterateTest.run("0", new int[] { 0 });
                iterateTest.run("1", new int[] { 1 });
                iterateTest.run("010011", new int[] { 0, 1, 0, 0, 1, 1 });
            });

            runner.testGroup("iterateBlocks()", () ->
            {
                final Action3<String,Integer,String[]> iterateBlocksTest = (String bitString, Integer blockSize, String[] expectedBlocks) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(bitString) + " and " + blockSize + "-bit blocks", (Test test) ->
                    {
                        final BitArray bits = BitArray.createFromBitString(bitString);

                        final Iterator<BitArray> blocks = bits.iterateBlocks(blockSize);
                        test.assertNotNull(blocks);
                        test.assertFalse(blocks.hasStarted());
                        test.assertFalse(blocks.hasCurrent());

                        for (final String expectedBlock : expectedBlocks)
                        {
                            test.assertTrue(blocks.next());
                            test.assertTrue(blocks.hasStarted());
                            test.assertTrue(blocks.hasCurrent());
                            test.assertNotNull(blocks.getCurrent());
                            test.assertEqual(expectedBlock, blocks.getCurrent().toBitString());
                        }

                        for (int i = 0; i < 2; ++i)
                        {
                            test.assertFalse(blocks.next());
                            test.assertTrue(blocks.hasStarted());
                            test.assertFalse(blocks.hasCurrent());
                            test.assertNull(blocks.getCurrent());
                        }
                    });
                };

                iterateBlocksTest.run("", 1, new String[0]);
                iterateBlocksTest.run("", 2, new String[0]);
                iterateBlocksTest.run("11010", 1, new String[] { "1", "1", "0", "1", "0" });
                iterateBlocksTest.run("000111", 3, new String[] { "000", "111" });
            });

            runner.testGroup("iterateIntegers()", () ->
            {
                final Action2<String,int[]> iterateIntegersTest = (String hexString, int[] expectedIntegers) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(hexString), (Test test) ->
                    {
                        final BitArray bits = BitArray.createFromHexString(hexString);

                        final Iterator<Integer> integers = bits.iterateIntegers();
                        test.assertNotNull(integers);
                        test.assertFalse(integers.hasStarted());
                        test.assertFalse(integers.hasCurrent());
                        test.assertThrows(integers::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));

                        for (final int expectedInteger : expectedIntegers)
                        {
                            test.assertTrue(integers.next());
                            test.assertTrue(integers.hasStarted());
                            test.assertTrue(integers.hasCurrent());
                            test.assertNotNull(integers.getCurrent());
                            test.assertEqual(expectedInteger, integers.getCurrent());
                        }

                        for (int i = 0; i < 2; ++i)
                        {
                            test.assertFalse(integers.next());
                            test.assertTrue(integers.hasStarted());
                            test.assertFalse(integers.hasCurrent());
                            test.assertThrows(integers::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
                        }
                    });
                };

                iterateIntegersTest.run("", new int[0]);
                iterateIntegersTest.run("00000000", new int[] { 0 });
                iterateIntegersTest.run("FFFFFFFF", new int[] { -1 });
                iterateIntegersTest.run("00000001", new int[] { 1 });
                iterateIntegersTest.run("00000000000000010000000200000003", new int[] { 0, 1, 2, 3 });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with no block on", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    test.assertEqual("00000", block.toString());
                });

                runner.test("with first bit on", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    block.set(0, 1);
                    test.assertEqual("10000", block.toString());
                });

                runner.test("with last bit on", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    block.set(4, 1);
                    test.assertEqual("00001", block.toString());
                });

                runner.test("with all block on", (Test test) ->
                {
                    final BitArray block = BitArray.create(6);
                    block.setAllBits(1);
                    test.assertEqual("111111", block.toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    test.assertFalse(block.equals((Object)null));
                });

                runner.test("with String", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    test.assertFalse(block.equals((Object)"00000"));
                });

                runner.test("with same", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    test.assertTrue(block.equals((Object)block));
                });

                runner.test("with equal", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    test.assertTrue(block.equals((Object)BitArray.create(5)));
                });

                runner.test("with different bit count but same block", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    final BitArray otherBits = BitArray.create(6);
                    test.assertFalse(block.equals((Object)otherBits));
                });

                runner.test("with equal bit count but different block", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    final BitArray otherBits = BitArray.create(5);
                    otherBits.set(0, 1);
                    test.assertFalse(block.equals((Object)otherBits));
                });
            });

            runner.testGroup("equals(BitArray)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    test.assertFalse(block.equals((BitArray)null));
                });

                runner.test("with same", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    test.assertTrue(block.equals(block));
                });

                runner.test("with equal", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    test.assertTrue(block.equals(BitArray.create(5)));
                });

                runner.test("with different bit count but same block", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    final BitArray otherBits = BitArray.create(6);
                    test.assertFalse(block.equals(otherBits));
                });

                runner.test("with equal bit count but different block", (Test test) ->
                {
                    final BitArray block = BitArray.create(5);
                    final BitArray otherBits = BitArray.create(5);
                    otherBits.set(0, 1);
                    test.assertFalse(block.equals(otherBits));
                });
            });

            runner.testGroup("fromBitString(String)", () ->
            {
                runner.test("with \"0\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("0");
                    test.assertEqual("0", bits.toString());
                });

                runner.test("with \"1\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1");
                    test.assertEqual("1", bits.toString());
                });

                runner.test("with \"1100\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1100");
                    test.assertEqual("1100", bits.toString());
                });
            });

            runner.testGroup("createFromHexString(String)", () ->
            {
                runner.test("with \"0\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromHexString("0");
                    test.assertEqual("0000", bits.toBitString());
                });

                runner.test("with \"1\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromHexString("1");
                    test.assertEqual("0001", bits.toBitString());
                });

                runner.test("with \"8\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromHexString("8");
                    test.assertEqual("1000", bits.toBitString());
                });

                runner.test("with \"a\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromHexString("a");
                    test.assertEqual("1010", bits.toBitString());
                });

                runner.test("with \"A\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromHexString("A");
                    test.assertEqual("1010", bits.toBitString());
                });

                runner.test("with \"f\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromHexString("f");
                    test.assertEqual("1111", bits.toBitString());
                });

                runner.test("with \"F\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromHexString("F");
                    test.assertEqual("1111", bits.toBitString());
                });

                runner.test("with \"10\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromHexString("10");
                    test.assertEqual("00010000", bits.toBitString());
                });

                runner.test("with \"0123456789ABCDEF\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromHexString("0123456789ABCDEF");
                    test.assertEqual("0000000100100011010001010110011110001001101010111100110111101111", bits.toBitString());
                });

                runner.test("with \"133457799BBCDFF1\"", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromHexString("133457799BBCDFF1");
                    test.assertEqual("0001001100110100010101110111100110011011101111001101111111110001", bits.toBitString());
                });
            });

            runner.testGroup("createFromBytes(byte[])", () ->
            {
                runner.test("with []", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBytes(new byte[0]);
                    test.assertEqual("", bits.toBitString());
                });

                runner.test("with [0]", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBytes(new byte[] { 0 });
                    test.assertEqual("00", bits.toHexString());
                });

                runner.test("with [-1]", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBytes(new byte[] { -1 });
                    test.assertEqual("FF", bits.toHexString());
                });

                runner.test("with [-1, 0]", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBytes(new byte[] { -1, 0 });
                    test.assertEqual("FF00", bits.toHexString());
                });

                runner.test("with [-1, 0, -2, 1]", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBytes(new byte[] { -1, 0, -2, 1 });
                    test.assertEqual("FF00FE01", bits.toHexString());
                });

                runner.test("with [-1, 0, -1, 0, -2, 1, -2, 1, -3]", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBytes(new byte[] { -1, 0, -1, 0, -2, 1, -2, 1, -3 });
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
                        test.assertEqual(0, BitArray.createFromBitString(Strings.repeat("0", bitCount)).toInteger());
                    });
                }

                runner.test("with " + Strings.escapeAndQuote(Strings.repeat("0", 33)), (Test test) ->
                {
                    test.assertThrows(() -> BitArray.createFromBitString(Strings.repeat("0", 33)).toInteger(),
                        new PreConditionFailure("length (33) must be between 1 and 32."));
                });

                runner.test("with \"1\"", (Test test) ->
                {
                    test.assertEqual(1, BitArray.createFromBitString("1").toInteger());
                });

                runner.test("with \"11\"", (Test test) ->
                {
                    test.assertEqual(3, BitArray.createFromBitString("11").toInteger());
                });

                runner.test("with \"111\"", (Test test) ->
                {
                    test.assertEqual(7, BitArray.createFromBitString("111").toInteger());
                });

                runner.test("with \"1111\"", (Test test) ->
                {
                    test.assertEqual(15, BitArray.createFromBitString("1111").toInteger());
                });

                runner.test("with \"10101\"", (Test test) ->
                {
                    test.assertEqual(21, BitArray.createFromBitString("10101").toInteger());
                });
            });

            runner.testGroup("toInteger(int,int)", () ->
            {
                runner.test("with 1000, 0, and 1", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("1000");
                    test.assertEqual(1, bits.toInteger(0, 1));
                });

                runner.test("with 110010, 1, and 4", (Test test) ->
                {
                    final BitArray bits = BitArray.createFromBitString("110010");
                    test.assertEqual(9, bits.toInteger(1, 4));
                });
            });

            runner.testGroup("toByteArray()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final BitArray array = BitArray.create(0);
                    test.assertEqual(new byte[0], array.toByteArray());
                });

                runner.test("with " + Strings.quote("1"), (Test test) ->
                {
                    final BitArray array = BitArray.createFromBitString("1");
                    test.assertEqual(new byte[] { -128 }, array.toByteArray());
                });

                runner.test("with " + Strings.quote("11"), (Test test) ->
                {
                    final BitArray array = BitArray.createFromBitString("11");
                    test.assertEqual(new byte[] { -64 }, array.toByteArray());
                });

                runner.test("with " + Strings.quote("1100011"), (Test test) ->
                {
                    final BitArray array = BitArray.createFromBitString("1100011");
                    test.assertEqual(new byte[] { -58 }, array.toByteArray());
                });

                runner.test("with " + Strings.quote("11000011"), (Test test) ->
                {
                    final BitArray array = BitArray.createFromBitString("11000011");
                    test.assertEqual(new byte[] { -61 }, array.toByteArray());
                });

                runner.test("with " + Strings.quote("110000110"), (Test test) ->
                {
                    final BitArray array = BitArray.createFromBitString("110000110");
                    test.assertEqual(new byte[] { -61, 0 }, array.toByteArray());
                });
            });

            runner.testGroup("toHexString()", () ->
            {
                runner.test("with 0", (Test test) ->
                {
                    test.assertEqual("0", BitArray.createFromBitString("0").toHexString());
                });

                runner.test("with 1", (Test test) ->
                {
                    test.assertEqual("8", BitArray.createFromBitString("1").toHexString());
                });

                runner.test("with 00", (Test test) ->
                {
                    test.assertEqual("0", BitArray.createFromBitString("00").toHexString());
                });

                runner.test("with 10", (Test test) ->
                {
                    test.assertEqual("8", BitArray.createFromBitString("10").toHexString());
                });

                runner.test("with 11", (Test test) ->
                {
                    test.assertEqual("C", BitArray.createFromBitString("11").toHexString());
                });

                runner.test("with 110", (Test test) ->
                {
                    test.assertEqual("C", BitArray.createFromBitString("11").toHexString());
                });

                runner.test("with 1100", (Test test) ->
                {
                    test.assertEqual("C", BitArray.createFromBitString("1100").toHexString());
                });

                runner.test("with 11000", (Test test) ->
                {
                    test.assertEqual("C0", BitArray.createFromBitString("11000").toHexString());
                });

                runner.test("with 8787878787878787", (Test test) ->
                {
                    test.assertEqual("8787878787878787", BitArray.createFromHexString("8787878787878787").toHexString());
                });
            });
        });
    }
}
