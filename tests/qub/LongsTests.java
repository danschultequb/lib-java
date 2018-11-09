package qub;

public class LongsTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Longs.class, () ->
        {
            runner.testGroup("parse(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Longs.parse(null));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> Longs.parse(""));
                });

                runner.test("with non-number", (Test test) ->
                {
                    test.assertError(new NumberFormatException("For input string: \"a\""), Longs.parse("a"));
                });

                runner.test("with negative long number", (Test test) ->
                {
                    test.assertSuccess(Longs.minimumValue, Longs.parse(Longs.toString(Longs.minimumValue)));
                });

                runner.test("with negative integer number", (Test test) ->
                {
                    test.assertSuccess(-5L, Longs.parse("-5"));
                });

                runner.test("with negative zero", (Test test) ->
                {
                    test.assertSuccess(0L, Longs.parse("-0"));
                });

                runner.test("with positive zero", (Test test) ->
                {
                    test.assertSuccess(0L, Longs.parse("0"));
                });

                runner.test("with positive integer number", (Test test) ->
                {
                    test.assertSuccess(5L, Longs.parse("5"));
                });

                runner.test("with positive long number", (Test test) ->
                {
                    test.assertSuccess(Longs.maximumValue, Longs.parse(Longs.toString(Longs.maximumValue)));
                });
            });
        });
    }
}
