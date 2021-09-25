package qub;

public interface RetryTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Retry.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final Retry retry = Retry.create();
                test.assertNotNull(retry);
            });

            runner.testGroup("setShouldRetryFunction(int)", () ->
            {
                final Action2<Integer,Throwable> setShouldRetryFunctionErrorTest = (Integer maximumAttempts, Throwable expected) ->
                {
                    runner.test("with " + maximumAttempts, (Test test) ->
                    {
                        final Retry retry = Retry.create();
                        test.assertThrows(() -> retry.setShouldRetryFunction(maximumAttempts),
                            expected);
                    });
                };

                setShouldRetryFunctionErrorTest.run(-1, new PreConditionFailure("maximumAttempts (-1) must be greater than or equal to 1."));
                setShouldRetryFunctionErrorTest.run(0, new PreConditionFailure("maximumAttempts (0) must be greater than or equal to 1."));

                final Action1<Integer> setShouldRetryFunctionTest = (Integer maximumAttempts) ->
                {
                    runner.test("with " + maximumAttempts, (Test test) ->
                    {
                        final Retry retry = Retry.create();
                        final Retry setShouldRetryFunctionResult = retry.setShouldRetryFunction(maximumAttempts);
                        test.assertSame(retry, setShouldRetryFunctionResult);

                        final IntegerValue attempts = IntegerValue.create(0);
                        final Result<Void> retryRunResult = retry.run(() ->
                        {
                            attempts.increment();
                            throw new RuntimeException(attempts.toString());
                        });

                        Throwable error;
                        Throwable expectedError;
                        if (maximumAttempts == 1)
                        {
                            error = test.assertThrows(RuntimeException.class, retryRunResult::await);

                            expectedError = new RuntimeException("1");
                        }
                        else
                        {
                            error = test.assertThrows(ErrorIterable.class, retryRunResult::await);

                            final List<Throwable> expectedErrors = List.create();
                            for (int i = 1; i <= maximumAttempts; i++)
                            {
                                expectedErrors.add(new RuntimeException(Integers.toString(i)));
                            }
                            expectedError = ErrorIterable.create(expectedErrors);
                        }
                        test.assertEqual(maximumAttempts, attempts.get());
                        test.assertEqual(expectedError, error);
                    });
                };

                for (int i = 1; i <= 4; i++)
                {
                    setShouldRetryFunctionTest.run(i);
                }
            });

            runner.testGroup("setShouldRetryFunction(int,Action0)", () ->
            {
                final Action3<Integer,Action0,Throwable> setShouldRetryFunctionErrorTest = (Integer maximumAttempts, Action0 betweenAttemptsAction, Throwable expected) ->
                {
                    runner.test("with " + English.andList(maximumAttempts, (betweenAttemptsAction == null ? "null" : "non-null")), (Test test) ->
                    {
                        final Retry retry = Retry.create();
                        test.assertThrows(() -> retry.setShouldRetryFunction(maximumAttempts, betweenAttemptsAction),
                            expected);
                    });
                };

                setShouldRetryFunctionErrorTest.run(-1, Action0.empty, new PreConditionFailure("maximumAttempts (-1) must be greater than or equal to 1."));
                setShouldRetryFunctionErrorTest.run(0, Action0.empty, new PreConditionFailure("maximumAttempts (0) must be greater than or equal to 1."));
                setShouldRetryFunctionErrorTest.run(1, null, new PreConditionFailure("betweenAttemptsAction cannot be null."));

                final Action1<Integer> setShouldRetryFunctionTest = (Integer maximumAttempts) ->
                {
                    runner.test("with " + maximumAttempts, (Test test) ->
                    {
                        final Retry retry = Retry.create();
                        final IntegerValue betweenAttemptsCounter = IntegerValue.create(0);
                        final Retry setShouldRetryFunctionResult = retry.setShouldRetryFunction(maximumAttempts, betweenAttemptsCounter::increment);
                        test.assertSame(retry, setShouldRetryFunctionResult);

                        final IntegerValue attempts = IntegerValue.create(0);
                        final Result<Void> retryRunResult = retry.run(() ->
                        {
                            attempts.increment();
                            throw new RuntimeException(attempts.toString());
                        });

                        Throwable error;
                        Throwable expectedError;
                        if (maximumAttempts == 1)
                        {
                            error = test.assertThrows(RuntimeException.class, retryRunResult::await);

                            expectedError = new RuntimeException("1");
                        }
                        else
                        {
                            error = test.assertThrows(ErrorIterable.class, retryRunResult::await);

                            final List<Throwable> expectedErrors = List.create();
                            for (int i = 1; i <= maximumAttempts; i++)
                            {
                                expectedErrors.add(new RuntimeException(Integers.toString(i)));
                            }
                            expectedError = ErrorIterable.create(expectedErrors);
                        }
                        test.assertEqual(maximumAttempts, attempts.get());
                        test.assertEqual(maximumAttempts - 1, betweenAttemptsCounter.get());
                        test.assertEqual(expectedError, error);
                    });
                };

                for (int i = 1; i <= 4; i++)
                {
                    setShouldRetryFunctionTest.run(i);
                }
            });

            runner.testGroup("setShouldRetryFunction(int,Action1<Integer>)", () ->
            {
                final Action3<Integer,Action1<Integer>,Throwable> setShouldRetryFunctionErrorTest = (Integer maximumAttempts, Action1<Integer> betweenAttemptsAction, Throwable expected) ->
                {
                    runner.test("with " + English.andList(maximumAttempts, (betweenAttemptsAction == null ? "null" : "non-null")), (Test test) ->
                    {
                        final Retry retry = Retry.create();
                        test.assertThrows(() -> retry.setShouldRetryFunction(maximumAttempts, betweenAttemptsAction),
                            expected);
                    });
                };

                setShouldRetryFunctionErrorTest.run(-1, (Integer failedAttempts) -> {}, new PreConditionFailure("maximumAttempts (-1) must be greater than or equal to 1."));
                setShouldRetryFunctionErrorTest.run(0, (Integer failedAttempts) -> {}, new PreConditionFailure("maximumAttempts (0) must be greater than or equal to 1."));
                setShouldRetryFunctionErrorTest.run(1, null, new PreConditionFailure("betweenAttemptsAction cannot be null."));

                final Action1<Integer> setShouldRetryFunctionTest = (Integer maximumAttempts) ->
                {
                    runner.test("with " + maximumAttempts, (Test test) ->
                    {
                        final Retry retry = Retry.create();
                        final List<Integer> betweenAttemptsList = List.create();
                        final Retry setShouldRetryFunctionResult = retry.setShouldRetryFunction(maximumAttempts, betweenAttemptsList::add);
                        test.assertSame(retry, setShouldRetryFunctionResult);

                        final IntegerValue attempts = IntegerValue.create(0);
                        final Result<Void> retryRunResult = retry.run(() ->
                        {
                            attempts.increment();
                            throw new RuntimeException(attempts.toString());
                        });

                        Throwable error;
                        Throwable expectedError;
                        final List<Integer> expectedBetweenAttemptsList = List.create();
                        if (maximumAttempts == 1)
                        {
                            error = test.assertThrows(RuntimeException.class, retryRunResult::await);

                            expectedError = new RuntimeException("1");
                        }
                        else
                        {
                            error = test.assertThrows(ErrorIterable.class, retryRunResult::await);

                            for (int i = 1; i <= maximumAttempts - 1; i++)
                            {
                                expectedBetweenAttemptsList.add(i);
                            }

                            final List<Throwable> expectedErrors = List.create();
                            for (int i = 1; i <= maximumAttempts; i++)
                            {
                                expectedErrors.add(new RuntimeException(Integers.toString(i)));
                            }
                            expectedError = ErrorIterable.create(expectedErrors);
                        }
                        test.assertEqual(maximumAttempts, attempts.get());
                        test.assertEqual(expectedBetweenAttemptsList, betweenAttemptsList);
                        test.assertEqual(expectedError, error);
                    });
                };

                for (int i = 1; i <= 4; i++)
                {
                    setShouldRetryFunctionTest.run(i);
                }
            });

            runner.testGroup("setShouldRetryFunction(int,Action2<Integer,Throwable>)", () ->
            {
                final Action3<Integer,Action2<Integer,Throwable>,Throwable> setShouldRetryFunctionErrorTest = (Integer maximumAttempts, Action2<Integer,Throwable> betweenAttemptsAction, Throwable expected) ->
                {
                    runner.test("with " + English.andList(maximumAttempts, (betweenAttemptsAction == null ? "null" : "non-null")), (Test test) ->
                    {
                        final Retry retry = Retry.create();
                        test.assertThrows(() -> retry.setShouldRetryFunction(maximumAttempts, betweenAttemptsAction),
                            expected);
                    });
                };

                setShouldRetryFunctionErrorTest.run(-1, (Integer failedAttempts, Throwable error) -> {}, new PreConditionFailure("maximumAttempts (-1) must be greater than or equal to 1."));
                setShouldRetryFunctionErrorTest.run(0, (Integer failedAttempts, Throwable error) -> {}, new PreConditionFailure("maximumAttempts (0) must be greater than or equal to 1."));
                setShouldRetryFunctionErrorTest.run(1, null, new PreConditionFailure("betweenAttemptsAction cannot be null."));

                final Action1<Integer> setShouldRetryFunctionTest = (Integer maximumAttempts) ->
                {
                    runner.test("with " + maximumAttempts, (Test test) ->
                    {
                        final Retry retry = Retry.create();
                        final List<Integer> betweenAttemptsList = List.create();
                        final List<Throwable> betweenAttemptsErrorList = List.create();
                        final Retry setShouldRetryFunctionResult = retry.setShouldRetryFunction(maximumAttempts, (Integer failedAttempts, Throwable error) ->
                        {
                            betweenAttemptsList.add(failedAttempts);
                            betweenAttemptsErrorList.add(error);
                        });
                        test.assertSame(retry, setShouldRetryFunctionResult);

                        final IntegerValue attempts = IntegerValue.create(0);
                        final Result<Void> retryRunResult = retry.run(() ->
                        {
                            attempts.increment();
                            throw new RuntimeException(attempts.toString());
                        });

                        Throwable error;
                        Throwable expectedError;
                        final List<Integer> expectedBetweenAttemptsList = List.create();
                        final List<Throwable> expectedBetweenAttemptsErrorList = List.create();
                        if (maximumAttempts == 1)
                        {
                            error = test.assertThrows(RuntimeException.class, retryRunResult::await);

                            expectedError = new RuntimeException("1");
                        }
                        else
                        {
                            error = test.assertThrows(ErrorIterable.class, retryRunResult::await);

                            final List<Throwable> expectedErrors = List.create();
                            for (int i = 1; i <= maximumAttempts; i++)
                            {
                                if (i < maximumAttempts)
                                {
                                    expectedBetweenAttemptsList.add(i);
                                    expectedBetweenAttemptsErrorList.add(new RuntimeException(Integers.toString(i)));
                                }
                                expectedErrors.add(new RuntimeException(Integers.toString(i)));
                            }
                            expectedError = ErrorIterable.create(expectedErrors);
                        }
                        test.assertEqual(maximumAttempts, attempts.get());
                        test.assertEqual(expectedBetweenAttemptsList, betweenAttemptsList);
                        test.assertEqual(expectedBetweenAttemptsErrorList, betweenAttemptsErrorList);
                        test.assertEqual(expectedError, error);
                    });
                };

                for (int i = 1; i <= 4; i++)
                {
                    setShouldRetryFunctionTest.run(i);
                }
            });

            runner.testGroup("setShouldRetryFunction(Function0<Boolean>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Retry retry = Retry.create();
                    test.assertThrows(() -> retry.setShouldRetryFunction((Function0<Boolean>)null),
                        new PreConditionFailure("shouldRetryFunction cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Retry retry = Retry.create();
                    final IntegerValue shouldRetryCounter = IntegerValue.create(0);
                    final Retry setBetweenAttemptsActionResult = retry.setShouldRetryFunction(() ->
                    {
                        shouldRetryCounter.increment();
                        return false;
                    });
                    test.assertSame(retry, setBetweenAttemptsActionResult);

                    final IntegerValue attemptCounter = IntegerValue.create(0);
                    final RuntimeException exception = test.assertThrows(RuntimeException.class, () -> retry.run(() ->
                    {
                        attemptCounter.increment();
                        throw new RuntimeException(attemptCounter.toString());
                    }).await());

                    test.assertEqual(1, shouldRetryCounter.get());
                    test.assertEqual(1, attemptCounter.get());
                    test.assertEqual(new RuntimeException("1"), exception);
                });
            });

            runner.testGroup("setShouldRetryFunction(Function1<Integer,Boolean>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Retry retry = Retry.create();
                    test.assertThrows(() -> retry.setShouldRetryFunction((Function1<Integer,Boolean>)null),
                        new PreConditionFailure("shouldRetryFunction cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Retry retry = Retry.create();
                    final List<Integer> shouldRetryAttemptList = List.create();
                    final Retry setBetweenAttemptsActionResult = retry.setShouldRetryFunction((Integer failedAttempts) ->
                    {
                        shouldRetryAttemptList.add(failedAttempts);
                        return failedAttempts < 2;
                    });
                    test.assertSame(retry, setBetweenAttemptsActionResult);

                    final IntegerValue attemptCounter = IntegerValue.create(0);
                    final ErrorIterable exception = test.assertThrows(ErrorIterable.class, () -> retry.run(() ->
                    {
                        attemptCounter.increment();
                        throw new RuntimeException(attemptCounter.toString());
                    }).await());

                    test.assertEqual(Iterable.create(1, 2), shouldRetryAttemptList);
                    test.assertEqual(2, attemptCounter.get());
                    test.assertEqual(
                        ErrorIterable.create(
                            new RuntimeException("1"),
                            new RuntimeException("2")),
                        exception);
                });
            });

            runner.testGroup("setShouldRetryFunction(Function2<Integer,Throwable,Boolean>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Retry retry = Retry.create();
                    test.assertThrows(() -> retry.setShouldRetryFunction((Function2<Integer,Throwable,Boolean>)null),
                        new PreConditionFailure("shouldRetryFunction cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Retry retry = Retry.create();
                    final List<Integer> shouldRetryAttemptList = List.create();
                    final List<Throwable> shouldRetryErrorList = List.create();
                    final Retry setBetweenAttemptsActionResult = retry.setShouldRetryFunction((Integer failedAttempts, Throwable error) ->
                    {
                        shouldRetryAttemptList.add(failedAttempts);
                        shouldRetryErrorList.add(error);
                        return failedAttempts < 4;
                    });
                    test.assertSame(retry, setBetweenAttemptsActionResult);

                    final IntegerValue attemptCounter = IntegerValue.create(0);
                    final ErrorIterable exception = test.assertThrows(ErrorIterable.class, () -> retry.run(() ->
                    {
                        attemptCounter.increment();
                        throw new RuntimeException(attemptCounter.toString());
                    }).await());

                    test.assertEqual(Iterable.create(1, 2, 3, 4), shouldRetryAttemptList);
                    test.assertEqual(
                        Iterable.create(
                            new RuntimeException("1"),
                            new RuntimeException("2"),
                            new RuntimeException("3"),
                            new RuntimeException("4")),
                        shouldRetryErrorList);
                    test.assertEqual(4, attemptCounter.get());
                    test.assertEqual(
                        ErrorIterable.create(
                            new RuntimeException("1"),
                            new RuntimeException("2"),
                            new RuntimeException("3"),
                            new RuntimeException("4")),
                        exception);
                });
            });

            runner.testGroup("run(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Retry retry = Retry.create();
                    test.assertThrows(() -> retry.run((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with an action that doesn't throw", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Retry retry = Retry.create();
                    final Result<Void> result = retry.run(() -> { value.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(0, value.get());
                    test.assertFalse(result.isCompleted());

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertNull(result.await());
                        test.assertEqual(1, value.get());
                        test.assertTrue(result.isCompleted());
                    }
                });

                runner.test("with an action that throws on the first attempt", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Retry retry = Retry.create();
                    final Result<Void> result = retry.run(() ->
                    {
                        value.increment();
                        if (value.getAsInt() <= 1)
                        {
                            throw new NotFoundException(value.toString());
                        }
                    });
                    test.assertNotNull(result);
                    test.assertEqual(0, value.get());
                    test.assertFalse(result.isCompleted());

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertNull(result.await());
                        test.assertEqual(2, value.get());
                        test.assertTrue(result.isCompleted());
                    }
                });

                runner.test("with an action that throws on the first and second attempts", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Retry retry = Retry.create();
                    final Result<Void> result = retry.run(() ->
                    {
                        value.increment();
                        if (value.getAsInt() <= 2)
                        {
                            throw new NotFoundException(value.toString());
                        }
                    });
                    test.assertNotNull(result);
                    test.assertEqual(0, value.get());
                    test.assertFalse(result.isCompleted());

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertNull(result.await());
                        test.assertEqual(3, value.get());
                        test.assertTrue(result.isCompleted());
                    }
                });

                runner.test("with an action that throws on the first, second, and third attempts", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Retry retry = Retry.create();
                    final Result<Void> result = retry.run(() ->
                    {
                        value.increment();
                        if (value.getAsInt() <= 3)
                        {
                            throw new NotFoundException(value.toString());
                        }
                    });
                    test.assertNotNull(result);
                    test.assertEqual(0, value.get());
                    test.assertFalse(result.isCompleted());

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertThrows(result::await,
                            ErrorIterable.create(
                                new NotFoundException("1"),
                                new NotFoundException("2"),
                                new NotFoundException("3")));
                        test.assertEqual(3, value.get());
                        test.assertTrue(result.isCompleted());
                    }
                });

                runner.test("with an action that throws on every attempt", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Retry retry = Retry.create();
                    final Result<Void> result = retry.run(() ->
                    {
                        throw new NotFoundException(value.increment().toString());
                    });
                    test.assertNotNull(result);
                    test.assertEqual(0, value.get());
                    test.assertFalse(result.isCompleted());

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertThrows(result::await,
                            ErrorIterable.create(
                                new NotFoundException("1"),
                                new NotFoundException("2"),
                                new NotFoundException("3")));
                        test.assertEqual(3, value.get());
                        test.assertTrue(result.isCompleted());
                    }
                });
            });

            runner.testGroup("run(Function0<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Retry retry = Retry.create();
                    test.assertThrows(() -> retry.run((Function0<Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with an action that doesn't throw", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Retry retry = Retry.create();
                    final Result<Integer> result = retry.run(() -> value.increment().get() * 10);
                    test.assertNotNull(result);
                    test.assertEqual(0, value.get());
                    test.assertFalse(result.isCompleted());

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(10, result.await());
                        test.assertEqual(1, value.get());
                        test.assertTrue(result.isCompleted());
                    }
                });

                runner.test("with an action that throws on the first attempt", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Retry retry = Retry.create();
                    final Result<Integer> result = retry.run(() ->
                    {
                        value.increment();
                        if (value.getAsInt() <= 1)
                        {
                            throw new NotFoundException(value.toString());
                        }
                        return value.get() * 10;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(0, value.get());
                    test.assertFalse(result.isCompleted());

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(20, result.await());
                        test.assertEqual(2, value.get());
                        test.assertTrue(result.isCompleted());
                    }
                });

                runner.test("with an action that throws on the first and second attempts", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Retry retry = Retry.create();
                    final Result<Integer> result = retry.run(() ->
                    {
                        value.increment();
                        if (value.getAsInt() <= 2)
                        {
                            throw new NotFoundException(value.toString());
                        }
                        return value.get() * 10;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(0, value.get());
                    test.assertFalse(result.isCompleted());

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertEqual(30, result.await());
                        test.assertEqual(3, value.get());
                        test.assertTrue(result.isCompleted());
                    }
                });

                runner.test("with an action that throws on the first, second, and third attempts", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Retry retry = Retry.create();
                    final Result<Integer> result = retry.run(() ->
                    {
                        value.increment();
                        if (value.getAsInt() <= 3)
                        {
                            throw new NotFoundException(value.toString());
                        }
                        return value.get() * 10;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(0, value.get());
                    test.assertFalse(result.isCompleted());

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertThrows(result::await,
                            ErrorIterable.create(
                                new NotFoundException("1"),
                                new NotFoundException("2"),
                                new NotFoundException("3")));
                        test.assertEqual(3, value.get());
                        test.assertTrue(result.isCompleted());
                    }
                });

                runner.test("with an action that throws on every attempt", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Retry retry = Retry.create();
                    final Result<Integer> result = retry.run(() ->
                    {
                        throw new NotFoundException(value.increment().toString());
                    });
                    test.assertNotNull(result);
                    test.assertEqual(0, value.get());
                    test.assertFalse(result.isCompleted());

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertThrows(result::await,
                            ErrorIterable.create(
                                new NotFoundException("1"),
                                new NotFoundException("2"),
                                new NotFoundException("3")));
                        test.assertEqual(3, value.get());
                        test.assertTrue(result.isCompleted());
                    }
                });
            });
        });
    }
}
