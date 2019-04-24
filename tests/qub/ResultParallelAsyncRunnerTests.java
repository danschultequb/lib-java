package qub;

public interface ResultParallelAsyncRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ResultParallelAsyncRunner.class, () ->
        {
            ResultAsyncRunnerTests.test(runner, ResultParallelAsyncRunner::new);

            runner.test("constructor()", (Test test) ->
            {
                final ResultParallelAsyncRunner asyncRunner = new ResultParallelAsyncRunner();
                test.assertNotNull(asyncRunner);
            });
        });
    }
}
