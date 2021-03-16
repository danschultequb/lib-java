package qub;

public interface FakeNetworkTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeNetwork.class, () ->
        {
            NetworkTests.test(runner, FakeNetwork::create);

            runner.test("with valid arguments and server listening on same thread",
                runner.skip(),
                (TestResources resources) -> Tuple.create(resources.getMainAsyncRunner(), resources.getClock()),
                (Test test, AsyncRunner mainAsyncRunner, Clock clock) ->
            {
                final FakeNetwork network = FakeNetwork.create(clock);

                final byte[] bytes = new byte[] { 1, 2, 3, 4, 5 };

                final int port = 8088;
                final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(20));

                final Result<Void> serverTask = mainAsyncRunner.schedule(() ->
                {
                    try (final FakeTCPServer tcpServer = network.createTCPServer(IPv4Address.localhost, port).await())
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

                final Result<Void> clientTask = mainAsyncRunner.schedule(() ->
                {
                    try (final FakeTCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port, timeout).await())
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
    }
}
