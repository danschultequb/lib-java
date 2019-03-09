package qub;

import java.util.concurrent.atomic.AtomicInteger;

public class JavaTCPServerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaTCPServer.class, () ->
        {
            final AtomicInteger port = new AtomicInteger(20138);

            AsyncDisposableTests.test(runner, (Test test) -> JavaTCPServer.create(port.getAndIncrement(), test.getMainAsyncRunner()).awaitError());

            runner.testGroup("create(int, AsyncRunner)", () ->
            {
                runner.test("with -1 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(-1, test.getParallelAsyncRunner()));
                });

                runner.test("with 0 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(0, test.getParallelAsyncRunner()));
                });

                runner.test("with null asyncRunner", (Test test) ->
                {
                    final TCPServer server = JavaTCPServer.create(1, null).awaitError();
                    test.assertNotNull(server);
                    test.assertTrue(server.dispose().awaitError());
                });

                runner.test("with " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(port.get(), test.getParallelAsyncRunner()).awaitError();
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.dispose().awaitError());
                });

                runner.test("with " + port.incrementAndGet() + " port when a different TCPServer is already bound to " + port, (Test test) ->
                {
                    final TCPServer tcpServer1 = JavaTCPServer.create(port.get(), test.getParallelAsyncRunner()).awaitError();
                    test.assertNotNull(tcpServer1);

                    try
                    {
                        test.assertThrows(() -> JavaTCPServer.create(port.get(), test.getParallelAsyncRunner()).awaitError(),
                            new RuntimeException(new java.net.BindException("Address already in use: JVM_Bind")));
                    }
                    finally
                    {
                        test.assertTrue(tcpServer1.dispose().awaitError());
                    }
                });
            });

            runner.testGroup("create(IPv4Address, int, AsyncRunner)", () ->
            {
                runner.test("with null and " + port.incrementAndGet() + " port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(null, port.get(), test.getParallelAsyncRunner()));
                });

                runner.test("with 127.0.0.1 and -1 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), -1, test.getParallelAsyncRunner()));
                });

                runner.test("with 127.0.0.1 and 0 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), 0, test.getParallelAsyncRunner()));
                });

                runner.test("with 127.0.0.1, " + port.incrementAndGet() + " port, and null asyncRunner", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), null).awaitError();
                    try
                    {
                        test.assertNotNull(tcpServer);
                    }
                    finally
                    {
                        tcpServer.dispose().awaitError();
                    }
                });

                runner.test("with 127.0.0.1 and " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getParallelAsyncRunner()).awaitError();
                    try
                    {
                        test.assertNotNull(tcpServer);
                    }
                    finally
                    {
                        tcpServer.dispose().awaitError();
                    }
                });

                runner.test("with 127.0.0.1 and " + port.incrementAndGet() + " port when a different TCPServer is already bound to 127.0.0.1 and " + port, (Test test) ->
                {
                    final TCPServer tcpServer1 = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getParallelAsyncRunner()).awaitError();
                    test.assertNotNull(tcpServer1);
                    try
                    {
                        test.assertThrows(() -> JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getParallelAsyncRunner()).awaitError(),
                            new RuntimeException(new java.net.BindException("Address already in use: JVM_Bind")));
                    }
                    finally
                    {
                        test.assertTrue(tcpServer1.dispose().awaitError());
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
                        try (final TCPClient tcpClient = tcpClientResult.awaitError())
                        {
                            test.assertSuccess(bytes.length, tcpClient.writeBytes(bytes));
                            clientReadBytes.set(tcpClient.readBytes(bytes.length).awaitError());
                        }
                    });

                    try (final TCPServer tcpServer = network.createTCPServer(ipAddress, port.get()).awaitError())
                    {
                        test.assertNotNull(tcpServer);

                        try (final TCPClient serverClient = tcpServer.accept().awaitError())
                        {
                            test.assertNotNull(serverClient);

                            final byte[] serverReadBytes = serverClient.readBytes(bytes.length).awaitError();
                            test.assertEqual(bytes, serverReadBytes);
                            serverClient.writeAllBytes(serverReadBytes).awaitError();
                        }
                    }

                    clientTask.await();

                    test.assertEqual(bytes, clientReadBytes.get());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), test.getParallelAsyncRunner()).awaitError();
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.dispose().awaitError());

                    test.assertThrows(() -> tcpServer.accept().awaitError(),
                        new RuntimeException(new java.net.SocketException("Socket is closed")));
                });

                runner.test("on ParallelAsyncRunner, with connection while accepting on port " + port.incrementAndGet(), (Test test) ->
                {
                    final IPv4Address ipAddress = IPv4Address.parse("127.0.0.1");
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();
                    final Network network = new JavaNetwork(asyncRunner);
                    final Value<byte[]> clientReadBytes = Value.create();

                    try (final TCPServer tcpServer = network.createTCPServer(ipAddress, port.get()).awaitError())
                    {
                        final AsyncAction clientTask = asyncRunner.schedule(() ->
                        {
                            // Parallel
                            try (final TCPClient tcpClient = network.createTCPClient(ipAddress, port.get()).awaitError())
                            {
                                // Block
                                test.assertSuccess(bytes.length, tcpClient.writeBytes(bytes));
                                // Block
                                clientReadBytes.set(tcpClient.readBytes(bytes.length).awaitError());
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

                                try (final TCPClient serverClient = serverClientResult.awaitError())
                                {
                                    // Block
                                    final byte[] serverReadBytes = serverClient.readBytes(bytes.length).awaitError();
                                    test.assertEqual(bytes, serverReadBytes);

                                    // Block
                                    test.assertEqual(serverReadBytes.length, serverClient.writeBytes(serverReadBytes).awaitError());
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

                    try (final TCPServer tcpServer = network.createTCPServer(IPv4Address.localhost, port.get()).awaitError())
                    {
                        final AsyncAction serverTask = tcpServer.acceptAsync()
                            .then((Result<TCPClient> tcpClientResult) ->
                            {
                                try (final TCPClient tcpClient = tcpClientResult.awaitError())
                                {
                                    final byte[] serverReadBytes = tcpClient.readBytes(bytes.length).awaitError();
                                    test.assertEqual(bytes, serverReadBytes);
                                    test.assertEqual(bytes.length, tcpClient.writeBytes(serverReadBytes).awaitError());
                                }
                            });

                        final AsyncAction clientTask = asyncRunner.schedule(() ->
                        {
                            // The tcpClient code needs to be in a different thread because the
                            // tcpServer runs its acceptAsync().then() action on the main thread.
                            try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port.get()).awaitError())
                            {
                                tcpClient.writeBytes(bytes);
                                test.assertEqual(bytes, tcpClient.readBytes(bytes.length).awaitError());
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
                    final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), test.getParallelAsyncRunner()).awaitError();
                    test.assertTrue(tcpServer.dispose().awaitError());
                    test.assertFalse(tcpServer.dispose().awaitError());
                });
            });
        });
    }
}
