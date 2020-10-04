package qub;

public interface NetworkTests
{
    static void test(TestRunner runner, Function1<Test,Network> creator)
    {
        runner.testGroup(Network.class, () ->
        {
            runner.testGroup("createTCPClient(IPv4Address,int)", () ->
            {
                runner.test("with null remoteIPAddress", (Test test) ->
                {
                    final Network network = creator.run(test);
                    test.assertThrows(() -> network.createTCPClient(null, 80),
                        new PreConditionFailure("remoteIPAddress cannot be null."));
                });

                runner.test("with -1 remotePort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    test.assertThrows(() -> network.createTCPClient(IPv4Address.localhost, -1),
                        new PreConditionFailure("remotePort (-1) must be between 1 and 65535."));
                });

                runner.test("with 0 remotePort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    test.assertThrows(() -> network.createTCPClient(IPv4Address.localhost, 0),
                        new PreConditionFailure("remotePort (0) must be between 1 and 65535."));
                });

                runner.test("with valid arguments but no server listening", (Test test) ->
                {
                    final Network network = creator.run(test);
                    test.assertThrows(() -> network.createTCPClient(IPv4Address.localhost, 38827).await(),
                        new java.net.ConnectException("Connection refused: connect"));
                });

                runner.test("with valid arguments and server listening", runner.skip("flaky test for FakeNetwork"), (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();
                    final Network network = creator.run(test);

                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5 };

                    final int port = 8088;
                    final DateTime timeout = test.getClock().getCurrentDateTime().plus(Duration.seconds(5));

                    final Result<Void> serverTask = asyncRunner.schedule(() ->
                    {
                        try (final TCPServer tcpServer = network.createTCPServer(IPv4Address.localhost, port).await())
                        {
                            test.assertEqual(IPv4Address.localhost, tcpServer.getLocalIPAddress());
                            test.assertEqual(port, tcpServer.getLocalPort());
                            try (final TCPClient acceptedClient = tcpServer.accept(timeout).await())
                            {
                                test.assertEqual(bytes, acceptedClient.readBytes(bytes.length).await());
                                test.assertEqual(bytes.length, acceptedClient.write(bytes).await());
                            }
                        }
                    });

                    final Result<Void> clientTask = asyncRunner.schedule(() ->
                    {
                        try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port, timeout).await())
                        {
                            test.assertEqual(IPv4Address.localhost, tcpClient.getLocalIPAddress());
                            test.assertNotEqual(port, tcpClient.getLocalPort());
                            test.assertEqual(IPv4Address.localhost, tcpClient.getRemoteIPAddress());
                            test.assertEqual(port, tcpClient.getRemotePort());
                            test.assertEqual(bytes.length, tcpClient.write(bytes).await());
                            test.assertEqual(bytes, tcpClient.readBytes(bytes.length).await());
                        }
                    });

                    Result.await(clientTask, serverTask);
                });
            });

            runner.testGroup("createTCPServer(int)", () ->
            {
                runner.test("with -1 localPort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    test.assertThrows(() -> network.createTCPServer(-1), new PreConditionFailure("localPort (-1) must be between 1 and 65535."));
                });

                runner.test("with 0 localPort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    test.assertThrows(() -> network.createTCPServer(0), new PreConditionFailure("localPort (0) must be between 1 and 65535."));
                });

                runner.test("with 8088 localPort", (Test test) ->
                {
                    final Network network = creator.run(test);
                    final TCPServer tcpServer = network.createTCPServer(8088).await();
                    test.assertTrue(tcpServer.dispose().await());
                });
            });
        });
    }
}
