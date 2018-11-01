package qub;

public class BasicAsyncFunctionTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(BasicAsyncFunction.class, () ->
        {
            BasicAsyncTaskTests.test(runner, BasicAsyncFunctionTests::create);
            
            runner.test("constructor(Getable<AsyncRunner>,Getable<AsyncTask>,Function0<T>)", (Test test) ->
            {
                final AsyncRunner asyncRunner = new ManualAsyncRunner();
                final BasicAsyncFunction<Integer> basicAsyncFunction = new BasicAsyncFunction<>(new Value<>(asyncRunner), () -> 0);
                test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                test.assertFalse(basicAsyncFunction.isCompleted());
            });
            
            runner.testGroup("then(Action1<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(test);
                    test.assertThrows(() -> basicAsyncFunction.then((Action1<Integer>)null));
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(test);
                    final AsyncAction thenAsyncAction = basicAsyncFunction.then((Integer arg) -> {});
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    asyncRunner.await();

                    final AsyncAction thenAsyncAction = basicAsyncFunction.then((Integer arg) -> {});
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    asyncRunner.await();
                });
            });

            runner.testGroup("then(Function1<T,U>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(test);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then((Function1<Integer,Integer>)null);
                    test.assertNull(thenAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(test);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then((Integer value) -> value);
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    asyncRunner.await();

                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then((Integer value) -> value);
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    asyncRunner.await();
                });
            });

            runner.testGroup("thenAsyncAction(Function1<T,AsyncAction>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(test);
                    test.assertNull(basicAsyncFunction.thenAsyncAction((Function1<Integer,AsyncAction>)null));
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    final AsyncAction asyncAction = basicAsyncFunction.thenAsyncAction((Integer value) -> asyncRunner.schedule(() -> {}));
                    test.assertNotNull(asyncAction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    asyncRunner.await();
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertTrue(basicAsyncFunction.isCompleted());
                    test.assertTrue(asyncAction.isCompleted());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    asyncRunner.await();
                    test.assertTrue(basicAsyncFunction.isCompleted());
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());

                    final AsyncAction asyncAction = basicAsyncFunction.thenAsyncAction((Integer value) -> asyncRunner.schedule(() -> {}));
                    test.assertNotNull(asyncAction);
                    test.assertFalse(asyncAction.isCompleted());
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    asyncRunner.await();
                });

                runner.test("with second thenAsyncAction()", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    final AsyncAction asyncAction1 = basicAsyncFunction.thenAsyncAction((Integer value) -> asyncRunner.schedule(() -> {}));
                    test.assertNotNull(asyncAction1);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    final AsyncAction asyncAction2 = asyncAction1.thenAsyncAction(() -> asyncRunner.schedule(() -> {}));
                    test.assertNotNull(asyncAction2);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    asyncRunner.await();
                });
            });

            runner.testGroup("thenAsyncFunction(Function1<T,AsyncFunction<U>>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(test);
                    test.assertNull(basicAsyncFunction.thenAsyncFunction((Function1<Integer,AsyncFunction<Boolean>>)null));
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    final AsyncFunction<Boolean> asyncFunction = basicAsyncFunction.thenAsyncFunction((Integer value) -> asyncRunner.schedule(() -> true));
                    test.assertNotNull(asyncFunction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    asyncRunner.await();
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertTrue(basicAsyncFunction.isCompleted());
                    test.assertTrue(asyncFunction.isCompleted());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    asyncRunner.await();
                    test.assertTrue(basicAsyncFunction.isCompleted());
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());

                    final AsyncFunction<Boolean> asyncFunction = basicAsyncFunction.thenAsyncFunction((Integer value) -> asyncRunner.schedule(() -> true));
                    test.assertNotNull(asyncFunction);
                    test.assertFalse(asyncFunction.isCompleted());
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    asyncRunner.await();
                });

                runner.test("with second thenAsyncFunction()", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    final AsyncFunction<Boolean> asyncFunction1 = basicAsyncFunction.thenAsyncFunction((Integer value) -> asyncRunner.schedule(() -> true));
                    test.assertNotNull(asyncFunction1);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    final AsyncFunction<String> asyncFunction2 = asyncFunction1.thenAsyncFunction((Boolean value) -> asyncRunner.schedule(() -> "abc"));
                    test.assertNotNull(asyncFunction2);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    asyncRunner.await();
                });
            });

            runner.testGroup("thenOn(AsyncRunner,Action1<T>)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final AsyncRunner asyncRunner2 = null;
                    final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(asyncRunner2, (Integer value) -> {});
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());

                    asyncRunner1.await();
                });

                runner.test("with null Action1", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(asyncRunner2, (Action1<Integer>)null);
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());

                    asyncRunner1.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final ManualAsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(asyncRunner2, (Integer value) -> {});
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());

                    asyncRunner1.await();
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);
                    asyncRunner1.await();
                    test.assertTrue(basicAsyncFunction.isCompleted());

                    final ManualAsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(asyncRunner2, (Integer value) -> {});
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenOn(AsyncRunner,Function1<T,U>)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final AsyncRunner asyncRunner2 = null;
                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(asyncRunner2, (Integer value) -> value);
                    test.assertNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());

                    asyncRunner1.await();
                });

                runner.test("with null Action1", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(asyncRunner2, (Function1<Integer,Integer>)null);
                    test.assertNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());

                    asyncRunner1.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final ManualAsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(asyncRunner2, (Integer value) -> value);
                    test.assertNotNull(thenOnAsyncFunction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());

                    asyncRunner1.await();
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);
                    asyncRunner1.await();

                    final ManualAsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(asyncRunner2, (Integer value) -> value);
                    test.assertNotNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenOn(AsyncRunner)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    asyncRunner.await();
                    test.assertNull(basicAsyncFunction.thenOn(null));
                    test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                });

                runner.test("with same AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    test.assertSame(basicAsyncFunction, basicAsyncFunction.thenOn(asyncRunner));
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());
                });

                runner.test("with different AsyncRunner", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final ManualAsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final AsyncFunction<Integer> thenAsyncAction = basicAsyncFunction.thenOn(asyncRunner2);
                    test.assertNotNull(thenAsyncAction);
                    test.assertNotSame(basicAsyncFunction, thenAsyncAction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());

                    asyncRunner1.await();
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });
            });

            runner.test("awaitReturn()", (Test test) ->
            {
                final AsyncRunner asyncRunner = test.getParallelAsyncRunner();
                final AsyncFunction<Integer> asyncFunction = asyncRunner.schedule(() -> 20);
                test.assertEqual(20, asyncFunction.awaitReturn());
                test.assertTrue(asyncFunction.isCompleted());

                test.assertEqual(20, asyncFunction.awaitReturn());
            });

            runner.testGroup("catchError(Function1<Throwable,T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(new ManualAsyncRunner());
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncFunction.catchError((Function1<Throwable,Integer>)null);
                    test.assertNull(onErrorAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-throwing parent", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);

                    final Value<Boolean> value = new Value<>();
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncFunction.catchError((Throwable error) ->
                    {
                        value.set(true);
                        return 7;
                    });
                    test.assertNotNull(onErrorAsyncFunction);

                    asyncRunner.await();
                    test.assertTrue(onErrorAsyncFunction.isCompleted());
                    test.assertFalse(value.hasValue());
                });

                runner.test("with throwing parent", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncTask = createScheduled(asyncRunner);

                    final Value<Boolean> value = new Value<>();
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncTask
                        .then(() ->
                        {
                            if (true)
                            {
                                throw new RuntimeException("abc");
                            }
                            return 5;
                        })
                        .catchError((Throwable error) ->
                        {
                            value.set(true);
                            return 7;
                        });
                    test.assertNotNull(onErrorAsyncFunction);

                    asyncRunner.await();
                    test.assertTrue(onErrorAsyncFunction.isCompleted());
                    test.assertEqual(true, value.get());
                    test.assertEqual(7, onErrorAsyncFunction.awaitReturn());
                });
            });

            runner.testGroup("catchErrorOn(AsyncRunner,Function1<Throwable,T>)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncTask = create(new ManualAsyncRunner());
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncTask.catchErrorOn(null, (Throwable eror) -> 3);
                    test.assertNull(onErrorAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with null Action", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncTask = createScheduled(asyncRunner1);
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncTask.catchErrorOn(asyncRunner2, (Function1<Throwable,Integer>)null);
                    test.assertNull(onErrorAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-throwing parent", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final ManualAsyncRunner asyncRunner2 = new ManualAsyncRunner();

                    final BasicAsyncFunction<Integer> basicAsyncTask = createScheduled(asyncRunner1);

                    final Value<Boolean> value = new Value<>();
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncTask.catchErrorOn(asyncRunner2, (Throwable error) ->
                    {
                        value.set(true);
                        return 11;
                    });
                    test.assertNotNull(onErrorAsyncFunction);

                    asyncRunner1.await();
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                    test.assertTrue(onErrorAsyncFunction.isCompleted());
                    test.assertFalse(value.hasValue());
                    test.assertNull(onErrorAsyncFunction.awaitReturn());
                });

                runner.test("with throwing parent", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final ManualAsyncRunner asyncRunner2 = new ManualAsyncRunner();

                    final BasicAsyncFunction<Integer> basicAsyncTask = createScheduled(asyncRunner1);

                    final Value<Boolean> value = new Value<>();
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncTask
                        .then(() ->
                        {
                            if (true)
                            {
                                throw new RuntimeException("abc");
                            }
                            return 11;
                        })
                        .catchErrorOn(asyncRunner2, (Throwable error) ->
                        {
                            value.set(true);
                            return 12;
                        });
                    test.assertNotNull(onErrorAsyncFunction);

                    asyncRunner1.await();
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                    test.assertTrue(onErrorAsyncFunction.isCompleted());
                    test.assertEqual(true, value.get());
                    test.assertEqual(12, onErrorAsyncFunction.awaitReturn());
                });
            });

            runner.testGroup("catchErrorAsyncFunction(Function1<Throwable,AsyncFunction<T>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(new ManualAsyncRunner());
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncFunction.catchErrorAsyncFunction(null);
                    test.assertNull(onErrorAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-throwing parent", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncTask = createScheduled(asyncRunner);

                    final Value<Boolean> value1 = new Value<>();
                    final Value<String> value2 = new Value<>();
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncTask.catchErrorAsyncFunction((Throwable error) ->
                    {
                        value1.set(true);
                        return asyncRunner.schedule(() ->
                        {
                            value2.set("hello");
                            return 30;
                        });
                    });
                    test.assertNotNull(onErrorAsyncFunction);

                    asyncRunner.await();
                    test.assertTrue(onErrorAsyncFunction.isCompleted());
                    test.assertFalse(value1.hasValue());
                    test.assertFalse(value2.hasValue());
                    test.assertNull(onErrorAsyncFunction.awaitReturn());
                });

                runner.test("with throwing parent", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncTask = createScheduled(asyncRunner);

                    final Value<Boolean> value1 = new Value<>();
                    final Value<String> value2 = new Value<>();
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncTask
                        .then(() ->
                        {
                            if (true)
                            {
                                throw new RuntimeException("abc");
                            }
                            return 30;
                        })
                        .catchErrorAsyncFunction((Throwable error) ->
                        {
                            value1.set(true);
                            return asyncRunner.schedule(() ->
                            {
                                value2.set("hello");
                                return 40;
                            });
                        });
                    test.assertNotNull(onErrorAsyncFunction);

                    asyncRunner.await();
                    test.assertTrue(onErrorAsyncFunction.isCompleted());
                    test.assertEqual(true, value1.get());
                    test.assertEqual("hello", value2.get());
                    test.assertEqual(40, onErrorAsyncFunction.awaitReturn());
                });
            });

            runner.testGroup("catchErrorAsyncFunctionOn(AsyncRunner,Function1<Throwable,AsyncFunction<T>>)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncTask = createScheduled(asyncRunner);
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncTask.catchErrorAsyncFunctionOn(null, (Throwable error) -> asyncRunner.schedule(() -> 20));
                    test.assertNull(onErrorAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with null function", (Test test) ->
                {
                    final AsyncRunner asyncRunner = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncTask = createScheduled(asyncRunner);
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncTask.catchErrorAsyncFunctionOn(asyncRunner, null);
                    test.assertNull(onErrorAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-throwing parent", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final ManualAsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncTask = createScheduled(asyncRunner1);

                    final Value<Boolean> value1 = new Value<>();
                    final Value<String> value2 = new Value<>();
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncTask.catchErrorAsyncFunctionOn(asyncRunner2, (Throwable error) ->
                    {
                        value1.set(true);
                        return asyncRunner1.schedule(() ->
                        {
                            value2.set("hello");
                            return 21;
                        });
                    });
                    test.assertNotNull(onErrorAsyncFunction);

                    asyncRunner1.await();

                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();

                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                    test.assertTrue(onErrorAsyncFunction.isCompleted());
                    test.assertFalse(value1.hasValue());
                    test.assertFalse(value2.hasValue());
                    test.assertNull(onErrorAsyncFunction.awaitReturn());
                });

                runner.test("with throwing parent", (Test test) ->
                {
                    final ManualAsyncRunner asyncRunner1 = new ManualAsyncRunner();
                    final ManualAsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncTask = createScheduled(asyncRunner1);

                    final Value<Boolean> value1 = new Value<>();
                    final Value<String> value2 = new Value<>();
                    final AsyncFunction<Integer> onErrorAsyncFunction = basicAsyncTask
                        .then(() ->
                        {
                            if (true)
                            {
                                throw new RuntimeException("abc");
                            }
                            return 40;
                        })
                        .catchErrorAsyncFunctionOn(asyncRunner2, (Throwable error) ->
                        {
                            value1.set(true);
                            return asyncRunner1.schedule(() ->
                            {
                                value2.set("hello");
                                return 22;
                            });
                        });
                    test.assertNotNull(onErrorAsyncFunction);

                    asyncRunner1.await();
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(1, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                    test.assertFalse(onErrorAsyncFunction.isCompleted());
                    test.assertEqual(true, value1.get());
                    test.assertNull(value2.get());

                    asyncRunner1.await();
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                    test.assertTrue(onErrorAsyncFunction.isCompleted());
                    test.assertEqual(true, value1.get());
                    test.assertEqual("hello", value2.get());
                    test.assertEqual(22, onErrorAsyncFunction.awaitReturn());
                });
            });
        });
    }
    
    private static BasicAsyncFunction<Integer> create(AsyncRunner runner)
    {
        return new BasicAsyncFunction<>(new Value<>(runner), () -> 0);
    }

    private static BasicAsyncFunction<Integer> create(Test test)
    {
        return create(new ManualAsyncRunner());
    }

    private static BasicAsyncFunction<Integer> createScheduled(AsyncRunner runner)
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create(runner);
        basicAsyncFunction.schedule();
        return basicAsyncFunction;
    }
}
