package qub;

public class NetworkTests
{
    public static void test(TestRunner runner, Function1<Test,Network> creator)
    {
        runner.testGroup(Network.class, () ->
        {
            runner.testGroup("createTCPClient(IPv4Address,int)", () ->
            {
                runner.test("with null remoteIPAddress", (Test test) ->
                {
                    final Network network = creator.run(test);
                    test.assertThrows(() -> network.createTCPClient(null, 80));
                });

                runner.test("with -1 remotePort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    test.assertThrows(() -> network.createTCPClient(IPv4Address.parse("127.0.0.1"), -1));
                });

                runner.test("with 0 remotePort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    test.assertThrows(() -> network.createTCPClient(IPv4Address.parse("127.0.0.1"), 0));
                });

                runner.test("with valid arguments but no server listening", (Test test) ->
                {
                    final Network network = creator.run(test);
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 38827);
                    test.assertError(new java.net.ConnectException("Connection refused: connect"), tcpClientResult);
                });

                runner.test("with valid arguments and server listening", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();
                    final Network network = creator.run(test);

                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5 };

                    final int port = 8088;

                    final AsyncAction serverTask = asyncRunner.schedule(() ->
                    {
                        final Result<TCPServer> tcpServerResult = network.createTCPServer(IPv4Address.localhost, port);
                        test.assertSuccess(tcpServerResult);
                        try (final TCPServer tcpServer = tcpServerResult.getValue())
                        {
                            test.assertEqual(IPv4Address.localhost, tcpServer.getLocalIPAddress());
                            test.assertEqual(port, tcpServer.getLocalPort());
                            final Duration acceptTimeout = Duration.seconds(5);
                            final Result<TCPClient> acceptedClientResult = tcpServer.accept(acceptTimeout);
                            test.assertSuccess(acceptedClientResult);
                            try (final TCPClient acceptedClient = acceptedClientResult.getValue())
                            {
                                test.assertSuccess(bytes, acceptedClient.readBytes(bytes.length));
                                test.assertSuccess(bytes.length, acceptedClient.writeBytes(bytes));
                            }
                        }
                    });

                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.localhost, port, Duration.seconds(5));
                        test.assertSuccess(tcpClientResult);
                        try (final TCPClient tcpClient = tcpClientResult.getValue())
                        {
                            test.assertEqual(IPv4Address.localhost, tcpClient.getLocalIPAddress());
                            test.assertNotEqual(port, tcpClient.getLocalPort());
                            test.assertEqual(IPv4Address.localhost, tcpClient.getRemoteIPAddress());
                            test.assertEqual(port, tcpClient.getRemotePort());
                            test.assertSuccess(bytes.length, tcpClient.writeBytes(bytes));
                            test.assertSuccess(bytes, tcpClient.readBytes(bytes.length));
                        }
                    });

                    asyncRunner.awaitAll(clientTask, serverTask);
                });
            });

            runner.testGroup("createTCPServer(int)", () ->
            {
                runner.test("with -1 localPort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    test.assertThrows(() -> network.createTCPServer(-1));
                });

                runner.test("with 0 localPort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    test.assertThrows(() -> network.createTCPServer(0));
                });

                runner.test("with 8088 localPort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(8088);
                    test.assertSuccess(tcpServerResult);
                    test.assertSuccess(tcpServerResult.getValue().dispose());
                });
            });
        });
    }
}
