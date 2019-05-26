package qub;

public class JavaNetworkTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaNetwork.class, () ->
        {
            NetworkTests.test(runner, (Test test) -> new JavaNetwork(test.getClock()));
        });
    }
}
