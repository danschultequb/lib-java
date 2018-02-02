package qub;

public class AsyncRunnerRegistryTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("AsyncRunnerRegistry", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        test.assertNotNull(new AsyncRunnerRegistry());
                    }
                });

                runner.testGroup("getCurrentThreadAsyncRunner()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no registered runner", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final AsyncRunner backupRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
                                AsyncRunnerRegistry.removeCurrentThreadAsyncRunner();
                                try
                                {
                                    test.assertNull(AsyncRunnerRegistry.getCurrentThreadAsyncRunner());
                                }
                                finally
                                {
                                    AsyncRunnerRegistry.setCurrentThreadAsyncRunner(backupRunner);
                                }

                            }
                        });

                        runner.test("with registered runner", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final AsyncRunner backupRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner(new Synchronization());
                                AsyncRunnerRegistry.setCurrentThreadAsyncRunner(runner);
                                try
                                {
                                    test.assertSame(runner, AsyncRunnerRegistry.getCurrentThreadAsyncRunner());
                                }
                                finally
                                {
                                    AsyncRunnerRegistry.removeCurrentThreadAsyncRunner();
                                    if (backupRunner != null)
                                    {
                                        AsyncRunnerRegistry.setCurrentThreadAsyncRunner(backupRunner);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
