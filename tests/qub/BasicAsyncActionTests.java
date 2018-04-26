package qub;

public class BasicAsyncActionTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(BasicAsyncAction.class, () ->
        {
            BasicAsyncTaskTests.test(runner, BasicAsyncActionTests::create);

            runner.test("constructor(Getable<AsyncRunner>,Getable<AsyncTask>,Action0)", (Test test) ->
            {
                final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                final BasicAsyncAction basicAsyncAction = new BasicAsyncAction(new Value<>(asyncRunner), new Array<AsyncTask>(0), () -> {});
                test.assertSame(asyncRunner, basicAsyncAction.getAsyncRunner());
                test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                test.assertFalse(basicAsyncAction.isCompleted());
            });

            runner.testGroup("thenOn(AsyncRunner)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = create(asyncRunner);
                    test.assertNull(basicAsyncAction.thenOn(null));
                });

                runner.test("with same AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner);
                    test.assertSame(basicAsyncAction, basicAsyncAction.thenOn(asyncRunner));
                    test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());
                    basicAsyncAction.await();
                });

                runner.test("with different AsyncRunner", (Test test) ->
                {
                    final AsyncRunner mainRunner = test.getMainAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(mainRunner);

                    final AsyncRunner otherRunner = new ManualAsyncRunner();
                    final AsyncAction thenAsyncAction = basicAsyncAction.thenOn(otherRunner);
                    test.assertNotNull(thenAsyncAction);
                    test.assertNotSame(basicAsyncAction, thenAsyncAction);
                    test.assertEqual(1, basicAsyncAction.getPausedTaskCount());

                    mainRunner.await();
                    test.assertEqual(1, otherRunner.getScheduledTaskCount());

                    otherRunner.await();
                    test.assertEqual(0, otherRunner.getScheduledTaskCount());
                });
            });

            runner.testGroup("catchError(Action1<Throwable>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = create(asyncRunner);
                    final AsyncAction onErrorAsyncAction = basicAsyncAction.catchError(null);
                    test.assertNull(onErrorAsyncAction);
                    test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                });

                runner.test("with non-throwing parent", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction onErrorAsyncAction = basicAsyncAction
                        .catchError((Throwable error) ->
                        {
                            value.set(2);
                        });
                    test.assertNotNull(onErrorAsyncAction);

                    asyncRunner.await();
                    test.assertTrue(onErrorAsyncAction.isCompleted());
                    test.assertFalse(value.hasValue());
                });

                runner.test("with throwing parent", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner);

                    final Value<Boolean> value = new Value<>();
                    final AsyncAction onErrorAsyncAction = basicAsyncAction
                        .then((Action0)() ->
                        {
                            throw new RuntimeException("abc");
                        })
                        .catchError((Throwable error) ->
                        {
                            value.set(true);
                        });
                    test.assertNotNull(onErrorAsyncAction);

                    asyncRunner.await();
                    test.assertTrue(onErrorAsyncAction.isCompleted());
                    test.assertEqual(true, value.get());
                });
            });

            runner.testGroup("catchErrorOn(AsyncRunner,Action1<Throwable>)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final BasicAsyncAction basicAsyncAction = create(test.getMainAsyncRunner());
                    final AsyncAction onErrorAsyncAction = basicAsyncAction.catchErrorOn(null, (Throwable eror) -> {});
                    test.assertNull(onErrorAsyncAction);
                    test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                });

                runner.test("with null Action", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainAsyncRunner();
                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner1);
                    final AsyncAction onErrorAsyncAction = basicAsyncAction.catchErrorOn(asyncRunner2, (Action1<Throwable>)null);
                    test.assertNull(onErrorAsyncAction);
                    test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                    basicAsyncAction.await();
                });

                runner.test("with non-throwing parent", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainAsyncRunner();
                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner();

                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner1);

                    final Value<Boolean> value = new Value<>();
                    final AsyncAction onErrorAsyncAction = basicAsyncAction.catchErrorOn(asyncRunner2, (Throwable error) ->
                    {
                        value.set(true);
                    });
                    test.assertNotNull(onErrorAsyncAction);

                    asyncRunner1.await();
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                    test.assertTrue(onErrorAsyncAction.isCompleted());
                    test.assertFalse(value.hasValue());
                });

                runner.test("with throwing parent", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainAsyncRunner();
                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner();

                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner1);

                    final Value<Boolean> value = new Value<>();
                    final AsyncAction onErrorAsyncAction = basicAsyncAction
                        .then((Action0)() ->
                        {
                            throw new RuntimeException("abc");
                        })
                        .catchErrorOn(asyncRunner2, (Throwable error) ->
                        {
                            value.set(true);
                        });
                    test.assertNotNull(onErrorAsyncAction);

                    asyncRunner1.await();
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                    test.assertTrue(onErrorAsyncAction.isCompleted());
                    test.assertEqual(true, value.get());
                });
            });

            runner.testGroup("catchErrorAsyncAction(Function1<Throwable,AsyncAction>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final BasicAsyncAction basicAsyncAction = create(test.getMainAsyncRunner());
                    final AsyncAction onErrorAsyncAction = basicAsyncAction.catchErrorAsyncAction(null);
                    test.assertNull(onErrorAsyncAction);
                    test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                });

                runner.test("with non-throwing parent", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner);

                    final Value<Boolean> value1 = new Value<>();
                    final Value<String> value2 = new Value<>();
                    final AsyncAction onErrorAsyncAction = basicAsyncAction.catchErrorAsyncAction((Throwable error) ->
                    {
                        value1.set(true);
                        return asyncRunner.schedule(() ->
                        {
                            value2.set("hello");
                        });
                    });
                    test.assertNotNull(onErrorAsyncAction);

                    asyncRunner.await();
                    test.assertTrue(onErrorAsyncAction.isCompleted());
                    test.assertFalse(value1.hasValue());
                    test.assertFalse(value2.hasValue());
                });

                runner.test("with throwing parent", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner);

                    final Value<Boolean> value1 = new Value<>();
                    final Value<String> value2 = new Value<>();
                    final AsyncAction onErrorAsyncAction = basicAsyncAction
                        .then((Action0)() ->
                        {
                            throw new RuntimeException("abc");
                        })
                        .catchErrorAsyncAction((Throwable error) ->
                        {
                            value1.set(true);
                            return asyncRunner.schedule(() ->
                            {
                                value2.set("hello");
                            });
                        });
                    test.assertNotNull(onErrorAsyncAction);

                    asyncRunner.await();
                    test.assertTrue(onErrorAsyncAction.isCompleted());
                    test.assertEqual(true, value1.get());
                    test.assertEqual("hello", value2.get());
                });
            });

            runner.testGroup("catchErrorAsyncActionOn(AsyncRunner,Function1<Throwable,AsyncAction>)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner);
                    final AsyncAction onErrorAsyncAction = basicAsyncAction.catchErrorAsyncActionOn(null, (Throwable error) -> asyncRunner.schedule(() -> {}));
                    test.assertNull(onErrorAsyncAction);
                    test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                    basicAsyncAction.await();
                });

                runner.test("with null function", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner);
                    final AsyncAction onErrorAsyncAction = basicAsyncAction.catchErrorAsyncActionOn(asyncRunner, null);
                    test.assertNull(onErrorAsyncAction);
                    test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                    basicAsyncAction.await();
                });

                runner.test("with non-throwing parent", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainAsyncRunner();
                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner1);

                    final Value<Boolean> value1 = new Value<>();
                    final Value<String> value2 = new Value<>();
                    final AsyncAction onErrorAsyncAction = basicAsyncAction.catchErrorAsyncActionOn(asyncRunner2, (Throwable error) ->
                    {
                        value1.set(true);
                        return asyncRunner1.schedule(() ->
                        {
                            value2.set("hello");
                        });
                    });
                    test.assertNotNull(onErrorAsyncAction);

                    asyncRunner1.await();

                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();

                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                    test.assertTrue(onErrorAsyncAction.isCompleted());
                    test.assertFalse(value1.hasValue());
                    test.assertFalse(value2.hasValue());
                });

                runner.test("with throwing parent", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainAsyncRunner();
                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner1);

                    final Value<Boolean> value1 = new Value<>();
                    final Value<String> value2 = new Value<>();
                    final AsyncAction onErrorAsyncAction = basicAsyncAction
                        .then((Action0)() ->
                        {
                            throw new RuntimeException("abc");
                        })
                        .catchErrorAsyncActionOn(asyncRunner2, (Throwable error) ->
                        {
                            value1.set(true);
                            return asyncRunner1.schedule(() ->
                            {
                                value2.set("hello");
                            });
                        });
                    test.assertNotNull(onErrorAsyncAction);

                    asyncRunner1.await();
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(1, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                    test.assertFalse(onErrorAsyncAction.isCompleted());
                    test.assertEqual(true, value1.get());
                    test.assertNull(value2.get());

                    asyncRunner1.await();
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                    test.assertTrue(onErrorAsyncAction.isCompleted());
                    test.assertEqual(true, value1.get());
                    test.assertEqual("hello", value2.get());
                });
            });
        });
    }

    private static BasicAsyncAction create(AsyncRunner asyncRunner)
    {
        return new BasicAsyncAction(new Value<>(asyncRunner), new Array<AsyncTask>(0), () -> {});
    }

    private static BasicAsyncAction createScheduled(AsyncRunner runner)
    {
        final BasicAsyncAction basicAsyncAction = create(runner);
        basicAsyncAction.schedule();
        return basicAsyncAction;
    }
}
