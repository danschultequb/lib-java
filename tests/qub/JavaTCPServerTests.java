package qub;

import java.util.concurrent.atomic.AtomicInteger;

public class JavaTCPServerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaTCPServer.class, () ->
        {
            final AtomicInteger port = new AtomicInteger(20138);

            AsyncDisposableTests.test(runner, (Test test) -> JavaTCPServer.create(port.getAndIncrement(), test.getMainAsyncRunner()).await());

            runner.testGroup("create(int, AsyncRunner)", () ->
            {
                runner.test("with -1 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(-1, test.getParallelAsyncRunner()), new PreConditionFailure("localPort (-1) must be between 1 and 65535."));
                });

                runner.test("with 0 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(0, test.getParallelAsyncRunner()), new PreConditionFailure("localPort (0) must be between 1 and 65535."));
                });

                runner.test("with null asyncRunner", (Test test) ->
                {
                    final TCPServer server = JavaTCPServer.create(1, null).await();
                    test.assertNotNull(server);
                    test.assertTrue(server.dispose().await());
                });

                runner.test("with " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(port.get(), test.getParallelAsyncRunner()).await();
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.dispose().await());
                });

                runner.test("with " + port.incrementAndGet() + " port when a different TCPServer is already bound to " + port, (Test test) ->
                {
                    final TCPServer tcpServer1 = JavaTCPServer.create(port.get(), test.getParallelAsyncRunner()).await();
                    test.assertNotNull(tcpServer1);

                    try
                    {
                        test.assertThrows(() -> JavaTCPServer.create(port.get(), test.getParallelAsyncRunner()).await(),
                            new RuntimeException(new java.net.BindException("Address already in use: JVM_Bind")));
                    }
                    finally
                    {
                        test.assertTrue(tcpServer1.dispose().await());
                    }
                });
            });

            runner.testGroup("create(IPv4Address, int, AsyncRunner)", () ->
            {
                runner.test("with null and " + port.incrementAndGet() + " port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(null, port.get(), test.getParallelAsyncRunner()), new PreConditionFailure("localIPAddress cannot be null."));
                });

                runner.test("with 127.0.0.1 and -1 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), -1, test.getParallelAsyncRunner()), new PreConditionFailure("localPort (-1) must be between 1 and 65535."));
                });

                runner.test("with 127.0.0.1 and 0 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), 0, test.getParallelAsyncRunner()), new PreConditionFailure("localPort (0) must be between 1 and 65535."));
                });

                runner.test("with 127.0.0.1, " + port.incrementAndGet() + " port, and null asyncRunner", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), null).await();
                    try
                    {
                        test.assertNotNull(tcpServer);
                    }
                    finally
                    {
                        tcpServer.dispose().await();
                    }
                });

                runner.test("with 127.0.0.1 and " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getParallelAsyncRunner()).await();
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
                    final TCPServer tcpServer1 = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getParallelAsyncRunner()).await();
                    test.assertNotNull(tcpServer1);
                    try
                    {
                        test.assertThrows(() -> JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getParallelAsyncRunner()).await(),
                            new RuntimeException(new java.net.BindException("Address already in use: JVM_Bind")));
                    }
                    finally
                    {
                        test.assertTrue(tcpServer1.dispose().await());
                    }
                });
            });

            runner.testGroup("accept()", () ->
            {
                runner.test("with connection while accepting on port " + port.incrementAndGet(), (Test test) ->
                {
                    final IPv4Address ipAddress = IPv4Address.parse("127.0.0.1");
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    final Network network = new JavaNetwork(asyncRunner);
                    final Value<byte[]> clientReadBytes = Value.create();
                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        final Result<TCPClient> tcpClientResult = network.createTCPClient(ipAddress, port.get());
                        test.assertSuccess(tcpClientResult);
                        try (final TCPClient tcpClient = tcpClientResult.await())
                        {
                            test.assertSuccess(bytes.length, tcpClient.writeBytes(bytes));
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
                            serverClient.writeAllBytes(serverReadBytes).await();
                        }
                    }

                    clientTask.await();

                    test.assertEqual(bytes, clientReadBytes.get());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), test.getParallelAsyncRunner()).await();
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.dispose().await());

                    test.assertThrows(() -> tcpServer.accept().await(),
                        new RuntimeException(new java.net.SocketException("Socket is closed")));
                });

                runner.test("on ParallelAsyncRunner, with connection while accepting on port " + port.incrementAndGet(), (Test test) ->
                {
                    final IPv4Address ipAddress = IPv4Address.parse("127.0.0.1");
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();
                    final Network network = new JavaNetwork(asyncRunner);
                    final Value<byte[]> clientReadBytes = Value.create();

                    try (final TCPServer tcpServer = network.createTCPServer(ipAddress, port.get()).await())
                    {
                        final AsyncAction clientTask = asyncRunner.schedule(() ->
                        {
                            // Parallel
                            try (final TCPClient tcpClient = network.createTCPClient(ipAddress, port.get()).await())
                            {
                                // Block
                                test.assertSuccess(bytes.length, tcpClient.writeBytes(bytes));
                                // Block
                                clientReadBytes.set(tcpClient.readBytes(bytes.length).await());
                            }
                        });

                        final AsyncRunner currentThreadAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
                        // Parallel, Block
                        final AsyncAction serverTask = asyncRunner.schedule(() -> tcpServer.accept())
                            .thenOn(currentThreadAsyncRunner)
                            .then((Result<TCPClient> serverClientResult) ->
                            {
                                // Main
                                test.assertSuccess(serverClientResult);

                                try (final TCPClient serverClient = serverClientResult.await())
                                {
                                    // Block
                                    final byte[] serverReadBytes = serverClient.readBytes(bytes.length).await();
                                    test.assertEqual(bytes, serverReadBytes);

                                    // Block
                                    test.assertEqual(serverReadBytes.length, serverClient.writeBytes(serverReadBytes).await());
                                }
                            });

                        // Block
                        serverTask.await();
                        // Block
                        clientTask.await();

                        test.assertEqual(bytes, clientReadBytes.get());
                    }
                });
            });

            runner.testGroup("acceptAsync()", () ->
            {
                runner.test("with connection while accepting on port " + port.incrementAndGet(), (Test test) ->
                {
                    final byte[] bytes = new byte[] { 10, 20, 30, 40, 50 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();
                    final Network network = new JavaNetwork(asyncRunner);

                    try (final TCPServer tcpServer = network.createTCPServer(IPv4Address.localhost, port.get()).await())
                    {
                        final AsyncAction serverTask = tcpServer.acceptAsync()
                            .then((Result<TCPClient> tcpClientResult) ->
                            {
                                try (final TCPClient tcpClient = tcpClientResult.await())
                                {
                                    final byte[] serverReadBytes = tcpClient.readBytes(bytes.length).await();
                                    test.assertEqual(bytes, serverReadBytes);
                                    test.assertEqual(bytes.length, tcpClient.writeBytes(serverReadBytes).await());
                                }
                            });

                        final AsyncAction clientTask = asyncRunner.schedule(() ->
                        {
                            // The tcpClient code needs to be in a different thread because the
                            // tcpServer runs its acceptAsync().then() action on the main thread.
                            try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port.get()).await())
                            {
                                tcpClient.writeBytes(bytes);
                                test.assertEqual(bytes, tcpClient.readBytes(bytes.length).await());
                            }
                            catch (Exception e)
                            {
                                test.fail(e);
                            }
                        });

                        serverTask.await();
                        clientTask.await();
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                });
            });

            runner.testGroup("dispose()", () ->
            {
                runner.test("multiple times", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), test.getParallelAsyncRunner()).await();
                    test.assertTrue(tcpServer.dispose().await());
                    test.assertFalse(tcpServer.dispose().await());
                });
            });
        });
    }
}
