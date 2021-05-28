package qub;

public interface NetworkTests
{
    static void test(TestRunner runner, Function1<Clock,Network> creator)
    {
        runner.testGroup(Network.class, () ->
        {
            runner.testGroup("createTCPClient(IPv4Address,int)", () ->
            {
                runner.test("with null remoteIPAddress", (Test test) ->
                {
                    final Network network = creator.run(ManualClock.create());
                    test.assertThrows(() -> network.createTCPClient(null, 80),
                        new PreConditionFailure("remoteIPAddress cannot be null."));
                });

                runner.test("with -1 remotePort", (Test test) ->
                {
                    final Network network = creator.run(ManualClock.create());
                    test.assertThrows(() -> network.createTCPClient(IPv4Address.localhost, -1),
                        new PreConditionFailure("remotePort (-1) must be between 1 and 65535."));
                });

                runner.test("with 0 remotePort", (Test test) ->
                {
                    final Network network = creator.run(ManualClock.create());
                    test.assertThrows(() -> network.createTCPClient(IPv4Address.localhost, 0),
                        new PreConditionFailure("remotePort (0) must be between 1 and 65535."));
                });

                runner.test("with valid arguments but no server listening", (Test test) ->
                {
                    final Network network = creator.run(ManualClock.create());
                    test.assertThrows(() -> network.createTCPClient(IPv4Address.localhost, 38827).await(),
                        new java.net.ConnectException("Connection refused: connect"));
                });

                runner.test("with valid arguments and server listening",
                    (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner(), resources.getClock()),
                    (Test test, AsyncRunner parallelAsyncRunner, Clock clock) ->
                {
                    final Network network = creator.run(clock);

                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5 };

                    final int port = 8189;
                    final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(5));

                    try (final TCPServer tcpServer = network.createTCPServer(IPv4Address.localhost, port).await())
                    {
                        try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port).await())
                        {
                            final Result<Void> serverTask = parallelAsyncRunner.schedule(() ->
                            {
                                test.assertEqual(IPv4Address.localhost, tcpServer.getLocalIPAddress());
                                test.assertEqual(port, tcpServer.getLocalPort());
                                try (final TCPClient acceptedClient = tcpServer.accept(timeout).await())
                                {
                                    test.assertEqual(bytes, acceptedClient.readBytes(bytes.length).await());
                                    test.assertEqual(bytes.length, acceptedClient.write(bytes).await());
                                }
                            });

                            final Result<Void> clientTask = parallelAsyncRunner.schedule(() ->
                            {
                                test.assertEqual(IPv4Address.localhost, tcpClient.getLocalIPAddress());
                                test.assertNotEqual(port, tcpClient.getLocalPort());
                                test.assertEqual(IPv4Address.localhost, tcpClient.getRemoteIPAddress());
                                test.assertEqual(port, tcpClient.getRemotePort());
                                test.assertEqual(bytes.length, tcpClient.write(bytes).await());
                                test.assertEqual(bytes, tcpClient.readBytes(bytes.length).await());
                            });

                            Result.await(serverTask, clientTask);
                        }
                    }
                });
            });

            runner.testGroup("createTCPServer(int)", () ->
            {
                runner.test("with -1 localPort", (Test test) ->
                {
                    final Network network = creator.run(ManualClock.create());
                    test.assertThrows(() -> network.createTCPServer(-1),
                        new PreConditionFailure("localPort (-1) must be between 1 and 65535."));
                });

                runner.test("with 0 localPort", (Test test) ->
                {
                    final Network network = creator.run(ManualClock.create());
                    test.assertThrows(() -> network.createTCPServer(0),
                        new PreConditionFailure("localPort (0) must be between 1 and 65535."));
                });

                runner.test("with 8088 localPort", (Test test) ->
                {
                    final Network network = creator.run(ManualClock.create());
                    final TCPServer tcpServer = network.createTCPServer(8088).await();
                    test.assertTrue(tcpServer.dispose().await());
                });

                runner.test("with client and server on parallel threads",
                    (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                    (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
                {
                    final Network network = creator.run(clock);

                    final IPv4Address address = IPv4Address.localhost;
                    final int port = 8090;

                    try (final TCPServer tcpServer = network.createTCPServer(address, port).await())
                    {
                        try (final TCPClient tcpClient = network.createTCPClient(address, port).await())
                        {
                            final Result<Void> serverTask = parallelAsyncRunner.schedule(() ->
                            {
                                try (final TCPClient acceptedClient = tcpServer.accept().await())
                                {
                                    final CharacterReadStream readStream = CharacterReadStream.create(acceptedClient);
                                    final CharacterWriteStream writeStream = CharacterWriteStream.create(acceptedClient);

                                    test.assertEqual('a', readStream.readCharacter().await());
                                    test.assertEqual('b', readStream.readCharacter().await());

                                    test.assertEqual(1, writeStream.write('c').await());

                                    test.assertEqual('e', readStream.readCharacter().await());

                                    test.assertEqual(1, writeStream.write('g').await());

                                    test.assertEqual('f', readStream.readCharacter().await());
                                }
                            });

                            final Result<Void> clientTask = parallelAsyncRunner.schedule(() ->
                            {
                                final CharacterReadStream readStream = CharacterReadStream.create(tcpClient);
                                final CharacterWriteStream writeStream = CharacterWriteStream.create(tcpClient);

                                test.assertEqual(2, writeStream.write("ab").await());

                                test.assertEqual('c', readStream.readCharacter().await());

                                test.assertEqual(2, writeStream.write("ef").await());

                                test.assertEqual('g', readStream.readCharacter().await());
                            });

                            Result.await(serverTask, clientTask);
                        }
                    }
                });

                runner.test("with client on main thread and server on parallel thread",
                    (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                    (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
                {
                    final Network network = creator.run(clock);

                    final IPv4Address address = IPv4Address.localhost;
                    final int port = 8091;

                    try (final TCPServer tcpServer = network.createTCPServer(address, port).await())
                    {
                        try (final TCPClient tcpClient = network.createTCPClient(address, port).await())
                        {
                            final Result<Void> serverTask = parallelAsyncRunner.schedule(() ->
                            {
                                try (final TCPClient acceptedClient = tcpServer.accept().await())
                                {
                                    final CharacterReadStream readStream = CharacterReadStream.create(acceptedClient);
                                    final CharacterWriteStream writeStream = CharacterWriteStream.create(acceptedClient);

                                    test.assertEqual('a', readStream.readCharacter().await());
                                    test.assertEqual('b', readStream.readCharacter().await());

                                    test.assertEqual(1, writeStream.write('c').await());

                                    test.assertEqual('e', readStream.readCharacter().await());

                                    test.assertEqual(1, writeStream.write('g').await());

                                    test.assertEqual('f', readStream.readCharacter().await());
                                }
                            });

                            final CharacterReadStream readStream = CharacterReadStream.create(tcpClient);
                            final CharacterWriteStream writeStream = CharacterWriteStream.create(tcpClient);

                            test.assertEqual(2, writeStream.write("ab").await());

                            test.assertEqual('c', readStream.readCharacter().await());

                            test.assertEqual(2, writeStream.write("ef").await());

                            test.assertEqual('g', readStream.readCharacter().await());

                            Result.await(serverTask);
                        }
                    }
                });
            });
        });
    }
}
