package qub;

public interface LongValueTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(LongValue.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final LongValue value = new LongValue();
                test.assertFalse(value.hasValue());
                test.assertThrows(() -> value.get(), new PreConditionFailure("hasValue() cannot be false."));
                test.assertThrows(() -> value.getAsLong(), new PreConditionFailure("hasValue() cannot be false."));
            });

            runner.test("constructor(long)", (Test test) ->
            {
                final LongValue value = new LongValue(5);
                test.assertTrue(value.hasValue());
                test.assertEqual(5, value.get());
            });

            runner.testGroup("constructor(java.lang.Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new LongValue((java.lang.Integer)null),
                        new PreConditionFailure("value cannot be null."));
                });

                runner.test("with -1", (Test test) ->
                {
                    final LongValue value = new LongValue(java.lang.Integer.valueOf(-1));
                    test.assertEqual(-1, value.get());
                });
            });

            runner.testGroup("constructor(java.lang.Long)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new LongValue((java.lang.Long)null),
                        new PreConditionFailure("value cannot be null."));
                });

                runner.test("with -1", (Test test) ->
                {
                    final LongValue value = new LongValue(java.lang.Long.valueOf(-1));
                    test.assertEqual(-1, value.get());
                });
            });

            runner.test("create()", (Test test) ->
            {
                final LongValue value = LongValue.create();
                test.assertNotNull(value);
                test.assertFalse(value.hasValue());
            });

            runner.testGroup("set(java.lang.Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final LongValue value = new LongValue(7);
                    test.assertThrows(() -> value.set((java.lang.Integer)null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(7, value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final LongValue value = new LongValue(10);
                    value.set(java.lang.Integer.valueOf(11));
                    test.assertEqual(11, value.get());
                });
            });

            runner.testGroup("set(java.lang.Long)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final LongValue value = new LongValue(7);
                    test.assertThrows(() -> value.set((java.lang.Long)null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(7, value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final LongValue value = new LongValue(10);
                    value.set(java.lang.Long.valueOf(11));
                    test.assertEqual(11, value.get());
                });
            });

            runner.testGroup("plusAssign(java.lang.Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final LongValue value = new LongValue(3);
                    test.assertThrows(() -> value.plusAssign((java.lang.Integer)null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(3, value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final LongValue value = new LongValue(4);
                    value.plusAssign(java.lang.Integer.valueOf(7));
                    test.assertEqual(11, value.get());
                });
            });

            runner.testGroup("plusAssign(java.lang.Long)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final LongValue value = new LongValue(3);
                    test.assertThrows(() -> value.plusAssign((java.lang.Long)null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(3, value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final LongValue value = new LongValue(4);
                    value.plusAssign(java.lang.Long.valueOf(7));
                    test.assertEqual(11, value.get());
                });
            });

            runner.testGroup("minusAssign(java.lang.Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final LongValue value = new LongValue(3);
                    test.assertThrows(() -> value.minusAssign((java.lang.Integer)null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(3, value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final LongValue value = new LongValue(4);
                    value.minusAssign(java.lang.Integer.valueOf(7));
                    test.assertEqual(-3, value.get());
                });
            });

            runner.testGroup("minusAssign(java.lang.Long)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final LongValue value = new LongValue(3);
                    test.assertThrows(() -> value.minusAssign((java.lang.Long)null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(3, value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final LongValue value = new LongValue(4);
                    value.minusAssign(java.lang.Long.valueOf(7));
                    test.assertEqual(-3, value.get());
                });
            });

            runner.test("increment()", (Test test) ->
            {
                final LongValue value = new LongValue(20);
                test.assertSame(value, value.increment());
                test.assertEqual(21, value.get());
            });

            runner.test("decrement()", (Test test) ->
            {
                final LongValue value = new LongValue(30);
                test.assertSame(value, value.decrement());
                test.assertEqual(29, value.get());
            });
        });
    }
}
