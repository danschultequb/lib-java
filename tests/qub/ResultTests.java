package qub;

public interface ResultTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Result.class, () ->
        {
            runner.test("success()", (Test test) ->
            {
                final Result<String> result = Result.create();
                test.assertNotNull(result);
                test.assertFalse(result.isCompleted());

                test.assertNull(result.await());
                test.assertTrue(result.isCompleted());
            });

            runner.testGroup("success(T)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertNull(Result.success(null).await());
                });

                runner.test("with non-null", (Test test) ->
                {
                    test.assertEqual("hello", Result.success("hello").await());
                });
            });

            runner.testGroup("error(Throwable)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Result.error(null), new PreConditionFailure("error cannot be null."));
                });

                runner.test("with RuntimeException", (Test test) ->
                {
                    final Result<Integer> result = Result.error(new RuntimeException("abc"));
                    test.assertNotNull(result);
                    test.assertThrows(result::await, new RuntimeException("abc"));
                });

                runner.test("with Exception", (Test test) ->
                {
                    final Result<Integer> result = Result.error(new Exception("abc"));
                    test.assertNotNull(result);
                    test.assertThrows(result::await, new RuntimeException(new Exception("abc")));
                });
            });
        });
    }

    static void test(TestRunner runner, Function1<Function0<Integer>,Result<Integer>> creator)
    {
        runner.testGroup(Result.class, () ->
        {
            runner.testGroup("await()", () ->
            {
                runner.test("with null value", (Test test) ->
                {
                    final Result<Integer> result = creator.run(() -> null);

                    test.assertNull(result.await());
                    test.assertTrue(result.isCompleted());

                    test.assertNull(result.await());
                    test.assertTrue(result.isCompleted());
                });

                runner.test("with non-null value", (Test test) ->
                {
                    final Result<Integer> result = creator.run(() -> 1);

                    test.assertEqual(1, result.await());
                    test.assertTrue(result.isCompleted());

                    test.assertEqual(1, result.await());
                    test.assertTrue(result.isCompleted());
                });

                runner.test("with error", (Test test) ->
                {
                    final Result<Integer> result = creator.run(() -> { throw new NotFoundException("blah"); });

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertThrows(result::await, new AwaitException(new NotFoundException("blah")));
                        test.assertTrue(result.isCompleted());
                    }
                });
            });

            runner.testGroup("await(Class<TError>)", () ->
            {
                runner.test("with null error type", (Test test) ->
                {
                    final Result<Integer> result = creator.run(() -> null);

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertThrows(new PreConditionFailure("expectedErrorType cannot be null."), () ->
                        {
                            try
                            {
                                result.await((Class<java.io.IOException>)null);
                                test.fail("Expected an exception to be thrown.");
                            }
                            catch (PreConditionFailure e)
                            {
                                throw e;
                            }
                            catch (Throwable e)
                            {
                                test.fail(e);
                            }
                        });
                    }
                });

                runner.test("with no error value", (Test test) ->
                {
                    final Result<Integer> result = creator.run(() -> 1);

                    for (int i = 0; i < 2; ++i)
                    {
                        try
                        {
                            test.assertEqual(1, result.await(java.io.IOException.class));
                            test.assertTrue(result.isCompleted());
                        }
                        catch (java.io.IOException e)
                        {
                            test.fail(e);
                        }
                    }
                });

                runner.test("with non-matching error", (Test test) ->
                {
                    final Result<Integer> result = creator.run(() -> { throw Exceptions.asRuntime(new Exception("blah")); });

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertThrows(new AwaitException(new Exception("blah")), () ->
                        {
                            try
                            {
                                result.await(java.io.IOException.class);
                                test.fail("Expected an exception to be thrown.");
                            }
                            catch (java.io.IOException e)
                            {
                                test.fail(e);
                            }
                        });
                        test.assertTrue(result.isCompleted());
                    }
                });

                runner.test("with matching error", (Test test) ->
                {
                    final Result<Integer> result = creator.run(() -> { throw Exceptions.asRuntime(new java.io.IOException("hello")); });

                    for (int i = 0; i < 2; ++i)
                    {
                        try
                        {
                            result.await(java.io.IOException.class);
                            test.fail("Expected a java.io.IOException to be thrown.");
                        }
                        catch (java.io.IOException e)
                        {
                            test.assertEqual("hello", e.getMessage());
                        }
                        test.assertTrue(result.isCompleted());
                    }
                });

                runner.test("with super-matching error", (Test test) ->
                {
                    final Result<Integer> result = creator.run(() -> { throw Exceptions.asRuntime(new java.net.SocketException("hello")); });

                    for (int i = 0; i < 2; ++i)
                    {
                        try
                        {
                            result.await(java.io.IOException.class);
                            test.fail("Expected a java.io.IOException to be thrown.");
                        }
                        catch (java.io.IOException e)
                        {
                            test.assertInstanceOf(e, java.net.SocketException.class);
                            test.assertEqual("hello", e.getMessage());
                        }
                        test.assertTrue(result.isCompleted());
                    }
                });
            });

            runner.testGroup("then(Action0)", () ->
            {
                runner.test("with non-throwing parent result and null then-action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.then((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Void> thenResult = parentResult.then(() -> { thenCounter.increment(); });

                    test.assertNull(thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertNull(thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with non-throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Void> thenResult = parentResult.then((Action0)() ->
                    {
                        thenCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Void> thenResult = parentResult.then((Action0)() ->
                    {
                        thenCounter.increment();
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Void> thenResult = parentResult.then((Action0)() ->
                    {
                        thenCounter.increment();
                        throw new RuntimeException("abc");
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });
            });

            runner.testGroup("then(Action1<T>)", () ->
            {
                runner.test("with non-throwing parent result and null then-action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.then((Action1<Integer>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Void> thenResult = parentResult.then((Integer parentValue) -> { thenCounter.plusAssign(parentValue); });

                    test.assertNull(thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertNull(thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with non-throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Void> thenResult = parentResult.then((Action1<Integer>)(Integer parentValue) ->
                    {
                        thenCounter.plusAssign(parentValue);
                        throw new RuntimeException("foo");
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Void> thenResult = parentResult.then((Integer parentValue) ->
                    {
                        thenCounter.plusAssign(parentValue);
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Void> thenResult = parentResult.then((Action1<Integer>)(Integer parentValue) ->
                    {
                        thenCounter.plusAssign(parentValue);
                        throw new RuntimeException("abc");
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });
            });

            runner.testGroup("then(Function0<U>)", () ->
            {
                runner.test("with non-throwing parent result and null then-action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.then((Function0<String>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<String> thenResult = parentResult.then(() ->
                    {
                        thenCounter.increment();
                        return "abc";
                    });

                    test.assertEqual("abc", thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertEqual("abc", thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with non-throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<String> thenResult = parentResult.then((Function0<String>)() ->
                    {
                        thenCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<String> thenResult = parentResult.then(() ->
                    {
                        thenCounter.increment();
                        return "abc";
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<String> thenResult = parentResult.then((Function0<String>)() ->
                    {
                        thenCounter.increment();
                        throw new RuntimeException("abc");
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });
            });

            runner.testGroup("then(Function1<T,U>)", () ->
            {
                runner.test("with non-throwing parent result and null then-action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.then((Function1<Integer,String>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<String> thenResult = parentResult.then((Integer parentValue) ->
                    {
                        thenCounter.plusAssign(parentValue);
                        return "abc";
                    });

                    test.assertEqual("abc", thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertEqual("abc", thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with non-throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<String> thenResult = parentResult.then((Function1<Integer,String>)(Integer parentValue) ->
                    {
                        thenCounter.plusAssign(parentValue);
                        throw new RuntimeException("foo");
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<String> thenResult = parentResult.then((Integer parentValue) ->
                    {
                        thenCounter.plusAssign(parentValue);
                        return "abc";
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<String> thenResult = parentResult.then((Function1<Integer,String>)(Integer parentValue) ->
                    {
                        thenCounter.plusAssign(parentValue);
                        throw new RuntimeException("abc");
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });
            });

            runner.testGroup("onValue(Action0)", () ->
            {
                runner.test("with non-throwing parent result and null then-action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.onValue((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Integer> thenResult = parentResult.onValue(() -> { thenCounter.increment(); });

                    test.assertEqual(5, thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertEqual(5, thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with non-throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Integer> thenResult = parentResult.onValue((Action0)() ->
                    {
                        thenCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Integer> thenResult = parentResult.onValue((Action0)() ->
                    {
                        thenCounter.increment();
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Integer> thenResult = parentResult.onValue((Action0)() ->
                    {
                        thenCounter.increment();
                        throw new RuntimeException("abc");
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });
            });

            runner.testGroup("onValue(Action1<T>)", () ->
            {
                runner.test("with non-throwing parent result and null then-action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.onValue((Action1<Integer>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Integer> thenResult = parentResult.onValue((Integer parentValue) -> { thenCounter.plusAssign(parentValue); });

                    test.assertEqual(5, thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertEqual(5, thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with non-throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Integer> thenResult = parentResult.onValue((Action1<Integer>)(Integer parentValue) ->
                    {
                        thenCounter.plusAssign(parentValue);
                        throw new RuntimeException("foo");
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Integer> thenResult = parentResult.onValue((Integer parentValue) ->
                    {
                        thenCounter.plusAssign(parentValue);
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Integer> thenResult = parentResult.onValue((Action1<Integer>)(Integer parentValue) ->
                    {
                        thenCounter.plusAssign(parentValue);
                        throw new RuntimeException("abc");
                    });

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertThrows(thenResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });
            });

            runner.testGroup("catchError()", () ->
            {
                runner.test("with non-throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final Result<Integer> catchErrorResult = parentResult.catchError();

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    final Result<Integer> catchErrorResult = parentResult.catchError();

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });
            });

            runner.testGroup("catchError(Action0)", () ->
            {
                runner.test("with non-throwing parent result and null catchError-action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-throwing parent result and non-throwing catchError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(() -> { catchErrorCounter.increment(); });

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with non-throwing parent result and throwing catchError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError((Action0)() ->
                    {
                        catchErrorCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and non-throwing catchError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError((Action0)() ->
                    {
                        catchErrorCounter.increment();
                    });

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing catchError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError((Action0)() ->
                    {
                        catchErrorCounter.increment();
                        throw new RuntimeException("abc");
                    });

                    test.assertThrows(catchErrorResult::await, new RuntimeException("abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new RuntimeException("abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });
            });

            runner.testGroup("catchError(Action1<Throwable>)", () ->
            {
                runner.test("with non-throwing parent result and null catchError-action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError((Action1<Throwable>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-throwing parent result and non-throwing catchError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final List<Throwable> caughtExceptions = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError(caughtExceptions::add);

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with non-throwing parent result and throwing catchError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final List<Throwable> caughtExceptions = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError((Action1<Throwable>)(Throwable parentError) ->
                    {
                        caughtExceptions.add(parentError);
                        throw new RuntimeException("foo");
                    });

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and non-throwing catchError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/hello");
                    });

                    final List<Throwable> caughtExceptions = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError((Throwable parentError) ->
                    {
                        caughtExceptions.add(parentError);
                    });

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/hello")), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/hello")), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing catchError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/hello");
                    });

                    final List<Throwable> caughtExceptions = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError((Action1<Throwable>)(Throwable parentError) ->
                    {
                        caughtExceptions.add(parentError);
                        throw new FolderNotFoundException("/abc");
                    });

                    test.assertThrows(catchErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/hello")), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/hello")), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });
            });

            runner.testGroup("catchError(Class<TError extends Throwable>)", () ->
            {
                runner.test("with non-throwing parent result and null error type", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError((Class<? extends Throwable>)null),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with non-throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class);

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with non-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class);

                    test.assertThrows(catchErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with exact-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class);

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class);

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });
            });

            runner.testGroup("catchError(Class<TError extends Throwable>,Action0)", () ->
            {
                runner.test("with non-throwing parent result and null error type", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError((Class<? extends Throwable>)null, Action0.empty),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with non-throwing parent result and null catch-error action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError(NotFoundException.class, (Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, () ->
                    {
                        catchErrorCounter.increment();
                    });

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with non-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, () ->
                    {
                        catchErrorCounter.increment();
                    });

                    test.assertThrows(catchErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with exact-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(FileNotFoundException.class, () ->
                    {
                        catchErrorCounter.increment();
                    });

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("foo");
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, () ->
                    {
                        catchErrorCounter.increment();
                    });

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type and throwing catchError action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("foo");
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, (Action0)() ->
                    {
                        catchErrorCounter.increment();
                        throw new QueueEmptyException();
                    });

                    test.assertThrows(catchErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });
            });

            runner.testGroup("catchError(Class<TError extends Throwable>,Action1<TError>)", () ->
            {
                runner.test("with non-throwing parent result and null error type", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError((Class<NotFoundException>)null, (NotFoundException parentError) -> {}),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with non-throwing parent result and null catch-error action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError(NotFoundException.class, (Action1<NotFoundException>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final List<NotFoundException> caughtErrors = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, (NotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                    });

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with non-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    final List<NotFoundException> caughtErrors = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, (NotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                    });

                    test.assertThrows(catchErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with exact-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<Throwable> caughtErrors = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                    });

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<Throwable> caughtErrors = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, (NotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                    });

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertNull(catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type and throwing catchError action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<RuntimeException> caughtErrors = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, (Action1<NotFoundException>)(NotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                        throw new QueueEmptyException();
                    });

                    test.assertThrows(catchErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });
            });

            runner.testGroup("catchError(Function0<T>)", () ->
            {
                runner.test("with non-throwing parent result and null catch-error function", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError((Function0<Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(() ->
                    {
                        catchErrorCounter.increment();
                        return 7;
                    });

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(() ->
                    {
                        catchErrorCounter.increment();
                        return 7;
                    });

                    test.assertEqual(7, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(7, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing catchError function", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError((Function0<Integer>)() ->
                    {
                        catchErrorCounter.increment();
                        throw new QueueEmptyException();
                    });

                    test.assertThrows(catchErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });
            });

            runner.testGroup("catchError(Function1<Throwable,T>)", () ->
            {
                runner.test("with non-throwing parent result and null catch-error function", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError((Function1<Throwable,Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final List<Throwable> caughtExceptions = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError((Throwable error) ->
                    {
                        caughtExceptions.add(error);
                        return 7;
                    });

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<Throwable> caughtExceptions = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError((Throwable error) ->
                    {
                        caughtExceptions.add(error);
                        return 7;
                    });

                    test.assertEqual(7, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(7, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing catchError function", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<Throwable> caughtExceptions = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError((Function1<Throwable,Integer>)(Throwable error) ->
                    {
                        caughtExceptions.add(error);
                        throw new QueueEmptyException();
                    });

                    test.assertThrows(catchErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });
            });

            runner.testGroup("catchError(Class<TError extends Throwable>,Function0<T>)", () ->
            {
                runner.test("with non-throwing parent result and null error type", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError((Class<? extends Throwable>)null, () -> 5),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with non-throwing parent result and null catch-error action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError(NotFoundException.class, (Function0<Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, () ->
                    {
                        catchErrorCounter.increment();
                        return 7;
                    });

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with non-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, () ->
                    {
                        catchErrorCounter.increment();
                        return 7;
                    });

                    test.assertThrows(catchErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with exact-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(FileNotFoundException.class, () ->
                    {
                        catchErrorCounter.increment();
                        return 7;
                    });

                    test.assertEqual(7, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(7, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, () ->
                    {
                        catchErrorCounter.increment();
                        return 7;
                    });

                    test.assertEqual(7, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(7, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type and throwing catchError function", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("foo");
                    });

                    final IntegerValue catchErrorCounter = IntegerValue.create(0);
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, (Function0<Integer>)() ->
                    {
                        catchErrorCounter.increment();
                        throw new QueueEmptyException();
                    });

                    test.assertThrows(catchErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, catchErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });
            });

            runner.testGroup("catchError(Class<TError extends Throwable>,Function1<TError,T>)", () ->
            {
                runner.test("with non-throwing parent result and null error type", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError((Class<NotFoundException>)null, (NotFoundException parentError) -> { return 7; }),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with non-throwing parent result and null catch-error action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.catchError(NotFoundException.class, (Function1<NotFoundException,Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final List<NotFoundException> caughtErrors = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, (NotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                        return 7;
                    });

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(5, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with non-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    final List<NotFoundException> caughtErrors = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, (NotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                        return 7;
                    });

                    test.assertThrows(catchErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with exact-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<RuntimeException> caughtErrors = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                        return 7;
                    });

                    test.assertEqual(7, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(7, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type and non-throwing catchError function", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<Throwable> caughtErrors = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, (NotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                        return 7;
                    });

                    test.assertEqual(7, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertEqual(7, catchErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type and throwing catchError function", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<Throwable> caughtErrors = List.create();
                    final Result<Integer> catchErrorResult = parentResult.catchError(NotFoundException.class, (Function1<NotFoundException,Integer>)(NotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                        throw new FolderNotFoundException("/abc");
                    });

                    test.assertThrows(catchErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());

                    test.assertThrows(catchErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(catchErrorResult.isCompleted());
                });
            });

            runner.testGroup("onError(Action0)", () ->
            {
                runner.test("with non-throwing parent result and null then-action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.onError((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-throwing parent result and non-throwing onError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue onErrorCounter = IntegerValue.create(0);
                    final Result<Integer> onErrorResult = parentResult.onError(() ->
                    {
                        onErrorCounter.increment();
                    });

                    test.assertEqual(5, onErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertEqual(5, onErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with non-throwing parent result and throwing onError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue onErrorCounter = IntegerValue.create(0);
                    final Result<Integer> onErrorResult = parentResult.onError((Action0)() ->
                    {
                        onErrorCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    test.assertEqual(5, onErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertEqual(5, onErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and non-throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue onErrorCounter = IntegerValue.create(0);
                    final Result<Integer> onErrorResult = parentResult.onError((Action0)() ->
                    {
                        onErrorCounter.increment();
                    });

                    test.assertThrows(onErrorResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertThrows(onErrorResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue onErrorCounter = IntegerValue.create(0);
                    final Result<Integer> onErrorResult = parentResult.onError((Action0)() ->
                    {
                        onErrorCounter.increment();
                        throw new RuntimeException("abc");
                    });

                    test.assertThrows(onErrorResult::await, new RuntimeException("abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertThrows(onErrorResult::await, new RuntimeException("abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });
            });

            runner.testGroup("onError(Action1<Throwable>)", () ->
            {
                runner.test("with non-throwing parent result and null onError-action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.onError((Action1<Throwable>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-throwing parent result and non-throwing onError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final List<Throwable> foundExceptions = List.create();
                    final Result<Integer> onErrorResult = parentResult.onError(foundExceptions::add);

                    test.assertEqual(5, onErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertEqual(5, onErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with non-throwing parent result and throwing onError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final List<Throwable> foundExceptions = List.create();
                    final Result<Integer> onErrorResult = parentResult.onError((Action1<Throwable>)(Throwable parentError) ->
                    {
                        foundExceptions.add(parentError);
                        throw new RuntimeException("foo");
                    });

                    test.assertEqual(5, onErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertEqual(5, onErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and non-throwing onError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final List<Throwable> foundExceptions = List.create();
                    final Result<Integer> onErrorResult = parentResult.onError(foundExceptions::add);

                    test.assertThrows(onErrorResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new RuntimeException("hello")), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertThrows(onErrorResult::await, new RuntimeException("hello"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new RuntimeException("hello")), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing onError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/hello");
                    });

                    final List<Throwable> foundExceptions = List.create();
                    final Result<Integer> onErrorResult = parentResult.onError((Action1<Throwable>)(Throwable parentError) ->
                    {
                        foundExceptions.add(parentError);
                        throw new FolderNotFoundException("/abc");
                    });

                    test.assertThrows(onErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/hello")), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertThrows(onErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/hello")), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });
            });

            runner.testGroup("onError(Class<TError extends Throwable>,Action0)", () ->
            {
                runner.test("with non-throwing parent result and null error type", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.onError((Class<? extends Throwable>)null, Action0.empty),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with non-throwing parent result and null onError action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.onError(NotFoundException.class, (Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue onErrorCounter = IntegerValue.create(0);
                    final Result<Integer> onErrorResult = parentResult.onError(NotFoundException.class, () ->
                    {
                        onErrorCounter.increment();
                    });

                    test.assertEqual(5, onErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertEqual(5, onErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with non-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    final IntegerValue onErrorCounter = IntegerValue.create(0);
                    final Result<Integer> onErrorResult = parentResult.onError(NotFoundException.class, () ->
                    {
                        onErrorCounter.increment();
                    });

                    test.assertThrows(onErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertThrows(onErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with exact-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final IntegerValue onErrorCounter = IntegerValue.create(0);
                    final Result<Integer> onErrorResult = parentResult.onError(FileNotFoundException.class, () ->
                    {
                        onErrorCounter.increment();
                    });

                    test.assertThrows(onErrorResult::await, new FileNotFoundException("/foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertThrows(onErrorResult::await, new FileNotFoundException("/foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final IntegerValue onErrorCounter = IntegerValue.create(0);
                    final Result<Integer> onErrorResult = parentResult.onError(NotFoundException.class, () ->
                    {
                        onErrorCounter.increment();
                    });

                    test.assertThrows(onErrorResult::await, new FileNotFoundException("/foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertThrows(onErrorResult::await, new FileNotFoundException("/foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with matching error type and throwing onError action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final IntegerValue onErrorCounter = IntegerValue.create(0);
                    final Result<Integer> onErrorResult = parentResult.onError(NotFoundException.class, () ->
                    {
                        onErrorCounter.increment();
                        throw new FolderNotFoundException("/abc");
                    });

                    test.assertThrows(onErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertThrows(onErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, onErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });
            });

            runner.testGroup("onError(Class<TError extends Throwable>,Action1<TError>)", () ->
            {
                runner.test("with non-throwing parent result and null error type", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.onError((Class<NotFoundException>)null, (NotFoundException parentError) -> {}),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with non-throwing parent result and null onError action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.onError(NotFoundException.class, (Action1<NotFoundException>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final List<NotFoundException> foundErrors = List.create();
                    final Result<Integer> onErrorResult = parentResult.onError(NotFoundException.class, (NotFoundException parentError) ->
                    {
                        foundErrors.add(parentError);
                    });

                    test.assertEqual(5, onErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertEqual(5, onErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with non-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    final List<NotFoundException> foundErrors = List.create();
                    final Result<Integer> onErrorResult = parentResult.onError(NotFoundException.class, (NotFoundException parentError) ->
                    {
                        foundErrors.add(parentError);
                    });

                    test.assertThrows(onErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertThrows(onErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with exact-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<Throwable> foundErrors = List.create();
                    final Result<Integer> onErrorResult = parentResult.onError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        foundErrors.add(parentError);
                    });

                    test.assertThrows(onErrorResult::await, new FileNotFoundException("/foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), foundErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertThrows(onErrorResult::await, new FileNotFoundException("/foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), foundErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<RuntimeException> caughtErrors = List.create();
                    final Result<Integer> onErrorResult = parentResult.onError(NotFoundException.class, (NotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                    });

                    test.assertThrows(onErrorResult::await, new FileNotFoundException("/foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertThrows(onErrorResult::await, new FileNotFoundException("/foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type and throwing onError action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<Throwable> caughtErrors = List.create();
                    final Result<Integer> onErrorResult = parentResult.onError(NotFoundException.class, (NotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                        throw new FolderNotFoundException("/abc");
                    });

                    test.assertThrows(onErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());

                    test.assertThrows(onErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(onErrorResult.isCompleted());
                });
            });

            runner.testGroup("convertError(Function0<? extends Throwable>)", () ->
            {
                runner.test("with non-throwing parent result and null then-action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.convertError((Function0<Throwable>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-throwing parent result and non-throwing convertError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue convertErrorCounter = IntegerValue.create(0);
                    final Result<Integer> convertErrorResult = parentResult.convertError(() ->
                    {
                        convertErrorCounter.increment();
                        return new NotFoundException("abc");
                    });

                    test.assertEqual(5, convertErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertEqual(5, convertErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with non-throwing parent result and throwing convertError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue convertErrorCounter = IntegerValue.create(0);
                    final Result<Integer> convertErrorResult = parentResult.convertError((Function0<Throwable>)() ->
                    {
                        convertErrorCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    test.assertEqual(5, convertErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertEqual(5, convertErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and non-throwing convertError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue convertErrorCounter = IntegerValue.create(0);
                    final Result<Integer> convertErrorResult = parentResult.convertError((Function0<Throwable>)() ->
                    {
                        convertErrorCounter.increment();
                        return new NotFoundException("abc");
                    });

                    test.assertThrows(convertErrorResult::await, new NotFoundException("abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertThrows(convertErrorResult::await, new NotFoundException("abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing then-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final IntegerValue convertErrorCounter = IntegerValue.create(0);
                    final Result<Integer> convertErrorResult = parentResult.convertError((Function0<Throwable>)() ->
                    {
                        convertErrorCounter.increment();
                        throw new RuntimeException("abc");
                    });

                    test.assertThrows(convertErrorResult::await, new RuntimeException("abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertThrows(convertErrorResult::await, new RuntimeException("abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });
            });

            runner.testGroup("convertError(Function1<Throwable,? extends Throwable>)", () ->
            {
                runner.test("with non-throwing parent result and null convertError-action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.convertError((Function1<Throwable,Throwable>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-throwing parent result and non-throwing convertError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final List<Throwable> foundExceptions = List.create();
                    final Result<Integer> convertErrorResult = parentResult.convertError((Throwable parentError) ->
                    {
                        foundExceptions.add(parentError);
                        return new NotFoundException("abc");
                    });

                    test.assertEqual(5, convertErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertEqual(5, convertErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with non-throwing parent result and throwing convertError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final List<Throwable> foundExceptions = List.create();
                    final Result<Integer> convertErrorResult = parentResult.convertError((Function1<Throwable,Throwable>)(Throwable parentError) ->
                    {
                        foundExceptions.add(parentError);
                        throw new RuntimeException("foo");
                    });

                    test.assertEqual(5, convertErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertEqual(5, convertErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and non-throwing convertError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("hello");
                    });

                    final List<Throwable> foundExceptions = List.create();
                    final Result<Integer> convertErrorResult = parentResult.convertError((Throwable parentError) ->
                    {
                        foundExceptions.add(parentError);
                        return new NotFoundException("abc");
                    });

                    test.assertThrows(convertErrorResult::await, new NotFoundException("abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new RuntimeException("hello")), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertThrows(convertErrorResult::await, new NotFoundException("abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new RuntimeException("hello")), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with throwing parent result and throwing convertError-action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/hello");
                    });

                    final List<Throwable> foundExceptions = List.create();
                    final Result<Integer> convertErrorResult = parentResult.convertError((Function1<Throwable,Throwable>)(Throwable parentError) ->
                    {
                        foundExceptions.add(parentError);
                        throw new FolderNotFoundException("/abc");
                    });

                    test.assertThrows(convertErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/hello")), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertThrows(convertErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/hello")), foundExceptions);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });
            });

            runner.testGroup("convertError(Class<? extends Throwable>,Function0<? extends Throwable>)", () ->
            {
                runner.test("with non-throwing parent result and null error type", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.convertError((Class<? extends Throwable>)null, () -> new RuntimeException("abc")),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with non-throwing parent result and null convertError action", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.convertError(NotFoundException.class, (Function0<Throwable>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final IntegerValue convertErrorCounter = IntegerValue.create(0);
                    final Result<Integer> convertErrorResult = parentResult.convertError(NotFoundException.class, () ->
                    {
                        convertErrorCounter.increment();
                        return new NotFoundException("abc");
                    });

                    test.assertEqual(5, convertErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertEqual(5, convertErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with non-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new RuntimeException("foo");
                    });

                    final IntegerValue convertErrorCounter = IntegerValue.create(0);
                    final Result<Integer> convertErrorResult = parentResult.convertError(NotFoundException.class, () ->
                    {
                        convertErrorCounter.increment();
                        return new QueueEmptyException();
                    });

                    test.assertThrows(convertErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertThrows(convertErrorResult::await, new RuntimeException("foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(0, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with exact-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final IntegerValue convertErrorCounter = IntegerValue.create(0);
                    final Result<Integer> convertErrorResult = parentResult.convertError(FileNotFoundException.class, () ->
                    {
                        convertErrorCounter.increment();
                        return new QueueEmptyException();
                    });

                    test.assertThrows(convertErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertThrows(convertErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final IntegerValue convertErrorCounter = IntegerValue.create(0);
                    final Result<Integer> convertErrorResult = parentResult.convertError(NotFoundException.class, () ->
                    {
                        convertErrorCounter.increment();
                        return new QueueEmptyException();
                    });

                    test.assertThrows(convertErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertThrows(convertErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with matching error type and throwing convertError action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final IntegerValue convertErrorCounter = IntegerValue.create(0);
                    final Result<Integer> convertErrorResult = parentResult.convertError(NotFoundException.class, (Function0<Throwable>)() ->
                    {
                        convertErrorCounter.increment();
                        throw new QueueEmptyException();
                    });

                    test.assertThrows(convertErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertThrows(convertErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, convertErrorCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });
            });

            runner.testGroup("convertError(Class<TError extends Throwable>,Function1<TError,? extends Throwable>)", () ->
            {
                runner.test("with non-throwing parent result and null error type", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.convertError((Class<NotFoundException>)null, (NotFoundException parentError) -> { return new RuntimeException("abc"); }),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with non-throwing parent result and null convertError function", (Test test) ->
                {
                    final Result<Integer> parentResult = creator.run(() -> 5);

                    test.assertThrows(() -> parentResult.convertError(NotFoundException.class, (Function1<NotFoundException,Throwable>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-throwing parent result", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });

                    final List<NotFoundException> foundErrors = List.create();
                    final Result<Integer> convertErrorResult = parentResult.convertError(NotFoundException.class, (NotFoundException parentError) ->
                    {
                        foundErrors.add(parentError);
                        return new QueueEmptyException();
                    });

                    test.assertEqual(5, convertErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertEqual(5, convertErrorResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with non-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<Throwable> foundErrors = List.create();
                    final Result<Integer> convertErrorResult = parentResult.convertError(ParseException.class, (ParseException parentError) ->
                    {
                        foundErrors.add(parentError);
                        return new QueueEmptyException();
                    });

                    test.assertThrows(convertErrorResult::await, new FileNotFoundException("/foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertThrows(convertErrorResult::await, new FileNotFoundException("/foo"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(), foundErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with exact-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<RuntimeException> foundErrors = List.create();
                    final Result<Integer> convertErrorResult = parentResult.convertError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        foundErrors.add(parentError);
                        return new QueueEmptyException();
                    });

                    test.assertThrows(convertErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), foundErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertThrows(convertErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), foundErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<RuntimeException> caughtErrors = List.create();
                    final Result<Integer> convertErrorResult = parentResult.convertError(NotFoundException.class, (NotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                        return new QueueEmptyException();
                    });

                    test.assertThrows(convertErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertThrows(convertErrorResult::await, new QueueEmptyException());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });

                runner.test("with throwing parent result with super-matching error type and throwing convertError action", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final Result<Integer> parentResult = creator.run(() ->
                    {
                        parentCounter.increment();
                        throw new FileNotFoundException("/foo");
                    });

                    final List<RuntimeException> caughtErrors = List.create();
                    final Result<Integer> convertErrorResult = parentResult.convertError(NotFoundException.class, (Function1<NotFoundException,Throwable>)(NotFoundException parentError) ->
                    {
                        caughtErrors.add(parentError);
                        throw new FolderNotFoundException("/abc");
                    });

                    test.assertThrows(convertErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());

                    test.assertThrows(convertErrorResult::await, new FolderNotFoundException("/abc"));
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(Iterable.create(new FileNotFoundException("/foo")), caughtErrors);
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(convertErrorResult.isCompleted());
                });
            });
        });
    }
}
