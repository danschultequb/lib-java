package qub;

public interface BitsTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Bits.class, () ->
        {
            runner.test("minimum", (Test test) ->
            {
                test.assertEqual(0, Bits.minimum);
            });

            runner.test("maximum", (Test test) ->
            {
                test.assertEqual(1, Bits.maximum);
            });

            runner.test("maximumIntegerBitArrayCount", (Test test) ->
            {
                test.assertEqual(68719476704L, Bits.maximumIntegerBitArrayCount);
            });

            runner.test("maximumLongBitArrayCount", (Test test) ->
            {
                test.assertEqual(137438953408L, Bits.maximumLongBitArrayCount);
            });

            runner.test("allowedValues", (Test test) ->
            {
                test.assertEqual(Iterable.create(0, 1), Bits.allowedValues);
            });

            runner.testGroup("getBit(byte,int)", () ->
            {
                final Action3<Byte,Integer,Throwable> getBitErrorTest = (Byte value, Integer bitIndex, Throwable expected) ->
                {
                    runner.test("with " + English.andList(value + " (" + Bytes.toHexString(value, true) + ")", bitIndex), (Test test) ->
                    {
                        test.assertThrows(() -> Bits.getBit(value, bitIndex), expected);
                    });
                };

                getBitErrorTest.run((byte)55, -1, new PreConditionFailure("bitIndex (-1) must be between 0 and 7."));
                getBitErrorTest.run((byte)55, 8, new PreConditionFailure("bitIndex (8) must be between 0 and 7."));

                final Action3<Byte,Integer,Integer> getBitTest = (Byte value, Integer bitIndex, Integer expected) ->
                {
                    runner.test("with " + English.andList(value + " (" + Bytes.toHexString(value, true) + ")", bitIndex), (Test test) ->
                    {
                        test.assertEqual(expected, Bits.getBit(value, bitIndex));
                    });
                };

                for (int i = 0; i < Bytes.bitCount; ++i)
                {
                    getBitTest.run((byte)0x00, i, 0);
                }

                for (int i = 0; i < Bytes.bitCount; ++i)
                {
                    getBitTest.run((byte)0x01, i, (i == 7 ? 1 : 0));
                }

                for (int i = 0; i < Bytes.bitCount; ++i)
                {
                    getBitTest.run((byte)0x02, i, (i == 6 ? 1 : 0));
                }

                for (int i = 0; i < Bytes.bitCount; ++i)
                {
                    getBitTest.run((byte)0x80, i, (i == 0 ? 1 : 0));
                }

                for (int i = 0; i < Bytes.bitCount; ++i)
                {
                    getBitTest.run((byte)0xFF, i, 1);
                }
            });
        });
    }
}
