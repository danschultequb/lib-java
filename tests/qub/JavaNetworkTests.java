package qub;

public class JavaNetworkTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaNetwork.class, () ->
        {
            runner.testGroup("createTCPClient(IPv4Address,int)", () ->
            {
                runner.test("with null targetIPAddress", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork();
                    final Result<TCPClient> tcpClient = network.createTCPClient(null, 80);
                    test.assertTrue(tcpClient.isError());
                    test.assertEqual("targetIPAddress cannot be null.", tcpClient.getErrorMessage());
                });

                runner.test("with -1 targetPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork();
                    final Result<TCPClient> tcpClient = network.createTCPClient(IPv4Address.parse("127.0.0.1"), -1);
                    test.assertTrue(tcpClient.isError());
                    test.assertEqual("targetPort must be greater than 0.", tcpClient.getErrorMessage());
                });

                runner.test("with 0 targetPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork();
                    final Result<TCPClient> tcpClient = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 0);
                    test.assertTrue(tcpClient.isError());
                    test.assertEqual("targetPort must be greater than 0.", tcpClient.getErrorMessage());
                });

                runner.test("with valid arguments but no server listening", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork();
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 80);
                    test.assertTrue(tcpClientResult.isError());
                    test.assertEqual("Connection refused: connect", tcpClientResult.getErrorMessage());
                });
            });
        });
    }
}
