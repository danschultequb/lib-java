package qub;

public class DESTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(DES.class, () ->
        {
            runner.testGroup("initializationVectorToKey(BitArray)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DES des = new DES();
                    test.assertThrows(() -> des.initializationVectorToKey(null));
                });

                runner.test("with 56-bit BitArray", (Test test) ->
                {
                    final DES des = new DES();

                    final BitArray initializationVector = BitArray.fromBitCount(56);
                    initializationVector.setAllBits(1);

                    final BitArray key = des.initializationVectorToKey(initializationVector);
                    test.assertNotNull(key);
                    test.assertEqual(64, key.getBitCount());
                    test.assertEqual("1111111011111110111111101111111011111110111111101111111011111110", key.toString());
                });

                runner.test("with 64-bit BitArray", (Test test) ->
                {
                    final DES des = new DES();

                    final BitArray initializationVector = BitArray.fromBitCount(64);
                    final BitArray key = des.initializationVectorToKey(initializationVector);
                    test.assertNotNull(key);
                    test.assertEqual(64, key.getBitCount());
                    test.assertNotSame(initializationVector, key);
                    test.assertEqual(initializationVector, key);
                });
            });

            runner.testGroup("permutedChoice1(BitArray)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DES des = new DES();
                    test.assertThrows(() -> des.permutedChoice1(null));
                });

                runner.test("with every bit turned off", (Test test) ->
                {
                    final DES des = new DES();

                    final BitArray key = BitArray.fromBitCount(64);

                    final BitArray permutedChoice1 = des.permutedChoice1(key);
                    test.assertNotNull(permutedChoice1);
                    test.assertEqual(56, permutedChoice1.getBitCount());
                    for (int i = 0; i < permutedChoice1.getBitCount(); ++i)
                    {
                        test.assertEqual(0, permutedChoice1.getBit(i), "Expected the bit at index " + i + " to be 0.");
                    }
                });

                runner.test("with every bit turned on", (Test test) ->
                {
                    final DES des = new DES();

                    final BitArray key = BitArray.fromBitCount(64);
                    key.setAllBits(1);

                    final BitArray permutedChoice1 = des.permutedChoice1(key);
                    test.assertNotNull(permutedChoice1);
                    test.assertEqual(56, permutedChoice1.getBitCount());
                    for (int i = 0; i < permutedChoice1.getBitCount(); ++i)
                    {
                        test.assertEqual(1, permutedChoice1.getBit(i), "Expected the bit at index " + i + " to be 1.");
                    }
                });

                runner.test("with every-other bit turned on", (Test test) ->
                {
                    final DES des = new DES();

                    final BitArray key = BitArray.fromBitCount(64);
                    for (int i = 0; i < key.getBitCount(); i += 2)
                    {
                        key.setBit(i, 1);
                    }

                    final BitArray permutedChoice1 = des.permutedChoice1(key);
                    test.assertNotNull(permutedChoice1);
                    test.assertEqual(56, permutedChoice1.getBitCount());
                    test.assertEqual("11111111000000001111111100001111111100000000111111110000", permutedChoice1.toString());
                });

                runner.test("with \"1111000011110000111100001111000011110000111100001111000011110000\"", (Test test) ->
                {
                    final DES des = new DES();

                    final BitArray key = BitArray.fromBitString("1111000011110000111100001111000011110000111100001111000011110000");

                    final BitArray permutedChoice1 = des.permutedChoice1(key);
                    test.assertNotNull(permutedChoice1);
                    test.assertEqual(56, permutedChoice1.getBitCount());
                    test.assertEqual("11111111111111111111111111110000000000000000000000001111", permutedChoice1.toString());
                });
            });
        });
    }
}
