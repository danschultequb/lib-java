package qub;

public class DoublesTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Doubles.class, () ->
        {
            runner.test("constructor", (Test test) ->
            {
                final Doubles value = new Doubles();
                test.assertNotNull(value);
            });

            runner.test("bitCount", (Test test) ->
            {
                test.assertEqual(64, Doubles.bitCount);
            });

            runner.test("byteCount", (Test test) ->
            {
                test.assertEqual(8, Doubles.byteCount);
            });

            runner.testGroup("hashCode(double)", () ->
            {
                runner.test("with different values", (Test test) ->
                {
                    test.assertNotEqual(Doubles.hashCode(5.1), Doubles.hashCode(5.2));
                });

                runner.test("with equal values", (Test test) ->
                {
                    test.assertEqual(Doubles.hashCode(5.1), Doubles.hashCode(5.1));
                });

                runner.test("with same value", (Test test) ->
                {
                    final double value = 5.1;
                    test.assertEqual(Doubles.hashCode(value), Doubles.hashCode(value));
                });
            });

            runner.testGroup("hashCode(Double)", () ->
            {
                runner.test("with different values", (Test test) ->
                {
                    test.assertNotEqual(Doubles.hashCode(Double.valueOf(5.1)), Doubles.hashCode(Double.valueOf(5.2)));
                });

                runner.test("with equal values", (Test test) ->
                {
                    test.assertEqual(Doubles.hashCode(Double.valueOf(5.1)), Doubles.hashCode(Double.valueOf(5.1)));
                });

                runner.test("with same value", (Test test) ->
                {
                    final Double value = Double.valueOf(5.1);
                    test.assertEqual(Doubles.hashCode(value), Doubles.hashCode(value));
                });

                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Doubles.hashCode((Double)null), new PreConditionFailure("value cannot be null."));
                });
            });

            runner.testGroup("toString(double)", () ->
            {
                runner.test("with 10.0", (Test test) ->
                {
                    test.assertEqual("10.0", Doubles.toString(10.0));
                });

                runner.test("with -3.2", (Test test) ->
                {
                    test.assertEqual("-3.2", Doubles.toString(-3.2));
                });

                runner.test("with 0.0", (Test test) ->
                {
                    test.assertEqual("0.0", Doubles.toString(0.0));
                });
            });

            runner.testGroup("toString(Double)", () ->
            {
                runner.test("with 10.0", (Test test) ->
                {
                    test.assertEqual("10.0", Doubles.toString(Double.valueOf(10.0)));
                });

                runner.test("with -3.2", (Test test) ->
                {
                    test.assertEqual("-3.2", Doubles.toString(Double.valueOf(-3.2)));
                });

                runner.test("with 0.0", (Test test) ->
                {
                    test.assertEqual("0.0", Doubles.toString(Double.valueOf(0.0)));
                });

                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Doubles.toString((Double)null), new PreConditionFailure("value cannot be null."));
                });
            });
        });
    }
}
