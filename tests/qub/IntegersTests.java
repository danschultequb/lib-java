package qub;

public class IntegersTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Integers.class, () ->
        {
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
