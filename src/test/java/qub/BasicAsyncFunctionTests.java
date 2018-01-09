package qub;

public class BasicAsyncFunctionTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("BasicAsyncFunction<T>", new Action0()
        {
            @Override
            public void run()
            {
                BasicAsyncTaskTests.test(runner, new Function1<AsyncRunner, BasicAsyncTask>()
                {
                    @Override
                    public BasicAsyncTask run(AsyncRunner asyncRunner)
                    {
                        return create(asyncRunner);
                    }
                });
                
                runner.test("constructor()", new Action1<qub.Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                        final BasicAsyncFunction<Integer> basicAsyncFunction = new BasicAsyncFunction<>(runner, new Synchronization(), TestUtils.emptyFunction0);
                        test.assertEqual(0, runner.getScheduledTaskCount());
                        test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                        test.assertFalse(basicAsyncFunction.isCompleted());
                    }
                });
                
                runner.testGroup("then(Action1)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final BasicAsyncFunction<Integer> basicAsyncFunction = create();
                                final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.nullAction1);
                                test.assertNull(thenAsyncAction);
                                test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                            }
                        });

                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final BasicAsyncFunction<Integer> basicAsyncFunction = create();
                                final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.emptyAction1);
                                test.assertNotNull(thenAsyncAction);
                                test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                            }
                        });

                        runner.test("with non-null when completed", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);
                                runner.await();

                                final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.emptyAction1);
                                test.assertNotNull(thenAsyncAction);
                                test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                                test.assertEqual(1, runner.getScheduledTaskCount());
                            }
                        });
                    }
                });

                runner.testGroup("then(Function1)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final BasicAsyncFunction<Integer> basicAsyncFunction = create();
                                final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.nullFunction1);
                                test.assertNull(thenAsyncFunction);
                                test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                            }
                        });

                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final BasicAsyncFunction<Integer> basicAsyncFunction = create();
                                final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.emptyFunction1);
                                test.assertNotNull(thenAsyncFunction);
                                test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                            }
                        });

                        runner.test("with non-null when completed", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);
                                runner.await();

                                final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.emptyFunction1);
                                test.assertNotNull(thenAsyncFunction);
                                test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                                test.assertEqual(1, runner.getScheduledTaskCount());
                            }
                        });
                    }
                });

                runner.testGroup("thenOn(AsyncRunner,Action1)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                                final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);

                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyAction1);
                                test.assertNotNull(thenOnAsyncAction);
                                test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());

                                runner1.await();
                                test.assertEqual(1, runner2.getScheduledTaskCount());

                                runner2.await();
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                            }
                        });

                        runner.test("with non-null when completed", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                                final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);
                                runner1.await();
                                test.assertTrue(basicAsyncFunction.isCompleted());

                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyAction1);
                                test.assertNotNull(thenOnAsyncAction);
                                test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                                test.assertEqual(1, runner2.getScheduledTaskCount());

                                runner2.await();
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                            }
                        });
                    }
                });

                runner.testGroup("thenOn(AsyncRunner,Function1)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                                final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);

                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyFunction1);
                                test.assertNotNull(thenOnAsyncFunction);
                                test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());

                                runner1.await();
                                test.assertEqual(1, runner2.getScheduledTaskCount());

                                runner2.await();
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                            }
                        });

                        runner.test("with non-null when completed", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                                final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);
                                runner1.await();

                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyFunction1);
                                test.assertNotNull(thenOnAsyncFunction);
                                test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                                test.assertEqual(1, runner2.getScheduledTaskCount());

                                runner2.await();
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                            }
                        });
                    }
                });

                runner.testGroup("thenOn(AsyncRunner)", new Action0()
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
                                final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);
                                test.assertNull(basicAsyncFunction.thenOn(null));
                            }
                        });

                        runner.test("with same AsyncRunner", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);
                                test.assertSame(basicAsyncFunction, basicAsyncFunction.thenOn(runner));
                                test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                                test.assertEqual(1, runner.getScheduledTaskCount());
                            }
                        });

                        runner.test("with different AsyncRunner", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);

                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final AsyncFunction<Integer> thenAsyncAction = basicAsyncFunction.thenOn(runner2);
                                test.assertNotNull(thenAsyncAction);
                                test.assertNotSame(basicAsyncFunction, thenAsyncAction);
                                test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());

                                runner.await();
                                test.assertEqual(1, runner2.getScheduledTaskCount());

                                runner2.await();
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                            }
                        });
                    }
                });

                runner.test("awaitReturn()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final ParallelAsyncRunner runner = new ParallelAsyncRunner(new Synchronization());
                        final AsyncFunction<Integer> asyncFunction = runner.schedule(new Function0<Integer>()
                        {
                            @Override
                            public Integer run()
                            {
                                return 20;
                            }
                        });
                        test.assertEqual(20, asyncFunction.awaitReturn());
                        test.assertTrue(asyncFunction.isCompleted());

                        test.assertEqual(20, asyncFunction.awaitReturn());
                    }
                });
            }
        });
    }
    
    private static BasicAsyncFunction<Integer> create(AsyncRunner runner)
    {
        return new BasicAsyncFunction<>(runner, new Synchronization(), TestUtils.emptyFunction0);
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

    private static BasicAsyncFunction<Integer> create()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        return create(runner);
    }

    private static BasicAsyncFunction<Integer> createScheduled(AsyncRunner runner)
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create(runner);
        basicAsyncFunction.schedule();
        return basicAsyncFunction;
    }
}
