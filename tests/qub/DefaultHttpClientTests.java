package qub;

public class DefaultHttpClientTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(DefaultHttpClient.class, () ->
        {
            runner.testGroup("create(TCPClient)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Result<DefaultHttpClient> result = DefaultHttpClient.create(null);
                    test.assertError(new IllegalArgumentException("tcpClient cannot be null."), result);
                });
            });
        });
    }
}
