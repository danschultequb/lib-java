package qub;

public interface JavaTCPServerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaTCPServer.class, () ->
        {
            final IntegerValue port = IntegerValue.create(20138);

            runner.testGroup("create(int,Clock)", () ->
            {
                runner.test("with -1 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(-1, ManualClock.create()),
                        new PreConditionFailure("localPort (-1) must be between 1 and 65535."));
                });

                runner.test("with 0 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(0, ManualClock.create()),
                        new PreConditionFailure("localPort (0) must be between 1 and 65535."));
                });

                runner.test("with null clock", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(1, null),
                        new PreConditionFailure("clock cannot be null."));
                });

                runner.test("with " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(port.get(), ManualClock.create()).await();
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.dispose().await());
                });

                runner.test("with " + port.incrementAndGet() + " port when a different TCPServer is already bound to " + port, (Test test) ->
                {
                    final TCPServer tcpServer1 = JavaTCPServer.create(port.get(), ManualClock.create()).await();
                    try
                    {
                        test.assertThrows(() -> JavaTCPServer.create(port.get(), ManualClock.create()).await(),
                            new RuntimeException(new java.net.BindException("Address already in use: bind")));
                    }
                    finally
                    {
                        test.assertTrue(tcpServer1.dispose().await());
                    }
                });
            });

            runner.testGroup("create(IPv4Address,int,Clock)", () ->
            {
                final Action3<IPv4Address,Integer,Throwable> createErrorTest = (IPv4Address localIPAddress, Integer localPort, Throwable expected) ->
                {
                    runner.test("with " + English.andList(localIPAddress, localPort), (Test test) ->
                    {
                        test.assertThrows(() -> JavaTCPServer.create(localIPAddress, localPort, ManualClock.create()).await(),
                            expected);
                    });
                };

                createErrorTest.run(null, port.incrementAndGet(), new PreConditionFailure("localIPAddress cannot be null."));
                createErrorTest.run(IPv4Address.localhost, -1, new PreConditionFailure("localPort (-1) must be between 1 and 65535."));
                createErrorTest.run(IPv4Address.localhost, 0, new PreConditionFailure("localPort (0) must be between 1 and 65535."));

                runner.test("with " + English.andList(IPv4Address.localhost, port.incrementAndGet(), "null clock"), (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(IPv4Address.localhost, port.get(), null),
                        new PreConditionFailure("clock cannot be null."));
                });

                runner.test("with " + English.andList(IPv4Address.localhost, port.incrementAndGet(), "non-null clock"), (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(IPv4Address.localhost, port.get(), ManualClock.create()).await();
                    try
                    {
                        test.assertNotNull(tcpServer);
                    }
                    finally
                    {
                        tcpServer.dispose().await();
                    }
                });

                runner.test("with 127.0.0.1 and " + port.incrementAndGet() + " port when a different TCPServer is already bound to 127.0.0.1 and " + port, (Test test) ->
                {
                    final TCPServer tcpServer1 = JavaTCPServer.create(IPv4Address.localhost, port.get(), ManualClock.create()).await();
                    try
                    {
                        test.assertThrows(() -> JavaTCPServer.create(IPv4Address.localhost, port.get(), ManualClock.create()).await(),
                            new java.net.BindException("Address already in use: bind"));
                    }
                    finally
                    {
                        test.assertTrue(tcpServer1.dispose().await());
                    }
                });
            });

            runner.testGroup("accept()", () ->
            {
                runner.test("with connection while accepting on port " + port.incrementAndGet(),
                    (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                    (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
                {
                    final IPv4Address ipAddress = IPv4Address.localhost;
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };

                    final Network network = JavaNetwork.create(clock);
                    final Value<byte[]> clientReadBytes = Value.create();
                    final Result<Void> clientTask = parallelAsyncRunner.schedule(() ->
                    {
                        try (final TCPClient tcpClient = network.createTCPClient(ipAddress, port.get()).await())
                        {
                            test.assertEqual(bytes.length, tcpClient.write(bytes).await());
                            clientReadBytes.set(tcpClient.readBytes(bytes.length).await());
                        }
                    });

                    try (final TCPServer tcpServer = network.createTCPServer(ipAddress, port.get()).await())
                    {
                        test.assertNotNull(tcpServer);

                        try (final TCPClient serverClient = tcpServer.accept().await())
                        {
                            test.assertNotNull(serverClient);

                            final byte[] serverReadBytes = serverClient.readBytes(bytes.length).await();
                            test.assertEqual(bytes, serverReadBytes);
                            serverClient.writeAll(serverReadBytes).await();
                        }
                    }

                    clientTask.await();

                    test.assertEqual(bytes, clientReadBytes.get());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), ManualClock.create()).await();
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.dispose().await());

                    test.assertThrows(() -> tcpServer.accept().await(),
                        new RuntimeException(new java.net.SocketException("Socket is closed")));
                });

                runner.test("on ParallelAsyncRunner, with connection while accepting on port " + port.incrementAndGet(),
                    (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                    (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
                {
                    final IPv4Address ipAddress = IPv4Address.localhost;
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
                    final Network network = JavaNetwork.create(clock);
                    final Value<byte[]> clientReadBytes = Value.create();

                    try (final TCPServer tcpServer = network.createTCPServer(ipAddress, port.get()).await())
                    {
                        final Result<Void> clientTask = parallelAsyncRunner.schedule(() ->
                        {
                            try (final TCPClient tcpClient = network.createTCPClient(ipAddress, port.get()).await())
                            {
                                test.assertEqual(bytes.length, tcpClient.write(bytes).await());
                                clientReadBytes.set(tcpClient.readBytes(bytes.length).await());
                            }
                        });


                        try (final TCPClient serverClient = parallelAsyncRunner.schedule(() -> tcpServer.accept().await()).await())
                        {
                            final byte[] serverReadBytes = serverClient.readBytes(bytes.length).await();
                            test.assertEqual(bytes, serverReadBytes);
                            test.assertEqual(serverReadBytes.length, serverClient.write(serverReadBytes).await());
                        }

                        clientTask.await();

                        test.assertEqual(bytes, clientReadBytes.get());
                    }
                });
            });

            runner.testGroup("dispose()", () ->
            {
                runner.test("multiple times", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), ManualClock.create()).await();
                    test.assertTrue(tcpServer.dispose().await());
                    test.assertFalse(tcpServer.dispose().await());
                });
            });
        });
    }
}
