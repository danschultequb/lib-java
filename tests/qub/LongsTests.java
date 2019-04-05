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
                    test.assertThrows(() -> Longs.parse(null), new PreConditionFailure("value cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> Longs.parse(""), new PreConditionFailure("value cannot be empty."));
                });

                runner.test("with non-number", (Test test) ->
                {
                    test.assertThrows(() -> Longs.parse("a").await(),
                        new NumberFormatException("For input string: \"a\""));
                });

                runner.test("with negative long number", (Test test) ->
                {
                    test.assertEqual(Longs.minimumValue, Longs.parse(Longs.toString(Longs.minimumValue)).await());
                });

                runner.test("with negative integer number", (Test test) ->
                {
                    test.assertEqual(-5L, Longs.parse("-5").await());
                });

                runner.test("with negative zero", (Test test) ->
                {
                    test.assertEqual(0L, Longs.parse("-0").await());
                });

                runner.test("with positive zero", (Test test) ->
                {
                    test.assertEqual(0L, Longs.parse("0").await());
                });

                runner.test("with positive integer number", (Test test) ->
                {
                    test.assertEqual(5L, Longs.parse("5").await());
                });

                runner.test("with positive long number", (Test test) ->
                {
                    test.assertEqual(Longs.maximumValue, Longs.parse(Longs.toString(Longs.maximumValue)).await());
                });
            });
        });
    }
}
