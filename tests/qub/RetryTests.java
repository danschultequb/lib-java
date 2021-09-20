package qub;

public interface RetryTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Retry.class, () ->
        {
            runner.testGroup("run(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Retry.run((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with an action that doesn't throw", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Result<Void> result = Retry.run(() -> { value.increment(); });
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
                    final Result<Void> result = Retry.run(() ->
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
                    final Result<Void> result = Retry.run(() ->
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
                    final Result<Void> result = Retry.run(() ->
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
                    final Result<Void> result = Retry.run(() ->
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
                    test.assertThrows(() -> Retry.run((Function0<Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with an action that doesn't throw", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Result<Integer> result = Retry.run(() -> value.increment().get() * 10);
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
                    final Result<Integer> result = Retry.run(() ->
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
                    final Result<Integer> result = Retry.run(() ->
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
                    final Result<Integer> result = Retry.run(() ->
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
                    final Result<Integer> result = Retry.run(() ->
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
