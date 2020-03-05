package qub;

public interface ValueTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Value.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final Value<java.lang.Character> value = Value.create();
                    test.assertFalse(value.hasValue());
                    test.assertThrows(value::get,
                        new PreConditionFailure("this.hasValue() cannot be false."));
                });

                runner.test("with null argument", (Test test) ->
                {
                    final Value<java.lang.Character> value = Value.create((java.lang.Character)null);
                    test.assertTrue(value.hasValue());
                    test.assertNull(value.get());
                });

                runner.test("with argument", (Test test) ->
                {
                    final Value<java.lang.Character> value = Value.create(java.lang.Character.valueOf('n'));
                    test.assertTrue(value.hasValue());
                    test.assertEqual('n', value.get());
                });
            });

            runner.testGroup("set()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Value<java.lang.Character> value = Value.create();
                    value.set(null);
                    test.assertTrue(value.hasValue());
                    test.assertNull(value.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Value<java.lang.Character> value = Value.create();
                    value.set('z');
                    test.assertTrue(value.hasValue());
                    test.assertEqual('z', value.get());
                });
            });

            runner.test("clear()", (Test test) ->
            {
                final Value<java.lang.Character> value = Value.create(java.lang.Character.valueOf('v'));
                value.clear();
                test.assertFalse(value.hasValue());
                test.assertThrows(value::get,
                    new PreConditionFailure("this.hasValue() cannot be false."));
            });
        });
    }
}
