package qub;

public class BasicAsyncTaskTests
{
    public static void test(TestRunner runner, Function1<AsyncRunner,BasicAsyncTask> creator)
    {
        runner.testGroup(BasicAsyncTask.class, () ->
        {
            runner.testGroup("then(Action0)", () ->
            {
                runner.test("with null", test ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncAction thenAsyncAction = basicAsyncTask.then((Action0)null);
                    test.assertNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null", test ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncAction thenAsyncAction = basicAsyncTask.then(() -> {});
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null when completed", test ->
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
                runner.test("with null", test ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(null);
                    test.assertNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null", test ->
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

                runner.test("with non-null when completed", test ->
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
                runner.test("with null", test ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then((Function0<Integer>)null);
                    test.assertNull(thenAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null", test ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(() -> 0);
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null when completed", test ->
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
                runner.test("with null", test ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncFunction(null);
                    test.assertNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null", test ->
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

                runner.test("with non-null when completed", test ->
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
                runner.test("with non-null Action0", test ->
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

                runner.test("with non-null Action0 when completed", test ->
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

            runner.testGroup("thenOnAsyncAction()", () ->
            {
                runner.test("with null AsyncRunner", test ->
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

                runner.test("with null Function", test ->
                {
                    final CurrentThreadAsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = create(creator);

                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOnAsyncAction(runner12, null);
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null Function", test ->
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

                runner.test("with non-null Function when completed", test ->
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
            });

            runner.testGroup("thenOn(AsyncRunner,Function0)", () ->
            {
                runner.test("with non-null Function0", test ->
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

                runner.test("with non-null Function0 when completed", test ->
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

            runner.testGroup("thenOnAsyncFunction()", () ->
            {
                runner.test("with null Function0", test ->
                {
                    final CurrentThreadAsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = create(creator);

                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOnAsyncAction(runner12, null);
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null Function0", test ->
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

                runner.test("with non-null Function0 when completed", test ->
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
