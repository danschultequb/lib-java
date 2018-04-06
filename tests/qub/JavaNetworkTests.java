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
                    test.assertTrue(tcpClient.hasError());
                    test.assertEqual("remoteIPAddress cannot be null.", tcpClient.getErrorMessage());
                });

                runner.test("with -1 remotePort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPClient> tcpClient = network.createTCPClient(IPv4Address.parse("127.0.0.1"), -1);
                    test.assertTrue(tcpClient.hasError());
                    test.assertEqual("remotePort must be greater than 0.", tcpClient.getErrorMessage());
                });

                runner.test("with 0 remotePort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPClient> tcpClient = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 0);
                    test.assertTrue(tcpClient.hasError());
                    test.assertEqual("remotePort must be greater than 0.", tcpClient.getErrorMessage());
                });

                runner.test("with valid arguments but no server listening", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 80);
                    test.assertTrue(tcpClientResult.hasError());
                    test.assertEqual("Connection refused: connect", tcpClientResult.getErrorMessage());
                });

                runner.test("with valid arguments and server listening", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();
                    final JavaNetwork network = new JavaNetwork(asyncRunner);

                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5 };

                    final IPv4Address localhost = IPv4Address.parse("127.0.0.1");
                    final int port = 8080;

                    asyncRunner.schedule(() ->
                    {
                        final Result<TCPServer> tcpServer = network.createTCPServer(localhost, port);
                        test.assertFalse(tcpServer.hasError());
                        test.assertNotNull(tcpServer.getValue());

                        final Result<TCPClient> acceptedClient = tcpServer.getValue().accept();
                        test.assertFalse(acceptedClient.hasError());
                        test.assertNotNull(acceptedClient.getValue());

                        test.assertTrue(acceptedClient.getValue().write(acceptedClient.getValue().readBytes(bytes.length)));

                        test.assertTrue(acceptedClient.getValue().dispose().getValue());
                        test.assertTrue(tcpServer.getValue().dispose().getValue());
                    });

                    try
                    {
                        Thread.sleep(50);
                    }
                    catch (InterruptedException ignored)
                    {
                    }

                    final Result<TCPClient> tcpClient = network.createTCPClient(localhost, port);
                    test.assertFalse(tcpClient.hasError());
                    test.assertNotNull(tcpClient.getValue());

                    test.assertTrue(tcpClient.getValue().write(bytes));
                    test.assertEqual(bytes, tcpClient.getValue().readBytes(5));
                    final Result<Boolean> tcpClientDispose = tcpClient.getValue().dispose();
                    test.assertNotNull(tcpClientDispose);
                    test.assertTrue(tcpClientDispose.getValue(), tcpClientDispose.getErrorMessage());

                    try
                    {
                        Thread.sleep(50);
                    }
                    catch (InterruptedException ignored)
                    {
                    }
                });
            });

            runner.testGroup("createTCPServer(int)", () ->
            {
                runner.test("with -1 localPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(-1);
                    test.assertTrue(tcpServerResult.hasError());
                    test.assertEqual("localPort must be greater than 0.", tcpServerResult.getErrorMessage());
                });

                runner.test("with 0 localPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(0);
                    test.assertTrue(tcpServerResult.hasError());
                    test.assertEqual("localPort must be greater than 0.", tcpServerResult.getErrorMessage());
                });

                runner.test("with 80 localPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(null);
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(80);
                    test.assertFalse(tcpServerResult.hasError());
                    tcpServerResult.getValue().dispose();
                });
            });
        });
    }
}
