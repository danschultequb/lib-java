package qub;

public class ManualAsyncRunnerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ManualAsyncRunner.class, () ->
        {
            AsyncRunnerTests.test(runner, ManualAsyncRunner::new);

            runner.test("constructor()", (Test test) ->
            {
                final ManualAsyncRunner runner1 = new ManualAsyncRunner();
                test.assertEqual(0, runner1.getScheduledTaskCount());
            });
        });
    }
}
