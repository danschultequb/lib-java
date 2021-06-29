package qub;

public interface LockedTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Locked.class, () ->
        {
            runner.testGroup("create(T)", () ->
            {
                runner.test("with null value", (Test test) ->
                {
                    final Locked<IntegerValue> value = Locked.create(null);
                    value.unlock((IntegerValue intValue) ->
                    {
                        test.assertNull(intValue);
                    });
                });

                runner.test("with non-null value", (Test test) ->
                {
                    final Locked<IntegerValue> value = Locked.create(Value.create(5));
                    value.unlock((IntegerValue intValue) ->
                    {
                        test.assertEqual(5, intValue.get());
                    });
                });
            });

            runner.testGroup("create(T,Mutex)", () ->
            {
                runner.test("with null Mutex", (Test test) ->
                {
                    test.assertThrows(() -> Locked.create(5, null),
                        new PreConditionFailure("mutex cannot be null."));
                });

                runner.test("with null value", (Test test) ->
                {
                    final Mutex mutex = SpinMutex.create();
                    final Locked<IntegerValue> value = Locked.create(null, mutex);
                    test.assertFalse(mutex.isAcquired());
                    value.unlock((IntegerValue intValue) ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        test.assertTrue(mutex.isAcquiredByCurrentThread());
                        test.assertNull(intValue);
                    });
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null value", (Test test) ->
                {
                    final Mutex mutex = SpinMutex.create();
                    final Locked<IntegerValue> value = Locked.create(Value.create(5), mutex);
                    test.assertFalse(mutex.isAcquired());
                    value.unlock((IntegerValue intValue) ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        test.assertTrue(mutex.isAcquiredByCurrentThread());
                        test.assertEqual(5, intValue.get());
                    });
                    test.assertFalse(mutex.isAcquired());
                });
            });

            runner.testGroup("unlock(Action1<T>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final Mutex mutex = SpinMutex.create();
                    final Locked<IntegerValue> lockedValue = Locked.create(IntegerValue.create(5), mutex);
                    test.assertThrows(new PreConditionFailure("action cannot be null."),
                        () -> lockedValue.unlock((Action1<IntegerValue>)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with multiple threads",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final Mutex mutex = SpinMutex.create();
                    final Locked<IntegerValue> lockedValue = Locked.create(IntegerValue.create(0), mutex);
                    final List<Result<Void>> tasks = List.create();
                    final int taskCount = 1000;

                    for (int i = 0; i < taskCount; ++i)
                    {
                        tasks.add(parallelAsyncRunner.schedule(() ->
                        {
                            lockedValue.unlock((IntegerValue value) ->
                            {
                                test.assertTrue(mutex.isAcquiredByCurrentThread());
                                value.increment();
                            });
                        }));
                    }
                    Result.await(tasks);

                    lockedValue.unlock((IntegerValue intValue) ->
                    {
                        test.assertEqual(taskCount, intValue.get());
                    });
                });
            });

            runner.testGroup("unlock(Function1<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = SpinMutex.create();
                    final Locked<IntegerValue> lockedValue = Locked.create(IntegerValue.create(5), mutex);
                    test.assertThrows(new PreConditionFailure("function cannot be null."),
                        () -> lockedValue.unlock((Function1<IntegerValue,Integer>)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with multiple threads",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final Mutex mutex = SpinMutex.create();
                    final Locked<IntegerValue> lockedValue = Locked.create(IntegerValue.create(0), mutex);
                    final List<Result<Integer>> tasks = List.create();
                    final int taskCount = 1000;

                    for (int i = 0; i < taskCount; ++i)
                    {
                        tasks.add(parallelAsyncRunner.schedule(() ->
                        {
                            return lockedValue.unlock((IntegerValue value) ->
                            {
                                test.assertTrue(mutex.isAcquiredByCurrentThread());
                                return value.increment().get();
                            });
                        }));
                    }
                    final List<Integer> values = Result.await(tasks).toList();
                    test.assertEqual(taskCount, values.getCount());
                    values.sort(Comparer::lessThan);
                    for (int i = 0; i < taskCount; ++i)
                    {
                        test.assertEqual(i + 1, values.get(i));
                    }

                    lockedValue.unlock((IntegerValue intValue) ->
                    {
                        test.assertEqual(taskCount, intValue.get());
                    });
                });
            });
        });
    }
}
