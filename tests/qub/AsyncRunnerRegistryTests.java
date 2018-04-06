package qub;

public class AsyncRunnerRegistryTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(AsyncRunnerRegistry.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                test.assertNotNull(new AsyncRunnerRegistry());
            });

            runner.testGroup("getCurrentThreadAsyncRunner()", () ->
            {
                runner.test("with no registered runner", (Test test) ->
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

                });

                runner.test("with registered runner", (Test test) ->
                {
                    final AsyncRunner backupRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
                    final ManualAsyncRunner runner1 = new ManualAsyncRunner();
                    AsyncRunnerRegistry.setCurrentThreadAsyncRunner(runner1);
                    try
                    {
                        test.assertSame(runner1, AsyncRunnerRegistry.getCurrentThreadAsyncRunner());
                    }
                    finally
                    {
                        AsyncRunnerRegistry.removeCurrentThreadAsyncRunner();
                        if (backupRunner != null)
                        {
                            AsyncRunnerRegistry.setCurrentThreadAsyncRunner(backupRunner);
                        }
                    }
                });
            });
        });
    }
}
