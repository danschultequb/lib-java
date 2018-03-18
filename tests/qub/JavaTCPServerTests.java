package qub;

public class JavaTCPServerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaTCPServer.class, () ->
        {
            runner.testGroup("create(int, AsyncRunner)", () ->
            {
                runner.test("with -1 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(-1, null);
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.isError());
                    test.assertEqual("localPort must be greater than 0.", tcpServer.getErrorMessage());
                });
            });
        });
    }
}
