package qub;

public interface FakeNetworkTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeNetwork.class, () ->
        {
            NetworkTests.test(runner, FakeNetwork::create);
        });
    }
}
