package qub;

public interface LongsTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Longs.class, () ->
        {
            runner.testGroup("sum(Integer...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Longs.sum((Long[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with null Integer", (Test test) ->
                {
                    test.assertThrows(() -> Longs.sum(1L, null, 3L),
                        new PreConditionFailure("values[1] cannot be null."));
                });

                runner.test("with no values", (Test test) ->
                {
                    test.assertEqual(0, Longs.sum());
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual(7, Longs.sum(7L));
                });

                runner.test("with multiple values", (Test test) ->
                {
                    test.assertEqual(34, Longs.sum(7L, 20L, 3L, 4L));
                });
            });

            runner.testGroup("sum(Iterable<Integer>)", () ->
            {
                runner.test("with null Iterable", (Test test) ->
                {
                    test.assertThrows(() -> Longs.sum((Iterable<Long>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with null Integer", (Test test) ->
                {
                    test.assertThrows(() -> Longs.sum(1L, 2L, null),
                        new PreConditionFailure("values[2] cannot be null."));
                });

                runner.test("with no values", (Test test) ->
                {
                    test.assertEqual(0, Longs.sum(Iterable.create()));
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual(7, Longs.sum(Iterable.create(7L)));
                });

                runner.test("with multiple values", (Test test) ->
                {
                    test.assertEqual(34, Longs.sum(Iterable.create(7L, 20L, 3L, 4L)));
                });
            });

            runner.testGroup("sum(Iterator<Integer>)", () ->
            {
                runner.test("with null Iterator", (Test test) ->
                {
                    test.assertThrows(() -> Longs.sum((Iterator<Long>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with null Integer", (Test test) ->
                {
                    test.assertThrows(() -> Longs.sum(Iterator.create((Long)null, 2L, 3L)),
                        new PreConditionFailure("values[0] cannot be null."));
                });

                runner.test("with no values", (Test test) ->
                {
                    test.assertEqual(0, Longs.sum(Iterator.create()));
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual(7, Longs.sum(Iterator.create(7L)));
                });

                runner.test("with multiple values", (Test test) ->
                {
                    test.assertEqual(34, Longs.sum(Iterator.create(7L, 20L, 3L, 4L)));
                });
            });

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
                    test.assertEqual(Longs.minimum, Longs.parse(Longs.toString(Longs.minimum)).await());
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
                    test.assertEqual(Longs.maximum, Longs.parse(Longs.toString(Longs.maximum)).await());
                });
            });
        });
    }
}
