package qub;

public class AsyncRunnerTests
{
    public static void test(TestRunner runner, Function0<AsyncRunner> createAsyncRunner)
    {
        runner.testGroup(AsyncRunner.class, () ->
        {
            runner.testGroup("markCompleted(Setable<Boolean>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        test.assertThrows(() -> asyncRunner.markCompleted(null));
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final Value<Boolean> value = Value.create();
                        asyncRunner.markCompleted(value);
                        test.assertTrue(value.get());
                    }
                });
            });

            runner.testGroup("schedule()", () ->
            {
                runner.test("with null Action0", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        test.assertThrows(() -> asyncRunner.schedule((Action0)null));
                        test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                    }
                });
                
                runner.test("with null Function0", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        test.assertThrows(() -> asyncRunner.schedule((Function0<Integer>)null));
                        test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                    }
                });

                runner.test("with null PausedAsyncTask", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        test.assertThrows(() -> asyncRunner.schedule((PausedAsyncTask)null));
                        test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                    }
                });
            });

            runner.testGroup("scheduleAsyncAction(Function0<AsyncAction>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        AsyncRunnerRegistry.withCurrentThreadAsyncRunner(asyncRunner, () ->
                        {
                            test.assertThrows(() -> asyncRunner.scheduleAsyncAction(null));
                            test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                        });
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        AsyncRunnerRegistry.withCurrentThreadAsyncRunner(asyncRunner, () ->
                        {
                            final Value<Boolean> value1 = Value.create();
                            final Value<Boolean> value2 = Value.create();

                            final AsyncAction asyncAction = asyncRunner.scheduleAsyncAction(() ->
                            {
                                value1.set(false);
                                return asyncRunner.schedule(() -> value2.set(true));
                            });
                            asyncAction.await();

                            test.assertEqual(false, value1.get());
                            test.assertEqual(true, value2.get());
                            test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                        });
                    }
                });
            });

            runner.testGroup("scheduleAsyncFunction(Function0<AsyncFunction<T>>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        AsyncRunnerRegistry.withCurrentThreadAsyncRunner(asyncRunner, () ->
                        {
                            test.assertThrows(() -> asyncRunner.scheduleAsyncFunction(null));
                            test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                        });
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        AsyncRunnerRegistry.withCurrentThreadAsyncRunner(asyncRunner, () ->
                        {
                            final Value<Boolean> value1 = Value.create();
                            final Value<Boolean> value2 = Value.create();

                            final AsyncFunction<Integer> asyncFunction = asyncRunner.scheduleAsyncFunction(() ->
                            {
                                value1.set(false);
                                return asyncRunner.schedule(() ->
                                {
                                    value2.set(true);
                                    return 5;
                                });
                            });
                            final Integer result = asyncFunction.awaitReturn();

                            test.assertEqual(false, value1.get());
                            test.assertEqual(true, value2.get());
                            test.assertEqual(5, result);
                            test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                        });
                    }
                });
            });

            runner.testGroup("whenAll(AsyncAction...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        test.assertThrows(asyncRunner::whenAll);
                        test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                    }
                });

                runner.test("with null AsyncAction array", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        test.assertThrows(() -> asyncRunner.whenAll((AsyncAction[])null));
                        test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                    }
                });

                runner.test("with one argument", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        AsyncRunnerRegistry.withCurrentThreadAsyncRunner(asyncRunner, () ->
                        {
                            final AsyncAction asyncAction = asyncRunner.schedule(() -> {});

                            final AsyncAction afterAsyncAction = asyncRunner.whenAll(asyncAction);
                            afterAsyncAction.await();

                            test.assertTrue(asyncAction.isCompleted());
                            test.assertTrue(afterAsyncAction.isCompleted());
                        });
                    }
                });

                runner.test("with two arguments", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        AsyncRunnerRegistry.withCurrentThreadAsyncRunner(asyncRunner, () ->
                        {
                            final AsyncAction asyncAction1 = asyncRunner.schedule(() -> {});
                            final AsyncAction asyncAction2 = asyncRunner.schedule(() -> {});

                            final AsyncAction afterAsyncAction = asyncRunner.whenAll(asyncAction1, asyncAction2);
                            afterAsyncAction.await();

                            test.assertTrue(asyncAction1.isCompleted());
                            test.assertTrue(asyncAction2.isCompleted());
                            test.assertTrue(afterAsyncAction.isCompleted());
                        });
                    }
                });

                runner.test("with an argument that throws an exception", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        AsyncRunnerRegistry.withCurrentThreadAsyncRunner(asyncRunner, () ->
                        {
                            final AsyncAction asyncAction1 = asyncRunner.schedule(() -> { throw new RuntimeException("hello"); });
                            final AsyncAction asyncAction2 = asyncRunner.schedule(() -> {});

                            final AsyncAction afterAsyncAction = asyncRunner.whenAll(asyncAction1, asyncAction2);
                            test.assertThrows(
                                afterAsyncAction::await,
                                new AwaitException(new RuntimeException("hello")));

                            test.assertTrue(asyncAction1.isCompleted());
                            test.assertTrue(asyncAction2.isCompleted());
                            test.assertTrue(afterAsyncAction.isCompleted());
                        });
                    }
                });
            });

            runner.testGroup("awaitAll(AsyncAction...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        asyncRunner.awaitAll();
                    }
                });

                runner.test("with null AsyncAction array", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        test.assertThrows(() -> asyncRunner.awaitAll((AsyncAction[])null), new PreConditionFailure("asyncActions cannot be null."));
                    }
                });

                runner.test("with one argument", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        AsyncRunnerRegistry.withCurrentThreadAsyncRunner(asyncRunner, () ->
                        {
                            final AsyncAction asyncAction = asyncRunner.schedule(() -> {});

                            asyncRunner.awaitAll(asyncAction);

                            test.assertTrue(asyncAction.isCompleted());
                        });
                    }
                });

                runner.test("with two arguments", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        AsyncRunnerRegistry.withCurrentThreadAsyncRunner(asyncRunner, () ->
                        {
                            final AsyncAction asyncAction1 = asyncRunner.schedule(() -> {});
                            final AsyncAction asyncAction2 = asyncRunner.schedule(() -> {});

                            asyncRunner.awaitAll(asyncAction1, asyncAction2);

                            test.assertTrue(asyncAction1.isCompleted());
                            test.assertTrue(asyncAction2.isCompleted());
                        });
                    }
                });

                runner.test("with an argument that throws an exception", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        AsyncRunnerRegistry.withCurrentThreadAsyncRunner(asyncRunner, () ->
                        {
                            final AsyncAction asyncAction1 = asyncRunner.schedule(() -> { throw new RuntimeException("hello"); });
                            final AsyncAction asyncAction2 = asyncRunner.schedule(() -> {});

                            test.assertThrows(
                                () -> asyncRunner.awaitAll(asyncAction1, asyncAction2),
                                new AwaitException(new RuntimeException("hello")));

                            test.assertTrue(asyncAction1.isCompleted());
                            test.assertTrue(asyncAction2.isCompleted());
                        });
                    }
                });
            });

            runner.test("dispose()", (Test test) ->
            {
                try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                {
                    test.assertSuccess(true, asyncRunner.dispose());
                    test.assertSuccess(false, asyncRunner.dispose());
                }
            });

            runner.test("success()", (Test test) ->
            {
                try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                {
                    final AsyncFunction<Result<Boolean>> asyncFunction = asyncRunner.success();
                    test.assertNotNull(asyncFunction);
                    test.assertTrue(asyncFunction.isCompleted());

                    final Result<Boolean> result = asyncFunction.awaitReturn();
                    test.assertSuccess(null, result);
                }
            });

            runner.testGroup("success(T)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final AsyncFunction<Result<Integer>> asyncFunction = asyncRunner.success((Integer)null);
                        test.assertNotNull(asyncFunction);
                        test.assertTrue(asyncFunction.isCompleted());

                        final Result<Integer> result = asyncFunction.awaitReturn();
                        test.assertSuccess(null, result);
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final AsyncFunction<Result<Integer>> asyncFunction = asyncRunner.success(19);
                        test.assertNotNull(asyncFunction);
                        test.assertTrue(asyncFunction.isCompleted());

                        final Result<Integer> result = asyncFunction.awaitReturn();
                        test.assertSuccess(19, result);
                    }
                });
            });

            runner.testGroup("error(Throwable)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        test.assertThrows(() -> asyncRunner.error(null));
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final AsyncFunction<Result<Integer>> asyncFunction = asyncRunner.error(new IllegalArgumentException("blah"));
                        test.assertNotNull(asyncFunction);
                        test.assertTrue(asyncFunction.isCompleted());

                        final Result<Integer> result = asyncFunction.awaitReturn();
                        test.assertError(new IllegalArgumentException("blah"), result);
                    }
                });
            });

            runner.testGroup("notNull(Object,String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final AsyncFunction<Result<Integer>> asyncFunction = asyncRunner.notNull(null, "blah");
                        test.assertNotNull(asyncFunction);
                        test.assertTrue(asyncFunction.isCompleted());

                        final Result<Integer> result = asyncFunction.awaitReturn();
                        test.assertError(new IllegalArgumentException("blah cannot be null."), result);
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final AsyncFunction<Result<Integer>> asyncFunction = asyncRunner.notNull(0, "blah");
                        test.assertNull(asyncFunction);
                    }
                });
            });

            runner.testGroup("equal(T,T,String)", () ->
            {
                runner.test("with null and null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final AsyncFunction<Result<Integer>> asyncFunction = asyncRunner.equal(null, null, "blah");
                        test.assertNull(asyncFunction);
                    }
                });

                runner.test("with null and non-null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final AsyncFunction<Result<Integer>> asyncFunction = asyncRunner.equal(null, 50, "blah");
                        test.assertNotNull(asyncFunction);
                        test.assertTrue(asyncFunction.isCompleted());

                        final Result<Integer> result = asyncFunction.awaitReturn();
                        test.assertError(new IllegalArgumentException("blah (50) must be null."), result);
                    }
                });

                runner.test("with non-null and null", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final AsyncFunction<Result<Integer>> asyncFunction = asyncRunner.equal(49, null, "blah");
                        test.assertNotNull(asyncFunction);
                        test.assertTrue(asyncFunction.isCompleted());

                        final Result<Integer> result = asyncFunction.awaitReturn();
                        test.assertError(new IllegalArgumentException("blah (null) must be 49."), result);
                    }
                });

                runner.test("with different values", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final AsyncFunction<Result<Integer>> asyncFunction = asyncRunner.equal(49, 50, "blah");
                        test.assertNotNull(asyncFunction);
                        test.assertTrue(asyncFunction.isCompleted());

                        final Result<Integer> result = asyncFunction.awaitReturn();
                        test.assertError(new IllegalArgumentException("blah (50) must be 49."), result);
                    }
                });

                runner.test("with equal values", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final AsyncFunction<Result<Integer>> asyncFunction = asyncRunner.equal(50, 50, "blah");
                        test.assertNull(asyncFunction);
                    }
                });
            });

            runner.testGroup("between(int,int,int,String)", () ->
            {
                runner.test("with valid values", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final AsyncFunction<Result<Integer>> asyncFunction = asyncRunner.between(1, 2, 3, "blah");
                        test.assertNull(asyncFunction);
                    }
                });

                runner.test("with invalid values", (Test test) ->
                {
                    try (final AsyncRunner asyncRunner = createAsyncRunner.run())
                    {
                        final AsyncFunction<Result<Integer>> asyncFunction = asyncRunner.between(1, 0, 3, "blah");
                        test.assertNotNull(asyncFunction);
                        test.assertTrue(asyncFunction.isCompleted());

                        final Result<Integer> result = asyncFunction.awaitReturn();
                        test.assertError(new IllegalArgumentException("blah (0) must be between 1 and 3."), result);
                    }
                });
            });
        });
    }
}
