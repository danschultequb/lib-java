package qub;

public interface IntegerValueTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(IntegerValue.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final IntegerValue value = new IntegerValue();
                test.assertFalse(value.hasValue());
                test.assertThrows(() -> value.get(), new PreConditionFailure("hasValue() cannot be false."));
                test.assertThrows(() -> value.getAsInt(), new PreConditionFailure("hasValue() cannot be false."));
            });

            runner.test("constructor(int)", (Test test) ->
            {
                final IntegerValue value = new IntegerValue(5);
                test.assertTrue(value.hasValue());
                test.assertEqual(5, value.get());
            });

            runner.testGroup("constructor(java.lang.Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new IntegerValue(null), new PreConditionFailure("value cannot be null."));
                });

                runner.test("with -1", (Test test) ->
                {
                    final IntegerValue value = new IntegerValue(java.lang.Integer.valueOf(-1));
                    test.assertEqual(-1, value.get());
                });
            });

            runner.test("create()", (Test test) ->
            {
                final IntegerValue value = IntegerValue.create();
                test.assertNotNull(value);
                test.assertFalse(value.hasValue());
            });

            runner.testGroup("set(java.lang.Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final IntegerValue value = new IntegerValue(7);
                    test.assertThrows(() -> value.set(null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(7, value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue value = new IntegerValue(10);
                    value.set(11);
                    test.assertEqual(11, value.get());
                });
            });

            runner.testGroup("plusAssign(java.lang.Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final IntegerValue value = new IntegerValue(3);
                    test.assertThrows(() -> value.plusAssign(null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(3, value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue value = new IntegerValue(4);
                    value.plusAssign(7);
                    test.assertEqual(11, value.get());
                });
            });

            runner.testGroup("minusAssign(java.lang.Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final IntegerValue value = new IntegerValue(3);
                    test.assertThrows(() -> value.minusAssign(null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(3, value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue value = new IntegerValue(4);
                    value.minusAssign(7);
                    test.assertEqual(-3, value.get());
                });
            });

            runner.test("increment()", (Test test) ->
            {
                final IntegerValue value = new IntegerValue(20);
                test.assertSame(value, value.increment());
                test.assertEqual(21, value.get());
            });

            runner.test("decrement()", (Test test) ->
            {
                final IntegerValue value = new IntegerValue(30);
                test.assertSame(value, value.decrement());
                test.assertEqual(29, value.get());
            });
        });
    }
}
