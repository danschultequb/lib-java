package qub;

public class RangeTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Range.class, () ->
        {
            runner.testGroup("greaterThanOrEqualTo(T,Function2<T,T,Comparison>)", () ->
            {
                runner.test("with null lowerBound", (Test test) ->
                {
                    test.assertThrows(() -> Range.greaterThanOrEqualTo((Integer)null, Comparer::lessThanOrEqualTo),
                        new PreConditionFailure("lowerBound cannot be null."));
                });

                runner.test("with null comparer", (Test test) ->
                {
                    test.assertThrows(() -> Range.greaterThanOrEqualTo(2, null),
                        new PreConditionFailure("comparer cannot be null."));
                });

                runner.test("with non-null lowerBound", (Test test) ->
                {
                    final Range<Integer> range = Range.greaterThanOrEqualTo(20, Comparer::lessThanOrEqualTo);
                    test.assertNotNull(range);
                    test.assertEqual(20, range.getLowerBound());
                    test.assertNull(range.getUpperBound());
                });
            });

            runner.test("greaterThanOrEqualTo(int)", (Test test) ->
            {
                final Range<Integer> range = Range.greaterThanOrEqualTo(10);
                test.assertNotNull(range);
                test.assertEqual(10, range.getLowerBound());
                test.assertNull(range.getUpperBound());
            });

            runner.testGroup("lessThanOrEqualTo(T,Function2<T,T,Comparison>)", () ->
            {
                runner.test("with null lowerBound", (Test test) ->
                {
                    test.assertThrows(() -> Range.lessThanOrEqualTo((Integer)null, Comparer::lessThanOrEqualTo),
                        new PreConditionFailure("upperBound cannot be null."));
                });

                runner.test("with null comparer", (Test test) ->
                {
                    test.assertThrows(() -> Range.lessThanOrEqualTo(2, null),
                        new PreConditionFailure("comparer cannot be null."));
                });

                runner.test("with non-null lowerBound", (Test test) ->
                {
                    final Range<Integer> range = Range.lessThanOrEqualTo(20, Comparer::lessThanOrEqualTo);
                    test.assertNotNull(range);
                    test.assertNull(range.getLowerBound());
                    test.assertEqual(20, range.getUpperBound());
                });
            });

            runner.test("lessThanOrEqualTo(int)", (Test test) ->
            {
                final Range<Integer> range = Range.lessThanOrEqualTo(10);
                test.assertNotNull(range);
                test.assertNull(range.getLowerBound());
                test.assertEqual(10, range.getUpperBound());
            });

            runner.testGroup("between(T,T,Function2<T,T,Comparison>)", () ->
            {
                runner.test("with null lowerBound", (Test test) ->
                {
                    test.assertThrows(() -> Range.between(null, 100, Comparer::lessThanOrEqualTo),
                        new PreConditionFailure("lowerBound cannot be null."));
                });

                runner.test("with null upperBound", (Test test) ->
                {
                    test.assertThrows(() -> Range.between(1, null, Comparer::lessThanOrEqualTo),
                        new PreConditionFailure("upperBound cannot be null."));
                });

                runner.test("with null comparer", (Test test) ->
                {
                    test.assertThrows(() -> Range.between(1, 100, null),
                        new PreConditionFailure("comparer cannot be null."));
                });

                runner.test("with lowerBound less than upperBound", (Test test) ->
                {
                    final Range<Integer> range = Range.between(1, 100, Comparer::lessThanOrEqualTo);
                    test.assertNotNull(range);
                    test.assertEqual(1, range.getLowerBound());
                    test.assertEqual(100, range.getUpperBound());
                });

                runner.test("with lowerBound equal to upperBound", (Test test) ->
                {
                    final Range<Integer> range = Range.between(5, 5, Comparer::lessThanOrEqualTo);
                    test.assertNotNull(range);
                    test.assertEqual(5, range.getLowerBound());
                    test.assertEqual(5, range.getUpperBound());
                });

                runner.test("with lowerBound greater than upperBound", (Test test) ->
                {
                    final Range<Integer> range = Range.between(15, 5, Comparer::lessThanOrEqualTo);
                    test.assertNotNull(range);
                    test.assertEqual(15, range.getLowerBound());
                    test.assertEqual(5, range.getUpperBound());
                });
            });

            runner.testGroup("contains(T)", () ->
            {
                runner.testGroup("with lessThanOrEqualTo Range", () ->
                {
                    runner.test("with null value", (Test test) ->
                    {
                        final Range<Integer> range = Range.lessThanOrEqualTo(1, Comparer::lessThanOrEqualTo);
                        test.assertThrows(() -> range.contains(null), new PreConditionFailure("value cannot be null."));
                    });

                    runner.test("with value less than upperBound", (Test test) ->
                    {
                        final Range<Integer> range = Range.lessThanOrEqualTo(1, Comparer::lessThanOrEqualTo);
                        test.assertTrue(range.contains(0));
                    });

                    runner.test("with value equal to upperBound", (Test test) ->
                    {
                        final Range<Integer> range = Range.lessThanOrEqualTo(1, Comparer::lessThanOrEqualTo);
                        test.assertTrue(range.contains(1));
                    });

                    runner.test("with value greater than upperBound", (Test test) ->
                    {
                        final Range<Integer> range = Range.lessThanOrEqualTo(1, Comparer::lessThanOrEqualTo);
                        test.assertFalse(range.contains(3));
                    });
                });

                runner.testGroup("with greaterThanOrEqualTo Range", () ->
                {
                    runner.test("with null value", (Test test) ->
                    {
                        final Range<Integer> range = Range.greaterThanOrEqualTo(1, Comparer::lessThanOrEqualTo);
                        test.assertThrows(() -> range.contains(null), new PreConditionFailure("value cannot be null."));
                    });

                    runner.test("with value less than lowerBound", (Test test) ->
                    {
                        final Range<Integer> range = Range.greaterThanOrEqualTo(1, Comparer::lessThanOrEqualTo);
                        test.assertFalse(range.contains(0));
                    });

                    runner.test("with value equal to lowerBound", (Test test) ->
                    {
                        final Range<Integer> range = Range.greaterThanOrEqualTo(1, Comparer::lessThanOrEqualTo);
                        test.assertTrue(range.contains(1));
                    });

                    runner.test("with value greater than lowerBound", (Test test) ->
                    {
                        final Range<Integer> range = Range.greaterThanOrEqualTo(1, Comparer::lessThanOrEqualTo);
                        test.assertTrue(range.contains(3));
                    });
                });

                runner.testGroup("with between Range", () ->
                {
                    runner.test("with null value", (Test test) ->
                    {
                        final Range<Integer> range = Range.between(1, 10, Comparer::lessThanOrEqualTo);
                        test.assertThrows(() -> range.contains(null), new PreConditionFailure("value cannot be null."));
                    });

                    runner.test("with value less than lowerBound", (Test test) ->
                    {
                        final Range<Integer> range = Range.between(1, 10, Comparer::lessThanOrEqualTo);
                        test.assertFalse(range.contains(0));
                    });

                    runner.test("with value equal to lowerBound", (Test test) ->
                    {
                        final Range<Integer> range = Range.between(1, 10, Comparer::lessThanOrEqualTo);
                        test.assertTrue(range.contains(1));
                    });

                    runner.test("with value greater than lowerBound and less than upperBound", (Test test) ->
                    {
                        final Range<Integer> range = Range.between(1, 10, Comparer::lessThanOrEqualTo);
                        test.assertTrue(range.contains(3));
                    });

                    runner.test("with value equal to upperBound", (Test test) ->
                    {
                        final Range<Integer> range = Range.between(1, 10, Comparer::lessThanOrEqualTo);
                        test.assertTrue(range.contains(10));
                    });

                    runner.test("with value greater than upperBound", (Test test) ->
                    {
                        final Range<Integer> range = Range.between(1, 10, Comparer::lessThanOrEqualTo);
                        test.assertFalse(range.contains(11));
                    });
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with lessThanOrEqualTo inclusive Range", (Test test) ->
                {
                    test.assertEqual("(..5]", Range.lessThanOrEqualTo(5, Comparer::lessThanOrEqualTo).toString());
                });

                runner.test("with lessThanOrEqualTo exclusive Range", (Test test) ->
                {
                    test.assertEqual("(..5)", Range.lessThanOrEqualTo(5, Comparer::lessThan).toString());
                });

                runner.test("with greaterThanOrEqualTo inclusive Range", (Test test) ->
                {
                    test.assertEqual("[5..)", Range.greaterThanOrEqualTo(5, Comparer::lessThanOrEqualTo).toString());
                });

                runner.test("with greaterThanOrEqualTo exclusive Range", (Test test) ->
                {
                    test.assertEqual("(5..)", Range.greaterThanOrEqualTo(5, Comparer::lessThan).toString());
                });

                runner.test("with between inclusive Range", (Test test) ->
                {
                    test.assertEqual("[1..5]", Range.between(1, 5, Comparer::lessThanOrEqualTo).toString());
                });

                runner.test("with between exclusive Range", (Test test) ->
                {
                    test.assertEqual("(1..5)", Range.between(1, 5, Comparer::lessThan).toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Range<Integer> range = Range.between(1, 100);
                    test.assertFalse(range.equals((Object)null));
                });

                runner.test("with non-Range", (Test test) ->
                {
                    final Range<Integer> range = Range.between(1, 100);
                    test.assertFalse(range.equals("range"));
                });

                runner.test("with different Range type", (Test test) ->
                {
                    final Range<Integer> range = Range.between(1, 100);
                    test.assertFalse(range.equals((Object)Range.between(Distance.miles(1), Distance.miles(100))));
                });

                runner.test("with different lowerBound", (Test test) ->
                {
                    final Range<Integer> range = Range.between(1, 100);
                    test.assertFalse(range.equals((Object)Range.between(0, 100)));
                });

                runner.test("with different upperBound", (Test test) ->
                {
                    final Range<Integer> range = Range.between(1, 100);
                    test.assertFalse(range.equals((Object)Range.between(1, 101)));
                });

                runner.test("with same Range", (Test test) ->
                {
                    final Range<Integer> range = Range.between(1, 100);
                    test.assertTrue(range.equals((Object)range));
                });

                runner.test("with equal Range", (Test test) ->
                {
                    final Range<Integer> range = Range.between(1, 100);
                    test.assertTrue(range.equals((Object)Range.between(1, 100)));
                });

                runner.test("with between and lessThanOrEqualTo Ranges", (Test test) ->
                {
                    final Range<Integer> range = Range.between(1, 100);
                    test.assertFalse(range.equals((Object)Range.lessThanOrEqualTo(100)));
                });

                runner.test("with between and greaterThanOrEqualTo Ranges", (Test test) ->
                {
                    final Range<Integer> range = Range.between(1, 100);
                    test.assertFalse(range.equals((Object)Range.greaterThanOrEqualTo(1)));
                });
            });

            runner.testGroup("equals(Range<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Range<Integer> range = Range.between(1, 100);
                    test.assertFalse(range.equals((Range<Integer>)null));
                });
            });

            runner.testGroup("hashCode()", () ->
            {
                runner.test("with lessThanOrEqualTo Range", (Test test) ->
                {
                    test.assertEqual(Range.lessThanOrEqualTo(100).hashCode(), Range.lessThanOrEqualTo(100).hashCode());
                });

                runner.test("with greaterThanOrEqualTo Range", (Test test) ->
                {
                    test.assertEqual(Range.greaterThanOrEqualTo(100).hashCode(), Range.greaterThanOrEqualTo(100).hashCode());
                });

                runner.test("with between Range", (Test test) ->
                {
                    test.assertEqual(Range.between(1, 100).hashCode(), Range.between(1, 100).hashCode());
                });
            });
        });
    }
}
