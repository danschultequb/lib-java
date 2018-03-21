package qub;

public class ManualAsyncRunnerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ManualAsyncRunner.class, () ->
        {
            AsyncRunnerTests.test(runner, () -> new ManualAsyncRunner(new Synchronization()));

            runner.test("constructor()", (Test test) ->
            {
                final ManualAsyncRunner runner1 = new ManualAsyncRunner(new Synchronization());
                test.assertEqual(0, runner1.getScheduledTaskCount());
            });

            runner.testGroup("withRegistered()", () ->
            {
                runner.test("with Synchronization", (Test test) ->
                {
                    final AsyncRunner backupRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
                    try
                    {
                        final Synchronization synchronization = new Synchronization();
                        ManualAsyncRunner.withRegistered(synchronization, (ManualAsyncRunner runner1) ->
                        {
                            test.assertNotNull(runner1);
                            test.assertSame(synchronization, runner1.getSynchronization());
                            test.assertSame(runner1, AsyncRunnerRegistry.getCurrentThreadAsyncRunner());
                        });
                    }
                    finally
                    {
                        AsyncRunnerRegistry.setCurrentThreadAsyncRunner(backupRunner);
                    }
                });

                runner.test("with Console", (Test test) ->
                {
                    final Console console = new Console();
                    ManualAsyncRunner.withRegistered(console, (ManualAsyncRunner mainRunner) ->
                    {
                        test.assertNotNull(mainRunner);
                        test.assertSame(console.getSynchronization(), mainRunner.getSynchronization());
                        test.assertSame(mainRunner, AsyncRunnerRegistry.getCurrentThreadAsyncRunner());
                    });
                });
            });
        });
    }
}
