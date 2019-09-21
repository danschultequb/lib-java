package qub;

public interface RealProcessRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(RealProcessRunner.class, () ->
        {
            ProcessRunnerTests.test(runner, (Test test) -> new RealProcessRunner(test.getParallelAsyncRunner()));
        });
    }
}
