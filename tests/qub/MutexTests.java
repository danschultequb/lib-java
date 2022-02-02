package qub;

public interface MutexTests
{
    static void test(TestRunner runner, Function1<Clock,Mutex> creator)
    {
        runner.testGroup(Mutex.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final Mutex mutex = creator.run(null);
                test.assertFalse(mutex.isAcquired());
                test.assertFalse(mutex.isAcquiredByCurrentThread());
            });

            runner.testGroup("isAcquired()", () ->
            {
                runner.test("when not acquired", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("when acquired by current thread", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    mutex.acquire().await();
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("when acquired by non-current thread",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final Mutex mutex = creator.run(null);

                    final Gate mutexAcquired = SpinGate.create(false);
                    final Gate isAcquiredChecked = SpinGate.create(false);

                    final Result<Void> task = parallelAsyncRunner.schedule(() ->
                    {
                        mutex.acquire().await();
                        mutexAcquired.open();
                        isAcquiredChecked.passThrough().await();
                    });

                    mutexAcquired.passThrough().await();
                    test.assertTrue(mutex.isAcquired());
                    isAcquiredChecked.open();
                    task.await();
                });
            });

            runner.testGroup("isAcquiredByCurrentThread()", () ->
            {
                runner.test("when not acquired", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertFalse(mutex.isAcquiredByCurrentThread());
                });

                runner.test("when acquired by current thread", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    mutex.acquire().await();
                    test.assertTrue(mutex.isAcquiredByCurrentThread());
                });

                runner.test("when acquired by non-current thread",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final Mutex mutex = creator.run(null);

                    final Gate mutexAcquired = SpinGate.create(false);
                    final Gate isAcquiredChecked = SpinGate.create(false);

                    final Result<Void> task = parallelAsyncRunner.schedule(() ->
                    {
                        mutex.acquire().await();
                        mutexAcquired.open();
                        isAcquiredChecked.passThrough();
                    });

                    mutexAcquired.passThrough();
                    test.assertFalse(mutex.isAcquiredByCurrentThread());
                    isAcquiredChecked.open();
                    task.await();
                });
            });

            runner.testGroup("acquire()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    mutex.acquire().await();
                    test.assertTrue(mutex.isAcquired());
                    test.assertTrue(mutex.isAcquiredByCurrentThread());
                });

                runner.test("with multiple threads",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertFalse(mutex.isAcquired());

                    final IntegerValue value = IntegerValue.create(0);
                    final int taskCount = 100;
                    final List<Result<Void>> tasks = List.create();
                    for (int i = 0; i < taskCount; ++i)
                    {
                        tasks.add(parallelAsyncRunner.schedule(() ->
                        {
                            mutex.acquire().await();
                            try
                            {
                                value.increment();
                            }
                            finally
                            {
                                mutex.release().await();
                            }
                        }));
                    }
                    Result.await(tasks);
                    test.assertEqual(taskCount, value.get());
                });
            });

            runner.testGroup("acquire(Duration)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertThrows(() -> mutex.acquire((Duration)null),
                        new PreConditionFailure("durationTimeout cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with zero", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertThrows(() -> mutex.acquire(Duration.zero),
                        new PreConditionFailure("durationTimeout (0.0 Seconds) must be greater than 0.0 Seconds."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with null Clock", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertThrows(() -> mutex.acquire(Duration.minutes(5)),
                        new PreConditionFailure("this.clock cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with positive duration when Mutex is not acquired",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    test.assertNull(mutex.acquire(Duration.seconds(5)).await());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("with positive duration when Mutex is already acquired by the current thread",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    test.assertNull(mutex.acquire().await());
                    test.assertThrows(() -> mutex.acquire(Duration.seconds(0.1)).await(),
                        new TimeoutException());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("with positive duration when Mutex is already acquired by a different thread",
                    (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                    (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
                {
                    final Mutex mutex = creator.run(clock);
                    parallelAsyncRunner.schedule(() -> mutex.acquire().await()).await();
                    test.assertTrue(mutex.isAcquired());
                    test.assertFalse(mutex.isAcquiredByCurrentThread());

                    final Duration timeout = Duration.seconds(1);

                    final DateTime startTime = clock.getCurrentDateTime();
                    test.assertThrows(() -> mutex.acquire(timeout).await(),
                        new TimeoutException());
                    final DateTime endTime = clock.getCurrentDateTime();

                    test.assertTrue(mutex.isAcquired());
                    final Duration executionDuration = endTime.minus(startTime);
                    test.assertGreaterThanOrEqualTo(executionDuration, timeout);
                });
            });

            runner.testGroup("acquire(DateTime)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertThrows(() -> mutex.acquire((DateTime)null),
                        new PreConditionFailure("dateTimeTimeout cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with null Clock", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertThrows(() -> mutex.acquire(DateTime.create(2018, 1, 1)),
                        new PreConditionFailure("this.clock cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with timeout before current time",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    final DateTime timeout = clock.getCurrentDateTime().minus(Duration.milliseconds(10));
                    test.assertThrows(() -> mutex.acquire(timeout).await(),
                        new TimeoutException());
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with timeout at current time",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    final DateTime timeout = clock.getCurrentDateTime();
                    test.assertThrows(() -> mutex.acquire(timeout).await(),
                        new TimeoutException());
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with timeout after current time when mutex is not acquired",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(1));
                    test.assertNull(mutex.acquire(timeout).await());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("with timeout after current time when Mutex is already acquired by a different thread",
                    (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                    (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
                {
                    final Mutex mutex = creator.run(clock);
                    parallelAsyncRunner.schedule(() -> mutex.acquire().await()).await();
                    test.assertTrue(mutex.isAcquired());
                    test.assertFalse(mutex.isAcquiredByCurrentThread());

                    final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(1));
                    test.assertThrows(() -> mutex.acquire(timeout).await(),
                        new TimeoutException());

                    test.assertTrue(mutex.isAcquired());
                    test.assertGreaterThanOrEqualTo(clock.getCurrentDateTime(), timeout);
                });
            });

            runner.testGroup("tryAcquire()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertTrue(mutex.tryAcquire().await());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("when locked by same thread", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertNull(mutex.acquire().await());
                    test.assertFalse(mutex.tryAcquire().await());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("when locked by different thread",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                    (Test test, AsyncRunner parallelAsyncRunner) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertNull(mutex.acquire().await());

                    parallelAsyncRunner.schedule(() ->
                    {
                        test.assertFalse(mutex.tryAcquire().await());
                        test.assertTrue(mutex.isAcquired());
                    }).await();

                    test.assertTrue(mutex.isAcquired());
                });
            });

            runner.testGroup("release()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertThrows(() -> mutex.release().await(),
                        new PreConditionFailure("this.isAcquiredByCurrentThread() cannot be false."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("when locked", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    mutex.acquire().await();
                    test.assertTrue(mutex.isAcquired());
                    test.assertNull(mutex.release().await());
                    test.assertFalse(mutex.isAcquired());
                });
            });

            runner.testGroup("criticalSection(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertThrows(() -> mutex.criticalSection((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null action", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    final Value<Integer> value = IntegerValue.create();
                    mutex.criticalSection(() ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                    }).await();
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                });
            });

            runner.testGroup("criticalSection(Duration,Action0)", () ->
            {
                runner.test("with null Duration", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection((Duration)null, () -> { value.set(true); }),
                        new PreConditionFailure("durationTimeout cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with negative Duration", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection(Duration.seconds(-1), () -> { value.set(true); }),
                        new PreConditionFailure("durationTimeout (-1.0 Seconds) must be greater than 0.0 Seconds."));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with zero Duration", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection(Duration.zero, () -> { value.set(true); }),
                        new PreConditionFailure("durationTimeout (0.0 Seconds) must be greater than 0.0 Seconds."));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with positive duration when Mutex is not acquired",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    final Value<Boolean> value = Value.create(false);
                    test.assertNull(mutex.criticalSection(Duration.seconds(1), () -> { value.set(true); }).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with positive duration when Mutex is already acquired by the current thread",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    test.assertNull(mutex.acquire().await());

                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection(Duration.seconds(1), () -> { value.set(true); }).await(),
                        new TimeoutException());
                    test.assertTrue(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with positive duration when Mutex is already acquired by a different thread",
                    (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                    (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
                {
                    final Mutex mutex = creator.run(clock);
                    parallelAsyncRunner.schedule(() -> mutex.acquire().await()).await();
                    test.assertTrue(mutex.isAcquired());

                    final Duration timeout = Duration.seconds(1);

                    final Value<Boolean> value = Value.create(false);
                    final DateTime startTime = clock.getCurrentDateTime();
                    test.assertThrows(() -> mutex.criticalSection(timeout, () -> { value.set(true); }).await(),
                        new TimeoutException());
                    final DateTime endTime = clock.getCurrentDateTime();

                    test.assertTrue(mutex.isAcquired());
                    test.assertGreaterThanOrEqualTo(endTime.minus(startTime), timeout);
                    test.assertFalse(value.get());
                });
            });

            runner.testGroup("criticalSection(DateTime,Action0)", () ->
            {
                runner.test("with null DateTime", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection((DateTime)null, () -> { value.set(true); }),
                        new PreConditionFailure("dateTimeTimeout cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with DateTime in the past",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    final Value<Boolean> value = Value.create(false);
                    final DateTime timeout = clock.getCurrentDateTime().minus(Duration.seconds(1));
                    test.assertThrows(() -> mutex.criticalSection(timeout, () -> { value.set(true); }).await(),
                        new TimeoutException());
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with current DateTime",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    final Value<Boolean> value = Value.create(false);
                    final DateTime timeout = clock.getCurrentDateTime();
                    test.assertThrows(() -> mutex.criticalSection(timeout, () -> { value.set(true); }).await(),
                        new TimeoutException());
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with DateTime in the future when Mutex is not acquired",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    final Value<Boolean> value = Value.create(false);
                    final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(1));
                    test.assertNull(mutex.criticalSection(timeout, () -> { value.set(true); }).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with DateTime in the future when Mutex is already acquired by the current thread",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    mutex.acquire().await();

                    final Value<Boolean> value = Value.create(false);
                    final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(1));
                    test.assertThrows(() -> mutex.criticalSection(timeout, () -> { value.set(true); }).await(),
                        new TimeoutException());
                    test.assertTrue(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with DateTime in the future when Mutex is already acquired by a different thread",
                    (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                    (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
                {
                    final Mutex mutex = creator.run(clock);
                    parallelAsyncRunner.schedule(() -> mutex.acquire().await()).await();
                    test.assertTrue(mutex.isAcquired());
                    test.assertFalse(mutex.isAcquiredByCurrentThread());

                    final Duration timeoutDuration = Duration.seconds(0.1);

                    final Value<Boolean> value = Value.create(false);
                    final DateTime startTime = clock.getCurrentDateTime();
                    final DateTime timeout = startTime.plus(timeoutDuration);
                    test.assertThrows(() -> mutex.criticalSection(timeout, () -> { value.set(true); }).await(),
                        new TimeoutException());
                    final DateTime endTime = clock.getCurrentDateTime();
                    test.assertTrue(mutex.isAcquired());
                    test.assertGreaterThanOrEqualTo(endTime, timeout);
                    test.assertFalse(value.get());
                });
            });

            runner.testGroup("criticalSection(Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    test.assertThrows(new PreConditionFailure("function cannot be null."),
                        () -> mutex.criticalSection((Function0<Integer>)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Mutex mutex = creator.run(null);
                    final Value<Integer> value = Value.create();
                    test.assertTrue(mutex.criticalSection(() ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return true;
                    }).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                });
            });

            runner.testGroup("criticalSection(Duration,Function0<T>)", () ->
            {
                runner.test("with null function",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    test.assertThrows(() -> mutex.criticalSection(Duration.seconds(1), (Function0<Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    final Value<Integer> value = Value.create();
                    test.assertTrue(mutex.criticalSection(Duration.seconds(1), () ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return true;
                    }).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                });
            });

            runner.testGroup("criticalSection(DateTime,Function0<T>)", () ->
            {
                runner.test("with null function",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    test.assertThrows(() -> mutex.criticalSection(clock.getCurrentDateTime().plus(Duration.seconds(1)), (Function0<Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final Mutex mutex = creator.run(clock);
                    final Value<Integer> value = Value.create();
                    test.assertTrue(mutex.criticalSection(clock.getCurrentDateTime().plus(Duration.seconds(1)), () ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return true;
                    }).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                });
            });

            runner.testGroup(MutexCondition.class, () ->
            {
                runner.testGroup("await()", () ->
                {
                    runner.test("when Mutex is not acquired", (Test test) ->
                    {
                        final Mutex mutex = creator.run(null);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(condition::watch,
                            new PreConditionFailure("this.mutex.isAcquiredByCurrentThread() cannot be false."));
                    });

                    runner.test("when Mutex is not acquired by current thread",
                        (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                        (Test test, AsyncRunner parallelAsyncRunner) ->
                    {
                        final Mutex mutex = creator.run(null);
                        final MutexCondition condition = mutex.createCondition();

                        parallelAsyncRunner.schedule(() -> mutex.acquire().await()).await();
                        test.assertTrue(mutex.isAcquired());
                        test.assertFalse(mutex.isAcquiredByCurrentThread());

                        test.assertThrows(condition::watch,
                            new PreConditionFailure("this.mutex.isAcquiredByCurrentThread() cannot be false."));
                    });

                    final Action3<Integer,Integer,Integer> awaitTest = (Integer producerCount, Integer consumerCount, Integer valueCount) ->
                    {
                        runner.test("with " + producerCount + " producer" + (producerCount == 1 ? "" : "s") + " and " + consumerCount + " consumer" + (consumerCount == 1 ? "" : "s"),
                            (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                            (Test test, AsyncRunner parallelAsyncRunner) ->
                        {
                            final Mutex mutex = creator.run(null);
                            final List<Integer> values = List.create();
                            final IntegerValue removedValueCount = Value.create(0);
                            final MutexCondition listHasValues = mutex.createCondition();
                            final IntegerValue sum = Value.create(0);

                            final List<Result<Void>> tasks = List.create();
                            for (int i = 0; i < producerCount; ++i)
                            {
                                tasks.add(parallelAsyncRunner.schedule(() ->
                                {
                                    for (int j = 1; j <= valueCount; ++j)
                                    {
                                        mutex.acquire().await();
                                        try
                                        {
                                            values.add(j);
                                            listHasValues.signalAll();
                                        }
                                        finally
                                        {
                                            mutex.release().await();
                                        }
                                    }
                                }));
                            }

                            for (int i = 0; i < consumerCount; ++i)
                            {
                                tasks.add(parallelAsyncRunner.schedule(() ->
                                {
                                    while (true)
                                    {
                                        mutex.acquire().await();
                                        try
                                        {
                                            while (!values.any() && removedValueCount.get() != producerCount * valueCount)
                                            {
                                                listHasValues.watch().await();
                                            }

                                            if (removedValueCount.get() == producerCount * valueCount)
                                            {
                                                break;
                                            }
                                            else
                                            {
                                                final int value = values.removeFirst();
                                                removedValueCount.increment();
                                                sum.plusAssign(value);
                                            }
                                        }
                                        finally
                                        {
                                            mutex.release().await();
                                        }
                                    }
                                }));
                            }

                            Result.await(tasks);
                            test.assertEqual(producerCount * Math.summation(valueCount), sum.get());
                        });
                    };

                    awaitTest.run(1, 1, 1000);
                    awaitTest.run(2, 1, 1000);
                    awaitTest.run(5, 1, 1000);
                    awaitTest.run(1, 2, 1000);
                    awaitTest.run(1, 5, 1000);
                    awaitTest.run(2, 2, 1000);
                    awaitTest.run(5, 5, 1000);

                    final Action3<Integer,Integer,Integer> awaitTestWithCondition = (Integer producerCount, Integer consumerCount, Integer valueCount) ->
                    {
                        runner.test("with " + producerCount + " producer" + (producerCount == 1 ? "" : "s") + " and " + consumerCount + " consumer" + (consumerCount == 1 ? "" : "s"),
                            (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                            (Test test, AsyncRunner parallelAsyncRunner) ->
                        {
                            final Mutex mutex = creator.run(null);
                            final List<Integer> values = List.create();
                            final IntegerValue removedValueCount = Value.create(0);
                            final MutexCondition listHasValues = mutex.createCondition(() -> values.any() || removedValueCount.get() == producerCount * valueCount);
                            final IntegerValue sum = Value.create(0);

                            final List<Result<Void>> tasks = List.create();
                            for (int i = 0; i < producerCount; ++i)
                            {
                                tasks.add(parallelAsyncRunner.schedule(() ->
                                {
                                    for (int j = 1; j <= valueCount; ++j)
                                    {
                                        mutex.acquire().await();
                                        try
                                        {
                                            values.add(j);
                                            listHasValues.signalAll();
                                        }
                                        finally
                                        {
                                            mutex.release().await();
                                        }
                                    }
                                }));
                            }

                            for (int i = 0; i < consumerCount; ++i)
                            {
                                tasks.add(parallelAsyncRunner.schedule(() ->
                                {
                                    while (true)
                                    {
                                        mutex.acquire().await();
                                        try
                                        {
                                            listHasValues.watch().await();

                                            if (removedValueCount.get() == producerCount * valueCount)
                                            {
                                                break;
                                            }
                                            else
                                            {
                                                final int value = values.removeFirst();
                                                removedValueCount.increment();
                                                sum.plusAssign(value);
                                            }
                                        }
                                        finally
                                        {
                                            mutex.release().await();
                                        }
                                    }
                                }));
                            }

                            Result.await(tasks);
                            test.assertEqual(producerCount * Math.summation(valueCount), sum.get());
                        });
                    };

                    awaitTestWithCondition.run(1, 1, 1000);
                    awaitTestWithCondition.run(2, 1, 1000);
                    awaitTestWithCondition.run(5, 1, 1000);
                    awaitTestWithCondition.run(1, 2, 1000);
                    awaitTestWithCondition.run(1, 5, 1000);
                    awaitTestWithCondition.run(2, 2, 1000);
                    awaitTestWithCondition.run(5, 5, 1000);
                });

                runner.testGroup("await(Duration)", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final Mutex mutex = creator.run(null);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(() -> condition.watch((Duration)null),
                            new PreConditionFailure("timeout cannot be null."));
                    });

                    runner.test("with zero", (Test test) ->
                    {
                        final Mutex mutex = creator.run(null);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(() -> condition.watch(Duration.zero),
                            new PreConditionFailure("timeout (0.0 Seconds) must be greater than 0.0 Seconds."));
                    });

                    runner.test("when Mutex is not acquired",
                        (TestResources resources) -> Tuple.create(resources.getClock()),
                        (Test test, Clock clock) ->
                    {
                        final Mutex mutex = creator.run(clock);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(() -> condition.watch(Duration.seconds(1)),
                            new PreConditionFailure("this.mutex.isAcquiredByCurrentThread() cannot be false."));
                    });

                    runner.test("when Mutex is not acquired by current thread",
                        (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                        (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
                    {
                        final Mutex mutex = creator.run(clock);
                        final MutexCondition condition = mutex.createCondition();

                        parallelAsyncRunner.schedule(() -> mutex.acquire().await()).await();
                        test.assertTrue(mutex.isAcquired());
                        test.assertFalse(mutex.isAcquiredByCurrentThread());

                        test.assertThrows(() -> condition.watch(Duration.seconds(1)),
                            new PreConditionFailure("this.mutex.isAcquiredByCurrentThread() cannot be false."));
                    });

                    runner.test("when Mutex doesn't have a Clock", (Test test) ->
                    {
                        final Mutex mutex = creator.run(null);
                        final MutexCondition condition = mutex.createCondition();

                        mutex.acquire().await();
                        test.assertThrows(() -> condition.watch(Duration.seconds(1)),
                            new PreConditionFailure("this.clock cannot be null."));
                    });

                    runner.test("when timeout expires",
                        (TestResources resources) -> Tuple.create(resources.getClock()),
                        (Test test, Clock clock) ->
                    {
                        final Mutex mutex = creator.run(clock);
                        final MutexCondition condition = mutex.createCondition();

                        mutex.acquire().await();
                        test.assertThrows(() -> condition.watch(Duration.seconds(0.01)).await(),
                            new TimeoutException());
                    });

                    final Action3<Integer,Integer,Integer> awaitTest = (Integer producerCount, Integer consumerCount, Integer valueCount) ->
                    {
                        runner.test("with " + producerCount + " producer" + (producerCount == 1 ? "" : "s") + " and " + consumerCount + " consumer" + (consumerCount == 1 ? "" : "s"),
                            (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                            (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
                        {
                            final Mutex mutex = creator.run(clock);
                            final MutexCondition listHasValues = mutex.createCondition();
                            final List<Integer> values = List.create();
                            final IntegerValue sum = Value.create(0);

                            final List<Result<Void>> tasks = List.create();
                            for (int i = 0; i < producerCount; ++i)
                            {
                                tasks.add(parallelAsyncRunner.schedule(() ->
                                {
                                    for (int j = 1; j <= valueCount; ++j)
                                    {
                                        mutex.acquire().await();
                                        try
                                        {
                                            values.add(j);
                                            listHasValues.signalAll();
                                        }
                                        finally
                                        {
                                            mutex.release().await();
                                        }
                                    }
                                }));
                            }

                            final IntegerValue removedValueCount = IntegerValue.create(0);
                            for (int i = 0; i < consumerCount; ++i)
                            {
                                tasks.add(parallelAsyncRunner.schedule(() ->
                                {
                                    while (true)
                                    {
                                        mutex.acquire().await();
                                        try
                                        {
                                            while (!values.any() && removedValueCount.get() != producerCount * valueCount)
                                            {
                                                listHasValues.watch(Duration.seconds(0.1)).await();
                                            }

                                            if (removedValueCount.get() == producerCount * valueCount)
                                            {
                                                break;
                                            }
                                            else
                                            {
                                                final int value = values.removeFirst();
                                                removedValueCount.increment();
                                                sum.plusAssign(value);
                                            }
                                        }
                                        finally
                                        {
                                            mutex.release().await();
                                        }
                                    }
                                }));
                            }

                            Result.await(tasks);
                            test.assertEqual(producerCount * Math.summation(valueCount), sum.get());
                        });
                    };

                    awaitTest.run(1, 1, 1000);
                    awaitTest.run(2, 1, 1000);
                    awaitTest.run(5, 1, 1000);
                    awaitTest.run(1, 2, 1000);
                    awaitTest.run(1, 5, 1000);
                    awaitTest.run(2, 2, 1000);
                    awaitTest.run(5, 5, 1000);
                });

                runner.testGroup("await(DateTime)", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final Mutex mutex = creator.run(null);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(() -> condition.watch((DateTime)null),
                            new PreConditionFailure("timeout cannot be null."));
                    });

                    runner.test("when Mutex is not acquired",
                        (TestResources resources) -> Tuple.create(resources.getClock()),
                        (Test test, Clock clock) ->
                    {
                        final Mutex mutex = creator.run(clock);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(() -> condition.watch(clock.getCurrentDateTime()),
                            new PreConditionFailure("this.mutex.isAcquiredByCurrentThread() cannot be false."));
                    });

                    runner.test("when Mutex is not acquired by current thread",
                        (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                        (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
                    {
                        final Mutex mutex = creator.run(clock);
                        final MutexCondition condition = mutex.createCondition();

                        parallelAsyncRunner.schedule(() -> mutex.acquire().await()).await();
                        test.assertTrue(mutex.isAcquired());
                        test.assertFalse(mutex.isAcquiredByCurrentThread());

                        test.assertThrows(() -> condition.watch(clock.getCurrentDateTime()),
                            new PreConditionFailure("this.mutex.isAcquiredByCurrentThread() cannot be false."));
                    });

                    runner.test("when Mutex doesn't have a Clock",
                        (TestResources resources) -> Tuple.create(resources.getClock()),
                        (Test test, Clock clock) ->
                    {
                        final Mutex mutex = creator.run(null);
                        final MutexCondition condition = mutex.createCondition();

                        mutex.acquire().await();
                        test.assertThrows(() -> condition.watch(clock.getCurrentDateTime()),
                            new PreConditionFailure("this.clock cannot be null."));
                    });

                    final Action3<Integer,Integer,Integer> awaitTest = (Integer producerCount, Integer consumerCount, Integer valueCount) ->
                    {
                        runner.test("with " + producerCount + " producer" + (producerCount == 1 ? "" : "s") + " and " + consumerCount + " consumer" + (consumerCount == 1 ? "" : "s"),
                            (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                            (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
                        {
                            final Mutex mutex = creator.run(clock);
                            final MutexCondition listHasValues = mutex.createCondition();
                            final List<Integer> values = List.create();
                            final IntegerValue sum = Value.create(0);

                            final List<Result<Void>> tasks = List.create();
                            for (int i = 0; i < producerCount; ++i)
                            {
                                tasks.add(parallelAsyncRunner.schedule(() ->
                                {
                                    for (int j = 1; j <= valueCount; ++j)
                                    {
                                        mutex.acquire().await();
                                        try
                                        {
                                            values.add(j);
                                            listHasValues.signalAll();
                                        }
                                        finally
                                        {
                                            mutex.release().await();
                                        }
                                    }
                                }));
                            }

                            final IntegerValue removedValueCount = Value.create(0);
                            for (int i = 0; i < consumerCount; ++i)
                            {
                                tasks.add(parallelAsyncRunner.schedule(() ->
                                {
                                    while (true)
                                    {
                                        mutex.acquire().await();
                                        try
                                        {
                                            while (!values.any() && removedValueCount.get() != producerCount * valueCount)
                                            {
                                                final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(0.1));
                                                listHasValues.watch(timeout).await();
                                            }

                                            if (removedValueCount.get() == producerCount * valueCount)
                                            {
                                                break;
                                            }
                                            else
                                            {
                                                final int value = values.removeFirst();
                                                removedValueCount.increment();
                                                sum.plusAssign(value);
                                            }
                                        }
                                        finally
                                        {
                                            mutex.release().await();
                                        }
                                    }
                                }));
                            }

                            Result.await(tasks);
                            test.assertEqual(producerCount * Math.summation(valueCount), sum.get());
                        });
                    };

                    awaitTest.run(1, 1, 1000);
                    awaitTest.run(2, 1, 1000);
                    awaitTest.run(5, 1, 1000);
                    awaitTest.run(1, 2, 1000);
                    awaitTest.run(1, 5, 1000);
                    awaitTest.run(2, 2, 1000);
                    awaitTest.run(5, 5, 1000);
                });

                runner.testGroup("signalAll()", () ->
                {
                    runner.test("when Mutex is not acquired", (Test test) ->
                    {
                        final Mutex mutex = creator.run(null);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(() -> condition.signalAll(),
                            new PreConditionFailure("this.mutex.isAcquiredByCurrentThread() cannot be false."));
                    });

                    runner.test("when Mutex is not acquired by current thread",
                        (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                        (Test test, AsyncRunner parallelAsyncRunner) ->
                    {
                        final Mutex mutex = creator.run(null);
                        final MutexCondition condition = mutex.createCondition();

                        parallelAsyncRunner.schedule(() -> mutex.acquire().await()).await();
                        test.assertTrue(mutex.isAcquired());
                        test.assertFalse(mutex.isAcquiredByCurrentThread());

                        test.assertThrows(() -> condition.signalAll(),
                            new PreConditionFailure("this.mutex.isAcquiredByCurrentThread() cannot be false."));
                    });
                });
            });
        });
    }
}
