package qub;

public interface SaveableValueTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SaveableValue.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final SaveableValue<Integer> value = SaveableValue.create();
                test.assertNotNull(value);
                test.assertFalse(value.hasValue());
            });

            runner.testGroup("create(T)", () ->
            {
                final Action1<Integer> createTest = (Integer initialValue) ->
                {
                    runner.test("with " + initialValue, (Test test) ->
                    {
                        final SaveableValue<Integer> value = SaveableValue.create(initialValue);
                        test.assertNotNull(value);
                        test.assertTrue(value.hasValue());
                        test.assertEqual(initialValue, value.get());
                    });
                };

                createTest.run(null);
                createTest.run(50);
            });

            runner.testGroup("create(Value<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Value<Integer> innerValue = null;
                    test.assertThrows(() -> SaveableValue.create(innerValue),
                        new PreConditionFailure("value cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Value<Integer> innerValue = Value.create();
                    final SaveableValue<Integer> value = SaveableValue.create(innerValue);
                    test.assertFalse(value.hasValue());

                    final Save save1 = value.save();
                    test.assertNotNull(save1);

                    final SaveableValue<Integer> valueSetResult = value.set(20);
                    test.assertSame(value, valueSetResult);
                    test.assertEqual(20, innerValue.get());
                    test.assertEqual(20, value.get());

                    final Value<Integer> innerValueSetResult = innerValue.set(30);
                    test.assertSame(innerValue, innerValueSetResult);
                    test.assertEqual(30, innerValue.get());
                    test.assertEqual(30, value.get());

                    final Save save2 = value.save();
                    test.assertNotNull(save2);

                    value.set(40);

                    save2.restore().await();

                    test.assertEqual(30, innerValue.get());
                    test.assertEqual(30, value.get());

                    final SaveableValue<Integer> valueClearResult = value.clear();
                    test.assertSame(value, valueClearResult);
                    test.assertFalse(value.hasValue());
                    test.assertFalse(innerValue.hasValue());

                    save2.restore().await();

                    test.assertEqual(30, innerValue.get());
                    test.assertEqual(30, value.get());

                    save1.restore().await();

                    test.assertFalse(value.hasValue());
                    test.assertFalse(innerValue.hasValue());
                });
            });

            runner.testGroup("clear()", () ->
            {
                runner.test("with no value", (Test test) ->
                {
                    final SaveableValue<Boolean> value = SaveableValue.create();
                    final SaveableValue<Boolean> clearResult = value.clear();
                    test.assertSame(value, clearResult);
                    test.assertFalse(value.hasValue());
                });

                runner.test("with value", (Test test) ->
                {
                    final SaveableValue<Boolean> value = SaveableValue.create(true);
                    final SaveableValue<Boolean> clearResult = value.clear();
                    test.assertSame(value, clearResult);
                    test.assertFalse(value.hasValue());
                });
            });

            runner.testGroup("set(T)", () ->
            {
                final Action2<SaveableValue<Integer>,Integer> setTest = (SaveableValue<Integer> value, Integer newValue) ->
                {
                    runner.test("with " + English.andList(value, newValue), (Test test) ->
                    {
                        final SaveableValue<Integer> setResult = value.set(newValue);
                        test.assertSame(value, setResult);
                        test.assertTrue(value.hasValue());
                        test.assertEqual(newValue, value.get());
                    });
                };

                setTest.run(SaveableValue.create(), null);
                setTest.run(SaveableValue.create(), 20);
                setTest.run(SaveableValue.create(10), null);
                setTest.run(SaveableValue.create(15), 20);
            });

            runner.testGroup("save()", () ->
            {
                runner.test("with no value", (Test test) ->
                {
                    final SaveableValue<String> value = SaveableValue.create();

                    final Save save = value.save();
                    test.assertNotNull(save);

                    value.set("hello");

                    save.restore().await();

                    test.assertFalse(value.hasValue());

                    value.set("there");

                    test.assertTrue(save.dispose().await());
                    test.assertTrue(save.isDisposed());
                    test.assertEqual("there", value.get());

                    test.assertThrows(() -> save.restore(),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                    test.assertTrue(save.isDisposed());

                    test.assertEqual("there", value.get());
                });

                runner.test("with value", (Test test) ->
                {
                    final SaveableValue<String> value = SaveableValue.create("abc");

                    final Save save = value.save();
                    test.assertNotNull(save);

                    value.set("hello");

                    save.restore().await();

                    test.assertEqual("abc", value.get());

                    value.set("there");

                    test.assertTrue(save.dispose().await());
                    test.assertTrue(save.isDisposed());
                    test.assertEqual("there", value.get());

                    test.assertThrows(() -> save.restore(),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                    test.assertTrue(save.isDisposed());

                    test.assertEqual("there", value.get());
                });

                runner.test("with multiple saves", (Test test) ->
                {
                    final SaveableValue<String> value = SaveableValue.create();

                    final Save save1 = value.save();
                    test.assertNotNull(save1);

                    value.set("hello");

                    final Save save2 = value.save();
                    test.assertNotNull(save2);

                    save1.restore().await();
                    test.assertFalse(value.hasValue());

                    save2.restore().await();
                    test.assertEqual("hello", value.get());

                    save1.restore().await();
                    test.assertFalse(value.hasValue());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<SaveableValue<Integer>,Object,Boolean> equalsTest = (SaveableValue<Integer> lhs, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(SaveableValue.create(), null, false);
                equalsTest.run(SaveableValue.create(), "hello", false);
                equalsTest.run(SaveableValue.create(), 20, false);
                equalsTest.run(SaveableValue.create(), SaveableValue.create(), true);
                equalsTest.run(SaveableValue.create(), SaveableValue.create((Integer)null), false);
                equalsTest.run(SaveableValue.create(), SaveableValue.create(30), false);
                equalsTest.run(SaveableValue.create((Integer)null), null, true);
                equalsTest.run(SaveableValue.create((Integer)null), "hello", false);
                equalsTest.run(SaveableValue.create((Integer)null), 20, false);
                equalsTest.run(SaveableValue.create((Integer)null), SaveableValue.create(), false);
                equalsTest.run(SaveableValue.create((Integer)null), SaveableValue.create((Integer)null), true);
                equalsTest.run(SaveableValue.create((Integer)null), SaveableValue.create(30), false);
                equalsTest.run(SaveableValue.create(20), null, false);
                equalsTest.run(SaveableValue.create(20), "hello", false);
                equalsTest.run(SaveableValue.create(20), 20, true);
                equalsTest.run(SaveableValue.create(20), 30, false);
                equalsTest.run(SaveableValue.create(20), SaveableValue.create(), false);
                equalsTest.run(SaveableValue.create(20), SaveableValue.create((Integer)null), false);
                equalsTest.run(SaveableValue.create(20), SaveableValue.create(20), true);
                equalsTest.run(SaveableValue.create(20), SaveableValue.create(30), false);
            });
        });
    }
}
