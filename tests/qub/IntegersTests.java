package qub;

public interface IntegersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Integers.class, () ->
        {
            runner.testGroup("sum(Integer...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Integers.sum((Integer[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with null Integer", (Test test) ->
                {
                    test.assertThrows(() -> Integers.sum(1, null, 3),
                        new PreConditionFailure("values[1] cannot be null."));
                });

                runner.test("with no values", (Test test) ->
                {
                    test.assertEqual(0, Integers.sum());
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual(7, Integers.sum(7));
                });

                runner.test("with multiple values", (Test test) ->
                {
                    test.assertEqual(34, Integers.sum(7, 20, 3, 4));
                });
            });

            runner.testGroup("sum(Iterable<Integer>)", () ->
            {
                runner.test("with null Iterable", (Test test) ->
                {
                    test.assertThrows(() -> Integers.sum((Iterable<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with null Integer", (Test test) ->
                {
                    test.assertThrows(() -> Integers.sum(1, 2, null),
                        new PreConditionFailure("values[2] cannot be null."));
                });

                runner.test("with no values", (Test test) ->
                {
                    test.assertEqual(0, Integers.sum(Iterable.create()));
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual(7, Integers.sum(Iterable.create(7)));
                });

                runner.test("with multiple values", (Test test) ->
                {
                    test.assertEqual(34, Integers.sum(Iterable.create(7, 20, 3, 4)));
                });
            });

            runner.testGroup("sum(Iterator<Integer>)", () ->
            {
                runner.test("with null Iterator", (Test test) ->
                {
                    test.assertThrows(() -> Integers.sum((Iterator<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with null Integer", (Test test) ->
                {
                    test.assertThrows(() -> Integers.sum(Iterator.create((Integer)null, 2, 3)),
                        new PreConditionFailure("values[0] cannot be null."));
                });

                runner.test("with no values", (Test test) ->
                {
                    test.assertEqual(0, Integers.sum(Iterator.create()));
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual(7, Integers.sum(Iterator.create(7)));
                });

                runner.test("with multiple values", (Test test) ->
                {
                    test.assertEqual(34, Integers.sum(Iterator.create(7, 20, 3, 4)));
                });
            });

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

            runner.testGroup("parse(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Integers.parse(null),
                        new PreConditionFailure("text cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> Integers.parse(""),
                        new PreConditionFailure("text cannot be empty."));
                });

                runner.test("with letters", (Test test) ->
                {
                    test.assertThrows(() -> Integers.parse("abc").await(),
                        new NumberFormatException("For input string: \"abc\""));
                });

                runner.test("with digits", (Test test) ->
                {
                    test.assertEqual(512, Integers.parse("512").await());
                });
            });
        });
    }
}
