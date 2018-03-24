package qub;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaTCPServerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaTCPServer.class, () ->
        {
            final AtomicInteger port = new AtomicInteger(10030);

            runner.testGroup("create(int, AsyncRunner)", () ->
            {
                runner.test("with -1 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(-1, null);
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.isError());
                    test.assertEqual("localPort must be greater than 0.", tcpServer.getErrorMessage());
                });

                runner.test("with 0 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(0, null);
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.isError());
                    test.assertEqual("localPort must be greater than 0.", tcpServer.getErrorMessage());
                });

                runner.test("with " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(port.get(), null);
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.isSuccess());
                    test.assertTrue(tcpServer.getValue().dispose().getValue());
                });

                runner.test("with " + port.incrementAndGet() + " port when a different TCPServer is already bound to " + port, (Test test) ->
                {
                    final Result<TCPServer> tcpServer1 = JavaTCPServer.create(port.get(), null);
                    test.assertNotNull(tcpServer1);
                    test.assertTrue(tcpServer1.isSuccess());

                    try
                    {
                        final Result<TCPServer> tcpServer2 = JavaTCPServer.create(port.get(), null);
                        test.assertNotNull(tcpServer2);
                        test.assertTrue(tcpServer2.isError());
                        test.assertEqual("Address already in use: NET_Bind", tcpServer2.getErrorMessage());
                    }
                    finally
                    {
                        test.assertTrue(tcpServer1.getValue().dispose().getValue());
                    }
                });
            });

            runner.testGroup("create(IPv4Address, int, AsyncRunner)", () ->
            {
                runner.test("with null and " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(null, port.get(), null);
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.isError());
                    test.assertEqual("localIPAddress cannot be null.", tcpServer.getErrorMessage());
                });

                runner.test("with 127.0.0.1 and -1 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), -1, null);
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.isError());
                    test.assertEqual("localPort must be greater than 0.", tcpServer.getErrorMessage());
                });

                runner.test("with 127.0.0.1 and 0 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), 0, null);
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.isError());
                    test.assertEqual("localPort must be greater than 0.", tcpServer.getErrorMessage());
                });

                runner.test("with 127.0.0.1 and " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), null);
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.isSuccess());
                    test.assertTrue(tcpServer.getValue().dispose().getValue());
                });

                runner.test("with 127.0.0.1 and " + port.incrementAndGet() + " port when a different TCPServer is already bound to 127.0.0.1 and " + port, (Test test) ->
                {
                    final Result<TCPServer> tcpServer1 = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), null);
                    test.assertNotNull(tcpServer1);
                    test.assertTrue(tcpServer1.isSuccess());

                    try
                    {
                        final Result<TCPServer> tcpServer2 = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), null);
                        test.assertNotNull(tcpServer2);
                        test.assertTrue(tcpServer2.isError());
                        test.assertEqual("Address already in use: NET_Bind", tcpServer2.getErrorMessage());
                    }
                    finally
                    {
                        test.assertTrue(tcpServer1.getValue().dispose().getValue());
                    }
                });
            });

            runner.testGroup("setExceptionHandler(Action1<IOException>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(port.incrementAndGet(), null);
                    try
                    {
                        tcpServer.getValue().setExceptionHandler(null);
                    }
                    finally
                    {
                        tcpServer.getValue().dispose();
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(port.incrementAndGet(), null);
                    try
                    {
                        tcpServer.getValue().setExceptionHandler((IOException e) -> {});
                    }
                    finally
                    {
                        tcpServer.getValue().dispose();
                    }
                });
            });

            runner.testGroup("accept()", () ->
            {
                runner.test("with connection while accepting on port " + port.incrementAndGet(), (Test test) ->
                {
                    final IPv4Address ipAddress = IPv4Address.parse("127.0.0.1");
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };

                    final Synchronization synchronization = new Synchronization();
                    try (final ParallelAsyncRunner asyncRunner = new ParallelAsyncRunner(synchronization);
                         final TCPServer tcpServer = JavaTCPServer.create(ipAddress, port.get(), asyncRunner).getValue())
                    {
                        final Gate connectGate = synchronization.createGate(false);

                        final Value<byte[]> clientReadBytes = new Value<>();
                        asyncRunner.schedule(() ->
                        {
                            connectGate.passThrough();
                            try (final TCPClient tcpClient = JavaTCPClient.create(ipAddress, port.get()).getValue())
                            {
                                test.assertTrue(tcpClient.write(bytes));
                                clientReadBytes.set(tcpClient.readBytes(bytes.length));
                            }
                        });

                        connectGate.open();
                        final Result<TCPClient> acceptResult = tcpServer.accept();
                        test.assertNotNull(acceptResult);
                        test.assertTrue(acceptResult.isSuccess());
                        test.assertNotNull(acceptResult.getValue());
                        final byte[] serverReadBytes = acceptResult.getValue().readBytes(bytes.length);
                        test.assertEqual(bytes, serverReadBytes);
                        test.assertTrue(acceptResult.getValue().write(serverReadBytes));
                        acceptResult.getValue().dispose();

                        asyncRunner.await();

                        test.assertEqual(bytes, clientReadBytes.get());
                    }
                });

                runner.test("with disposal while accepting()", (Test test) ->
                {
                    final Synchronization synchronization = new Synchronization();
                    try (final ParallelAsyncRunner asyncRunner = new ParallelAsyncRunner();
                         final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), asyncRunner).getValue())
                    {
                        final Gate disposalGate = synchronization.createGate(false);

                        asyncRunner.schedule(() ->
                        {
                            disposalGate.passThrough();
                            tcpServer.dispose();
                        });

                        disposalGate.open();
                        final Result<TCPClient> acceptResult = tcpServer.accept();
                        test.assertNotNull(acceptResult);
                        test.assertTrue(acceptResult.isError());
                        test.assertEqual("socket closed", acceptResult.getErrorMessage());
                    }
                });

                runner.test("when disposed", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), null).getValue();
                    tcpServer.dispose();

                    final Result<TCPClient> acceptResult = tcpServer.accept();
                    test.assertNotNull(acceptResult);
                    test.assertTrue(acceptResult.isError());
                    test.assertEqual("Socket is closed", acceptResult.getErrorMessage());
                });
            });

            runner.testGroup("acceptAsync()", () ->
            {
                runner.test("with disposal while accepting()", (Test test) ->
                {
                    try (final ParallelAsyncRunner asyncRunner = new ParallelAsyncRunner())
                    {
                        final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), asyncRunner).getValue();

                        final AsyncFunction<TCPClient> acceptAsyncResult = tcpServer.acceptAsync();
                        test.assertNotNull(acceptAsyncResult);

                        final Value<TCPClient> tcpClientValue = new Value<>();
                        acceptAsyncResult.then(tcpClientValue::set);

                        tcpServer.dispose();
                        test.assertTrue(tcpServer.isDisposed());

                        asyncRunner.await();

                        test.assertFalse(tcpClientValue.hasValue());
                    }
                });

                runner.test("with connection while accepting() on port " + port.incrementAndGet(), runner.skip(), (Test test) ->
                {
                    final byte[] bytes = new byte[] { 10, 20, 30, 40, 50 };
                    try (final TCPServer tcpServer = JavaTCPServer.create(IPv4Address.localhost, port.get(), test.getParallelRunner()).getValue())
                    {
                        final AsyncFunction<TCPClient> acceptAsyncResult = tcpServer.acceptAsync();
                        test.assertNotNull(acceptAsyncResult);

                        acceptAsyncResult.then((TCPClient tcpClient) ->
                        {
                            final byte[] serverReadBytes = tcpClient.readBytes(bytes.length);
                            tcpClient.write(serverReadBytes);
                            tcpClient.dispose();
                        });

                        test.getParallelRunner().schedule(() ->
                        {
                            // The tcpClient code needs to be in a different thread because the
                            // tcpServer runs its acceptAsync().then() action on the main thread.
                            try (final TCPClient tcpClient = JavaTCPClient.create(IPv4Address.localhost, port.get()).getValue())
                            {
                                tcpClient.write(bytes);
                                final byte[] clientReadBytes = tcpClient.readBytes(bytes.length);
                                test.assertEqual(bytes, clientReadBytes);
                            }
                        });

                        test.await();
                    }
                });
            });

            runner.testGroup("dispose()", () ->
            {
                runner.test("multiple times", (Test test) ->
                {
                    final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), null).getValue();

                    final Result<Boolean> disposeResult1 = tcpServer.dispose();
                    test.assertNotNull(disposeResult1);
                    test.assertTrue(disposeResult1.isSuccess());
                    test.assertTrue(disposeResult1.getValue());

                    final Result<Boolean> disposeResult2 = tcpServer.dispose();
                    test.assertNotNull(disposeResult2);
                    test.assertTrue(disposeResult2.isSuccess());
                    test.assertFalse(disposeResult2.getValue());
                });
            });
        });
    }
}
