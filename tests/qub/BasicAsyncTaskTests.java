package qub;

public class BasicAsyncTaskTests
{
    public static void test(TestRunner runner, Function1<AsyncRunner,BasicAsyncTask> creator)
    {
        runner.testGroup(BasicAsyncTask.class, () ->
        {
            runner.testGroup("then(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncAction thenAsyncAction = basicAsyncTask.then((Action0)null);
                    test.assertNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncAction thenAsyncAction = basicAsyncTask.then(() -> {});
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner12);
                    runner12.await();
                    test.assertTrue(basicAsyncTask.isCompleted());

                    final AsyncAction thenAsyncAction = basicAsyncTask.then(() -> {});

                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner12.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenAsyncAction()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(null);
                    test.assertNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner12);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(() ->
                    {
                        return runner12.schedule(() -> value.set(5));
                    });
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());

                    runner12.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, runner12.getScheduledTaskCount());
                    test.assertEqual(5, value.get());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner12);
                    runner12.await();

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(() ->
                    {
                        return runner12.schedule(() -> value.set(5));
                    });
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner12.getScheduledTaskCount());

                    runner12.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, runner12.getScheduledTaskCount());
                    test.assertEqual(5, value.get());
                });
            });

            runner.testGroup("then(Function0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then((Function0<Integer>)null);
                    test.assertNull(thenAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(() -> 0);
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner12);
                    runner12.await();
                    test.assertTrue(basicAsyncTask.isCompleted());

                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(() -> 0);

                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner12.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenAsyncFunction()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncFunction(null);
                    test.assertNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner12);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenAsyncAction = basicAsyncTask
                        .thenAsyncFunction(() ->
                        {
                            return runner12.schedule(() ->
                            {
                                value.set(5);
                                return 6;
                            });
                        })
                        .then((Integer asyncFunctionReturnValue) ->
                        {
                            test.assertEqual(6, asyncFunctionReturnValue);
                        });
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());

                    runner12.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, runner12.getScheduledTaskCount());
                    test.assertEqual(5, value.get());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner12);
                    runner12.await();

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenAsyncAction = basicAsyncTask
                        .thenAsyncFunction(() ->
                        {
                            return runner12.schedule(() ->
                            {
                                value.set(5);
                                return 6;
                            });
                        })
                        .then((Integer asyncFunctionReturnValue) ->
                        {
                            test.assertEqual(6, asyncFunctionReturnValue);
                        });
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner12.getScheduledTaskCount());

                    runner12.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, runner12.getScheduledTaskCount());
                    test.assertEqual(5, value.get());
                });
            });

            runner.testGroup("thenOn(AsyncRunner,Action0)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = null;
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, () -> {});
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());

                    runner1.await();
                });

                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, (Action0)null);
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());

                    runner1.await();
                });

                runner.test("with non-null Action0", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, () -> {});
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());

                    runner1.await();
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                });

                runner.test("with non-null Action0 when completed", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);
                    runner1.await();

                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, () -> {});
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenOnAsyncAction(AsyncRunner,Function0<AsyncAction>)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = create(creator);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOnAsyncAction(null, () ->
                    {
                        return runner12.schedule(() ->
                        {
                            value.set(5);
                        });
                    });
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with null Function", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = create(creator);

                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOnAsyncAction(runner12, null);
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null Function", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenOnAsyncAction = basicAsyncTask
                        .thenOnAsyncAction(runner2, () ->
                        {
                            return runner3.schedule(() ->
                            {
                                value.set(4);
                            });
                        })
                        .then(() ->
                        {
                            test.assertEqual(4, value.get());
                        });
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner1.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner2.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(1, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner3.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertEqual(4, value.get());
                });

                runner.test("with non-null Function when completed", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);
                    runner1.await();

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenOnAsyncAction = basicAsyncTask
                        .thenOnAsyncAction(runner2, () ->
                        {
                            return runner3.schedule(() ->
                            {
                                value.set(4);
                            });
                        })
                        .then(() ->
                        {
                            test.assertEqual(4, value.get());
                        });
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner2.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(1, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner3.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertEqual(4, value.get());
                });
                
                runner.test("with multiple then tasks targeting different AsyncRunners", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final Value<Integer> value1 = new Value<>();
                    final AsyncAction thenOnAsyncAction1 = basicAsyncTask
                        .thenOnAsyncAction(runner2, () ->
                        {
                            return runner3.schedule(() ->value1.set(4));
                        })
                        .thenOn(runner3, () ->
                        {
                            test.assertEqual(4, value1.get());
                        });

                    final Value<Integer> value2 = new Value<>();
                    final AsyncAction thenOnAsyncAction2 = basicAsyncTask
                        .thenOnAsyncAction(runner3, () ->
                        {
                            return runner2.schedule(() -> value2.set(5));
                        })
                        .thenOn(runner2, () ->
                        {
                            test.assertEqual(5, value2.get());
                        });

                    test.assertNotNull(thenOnAsyncAction1);
                    test.assertNotNull(thenOnAsyncAction2);
                    test.assertEqual(2, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value1.hasValue());
                    test.assertFalse(value2.hasValue());

                    runner1.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertEqual(1, runner3.getScheduledTaskCount());
                    test.assertFalse(value1.hasValue());
                    test.assertFalse(value2.hasValue());

                    runner2.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(2, runner3.getScheduledTaskCount());
                    test.assertFalse(value1.hasValue());
                    test.assertFalse(value2.hasValue());

                    runner3.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertEqual(4, value1.get());
                    test.assertFalse(value2.hasValue());

                    runner2.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertEqual(4, value1.get());
                    test.assertEqual(5, value2.get());
                });
            });

            runner.testGroup("thenOn(AsyncRunner,Function0)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(null, () -> 0);
                    test.assertNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());

                    runner1.await();
                });

                runner.test("with null Function0", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(runner2, (Function0<Integer>)null);
                    test.assertNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());

                    runner1.await();
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                });

                runner.test("with non-null Function0", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(runner2, () -> 0);
                    test.assertNotNull(thenOnAsyncFunction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());

                    runner1.await();
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                });

                runner.test("with non-null Function0", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(runner2, () -> 0);
                    test.assertNotNull(thenOnAsyncFunction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());

                    runner1.await();
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                });

                runner.test("with non-null Function0 when completed", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);
                    runner1.await();

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(runner2, () -> 0);
                    test.assertNotNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenOnAsyncFunction(AsyncRunner,Function0<AsyncFunction<T>>)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final CurrentThreadAsyncRunner asyncRunner = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = create(creator);

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOnAsyncFunction(null, () ->
                    {
                        return asyncRunner.schedule(() -> 10);
                    });
                    test.assertNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with null Function0", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = create(creator);

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOnAsyncFunction(runner12, null);
                    test.assertNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null Function0", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenOnAsyncAction = basicAsyncTask
                        .thenOnAsyncFunction(runner2, () ->
                        {
                            return runner3.schedule(() ->
                            {
                                value.set(4);
                                return 5;
                            });
                        })
                        .then((Integer asyncFunctionReturnValue) ->
                        {
                            test.assertEqual(5, asyncFunctionReturnValue);
                        });
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner1.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner2.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(1, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner3.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertEqual(4, value.get());
                });

                runner.test("with non-null Function0 when completed", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);
                    runner1.await();

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenOnAsyncAction = basicAsyncTask
                        .thenOnAsyncFunction(runner2, () ->
                        {
                            return runner3.schedule(() ->
                            {
                                value.set(4);
                                return 5;
                            });
                        })
                        .then((Integer asyncFunctionReturnValue) ->
                        {
                            test.assertEqual(5, asyncFunctionReturnValue);
                        });
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner2.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(1, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner3.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertEqual(4, value.get());
                });
            });
        });
    }

    private static CurrentThreadAsyncRunner createCurrentThreadAsyncRunner()
    {
        final Synchronization synchronization = new Synchronization();
        return new CurrentThreadAsyncRunner(() -> synchronization);
    }

    private static BasicAsyncTask create(Function1<AsyncRunner,BasicAsyncTask> creator)
    {
        return creator.run(createCurrentThreadAsyncRunner());
    }

    private static BasicAsyncTask createScheduled(Function1<AsyncRunner,BasicAsyncTask> creator, AsyncRunner runner)
    {
        final BasicAsyncTask basicAsyncAction = creator.run(runner);
        basicAsyncAction.schedule();
        return basicAsyncAction;
    }
}
