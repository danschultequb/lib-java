package qub;

public class JavaNetworkTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaNetwork.class, () ->
        {
            runner.testGroup("createTCPClient(IPv4Address,int)", () ->
            {
                runner.test("with null remoteIPAddress", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPClient> tcpClient = network.createTCPClient(null, 80);
                    test.assertTrue(tcpClient.isError());
                    test.assertEqual("remoteIPAddress cannot be null.", tcpClient.getErrorMessage());
                });

                runner.test("with -1 remotePort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPClient> tcpClient = network.createTCPClient(IPv4Address.parse("127.0.0.1"), -1);
                    test.assertTrue(tcpClient.isError());
                    test.assertEqual("remotePort must be greater than 0.", tcpClient.getErrorMessage());
                });

                runner.test("with 0 remotePort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPClient> tcpClient = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 0);
                    test.assertTrue(tcpClient.isError());
                    test.assertEqual("remotePort must be greater than 0.", tcpClient.getErrorMessage());
                });

                runner.test("with valid arguments but no server listening", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 80);
                    test.assertTrue(tcpClientResult.isError());
                    test.assertEqual("Connection refused: connect", tcpClientResult.getErrorMessage());
                });
            });

            runner.testGroup("createTCPServer(int)", () ->
            {
                runner.test("with -1 localPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(-1);
                    test.assertTrue(tcpServerResult.isError());
                    test.assertEqual("localPort must be greater than 0.", tcpServerResult.getErrorMessage());
                });

                runner.test("with 0 localPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(0);
                    test.assertTrue(tcpServerResult.isError());
                    test.assertEqual("localPort must be greater than 0.", tcpServerResult.getErrorMessage());
                });

                runner.test("with 80 localPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(80);
                    test.assertTrue(tcpServerResult.isSuccess());
                    tcpServerResult.getValue().dispose();
                });
            });
        });
    }
}
