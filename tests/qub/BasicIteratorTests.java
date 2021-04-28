package qub;

public interface BasicIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicIterator.class, () ->
        {
            IteratorTests.test(runner, (Integer count, Boolean started) ->
            {
                PreCondition.assertNotNull(count, "count");
                PreCondition.assertNotNull(started, "started");

                final BasicIterator<Integer> result = BasicIterator.create((IteratorActions<Integer> actions, Getter<Integer> currentValue) ->
                {
                    final int nextValue = !currentValue.hasValue()
                        ? 0
                        : currentValue.get() + 1;
                    if (nextValue < count)
                    {
                        actions.returnValue(nextValue);
                    }
                });

                if (started)
                {
                    result.start();
                }

                PostCondition.assertNotNull(result, "result");
                PostCondition.assertEqual(started, result.hasStarted(), "result.hasStarted()");

                return result;
            });

            runner.testGroup("create(Action1<IteratorActions<T>>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> BasicIterator.create((Action1<IteratorActions<Integer>>)null),
                        new PreConditionFailure("getNextValuesAction cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BasicIterator<Integer> iterator = BasicIterator.create((IteratorActions<Integer> actions) -> {});
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());

                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });
            });

            runner.testGroup("create(Action2<IteratorActions<T>,T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> BasicIterator.create((Action2<IteratorActions<Integer>,Getter<Integer>>)null),
                        new PreConditionFailure("getNextValuesAction cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BasicIterator<Integer> iterator = BasicIterator.create((IteratorActions<Integer> actions, Getter<Integer> currentValue) ->
                    {
                        test.assertNotNull(actions);
                        test.assertNotNull(currentValue);
                        test.assertFalse(currentValue.hasValue());
                    });
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());

                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });
            });
        });
    }
}
