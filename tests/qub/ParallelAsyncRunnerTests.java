package qub;

public class ParallelAsyncRunnerTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(ParallelAsyncRunner.class, () ->
        {
            AsyncRunnerTests.test(runner, ParallelAsyncRunner::new);

            runner.test("constructor()", (Test test) ->
            {
                try (final ParallelAsyncRunner runner1 = new ParallelAsyncRunner())
                {
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                }
            });
        });
    }
}
