package qub;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaTCPServerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaTCPServer.class, () ->
        {
            final AtomicInteger port = new AtomicInteger(20138);

            runner.testGroup("create(int, AsyncRunner)", () ->
            {
                runner.test("with -1 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(-1, null);
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.hasError());
                    test.assertEqual("localPort must be greater than 0.", tcpServer.getErrorMessage());
                });

                runner.test("with 0 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(0, null);
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.hasError());
                    test.assertEqual("localPort must be greater than 0.", tcpServer.getErrorMessage());
                });

                runner.test("with " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(port.get(), null);
                    test.assertNotNull(tcpServer);
                    test.assertFalse(tcpServer.hasError());
                    test.assertTrue(tcpServer.getValue().dispose().getValue());
                });

                runner.test("with " + port.incrementAndGet() + " port when a different TCPServer is already bound to " + port, (Test test) ->
                {
                    final Result<TCPServer> tcpServer1 = JavaTCPServer.create(port.get(), null);
                    test.assertNotNull(tcpServer1);
                    test.assertFalse(tcpServer1.hasError());

                    try
                    {
                        final Result<TCPServer> tcpServer2 = JavaTCPServer.create(port.get(), null);
                        test.assertNotNull(tcpServer2);
                        test.assertTrue(tcpServer2.hasError());
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
                    test.assertTrue(tcpServer.hasError());
                    test.assertEqual("localIPAddress cannot be null.", tcpServer.getErrorMessage());
                });

                runner.test("with 127.0.0.1 and -1 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), -1, null);
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.hasError());
                    test.assertEqual("localPort must be greater than 0.", tcpServer.getErrorMessage());
                });

                runner.test("with 127.0.0.1 and 0 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), 0, null);
                    test.assertNotNull(tcpServer);
                    test.assertTrue(tcpServer.hasError());
                    test.assertEqual("localPort must be greater than 0.", tcpServer.getErrorMessage());
                });

                runner.test("with 127.0.0.1 and " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), null);
                    test.assertNotNull(tcpServer);
                    test.assertFalse(tcpServer.hasError());
                    test.assertTrue(tcpServer.getValue().dispose().getValue());
                });

                runner.test("with 127.0.0.1 and " + port.incrementAndGet() + " port when a different TCPServer is already bound to 127.0.0.1 and " + port, (Test test) ->
                {
                    final Result<TCPServer> tcpServer1 = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), null);
                    test.assertNotNull(tcpServer1);
                    test.assertFalse(tcpServer1.hasError());

                    try
                    {
                        final Result<TCPServer> tcpServer2 = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), null);
                        test.assertNotNull(tcpServer2);
                        test.assertTrue(tcpServer2.hasError());
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
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    final Value<byte[]> clientReadBytes = new Value<>();
                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        try (final TCPClient tcpClient = JavaTCPClient.create(ipAddress, port.get()).getValue())
                        {
                            test.assertTrue(tcpClient.write(bytes));
                            clientReadBytes.set(tcpClient.readBytes(bytes.length));
                        }
                    });

                    try (final TCPServer tcpServer = JavaTCPServer.create(ipAddress, port.get(), asyncRunner).getValue())
                    {
                        final Result<TCPClient> acceptResult = tcpServer.accept();
                        test.assertSuccess(acceptResult);

                        try (final TCPClient serverClient = acceptResult.getValue())
                        {
                            final byte[] serverReadBytes = serverClient.readBytes(bytes.length);
                            test.assertEqual(bytes, serverReadBytes);
                            test.assertTrue(serverClient.write(serverReadBytes));
                        }
                    }

                    clientTask.await();

                    test.assertEqual(bytes, clientReadBytes.get());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final Result<TCPServer> tcpServerResult = JavaTCPServer.create(port.incrementAndGet(), null);
                    test.assertSuccess(tcpServerResult);

                    final TCPServer tcpServer = tcpServerResult.getValue();
                    tcpServer.dispose();

                    final Result<TCPClient> acceptResult = tcpServer.accept();
                    test.assertNotNull(acceptResult);
                    test.assertTrue(acceptResult.hasError());
                    test.assertEqual("Socket is closed", acceptResult.getErrorMessage());
                });
            });

            runner.testGroup("acceptAsync()", () ->
            {
                runner.test("with connection while accepting on port " + port.incrementAndGet(), (Test test) ->
                {
                    final IPv4Address ipAddress = IPv4Address.parse("127.0.0.1");
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    final Value<byte[]> clientReadBytes = new Value<>();
                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        try (final TCPClient tcpClient = JavaTCPClient.create(ipAddress, port.get()).getValue())
                        {
                            test.assertTrue(tcpClient.write(bytes));
                            clientReadBytes.set(tcpClient.readBytes(bytes.length));
                        }
                    });

                    try (final TCPServer tcpServer = JavaTCPServer.create(ipAddress, port.get(), asyncRunner).getValue())
                    {
                        final AsyncAction serverTask = asyncRunner.schedule(new Action0()
                        {
                            @Override
                            public void run()
                            {
                                final Result<TCPClient> acceptResult = tcpServer.accept();
                                test.assertSuccess(acceptResult);

                                try (final TCPClient serverClient = acceptResult.getValue())
                                {
                                    final byte[] serverReadBytes = serverClient.readBytes(bytes.length);
                                    test.assertEqual(bytes, serverReadBytes);
                                    test.assertTrue(serverClient.write(serverReadBytes));
                                }
                            }
                        });

                        serverTask.await();
                    }

                    clientTask.await();

                    test.assertEqual(bytes, clientReadBytes.get());
                });

                runner.test("with connection while accepting on port " + port.incrementAndGet(), (Test test) ->
                {
                    final IPv4Address ipAddress = IPv4Address.parse("127.0.0.1");
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    final Value<byte[]> clientReadBytes = new Value<>();
                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        try (final TCPClient tcpClient = JavaTCPClient.create(ipAddress, port.get()).getValue())
                        {
                            test.assertTrue(tcpClient.write(bytes));
                            clientReadBytes.set(tcpClient.readBytes(bytes.length));
                        }
                    });

                    try (final TCPServer tcpServer = JavaTCPServer.create(ipAddress, port.get(), asyncRunner).getValue())
                    {
                        final AsyncFunction<TCPClient> serverClientTask = asyncRunner.schedule(new Function0<TCPClient>()
                        {
                            @Override
                            public TCPClient run()
                            {
                                final Result<TCPClient> acceptResult = tcpServer.accept();
                                test.assertSuccess(acceptResult);
                                return acceptResult.getValue();
                            }
                        });

                        try (final TCPClient serverClient = serverClientTask.awaitReturn())
                        {
                            final byte[] serverReadBytes = serverClient.readBytes(bytes.length);
                            test.assertEqual(bytes, serverReadBytes);
                            test.assertTrue(serverClient.write(serverReadBytes));
                        }
                    }

                    clientTask.await();

                    test.assertEqual(bytes, clientReadBytes.get());
                });

                runner.test("with connection while accepting on port " + port.incrementAndGet(), (Test test) ->
                {
                    final IPv4Address ipAddress = IPv4Address.parse("127.0.0.1");
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    final Value<byte[]> clientReadBytes = new Value<>();
                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        try (final TCPClient tcpClient = JavaTCPClient.create(ipAddress, port.get()).getValue())
                        {
                            test.assertTrue(tcpClient.write(bytes));
                            clientReadBytes.set(tcpClient.readBytes(bytes.length));
                        }
                    });

                    try (final TCPServer tcpServer = JavaTCPServer.create(ipAddress, port.get(), asyncRunner).getValue())
                    {
                        final AsyncFunction<Result<TCPClient>> serverClientTask = asyncRunner.schedule(new Function0<Result<TCPClient>>()
                        {
                            @Override
                            public Result<TCPClient> run()
                            {
                                return tcpServer.accept();
                            }
                        });

                        final Result<TCPClient> serverClientResult = serverClientTask.awaitReturn();
                        test.assertSuccess(serverClientResult);

                        try (final TCPClient serverClient = serverClientResult.getValue())
                        {
                            final byte[] serverReadBytes = serverClient.readBytes(bytes.length);
                            test.assertEqual(bytes, serverReadBytes);
                            test.assertTrue(serverClient.write(serverReadBytes));
                        }
                    }

                    clientTask.await();

                    test.assertEqual(bytes, clientReadBytes.get());
                });

                runner.test("with connection while accepting on port " + port.incrementAndGet(), (Test test) ->
                {
                    final IPv4Address ipAddress = IPv4Address.parse("127.0.0.1");
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    final Value<byte[]> clientReadBytes = new Value<>();
                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        try (final TCPClient tcpClient = JavaTCPClient.create(ipAddress, port.get()).getValue())
                        {
                            test.assertTrue(tcpClient.write(bytes));
                            clientReadBytes.set(tcpClient.readBytes(bytes.length));
                        }
                    });

                    try (final TCPServer tcpServer = JavaTCPServer.create(ipAddress, port.get(), asyncRunner).getValue())
                    {
                        final AsyncRunner currentThreadAsyncRunner = test.getMainAsyncRunner();
                        final AsyncFunction<Result<TCPClient>> serverClientTask = asyncRunner.schedule(new Function0<Result<TCPClient>>()
                            {
                                @Override
                                public Result<TCPClient> run()
                                {
                                    return tcpServer.accept();
                                }
                            })
                            .thenOn(currentThreadAsyncRunner);

                        final Result<TCPClient> serverClientResult = serverClientTask.awaitReturn();
                        test.assertSuccess(serverClientResult);

                        try (final TCPClient serverClient = serverClientResult.getValue())
                        {
                            final byte[] serverReadBytes = serverClient.readBytes(bytes.length);
                            test.assertEqual(bytes, serverReadBytes);
                            test.assertTrue(serverClient.write(serverReadBytes));
                        }
                    }

                    clientTask.await();

                    test.assertEqual(bytes, clientReadBytes.get());
                });

                runner.test("with connection while accepting on port " + port.incrementAndGet(), runner.skip("Hangs forever"), (Test test) ->
                {
                    final IPv4Address ipAddress = IPv4Address.parse("127.0.0.1");
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    final Value<byte[]> clientReadBytes = new Value<>();
                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        try (final TCPClient tcpClient = JavaTCPClient.create(ipAddress, port.get()).getValue())
                        {
                            test.assertTrue(tcpClient.write(bytes));
                            clientReadBytes.set(tcpClient.readBytes(bytes.length));
                        }
                    });

                    try (final TCPServer tcpServer = JavaTCPServer.create(ipAddress, port.get(), asyncRunner).getValue())
                    {
                        final AsyncRunner currentThreadAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
                        final AsyncFunction<Result<TCPClient>> serverClientTask = asyncRunner.schedule(new Function0<Result<TCPClient>>()
                            {
                                @Override
                                public Result<TCPClient> run()
                                {
                                    return tcpServer.accept();
                                }
                            })
                            .thenOn(currentThreadAsyncRunner);

                        final Result<TCPClient> serverClientResult = serverClientTask.awaitReturn();
                        test.assertSuccess(serverClientResult);

                        try (final TCPClient serverClient = serverClientResult.getValue())
                        {
                            final byte[] serverReadBytes = serverClient.readBytes(bytes.length);
                            test.assertEqual(bytes, serverReadBytes);
                            test.assertTrue(serverClient.write(serverReadBytes));
                        }
                    }

                    clientTask.await();

                    test.assertEqual(bytes, clientReadBytes.get());
                });

                runner.test("with connection while accepting on port " + port.incrementAndGet(), runner.skip("Hangs forever"), (Test test) ->
                {
                    final IPv4Address ipAddress = IPv4Address.parse("127.0.0.1");
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    final Value<byte[]> clientReadBytes = new Value<>();

                    try (final TCPServer tcpServer = JavaTCPServer.create(ipAddress, port.get(), asyncRunner).getValue())
                    {
                        final AsyncRunner currentThreadAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
                        final AsyncAction serverTask = asyncRunner.schedule(new Function0<Result<TCPClient>>()
                            {
                                @Override
                                public Result<TCPClient> run()
                                {
                                    return tcpServer.accept();
                                }
                            })
                            .thenOn(currentThreadAsyncRunner)
                            .then((Result<TCPClient> serverClientResult) ->
                            {
                                test.assertSuccess(serverClientResult);

                                try (final TCPClient serverClient = serverClientResult.getValue())
                                {
                                    final byte[] serverReadBytes = serverClient.readBytes(bytes.length);
                                    test.assertEqual(bytes, serverReadBytes);
                                    test.assertTrue(serverClient.write(serverReadBytes));
                                }
                            });

                        final AsyncAction clientTask = asyncRunner.schedule(() ->
                        {
                            try (final TCPClient tcpClient = JavaTCPClient.create(ipAddress, port.get()).getValue())
                            {
                                test.assertTrue(tcpClient.write(bytes));
                                clientReadBytes.set(tcpClient.readBytes(bytes.length));
                            }
                        });

                        clientTask.await();
                        serverTask.await();

                        test.assertEqual(bytes, clientReadBytes.get());
                    }
                });

                runner.test("with connection while accepting on port " + port.incrementAndGet(), runner.skip("Hangs forever"), (Test test) ->
                {
                    final byte[] bytes = new byte[] { 10, 20, 30, 40, 50 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    try (final TCPServer tcpServer = JavaTCPServer.create(IPv4Address.localhost, port.get(), asyncRunner).getValue())
                    {
                        final AsyncAction serverTask = tcpServer.acceptAsync()
                            .then((TCPClient tcpClient) ->
                            {
                                System.out.println("SERVER: Got tcpClient.");
                                final byte[] serverReadBytes = tcpClient.readBytes(bytes.length);
                                System.out.println("SERVER: Read " + serverReadBytes.length + " bytes");
                                tcpClient.write(serverReadBytes);
                                System.out.println("SERVER: Wrote " + serverReadBytes.length + " bytes");
                                tcpClient.dispose();
                            });

                        // The tcpClient code needs to be in a different thread because the
                        // tcpServer runs its acceptAsync().then() action on the main thread.
                        try (final TCPClient tcpClient = JavaTCPClient.create(IPv4Address.localhost, port.get()).getValue())
                        {
                            tcpClient.write(bytes);
                            final byte[] clientReadBytes = tcpClient.readBytes(bytes.length);
                            test.assertEqual(bytes, clientReadBytes);
                        }

                        serverTask.await();
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
                    test.assertFalse(disposeResult1.hasError());
                    test.assertTrue(disposeResult1.getValue());

                    final Result<Boolean> disposeResult2 = tcpServer.dispose();
                    test.assertNotNull(disposeResult2);
                    test.assertFalse(disposeResult2.hasError());
                    test.assertFalse(disposeResult2.getValue());
                });
            });
        });
    }
}
