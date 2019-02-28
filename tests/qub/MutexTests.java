package qub;

public class MutexTests
{
    public static void test(TestRunner runner, Function1<Clock,Mutex> creator)
    {
        runner.testGroup(Mutex.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final Mutex mutex = create(creator);
                test.assertFalse(mutex.isAcquired());
            });

            runner.testGroup("acquire()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    mutex.acquire();
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("when locked by this thread", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    mutex.acquire();
                    test.assertTrue(mutex.isAcquired());

                    mutex.acquire();
                    test.assertTrue(mutex.isAcquired());
                });
            });

            runner.testGroup("acquire(Duration)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.acquire((Duration)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with zero", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.acquire(Duration.zero));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with null Clock", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertThrows(() -> mutex.acquire(Duration.minutes(5)));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with positive duration when Mutex is not acquired", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertSuccess(true, mutex.acquire(Duration.seconds(5)));
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("with positive duration when Mutex is already acquired by the current thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertSuccess(true, mutex.acquire());
                    test.assertSuccess(true, mutex.acquire(Duration.seconds(5)));
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("with positive duration when Mutex is already acquired by a different thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Clock clock = mutex.getClock();
                    test.getParallelAsyncRunner().schedule(() -> mutex.acquire()).await();
                    test.assertTrue(mutex.isAcquired());

                    final Duration timeout = Duration.seconds(1);

                    final DateTime startTime = clock.getCurrentDateTime();
                    test.assertError(new TimeoutException(), mutex.acquire(timeout));
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
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.acquire((DateTime)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with null Clock", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertThrows(() -> mutex.acquire(DateTime.date(2018, 1, 1)));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with timeout before current time", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Clock clock = mutex.getClock();
                    final DateTime timeout = clock.getCurrentDateTime().minus(Duration.milliseconds(10));
                    test.assertError(new TimeoutException(), mutex.acquire(timeout));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with timeout at current time", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Clock clock = mutex.getClock();
                    final DateTime timeout = clock.getCurrentDateTime();
                    test.assertError(new TimeoutException(), mutex.acquire(timeout));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with timeout after current time when mutex is not acquired", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Clock clock = mutex.getClock();
                    final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(1));
                    test.assertSuccess(true, mutex.acquire(timeout));
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("with timeout after current time when Mutex is already acquired by a different thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Clock clock = mutex.getClock();
                    test.getParallelAsyncRunner().schedule(() -> mutex.acquire()).await();
                    test.assertTrue(mutex.isAcquired());

                    final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(1));
                    test.assertError(new TimeoutException(), mutex.acquire(timeout));

                    test.assertTrue(mutex.isAcquired());
                    test.assertGreaterThanOrEqualTo(clock.getCurrentDateTime(), timeout);
                });
            });

            runner.testGroup("tryAcquire()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertTrue(mutex.tryAcquire());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("when locked by same thread", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    mutex.acquire();
                    test.assertTrue(mutex.tryAcquire());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("when locked by different thread", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    mutex.acquire();

                    test.getParallelAsyncRunner().schedule(() ->
                    {
                        test.assertFalse(mutex.tryAcquire());
                        test.assertTrue(mutex.isAcquired());
                    }).await();

                    test.assertTrue(mutex.isAcquired());
                });
            });

            runner.testGroup("release()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    mutex.release();
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("when locked", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    mutex.acquire();
                    test.assertTrue(mutex.isAcquired());
                    test.assertTrue(mutex.release());
                    test.assertFalse(mutex.isAcquired());
                });
            });

            runner.testGroup("criticalSection(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertThrows(() -> mutex.criticalSection((Action0)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null action", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    final Value<Integer> value = IntegerValue.create();
                    mutex.criticalSection(() ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                    });
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                });
            });

            runner.testGroup("criticalSection(Duration,Action0)", () ->
            {
                runner.test("with null Duration", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection((Duration)null, () -> value.set(true)));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with negative Duration", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection(Duration.seconds(-1), () -> value.set(true)));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with zero Duration", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection(Duration.zero, () -> value.set(true)));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with positive duration when Mutex is not acquired", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertSuccess(true, mutex.criticalSection(Duration.seconds(1), () -> value.set(true)));
                    test.assertFalse(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with positive duration when Mutex is already acquired by the current thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    mutex.acquire();

                    final Value<Boolean> value = Value.create(false);
                    test.assertSuccess(true, mutex.criticalSection(Duration.seconds(1), () -> value.set(true)));
                    test.assertTrue(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with positive duration when Mutex is already acquired by a different thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Clock clock = mutex.getClock();
                    test.getParallelAsyncRunner().schedule(() -> mutex.acquire()).await();
                    test.assertTrue(mutex.isAcquired());

                    final Duration timeout = Duration.seconds(1);

                    final Value<Boolean> value = Value.create(false);
                    final DateTime startTime = clock.getCurrentDateTime();
                    test.assertError(new TimeoutException(), mutex.criticalSection(timeout, () -> value.set(true)));
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
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection((DateTime)null, () -> value.set(true)));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with DateTime in the past", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    final Result<Boolean> result = mutex.criticalSection(test.getClock().getCurrentDateTime().minus(Duration.seconds(1)), () -> value.set(true));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                    test.assertError(new TimeoutException(), result);
                });

                runner.test("with current DateTime", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    final Result<Boolean> result = mutex.criticalSection(test.getClock().getCurrentDateTime(), () -> value.set(true));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                    test.assertError(new TimeoutException(), result);
                });

                runner.test("with DateTime in the future when Mutex is not acquired", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertSuccess(true, mutex.criticalSection(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), () -> value.set(true)));
                    test.assertFalse(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with DateTime in the future when Mutex is already acquired by the current thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    mutex.acquire();

                    final Value<Boolean> value = Value.create(false);
                    test.assertSuccess(true, mutex.criticalSection(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), () -> value.set(true)));
                    test.assertTrue(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with DateTime in the future when Mutex is already acquired by a different thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Clock clock = mutex.getClock();
                    test.getParallelAsyncRunner().schedule(() -> mutex.acquire()).await();
                    test.assertTrue(mutex.isAcquired());

                    final Duration timeoutDuration = Duration.seconds(0.1);

                    final Value<Boolean> value = Value.create(false);
                    final DateTime startTime = clock.getCurrentDateTime();
                    final DateTime timeout = startTime.plus(timeoutDuration);
                    test.assertError(new TimeoutException(), mutex.criticalSection(startTime.plus(timeoutDuration), () -> value.set(true)));
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
                    final Mutex mutex = create(creator);
                    test.assertThrows(() -> mutex.criticalSection((Function0<Integer>)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    final Value<Integer> value = Value.create();
                    final Boolean result = mutex.criticalSection(() ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return true;
                    });
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                    test.assertTrue(result);
                });
            });

            runner.testGroup("criticalSection(Duration,Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.criticalSection(Duration.seconds(1), (Function0<Integer>)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Integer> value = Value.create();
                    final Result<Boolean> result = mutex.criticalSection(Duration.seconds(1), () ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return true;
                    });
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                    test.assertSuccess(true, result);
                });
            });

            runner.testGroup("criticalSection(DateTime,Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.criticalSection(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), (Function0<Integer>)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Integer> value = Value.create();
                    final Result<Boolean> result = mutex.criticalSection(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), () ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return true;
                    });
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                    test.assertSuccess(true, result);
                });
            });

            runner.testGroup("criticalSectionResult(Duration,Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.criticalSectionResult(Duration.seconds(1), (Function0<Result<Integer>>)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Integer> value = Value.create();
                    final Result<Boolean> result = mutex.criticalSectionResult(Duration.seconds(1), () ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return Result.successTrue();
                    });
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                    test.assertSuccess(true, result);
                });
            });

            runner.testGroup("criticalSectionResult(DateTime,Function0<Result<T>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.criticalSectionResult(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), (Function0<Result<Integer>>)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Integer> value = Value.create();
                    final Result<Boolean> result = mutex.criticalSectionResult(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), () ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return Result.successTrue();
                    });
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                    test.assertSuccess(true, result);
                });

                runner.test("with null DateTime", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSectionResult((DateTime)null, () ->
                    {
                        value.set(true);
                        return Result.successTrue();
                    }));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with DateTime in the past", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    final Result<Boolean> result = mutex.criticalSectionResult(test.getClock().getCurrentDateTime().minus(Duration.seconds(1)), () ->
                    {
                        value.set(true);
                        return Result.successTrue();
                    });
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                    test.assertError(new TimeoutException(), result);
                });

                runner.test("with current DateTime", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    final Result<Boolean> result = mutex.criticalSectionResult(test.getClock().getCurrentDateTime(), () ->
                    {
                        value.set(true);
                        return Result.successTrue();
                    });
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                    test.assertError(new TimeoutException(), result);
                });

                runner.test("with DateTime in the future when Mutex is not acquired", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertSuccess(true, mutex.criticalSectionResult(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), () ->
                    {
                        value.set(true);
                        return Result.successTrue();
                    }));
                    test.assertFalse(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with DateTime in the future when Mutex is not acquired and function throws an exception", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertError(new RuntimeException("Oops!"), mutex.criticalSectionResult(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), () ->
                    {
                        throw new RuntimeException("Oops!");
                    }));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with DateTime in the future when Mutex is already acquired by the current thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    mutex.acquire();

                    final Value<Boolean> value = Value.create(false);
                    test.assertSuccess(true, mutex.criticalSectionResult(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), () ->
                    {
                        value.set(true);
                        return Result.successTrue();
                    }));
                    test.assertTrue(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with DateTime in the future when Mutex is already acquired by a different thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Clock clock = mutex.getClock();
                    test.getParallelAsyncRunner().schedule(() -> mutex.acquire()).await();
                    test.assertTrue(mutex.isAcquired());

                    final Duration timeoutDuration = Duration.seconds(0.1);

                    final Value<Boolean> value = Value.create(false);
                    final DateTime startTime = clock.getCurrentDateTime();
                    final DateTime timeout = startTime.plus(timeoutDuration);
                    test.assertError(new TimeoutException(), mutex.criticalSectionResult(startTime.plus(timeoutDuration), () ->
                    {
                        value.set(true);
                        return Result.successTrue();
                    }));
                    final DateTime endTime = clock.getCurrentDateTime();
                    test.assertTrue(mutex.isAcquired());
                    test.assertGreaterThanOrEqualTo(endTime, timeout);
                    test.assertFalse(value.get());
                });
            });

            runner.testGroup(MutexCondition.class, () ->
            {
                runner.testGroup("await(Duration)", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final Mutex mutex = create(creator, test);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(() -> condition.await((Duration)null));
                    });
                });

                runner.test("await() and signalAll()", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    final MutexCondition condition = mutex.createCondition();
                    final List<Integer> valueList = new ArrayList<>();
                    final int count = 10;

                    final AsyncRunner parallelAsyncRunner = test.getParallelAsyncRunner();

                    final List<AsyncTask> producers = new ArrayList<>();
                    for (int i = 0; i < count; ++i)
                    {
                        final int currentValue = i;
                        producers.add(parallelAsyncRunner.schedule(() ->
                        {
                            mutex.criticalSection(() ->
                            {
                                valueList.add(currentValue);
                                condition.signalAll();
                            });
                        }));
                    }

                    final List<Integer> resultList = new ArrayList<>();
                    mutex.criticalSection(() ->
                    {
                        while (resultList.getCount() < count)
                        {
                            while (!valueList.any())
                            {
                                condition.await();
                            }

                            resultList.add(valueList.removeFirst());
                        }
                    });

                    for (int i = 0; i < count; ++i)
                    {
                        test.assertTrue(resultList.contains(i), "Result list " + resultList + " does not contain " + i + ".");
                    }
                    test.assertEqual(count, resultList.getCount());

                    parallelAsyncRunner.awaitAll(producers);
                });
            });
        });
    }

    private static Mutex create(Function1<Clock,Mutex> creator)
    {
        PreCondition.assertNotNull(creator, "creator");

        return create(creator, (Clock)null);
    }

    private static Mutex create(Function1<Clock,Mutex> creator, Test test)
    {
        PreCondition.assertNotNull(creator, "creator");
        PreCondition.assertNotNull(test, "test");

        return create(creator, test.getClock());
    }

    private static Mutex create(Function1<Clock,Mutex> creator, Clock clock)
    {
        PreCondition.assertNotNull(creator, "creator");

        return creator.run(clock);
    }
}
