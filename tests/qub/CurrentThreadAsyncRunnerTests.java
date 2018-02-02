package qub;

public class CurrentThreadAsyncRunnerTests extends AsyncRunnerTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("CurrentThreadAsyncRunner", new Action0()
        {
            @Override
            public void run()
            {
                AsyncRunnerTests.test(runner, new Function0<AsyncRunner>()
                {
                    @Override
                    public AsyncRunner run()
                    {
                        return new CurrentThreadAsyncRunner(new Synchronization());
                    }
                });

                runner.test("constructor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner(new Synchronization());
                        test.assertEqual(0, runner.getScheduledTaskCount());
                    }
                });

                runner.testGroup("withRegistered()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with Synchronization", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
                            {
                                final AsyncRunner backupRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
                                try
                                {
                                    final Synchronization synchronization = new Synchronization();
                                    CurrentThreadAsyncRunner.withRegistered(synchronization, new Action1<CurrentThreadAsyncRunner>()
                                    {
                                        @Override
                                        public void run(CurrentThreadAsyncRunner runner)
                                        {
                                            test.assertNotNull(runner);
                                            test.assertSame(synchronization, runner.getSynchronization());
                                            test.assertSame(runner, AsyncRunnerRegistry.getCurrentThreadAsyncRunner());
                                        }
                                    });
                                }
                                finally
                                {
                                    AsyncRunnerRegistry.setCurrentThreadAsyncRunner(backupRunner);
                                }
                            }
                        });

                        runner.test("with Console", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
                            {
                                final AsyncRunner backupRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
                                try
                                {
                                    final Console console = new Console();
                                    CurrentThreadAsyncRunner.withRegistered(console, new Action1<CurrentThreadAsyncRunner>()
                                    {
                                        @Override
                                        public void run(CurrentThreadAsyncRunner runner)
                                        {
                                            test.assertNotNull(runner);
                                            test.assertSame(console.getSynchronization(), runner.getSynchronization());
                                            test.assertSame(runner, AsyncRunnerRegistry.getCurrentThreadAsyncRunner());
                                        }
                                    });
                                }
                                finally
                                {
                                    AsyncRunnerRegistry.setCurrentThreadAsyncRunner(backupRunner);
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
