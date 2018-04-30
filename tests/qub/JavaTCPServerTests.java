package qub;

import java.util.concurrent.atomic.AtomicInteger;

public class JavaTCPServerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaTCPServer.class, () ->
        {
            final AtomicInteger port = new AtomicInteger(20138);

            AsyncDisposableTests.test(runner, (AsyncRunner asyncRunner) -> JavaTCPServer.create(port.getAndIncrement(), asyncRunner).getValue());

            runner.testGroup("create(int, AsyncRunner)", () ->
            {
                runner.test("with -1 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(-1, test.getParallelAsyncRunner());
                    test.assertError(new IllegalArgumentException("localPort (-1) must be between 1 and 65535."), tcpServer);
                });

                runner.test("with 0 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(0, test.getParallelAsyncRunner());
                    test.assertError(new IllegalArgumentException("localPort (0) must be between 1 and 65535."), tcpServer);
                });

                runner.test("with null asyncRunner", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(1, null);
                    test.assertError(new IllegalArgumentException("asyncRunner cannot be null."), tcpServer);
                });

                runner.test("with " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(port.get(), test.getParallelAsyncRunner());
                    test.assertSuccess(tcpServer);
                    test.assertTrue(tcpServer.getValue().dispose().getValue());
                });

                runner.test("with " + port.incrementAndGet() + " port when a different TCPServer is already bound to " + port, (Test test) ->
                {
                    final Result<TCPServer> tcpServer1 = JavaTCPServer.create(port.get(), test.getParallelAsyncRunner());
                    test.assertSuccess(tcpServer1);

                    try
                    {
                        final Result<TCPServer> tcpServer2 = JavaTCPServer.create(port.get(), test.getParallelAsyncRunner());
                        test.assertError(new java.net.BindException("Address already in use: NET_Bind"), tcpServer2);
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
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(null, port.get(), test.getParallelAsyncRunner());
                    test.assertError(new IllegalArgumentException("localIPAddress cannot be null."), tcpServer);
                });

                runner.test("with 127.0.0.1 and -1 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), -1, test.getParallelAsyncRunner());
                    test.assertError(new IllegalArgumentException("localPort (-1) must be between 1 and 65535."), tcpServer);
                });

                runner.test("with 127.0.0.1 and 0 port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), 0, test.getParallelAsyncRunner());
                    test.assertError(new IllegalArgumentException("localPort (0) must be between 1 and 65535."), tcpServer);
                });

                runner.test("with 127.0.0.1, " + port.incrementAndGet() + " port, and null asyncRunner", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), null);
                    test.assertError(new IllegalArgumentException("asyncRunner cannot be null."), tcpServer);
                });

                runner.test("with 127.0.0.1 and " + port.incrementAndGet() + " port", (Test test) ->
                {
                    final Result<TCPServer> tcpServer = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getParallelAsyncRunner());
                    test.assertSuccess(tcpServer);
                    test.assertTrue(tcpServer.getValue().dispose().getValue());
                });

                runner.test("with 127.0.0.1 and " + port.incrementAndGet() + " port when a different TCPServer is already bound to 127.0.0.1 and " + port, (Test test) ->
                {
                    final Result<TCPServer> tcpServer1 = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getParallelAsyncRunner());
                    test.assertSuccess(tcpServer1);

                    try
                    {
                        final Result<TCPServer> tcpServer2 = JavaTCPServer.create(IPv4Address.parse("127.0.0.1"), port.get(), test.getParallelAsyncRunner());
                        test.assertError(new java.net.BindException("Address already in use: NET_Bind"), tcpServer2);
                    }
                    finally
                    {
                        test.assertTrue(tcpServer1.getValue().dispose().getValue());
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
                        try (final TCPClient tcpClient = JavaTCPClient.create(ipAddress, port.get(), asyncRunner).getValue())
                        {
                            test.assertSuccess(true, tcpClient.write(bytes));
                            clientReadBytes.set(tcpClient.readBytes(bytes.length).getValue());
                        }
                        catch (Exception e)
                        {
                            test.fail(e);
                        }
                    });

                    try (final TCPServer tcpServer = JavaTCPServer.create(ipAddress, port.get(), asyncRunner).getValue())
                    {
                        final Result<TCPClient> acceptResult = tcpServer.accept();
                        test.assertSuccess(acceptResult);

                        try (final TCPClient serverClient = acceptResult.getValue())
                        {
                            final Result<byte[]> serverReadBytes = serverClient.readBytes(bytes.length);
                            test.assertSuccess(bytes, serverReadBytes);
                            test.assertSuccess(true, serverClient.write(serverReadBytes.getValue()));
                        }
                        catch (Exception e)
                        {
                            test.fail(e);
                        }
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }

                    clientTask.await();

                    test.assertEqual(bytes, clientReadBytes.get());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final Result<TCPServer> tcpServerResult = JavaTCPServer.create(port.incrementAndGet(), test.getParallelAsyncRunner());
                    test.assertSuccess(tcpServerResult);

                    final TCPServer tcpServer = tcpServerResult.getValue();
                    tcpServer.dispose();

                    final Result<TCPClient> acceptResult = tcpServer.accept();
                    test.assertNotNull(acceptResult);
                    test.assertTrue(acceptResult.hasError());
                    test.assertEqual("Socket is closed", acceptResult.getErrorMessage());
                });

                runner.test("on ParallelAsyncRunner, with connection while accepting on port " + port.incrementAndGet(), (Test test) ->
                {
                    final IPv4Address ipAddress = IPv4Address.parse("127.0.0.1");
                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    final Value<byte[]> clientReadBytes = new Value<>();

                    try (final TCPServer tcpServer = JavaTCPServer.create(ipAddress, port.get(), asyncRunner).getValue())
                    {
                        final AsyncAction clientTask = asyncRunner.schedule(() ->
                        {
                            // Parallel
                            try (final TCPClient tcpClient = JavaTCPClient.create(ipAddress, port.get(), asyncRunner).getValue())
                            {
                                // Block
                                test.assertSuccess(true, tcpClient.write(bytes));
                                // Block
                                clientReadBytes.set(tcpClient.readBytes(bytes.length).getValue());
                            }
                            catch (Exception e)
                            {
                                test.fail(e);
                            }
                        });

                        final AsyncRunner currentThreadAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
                        // Parallel, Block
                        final AsyncAction serverTask = asyncRunner.schedule(tcpServer::accept)
                            .thenOn(currentThreadAsyncRunner)
                            .then((Result<TCPClient> serverClientResult) ->
                            {
                                // Main
                                test.assertSuccess(serverClientResult);

                                try (final TCPClient serverClient = serverClientResult.getValue())
                                {
                                    // Block
                                    final Result<byte[]> serverReadBytes = serverClient.readBytes(bytes.length);
                                    test.assertSuccess(bytes, serverReadBytes);

                                    // Block
                                    test.assertSuccess(true, serverClient.write(serverReadBytes.getValue()));
                                }
                                catch (Exception e)
                                {
                                    test.fail(e);
                                }
                            });

                        // Block
                        serverTask.await();
                        // Block
                        clientTask.await();

                        test.assertEqual(bytes, clientReadBytes.get());
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                });
            });

            runner.testGroup("acceptAsync()", () ->
            {
                runner.test("with connection while accepting on port " + port.incrementAndGet(), (Test test) ->
                {
                    final byte[] bytes = new byte[] { 10, 20, 30, 40, 50 };
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    try (final TCPServer tcpServer = JavaTCPServer.create(IPv4Address.localhost, port.get(), asyncRunner).getValue())
                    {
                        final AsyncAction serverTask = tcpServer.acceptAsync()
                            .then((Result<TCPClient> tcpClientResult) ->
                            {
                                test.assertSuccess(tcpClientResult);

                                final TCPClient tcpClient = tcpClientResult.getValue();
                                final Result<byte[]> serverReadBytes = tcpClient.readBytes(bytes.length);
                                test.assertSuccess(bytes, serverReadBytes);
                                tcpClient.write(serverReadBytes.getValue());
                                tcpClient.dispose();
                            });

                        final AsyncAction clientTask = asyncRunner.schedule(() ->
                        {
                            // The tcpClient code needs to be in a different thread because the
                            // tcpServer runs its acceptAsync().then() action on the main thread.
                            try (final TCPClient tcpClient = JavaTCPClient.create(IPv4Address.localhost, port.get(), asyncRunner).getValue())
                            {
                                tcpClient.write(bytes);
                                final Result<byte[]> clientReadBytes = tcpClient.readBytes(bytes.length);
                                test.assertSuccess(bytes, clientReadBytes);
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
                    final TCPServer tcpServer = JavaTCPServer.create(port.incrementAndGet(), test.getParallelAsyncRunner()).getValue();

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
