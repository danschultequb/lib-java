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

            runner.test("clear()", (Test test) ->
            {
                final Value<java.lang.Character> value = Value.create(java.lang.Character.valueOf('v'));
                value.clear();
                test.assertFalse(value.hasValue());
                test.assertThrows(value::get,
                    new PreConditionFailure("this.hasValue() cannot be false."));
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

            runner.testGroup("getOrSet(T)", () ->
            {
                runner.test("with null initialValue", (Test test) ->
                {
                    final Value<String> value = Value.create();
                    final String getOrSetResult = value.getOrSet((String)null);
                    test.assertNull(getOrSetResult);
                    test.assertTrue(value.hasValue());
                    test.assertNull(value.get());
                });

                runner.test("with no value", (Test test) ->
                {
                    final Value<String> value = Value.create();
                    final String getOrSetResult = value.getOrSet("hello");
                    test.assertEqual("hello", getOrSetResult);
                    test.assertTrue(value.hasValue());
                    test.assertEqual("hello", value.get());
                });

                runner.test("with existing value", (Test test) ->
                {
                    final Value<String> value = Value.create("there");
                    final String getOrSetResult = value.getOrSet("hello");
                    test.assertEqual("there", getOrSetResult);
                    test.assertTrue(value.hasValue());
                    test.assertEqual("there", value.get());
                });
            });

            runner.testGroup("getOrSet(Function0<T>)", () ->
            {
                runner.test("with null creator", (Test test) ->
                {
                    final Value<String> value = Value.create();
                    test.assertThrows(() -> value.getOrSet((Function0<String>)null),
                        new PreConditionFailure("creator cannot be null."));
                    test.assertFalse(value.hasValue());
                });

                runner.test("with no value", (Test test) ->
                {
                    final Value<String> value = Value.create();
                    final String getOrSetResult = value.getOrSet(() -> "hello");
                    test.assertEqual("hello", getOrSetResult);
                    test.assertTrue(value.hasValue());
                    test.assertEqual("hello", value.get());
                });

                runner.test("with existing value", (Test test) ->
                {
                    final Value<String> value = Value.create("there");
                    final IntegerValue counter = Value.create(0);
                    final String getOrSetResult = value.getOrSet(() ->
                    {
                        counter.increment();
                        return "hello";
                    });
                    test.assertEqual("there", getOrSetResult);
                    test.assertTrue(value.hasValue());
                    test.assertEqual("there", value.get());
                    test.assertEqual(0, counter.get());
                });
            });
        });
    }
}
