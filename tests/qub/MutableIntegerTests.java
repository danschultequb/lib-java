package qub;

public class MutableIntegerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MutableInteger.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final MutableInteger value = new MutableInteger();
                test.assertEqual(0, value.get());
            });

            runner.test("constructor(int)", (Test test) ->
            {
                final MutableInteger value = new MutableInteger(5);
                test.assertEqual(5, value.get());
            });

            runner.testGroup("constructor(java.lang.Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new MutableInteger(null), new PreConditionFailure("value cannot be null."));
                });

                runner.test("with -1", (Test test) ->
                {
                    final MutableInteger value = new MutableInteger(java.lang.Integer.valueOf(-1));
                    test.assertEqual(-1, value.get());
                });
            });

            runner.testGroup("set(java.lang.Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableInteger value = new MutableInteger(7);
                    test.assertThrows(() -> value.set(null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(7, value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final MutableInteger value = new MutableInteger(10);
                    value.set(11);
                    test.assertEqual(11, value.get());
                });
            });

            runner.testGroup("plusAssign(java.lang.Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableInteger value = new MutableInteger(3);
                    test.assertThrows(() -> value.plusAssign(null));
                    test.assertEqual(3, value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final MutableInteger value = new MutableInteger(4);
                    value.plusAssign(7);
                    test.assertEqual(11, value.get());
                });
            });

            runner.testGroup("minusAssign(java.lang.Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableInteger value = new MutableInteger(3);
                    test.assertThrows(() -> value.minusAssign(null));
                    test.assertEqual(3, value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final MutableInteger value = new MutableInteger(4);
                    value.minusAssign(7);
                    test.assertEqual(-3, value.get());
                });
            });

            runner.test("increment()", (Test test) ->
            {
                final MutableInteger value = new MutableInteger(20);
                test.assertEqual(21, value.increment());
                test.assertEqual(21, value.get());
            });

            runner.test("decrement()", (Test test) ->
            {
                final MutableInteger value = new MutableInteger(30);
                test.assertEqual(29, value.decrement());
                test.assertEqual(29, value.get());
            });
        });
    }
}
