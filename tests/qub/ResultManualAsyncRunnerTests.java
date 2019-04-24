package qub;

public interface ResultManualAsyncRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ResultManualAsyncRunner.class, () ->
        {
            ResultAsyncRunnerTests.test(runner, ResultManualAsyncRunner::new);

            runner.test("constructor()", (Test test) ->
            {
                final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                test.assertNotNull(asyncRunner);
            });
        });
    }
}
