package qub;

public class BytesTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Bytes.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                test.assertNotNull(new Bytes());
            });

            runner.testGroup("toUnsignedInt(byte)", () ->
            {
                final Action2<Integer,Integer> toUnsignedIntTest = (Integer byteValue, Integer expectedUnsignedInteger) ->
                {
                    runner.test("with " + byteValue, (Test test) ->
                    {
                        test.assertEqual(expectedUnsignedInteger, Bytes.toUnsignedInt(byteValue.byteValue()));
                    });
                };

                toUnsignedIntTest.run(0, 0);
                toUnsignedIntTest.run(1, 1);
                toUnsignedIntTest.run(127, 127);
                toUnsignedIntTest.run(128, 128);
                toUnsignedIntTest.run(129, 129);
                toUnsignedIntTest.run(254, 254);
                toUnsignedIntTest.run(255, 255);
                toUnsignedIntTest.run(256, 0);
                toUnsignedIntTest.run(-1, 255);
                toUnsignedIntTest.run(-2, 254);
                toUnsignedIntTest.run(-126, 130);
                toUnsignedIntTest.run(-127, 129);
                toUnsignedIntTest.run(-128, 128);
                toUnsignedIntTest.run(-129, 127);
            });

            runner.testGroup("getSignificantBitCount(byte)", () ->
            {
                final Action2<Byte,Integer> getSignificantBitCountTest = (Byte b, Integer expected) ->
                {
                    runner.test("with " + b, (Test test) ->
                    {
                        test.assertEqual(expected, Bytes.getSignificantBitCount(b));
                    });
                };

                for (int i = 0; i < 128; ++i)
                {
                    getSignificantBitCountTest.run((byte)i, 0);
                }

                for (int i = 128; i < 192; ++i)
                {
                    getSignificantBitCountTest.run((byte)i, 1);
                }

                for (int i = 192; i < 224; ++i)
                {
                    getSignificantBitCountTest.run((byte)i, 2);
                }

                for (int i = 224; i < 240; ++i)
                {
                    getSignificantBitCountTest.run((byte)i, 3);
                }

                for (int i = 240; i < 248; ++i)
                {
                    getSignificantBitCountTest.run((byte)i, 4);
                }

                for (int i = 248; i < 252; ++i)
                {
                    getSignificantBitCountTest.run((byte)i, 5);
                }

                for (int i = 252; i < 254; ++i)
                {
                    getSignificantBitCountTest.run((byte)i, 6);
                }

                getSignificantBitCountTest.run((byte)254, 7);
                getSignificantBitCountTest.run((byte)255, 8);
            });

            runner.testGroup("toHexChar(int)", () ->
            {
                final Action2<Integer,Character> toHexCharTest = (Integer value, Character expected) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertEqual(expected, Bytes.toHexChar(value));
                    });
                };

                toHexCharTest.run(-2, UTF8CharacterEncoding.replacementCharacter);
                toHexCharTest.run(-1, UTF8CharacterEncoding.replacementCharacter);

                for (int i = 0; i <= 9; ++i)
                {
                    toHexCharTest.run(i, (char)('0' + i));
                }

                for (int i = 10; i <= 15; ++i)
                {
                    toHexCharTest.run(i, (char)('A' + i - 10));
                }

                toHexCharTest.run(16, UTF8CharacterEncoding.replacementCharacter);
                toHexCharTest.run(17, UTF8CharacterEncoding.replacementCharacter);
            });
        });
    }
}
