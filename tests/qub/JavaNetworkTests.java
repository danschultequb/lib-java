package qub;

public interface JavaNetworkTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaNetwork.class, () ->
        {
            NetworkTests.test(runner, (Test test) -> JavaNetwork.create(test.getClock()));
        });
    }
}
