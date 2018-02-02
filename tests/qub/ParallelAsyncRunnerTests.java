package qub;

public class ParallelAsyncRunnerTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("ParallelAsyncRunner", new Action0()
        {
            @Override
            public void run()
            {
                AsyncRunnerTests.test(runner, new Function0<AsyncRunner>()
                {
                    @Override
                    public AsyncRunner run()
                    {
                        return new ParallelAsyncRunner(new Synchronization());
                    }
                });

                runner.test("constructor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final ParallelAsyncRunner runner = new ParallelAsyncRunner(new Synchronization());
                        test.assertEqual(0, runner.getScheduledTaskCount());
                    }
                });
            }
        });
    }
}
