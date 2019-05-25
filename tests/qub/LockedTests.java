package qub;

public interface LockedTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Locked.class, () ->
        {
            runner.testGroup("constructor(T)", () ->
            {
                runner.test("with null value", (Test test) ->
                {
                    final Locked<IntegerValue> value = new Locked<>(null);
                    value.get((IntegerValue intValue) ->
                    {
                        test.assertNull(intValue);
                    });
                });

                runner.test("with non-null value", (Test test) ->
                {
                    final Locked<IntegerValue> value = new Locked<>(Value.create(5));
                    value.get((IntegerValue intValue) ->
                    {
                        test.assertEqual(5, intValue.get());
                    });
                });
            });

            runner.testGroup("constructor(T,Mutex)", () ->
            {
                runner.test("with null Mutex", (Test test) ->
                {
                    test.assertThrows(new PreConditionFailure("mutex cannot be null."),
                        () -> new Locked<>(5, null));
                });

                runner.test("with null value", (Test test) ->
                {
                    final Mutex mutex = new SpinMutex();
                    final Locked<IntegerValue> value = new Locked<>(null, mutex);
                    test.assertFalse(mutex.isAcquired());
                    value.get((IntegerValue intValue) ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        test.assertTrue(mutex.isAcquiredByCurrentThread());
                        test.assertNull(intValue);
                    });
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null value", (Test test) ->
                {
                    final Mutex mutex = new SpinMutex();
                    final Locked<IntegerValue> value = new Locked<>(Value.create(5), mutex);
                    test.assertFalse(mutex.isAcquired());
                    value.get((IntegerValue intValue) ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        test.assertTrue(mutex.isAcquiredByCurrentThread());
                        test.assertEqual(5, intValue.get());
                    });
                    test.assertFalse(mutex.isAcquired());
                });
            });

            runner.testGroup("create(T)", () ->
            {
                runner.test("with null value", (Test test) ->
                {
                    final Locked<IntegerValue> value = Locked.create(null);
                    value.get((IntegerValue intValue) ->
                    {
                        test.assertNull(intValue);
                    });
                });

                runner.test("with non-null value", (Test test) ->
                {
                    final Locked<IntegerValue> value = Locked.create(Value.create(5));
                    value.get((IntegerValue intValue) ->
                    {
                        test.assertEqual(5, intValue.get());
                    });
                });
            });

            runner.testGroup("create(T,Mutex)", () ->
            {
                runner.test("with null Mutex", (Test test) ->
                {
                    test.assertThrows(new PreConditionFailure("mutex cannot be null."),
                        () -> Locked.create(5, null));
                });

                runner.test("with null value", (Test test) ->
                {
                    final Mutex mutex = new SpinMutex();
                    final Locked<IntegerValue> value = Locked.create(null, mutex);
                    test.assertFalse(mutex.isAcquired());
                    value.get((IntegerValue intValue) ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        test.assertTrue(mutex.isAcquiredByCurrentThread());
                        test.assertNull(intValue);
                    });
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null value", (Test test) ->
                {
                    final Mutex mutex = new SpinMutex();
                    final Locked<IntegerValue> value = Locked.create(Value.create(5), mutex);
                    test.assertFalse(mutex.isAcquired());
                    value.get((IntegerValue intValue) ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        test.assertTrue(mutex.isAcquiredByCurrentThread());
                        test.assertEqual(5, intValue.get());
                    });
                    test.assertFalse(mutex.isAcquired());
                });
            });

            runner.testGroup("get(Action1<T>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final Mutex mutex = new SpinMutex();
                    final Locked<IntegerValue> lockedValue = new Locked<>(IntegerValue.create(5), mutex);
                    test.assertThrows(new PreConditionFailure("action cannot be null."),
                        () -> lockedValue.get((Action1<IntegerValue>)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with multiple threads", (Test test) ->
                {
                    final Mutex mutex = new SpinMutex();
                    final Locked<IntegerValue> lockedValue = new Locked<>(IntegerValue.create(0), mutex);
                    final List<AsyncTask<Void>> tasks = List.create();
                    final int taskCount = 1000;

                    for (int i = 0; i < taskCount; ++i)
                    {
                        tasks.add(test.getParallelAsyncRunner().schedule(() ->
                        {
                            lockedValue.get((IntegerValue value) ->
                            {
                                test.assertTrue(mutex.isAcquiredByCurrentThread());
                                value.increment();
                            });
                        }));
                    }
                    Result.await(tasks);

                    lockedValue.get((IntegerValue intValue) ->
                    {
                        test.assertEqual(taskCount, intValue.get());
                    });
                });
            });

            runner.testGroup("get(Function1<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = new SpinMutex();
                    final Locked<IntegerValue> lockedValue = new Locked<>(IntegerValue.create(5), mutex);
                    test.assertThrows(new PreConditionFailure("function cannot be null."),
                        () -> lockedValue.get((Function1<IntegerValue,Integer>)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with multiple threads", (Test test) ->
                {
                    final Mutex mutex = new SpinMutex();
                    final Locked<IntegerValue> lockedValue = new Locked<>(IntegerValue.create(0), mutex);
                    final List<Result<Integer>> tasks = List.create();
                    final int taskCount = 1000;

                    for (int i = 0; i < taskCount; ++i)
                    {
                        tasks.add(test.getParallelAsyncRunner().schedule(() ->
                        {
                            return lockedValue.get((IntegerValue value) ->
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

                    lockedValue.get((IntegerValue intValue) ->
                    {
                        test.assertEqual(taskCount, intValue.get());
                    });
                });
            });
        });
    }
}
