package qub;

public class BasicAsyncActionTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("BasicAsyncAction", new Action0()
        {
            @Override
            public void run()
            {
                BasicAsyncTaskTests.test(runner, new Function1<AsyncRunner,BasicAsyncTask>()
                {
                    @Override
                    public BasicAsyncTask run(AsyncRunner asyncRunner)
                    {
                        return create(asyncRunner);
                    }
                });

                runner.test("constructor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                        final BasicAsyncAction basicAsyncAction = new BasicAsyncAction(runner, TestUtils.emptyAction0);
                        test.assertEqual(0, runner.getScheduledTaskCount());
                        test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                        test.assertFalse(basicAsyncAction.isCompleted());
                    }
                });

                runner.testGroup("thenOn()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null AsyncRunner", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncAction basicAsyncAction = create(runner);
                                test.assertNull(basicAsyncAction.thenOn(null));
                            }
                        });

                        runner.test("with same AsyncRunner", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncAction basicAsyncAction = createScheduled(runner);
                                test.assertSame(basicAsyncAction, basicAsyncAction.thenOn(runner));
                                test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                                test.assertEqual(1, runner.getScheduledTaskCount());
                            }
                        });

                        runner.test("with different AsyncRunner", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncAction basicAsyncAction = createScheduled(runner);

                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final AsyncAction thenAsyncAction = basicAsyncAction.thenOn(runner2);
                                test.assertNotNull(thenAsyncAction);
                                test.assertNotSame(basicAsyncAction, thenAsyncAction);
                                test.assertEqual(1, basicAsyncAction.getPausedTaskCount());

                                runner.await();
                                test.assertEqual(1, runner2.getScheduledTaskCount());

                                runner2.await();
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                            }
                        });
                    }
                });
            }
        });
    }

    private static BasicAsyncAction create(AsyncRunner asyncRunner)
    {
        return new BasicAsyncAction(asyncRunner, TestUtils.emptyAction0);
    }

    private static CurrentThreadAsyncRunner createCurrentThreadAsyncRunner()
    {
        final Synchronization synchronization = new Synchronization();
        return new CurrentThreadAsyncRunner(new Function0<Synchronization>()
        {
            @Override
            public Synchronization run()
            {
                return synchronization;
            }
        });
    }

    private static BasicAsyncAction createScheduled(AsyncRunner runner)
    {
        final BasicAsyncAction basicAsyncAction = create(runner);
        basicAsyncAction.schedule();
        return basicAsyncAction;
    }
}
