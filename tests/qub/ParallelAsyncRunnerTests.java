package qub;

public class ParallelAsyncRunnerTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("ParallelAsyncRunner", () ->
        {
            AsyncRunnerTests.test(runner, () -> new ParallelAsyncRunner(new Synchronization()));

            runner.test("constructor()", (Test test) ->
            {
                try (final ParallelAsyncRunner runner1 = new ParallelAsyncRunner(new Synchronization()))
                {
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                }
            });
        });
    }
}
