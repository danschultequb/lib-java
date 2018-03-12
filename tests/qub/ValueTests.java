package qub;

public class ValueTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Value.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final Value<Character> value = new Value<>();
                    test.assertFalse(value.hasValue());
                    test.assertNull(value.get());
                });

                runner.test("with null argument", (Test test) ->
                {
                    final Value<Character> value = new Value<>(null);
                    test.assertTrue(value.hasValue());
                    test.assertNull(value.get());
                });

                runner.test("with argument", (Test test) ->
                {
                    final Value<Character> value = new Value<>('n');
                    test.assertTrue(value.hasValue());
                    test.assertEqual('n', value.get());
                });
            });

            runner.testGroup("set()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Value<Character> value = new Value<>();
                    value.set(null);
                    test.assertTrue(value.hasValue());
                    test.assertNull(value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Value<Character> value = new Value<>();
                    value.set('z');
                    test.assertTrue(value.hasValue());
                    test.assertEqual('z', value.get());
                });
            });

            runner.test("clear()", (Test test) ->
            {
                final Value<Character> value = new Value<>('v');
                value.clear();
                test.assertFalse(value.hasValue());
                test.assertNull(value.get());
            });
        });
    }
}
