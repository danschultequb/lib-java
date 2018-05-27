package qub;

public class BasicHttpClientTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(BasicHttpClient.class, () ->
        {
            HttpClientTests.test(runner, (Test test) ->
            {
                return new BasicHttpClient(test.getNetwork());
            });
        });
    }
}
