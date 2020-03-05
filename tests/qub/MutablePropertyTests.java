package qub;

public interface MutablePropertyTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(MutableProperty.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableProperty<Integer> property = MutableProperty.create();
                test.assertNotNull(property);
                test.assertFalse(property.hasValue());
            });

            runner.testGroup("create(T)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableProperty<String> property = MutableProperty.create(null);
                    test.assertNotNull(property);
                    test.assertTrue(property.hasValue());
                    test.assertEqual(null, property.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final MutableProperty<String> property = MutableProperty.create("hello");
                    test.assertNotNull(property);
                    test.assertTrue(property.hasValue());
                    test.assertEqual("hello", property.get());
                });
            });
        });
    }

    static void test(TestRunner runner, Function0<? extends MutableProperty<Integer>> creator)
    {
        runner.testGroup(MutableProperty.class, () ->
        {
            Event2Tests.test(runner, creator);

            runner.testGroup("get()", () ->
            {
                runner.test("with no value", (Test test) ->
                {
                    final MutableProperty<Integer> property = creator.run();
                    test.assertFalse(property.hasValue());

                    test.assertThrows(property::get,
                        new PreConditionFailure("this.hasValue() cannot be false."));
                });

                runner.test("with value", (Test test) ->
                {
                    final MutableProperty<Integer> property = creator.run().set(20);
                    test.assertTrue(property.hasValue());

                    test.assertEqual(20, property.get());
                    test.assertEqual(20, property.get());
                });
            });

            runner.testGroup("clear()", () ->
            {
                runner.test("with no value", (Test test) ->
                {
                    final MutableProperty<Integer> property = creator.run();
                    test.assertFalse(property.hasValue());

                    final List<Integer> eventValues = List.create();
                    property.subscribe((Integer oldValue, Integer newValue) -> eventValues.addAll(oldValue, newValue));
                    test.assertEqual(Iterable.create(), eventValues);
                    test.assertFalse(property.hasValue());

                    final MutableProperty<Integer> clearResult = property.clear();
                    test.assertSame(property, clearResult);
                    test.assertFalse(property.hasValue());
                    test.assertEqual(Iterable.create(), eventValues);
                });

                runner.test("with value", (Test test) ->
                {
                    final MutableProperty<Integer> property = creator.run().set(1);
                    test.assertTrue(property.hasValue());
                    test.assertEqual(1, property.get());

                    final List<Integer> eventValues = List.create();
                    property.subscribe((Integer oldValue, Integer newValue) -> eventValues.addAll(oldValue, newValue));
                    test.assertEqual(Iterable.create(), eventValues);
                    test.assertTrue(property.hasValue());
                    test.assertEqual(1, property.get());

                    final MutableProperty<Integer> clearResult = property.clear();
                    test.assertSame(property, clearResult);
                    test.assertFalse(property.hasValue());
                    test.assertEqual(Iterable.create(1, null), eventValues);
                });
            });

            runner.testGroup("set(T)", () ->
            {
                final Action1<Integer> setWhenPropertyDoesntHaveAValueTest = (Integer value) ->
                {
                    runner.test("with " + value + " when property doesn't have a value", (Test test) ->
                    {
                        final MutableProperty<Integer> property = creator.run();
                        test.assertFalse(property.hasValue());

                        final List<Integer> eventValues = List.create();
                        property.subscribe((Integer oldValue, Integer newValue) -> eventValues.addAll(oldValue, newValue));

                        final MutableProperty<Integer> setResult = property.set(value);
                        test.assertSame(property, setResult);
                        test.assertTrue(property.hasValue());
                        test.assertEqual(value, property.get());
                        test.assertEqual(Iterable.create(null, value), eventValues);
                    });
                };

                setWhenPropertyDoesntHaveAValueTest.run(null);
                setWhenPropertyDoesntHaveAValueTest.run(3030);

                final Action3<Integer, Integer,Iterable<Integer>> setWhenPropertyHasAValueTest = (Integer initialValue, Integer value, Iterable<Integer> expectedEventValues) ->
                {
                    runner.test("with " + value + " when property has a " + initialValue + " value", (Test test) ->
                    {
                        final MutableProperty<Integer> property = creator.run().set(initialValue);
                        test.assertTrue(property.hasValue());
                        test.assertEqual(initialValue, property.get());

                        final List<Integer> eventValues = List.create();
                        property.subscribe((Integer oldValue, Integer newValue) -> eventValues.addAll(oldValue, newValue));

                        final MutableProperty<Integer> setResult = property.set(value);
                        test.assertSame(property, setResult);
                        test.assertTrue(property.hasValue());
                        test.assertEqual(value, property.get());
                        test.assertEqual(expectedEventValues, eventValues);
                    });
                };

                setWhenPropertyHasAValueTest.run(null, null, Iterable.create());
                setWhenPropertyHasAValueTest.run(null, 3030, Iterable.create(null, 3030));
                setWhenPropertyHasAValueTest.run(13, null, Iterable.create(13, null));
                setWhenPropertyHasAValueTest.run(13, 30, Iterable.create(13, 30));
                setWhenPropertyHasAValueTest.run(13, 13, Iterable.create());
            });
        });
    }
}
