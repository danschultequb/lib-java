package qub;

public interface DynamicValueTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(DynamicValue.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final DynamicValue<Integer> value = DynamicValue.create();
                test.assertNotNull(value);
                test.assertFalse(value.hasValue());
            });

            runner.test("create(T)", (Test test) ->
            {
                final DynamicValue<Integer> value = DynamicValue.create(5);
                test.assertNotNull(value);
                test.assertTrue(value.hasValue());
                test.assertEqual(5, value.get());
            });

            runner.testGroup("set(T)", () ->
            {
                final Action3<DynamicValue<Integer>,Integer,Iterable<Tuple2<Integer,Integer>>> setTest = (DynamicValue<Integer> dynamicValue, Integer value, Iterable<Tuple2<Integer,Integer>> expectedEventArguments) ->
                {
                    runner.test("with " + English.andList(dynamicValue, value), (Test test) ->
                    {
                        final List<Tuple2<Integer,Integer>> eventArguments = List.create();
                        dynamicValue.onChanged((Integer previousValue, Integer newValue) ->
                        {
                            eventArguments.add(Tuple.create(previousValue, newValue));
                        });

                        final DynamicValue<Integer> setResult = dynamicValue.set(value);
                        test.assertSame(dynamicValue, setResult);
                        test.assertTrue(dynamicValue.hasValue());
                        test.assertEqual(value, dynamicValue.get());
                        test.assertEqual(expectedEventArguments, eventArguments);
                    });
                };

                setTest.run(
                    DynamicValue.create(),
                    null,
                    Iterable.create(Tuple.create(null, null)));
                setTest.run(
                    DynamicValue.create(null),
                    null,
                    Iterable.create());
                setTest.run(
                    DynamicValue.create(),
                    10,
                    Iterable.create(Tuple.create(null, 10)));
                setTest.run(
                    DynamicValue.create(5),
                    10,
                    Iterable.create(Tuple.create(5, 10)));
                setTest.run(
                    DynamicValue.create(10),
                    10,
                    Iterable.create());
            });

            runner.testGroup("clear()", () ->
            {
                final Action2<DynamicValue<Integer>,Iterable<Tuple2<Integer,Integer>>> clearTest = (DynamicValue<Integer> dynamicValue, Iterable<Tuple2<Integer,Integer>> expectedEventArguments) ->
                {
                    runner.test("with " + dynamicValue, (Test test) ->
                    {
                        final List<Tuple2<Integer,Integer>> eventArguments = List.create();
                        dynamicValue.onChanged((Integer previousValue, Integer newValue) ->
                        {
                            eventArguments.add(Tuple.create(previousValue, newValue));
                        });

                        final DynamicValue<Integer> clearResult = dynamicValue.clear();
                        test.assertSame(dynamicValue, clearResult);
                        test.assertFalse(dynamicValue.hasValue());
                        test.assertEqual(expectedEventArguments, eventArguments);
                    });
                };

                clearTest.run(
                    DynamicValue.create(),
                    Iterable.create());
                clearTest.run(
                    DynamicValue.create(null),
                    Iterable.create(Tuple.create(null, null)));
                clearTest.run(
                    DynamicValue.create(5),
                    Iterable.create(Tuple.create(5, null)));
            });
        });
    }
}
