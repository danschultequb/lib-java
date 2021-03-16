package qub;

public interface JavaNetworkTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaNetwork.class, () ->
        {
            NetworkTests.test(runner, JavaNetwork::create);
        });
    }
}
