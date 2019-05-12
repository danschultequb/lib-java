package qub;

import java.util.concurrent.atomic.AtomicInteger;

public class JavaTCPServerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaTCPServer.class, () ->
        {
            final AtomicInteger port = new AtomicInteger(20138);

            runner.testGroup("create(int,Clock)", () ->
            {
                runner.test("with -1 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(-1, test.getClock(), test.getParallelAsyncRunner()),
                        new PreConditionFailure("localPort (-1) must be between 1 and 65535."));
                });

                runner.test("with 0 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(0, test.getClock(), test.getParallelAsyncRunner()),
                        new PreConditionFailure("localPort (0) must be between 1 and 65535."));
                });

                runner.test("with null clock", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(1, null, test.getParallelAsyncRunner()),
                        new PreConditionFailure("clock cannot be null."));
                });

                runner.test("with null asyncRunner", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(1, test.getClock(), null),
                        new PreConditionFailure("asyncRunner cannot be null."));
                });

                runner.test("with " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(port.get(), test.getClock(), test.getParallelAsyncRunner()).await();
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.dispose().await());
                });

                runner.test("with " + port.incrementAndGet() + " port when a different TCPServer is already bound to " + port, (Test test) ->
                {
                    final TCPServer tcpServer1 = JavaTCPServer.create(port.get(), test.getClock(), test.getParallelAsyncRunner()).await();
                    test.assertNotNull(tcpServer1);

                    try
                    {
                        test.assertThrows(() -> JavaTCPServer.create(port.get(), test.getClock(), test.getParallelAsyncRunner()).await(),
                            new RuntimeException(new java.net.BindException("Address already in use: JVM_Bind")));
                    }
                    finally
                    {
                        test.assertTrue(tcpServer1.dispose().await());
                    }
                });
            });

            runner.testGroup("create(IPv4Address,int,Clock,AsyncRunner)", () ->
            {
                runner.test("with null and " + port.incrementAndGet() + " port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(null, port.get(), test.getClock(), test.getParallelAsyncRunner()),
                        new PreConditionFailure("localIPAddress cannot be null."));
                });

                runner.test("with 127.0.0.1 and -1 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), -1, test.getClock(), test.getParallelAsyncRunner()),
                        new PreConditionFailure("localPort (-1) must be between 1 and 65535."));
                });

                runner.test("with 127.0.0.1 and 0 port", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), 0, test.getClock(), test.getParallelAsyncRunner()),
                        new PreConditionFailure("localPort (0) must be between 1 and 65535."));
                });

                runner.test("with 127.0.0.1, " + port.incrementAndGet() + " port, and null clock", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), null, test.getParallelAsyncRunner()),
                        new PreConditionFailure("clock cannot be null."));
                });

                runner.test("with 127.0.0.1, " + port.incrementAndGet() + " port, and null asyncRunner", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getClock(), null),
                        new PreConditionFailure("asyncRunner cannot be null."));
                });

                runner.test("with 127.0.0.1 and " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getClock(), test.getParallelAsyncRunner()).await();
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
                    final TCPServer tcpServer1 = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getClock(), test.getParallelAsyncRunner()).await();
                    test.assertNotNull(tcpServer1);
                    try
                    {
                        test.assertThrows(() -> JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getClock(), test.getParallelAsyncRunner()).await(),
                            new java.net.BindException("Address already in use: JVM_Bind"));
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

                    final Network network = new JavaNetwork(test.getClock(), test.getParallelAsyncRunner());
                    final Value<byte[]> clientReadBytes = Value.create();
                    final Result<Void> clientTask = test.getParallelAsyncRunner().schedule(() ->
                    {
                        try (final TCPClient tcpClient = network.createTCPClient(ipAddress, port.get()).await())
                        {
                            test.assertEqual(bytes.length, tcpClient.writeBytes(bytes).await());
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
                    final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), test.getClock(), test.getParallelAsyncRunner()).await();
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.dispose().await());

                    test.assertThrows(() -> tcpServer.accept().await(),
                        new RuntimeException(new java.net.SocketException("Socket is closed")));
                });

                runner.test("on ParallelAsyncRunner, with connection while accepting on port " + port.incrementAndGet(), (Test test) ->
                {
                    final IPv4Address ipAddress = IPv4Address.parse("127.0.0.1");
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
                    final Network network = new JavaNetwork(test.getClock(), test.getParallelAsyncRunner());
                    final Value<byte[]> clientReadBytes = Value.create();
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    try (final TCPServer tcpServer = network.createTCPServer(ipAddress, port.get()).await())
                    {
                        final Result<Void> clientTask = asyncRunner.schedule(() ->
                        {
                            try (final TCPClient tcpClient = network.createTCPClient(ipAddress, port.get()).await())
                            {
                                test.assertEqual(bytes.length, tcpClient.writeBytes(bytes).await());
                                clientReadBytes.set(tcpClient.readBytes(bytes.length).await());
                            }
                        });


                        try (final TCPClient serverClient = asyncRunner.scheduleResult(tcpServer::accept).await())
                        {
                            final byte[] serverReadBytes = serverClient.readBytes(bytes.length).await();
                            test.assertEqual(bytes, serverReadBytes);
                            test.assertEqual(serverReadBytes.length, serverClient.writeBytes(serverReadBytes).await());
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
                    final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), test.getClock(), test.getParallelAsyncRunner()).await();
                    test.assertTrue(tcpServer.dispose().await());
                    test.assertFalse(tcpServer.dispose().await());
                });
            });
        });
    }
}
