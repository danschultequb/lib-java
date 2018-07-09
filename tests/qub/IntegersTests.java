package qub;

public class IntegersTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Integers.class, () ->
        {
            runner.testGroup("rotateLeft(int)", () ->
            {
                final Action2<Integer,Integer> rotateLeftTest = (Integer value, Integer expected) ->
                {
                    runner.test("with 0x" + Integers.toHexString(value), (Test test) ->
                    {
                        test.assertEqual(expected, Integers.rotateLeft(value));
                    });
                };

                rotateLeftTest.run(0x00000000, 0x00000000);
                rotateLeftTest.run(0x00000001, 0x00000002);
                rotateLeftTest.run(0x00000002, 0x00000004);
                rotateLeftTest.run(0x00000004, 0x00000008);
                rotateLeftTest.run(0x80000000, 0x00000001);
                rotateLeftTest.run(0xFFFFFFFF, 0xFFFFFFFF);
                rotateLeftTest.run(0xFFFFFFFE, 0xFFFFFFFD);
            });

            runner.testGroup("rotateLeft(int,int)", () ->
            {
                final Action3<Integer,Integer,Integer> rotateLeftTest = (Integer value, Integer places, Integer expected) ->
                {
                    runner.test("with 0x" + Integers.toHexString(value) + " rotated " + places + " places", (Test test) ->
                    {
                        test.assertEqual(expected, Integers.rotateLeft(value, places));
                    });
                };

                rotateLeftTest.run(0x01010101, 0, 0x01010101);
                rotateLeftTest.run(0x01010101, 32, 0x01010101);
                rotateLeftTest.run(0x01010101, 64, 0x01010101);
                rotateLeftTest.run(0x01010101, -32, 0x01010101);
                rotateLeftTest.run(0x01010101, -64, 0x01010101);
                rotateLeftTest.run(0x00000000, 3, 0x00000000);
                rotateLeftTest.run(0x00000001, 2, 0x00000004);
                rotateLeftTest.run(0x00000002, 7, 0x00000100);
                rotateLeftTest.run(0x00000004, -1, 0x00000002);
            });

            runner.testGroup("rotateRight(int)", () ->
            {
                final Action2<Integer,Integer> rotateRightTest = (Integer value, Integer expected) ->
                {
                    runner.test("with 0x" + Integers.toHexString(value), (Test test) ->
                    {
                        test.assertEqual(expected, Integers.rotateRight(value));
                    });
                };

                rotateRightTest.run(0x00000000, 0x00000000);
                rotateRightTest.run(0x00000001, 0x80000000);
                rotateRightTest.run(0x00000002, 0x00000001);
                rotateRightTest.run(0x00000004, 0x00000002);
                rotateRightTest.run(0x80000000, 0x40000000);
                rotateRightTest.run(0xFFFFFFFF, 0xFFFFFFFF);
                rotateRightTest.run(0xFFFFFFFE, 0x7FFFFFFF);
            });

            runner.testGroup("rotateRight(int,int)", () ->
            {
                final Action3<Integer,Integer,Integer> rotateRightTest = (Integer value, Integer places, Integer expected) ->
                {
                    runner.test("with 0x" + Integers.toHexString(value) + " rotated " + places + " places", (Test test) ->
                    {
                        test.assertEqual(expected, Integers.rotateRight(value, places));
                    });
                };

                rotateRightTest.run(0x00000000, 3, 0x00000000);
                rotateRightTest.run(0x00000001, 2, 0x40000000);
                rotateRightTest.run(0x00000002, 7, 0x04000000);
                rotateRightTest.run(0x00000004, -1, 0x00000008);
            });

            runner.testGroup("fromHexChar(char)", () ->
            {
                runner.test("with '0'", (Test test) ->
                {
                    test.assertEqual(0, Integers.fromHexChar('0'));
                });

                runner.test("with '1'", (Test test) ->
                {
                    test.assertEqual(1, Integers.fromHexChar('1'));
                });

                runner.test("with '5'", (Test test) ->
                {
                    test.assertEqual(5, Integers.fromHexChar('5'));
                });

                runner.test("with '8'", (Test test) ->
                {
                    test.assertEqual(8, Integers.fromHexChar('8'));
                });

                runner.test("with '9'", (Test test) ->
                {
                    test.assertEqual(9, Integers.fromHexChar('9'));
                });

                runner.test("with 'a'", (Test test) ->
                {
                    test.assertEqual(10, Integers.fromHexChar('a'));
                });

                runner.test("with 'A'", (Test test) ->
                {
                    test.assertEqual(10, Integers.fromHexChar('A'));
                });

                runner.test("with 'b'", (Test test) ->
                {
                    test.assertEqual(11, Integers.fromHexChar('b'));
                });

                runner.test("with 'B'", (Test test) ->
                {
                    test.assertEqual(11, Integers.fromHexChar('B'));
                });

                runner.test("with 'f'", (Test test) ->
                {
                    test.assertEqual(15, Integers.fromHexChar('f'));
                });

                runner.test("with 'F'", (Test test) ->
                {
                    test.assertEqual(15, Integers.fromHexChar('F'));
                });
            });
        });
    }
}
