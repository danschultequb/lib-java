package qub;

public interface LazyValueTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(LazyValue.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final LazyValue<Integer> value = LazyValue.create();
                test.assertNotNull(value);
                test.assertFalse(value.hasValue());
                test.assertFalse(value.hasCreatorRun());
            });

            runner.testGroup("create(T)", () ->
            {
                final Action1<Integer> createTest = (Integer intValue) ->
                {
                    runner.test("with " + intValue, (Test test) ->
                    {
                        final LazyValue<Integer> value = LazyValue.create(intValue);
                        test.assertNotNull(value);
                        test.assertTrue(value.hasValue());
                        test.assertSame(intValue, value.get());
                    });
                };

                createTest.run(null);
                createTest.run(-1);
                createTest.run(10);
            });

            runner.testGroup("create(Function0<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> LazyValue.create((Function0<Integer>)null),
                        new PreConditionFailure("creator cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final LazyValue<Integer> value = LazyValue.create(() -> 5);
                    test.assertNotNull(value);
                    test.assertFalse(value.hasCreatorRun());
                    test.assertTrue(value.hasValue());
                    test.assertEqual(5, value.get());
                    test.assertTrue(value.hasCreatorRun());
                    test.assertTrue(value.hasValue());
                });
            });

            runner.testGroup("get()", () ->
            {
                runner.test("with no creator", (Test test) ->
                {
                    final LazyValue<Integer> value = LazyValue.create();

                    test.assertThrows(() -> value.get(),
                        new PreConditionFailure("this.hasValue() cannot be false."));
                    test.assertFalse(value.hasCreatorRun());
                    test.assertFalse(value.hasValue());

                    test.assertThrows(() -> value.get(),
                        new PreConditionFailure("this.hasValue() cannot be false."));
                    test.assertFalse(value.hasCreatorRun());
                    test.assertFalse(value.hasValue());
                });

                runner.test("with creator that throws exception", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    final LazyValue<Integer> value = LazyValue.create(() -> { throw new NotFoundException(counter.incrementAndGet().toString()); });
                    test.assertTrue(value.hasValue());

                    test.assertThrows(value::get, new NotFoundException("1"));
                    test.assertTrue(value.hasValue());
                    test.assertFalse(value.hasCreatorRun());

                    test.assertThrows(value::get, new NotFoundException("2"));
                    test.assertTrue(value.hasValue());
                    test.assertFalse(value.hasCreatorRun());
                });

                runner.test("with creator that returns a value", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    final LazyValue<Integer> value = LazyValue.create(counter::incrementAndGet);
                    test.assertTrue(value.hasValue());

                    test.assertEqual(1, value.get());
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.hasCreatorRun());

                    test.assertEqual(1, value.get());
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.hasCreatorRun());
                });
            });

            runner.test("clear()", (Test test) ->
            {
                final LazyValue<Integer> value = LazyValue.create(10);
                test.assertTrue(value.hasValue());
                test.assertFalse(value.hasCreatorRun());

                final LazyValue<Integer> clearResult1 = value.clear();
                test.assertSame(value, clearResult1);
                test.assertFalse(value.hasValue());
                test.assertFalse(value.hasCreatorRun());

                final LazyValue<Integer> clearResult2 = value.clear();
                test.assertSame(value, clearResult2);
                test.assertFalse(value.hasValue());
                test.assertFalse(value.hasCreatorRun());

                value.set(20);
                test.assertTrue(value.hasValue());
                test.assertFalse(value.hasCreatorRun());
                test.assertEqual(20, value.get());
                test.assertTrue(value.hasValue());
                test.assertTrue(value.hasCreatorRun());

                final LazyValue<Integer> clearResult3 = value.clear();
                test.assertSame(value, clearResult3);
                test.assertFalse(value.hasValue());
                test.assertFalse(value.hasCreatorRun());
            });

            runner.testGroup("set(T)", () ->
            {
                final Action1<Integer> setTest = (Integer intValue) ->
                {
                    runner.test("with " + intValue, (Test test) ->
                    {
                        final LazyValue<Integer> value = LazyValue.create();

                        final LazyValue<Integer> setResult = value.set(intValue);
                        test.assertSame(value, setResult);
                        test.assertTrue(value.hasValue());
                        test.assertFalse(value.hasCreatorRun());

                        test.assertEqual(intValue, value.get());
                        test.assertTrue(value.hasValue());
                        test.assertTrue(value.hasCreatorRun());

                        test.assertEqual(intValue, value.get());
                        test.assertTrue(value.hasValue());
                        test.assertTrue(value.hasCreatorRun());
                    });
                };

                setTest.run(null);
                setTest.run(0);
                setTest.run(100);
            });

            runner.testGroup("set(Function0<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final LazyValue<Integer> value = LazyValue.create();

                    test.assertThrows(() -> value.set((Function0<Integer>)null),
                        new PreConditionFailure("creator cannot be null."));
                    test.assertFalse(value.hasValue());
                    test.assertFalse(value.hasCreatorRun());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    final LazyValue<Integer> value = LazyValue.create();

                    final LazyValue<Integer> setResult = value.set(counter::incrementAndGet);
                    test.assertSame(value, setResult);
                    test.assertTrue(value.hasValue());
                    test.assertFalse(value.hasCreatorRun());

                    test.assertEqual(1, value.get());
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.hasCreatorRun());

                    test.assertEqual(1, value.get());
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.hasCreatorRun());
                });
            });

            runner.testGroup("getOrSet(Function0<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final LazyValue<Integer> value = LazyValue.create();

                    test.assertThrows(() -> value.getOrSet((Function0<Integer>)null),
                        new PreConditionFailure("creator cannot be null."));
                    test.assertFalse(value.hasValue());
                    test.assertFalse(value.hasCreatorRun());
                });

                runner.test("with non-null and pre-existing creator", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    final LazyValue<Integer> value = LazyValue.create(1);

                    test.assertEqual(1, value.getOrSet(counter::incrementAndGet));
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.hasCreatorRun());
                    test.assertEqual(0, counter.get());

                    test.assertEqual(1, value.getOrSet(counter::incrementAndGet));
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.hasCreatorRun());
                    test.assertEqual(0, counter.get());
                });

                runner.test("with non-null and no pre-existing creator", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    final LazyValue<Integer> value = LazyValue.create();

                    test.assertEqual(1, value.getOrSet(counter::incrementAndGet));
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.hasCreatorRun());
                    test.assertEqual(1, counter.get());

                    test.assertEqual(1, value.getOrSet(counter::incrementAndGet));
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.hasCreatorRun());
                    test.assertEqual(1, counter.get());
                });
            });
        });
    }
}
