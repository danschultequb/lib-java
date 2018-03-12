package qub;

public class CurrentThreadAsyncRunnerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(CurrentThreadAsyncRunner.class, () ->
        {
            AsyncRunnerTests.test(runner, () -> new CurrentThreadAsyncRunner(new Synchronization()));

            runner.test("constructor()", test ->
            {
                final CurrentThreadAsyncRunner runner1 = new CurrentThreadAsyncRunner(new Synchronization());
                test.assertEqual(0, runner1.getScheduledTaskCount());
            });

            runner.testGroup("withRegistered()", () ->
            {
                runner.test("with Synchronization", test ->
                {
                    final AsyncRunner backupRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
                    try
                    {
                        final Synchronization synchronization = new Synchronization();
                        CurrentThreadAsyncRunner.withRegistered(synchronization, (CurrentThreadAsyncRunner runner1) ->
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

                runner.test("with Console", test ->
                {
                    final AsyncRunner backupRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
                    try
                    {
                        final Console console = new Console();
                        CurrentThreadAsyncRunner.withRegistered(console, (CurrentThreadAsyncRunner runner1) ->
                        {
                            test.assertNotNull(runner1);
                            test.assertSame(console.getSynchronization(), runner1.getSynchronization());
                            test.assertSame(runner1, AsyncRunnerRegistry.getCurrentThreadAsyncRunner());
                        });
                    }
                    finally
                    {
                        AsyncRunnerRegistry.setCurrentThreadAsyncRunner(backupRunner);
                    }
                });
            });
        });
    }
}
