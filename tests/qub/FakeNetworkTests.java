package qub;

public class FakeNetworkTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(FakeNetwork.class, () ->
        {
            NetworkTests.test(runner, (Test test) -> new FakeNetwork(test.getParallelAsyncRunner()));
        });
    }
}
