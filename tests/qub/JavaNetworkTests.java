package qub;

public class JavaNetworkTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaNetwork.class, () ->
        {
            runner.testGroup("createTCPClient(IPv4Address,int)", () ->
            {
                runner.test("with null remoteIPAddress", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(null, 80);
                    test.assertError(new IllegalArgumentException("remoteIPAddress cannot be null."), tcpClientResult);
                });

                runner.test("with -1 remotePort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), -1);
                    test.assertError(new IllegalArgumentException("remotePort (-1) must be between 1 and 65535."), tcpClientResult);
                });

                runner.test("with 0 remotePort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 0);
                    test.assertError(new IllegalArgumentException("remotePort (0) must be between 1 and 65535."), tcpClientResult);
                });

                runner.test("with valid arguments but no server listening", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPClient> tcpClientResult = network.createTCPClient(IPv4Address.parse("127.0.0.1"), 38827);
                    test.assertError(new java.net.ConnectException("Connection refused: connect"), tcpClientResult);
                });

                runner.test("with valid arguments and server listening", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();
                    final JavaNetwork network = new JavaNetwork(asyncRunner);

                    final byte[] bytes = new byte[] { 1, 2, 3, 4, 5 };

                    final IPv4Address localhost = IPv4Address.parse("127.0.0.1");
                    final int port = 8080;

                    final AsyncAction serverTask = asyncRunner.schedule(() ->
                    {
                        final Result<TCPServer> tcpServerResult = network.createTCPServer(localhost, port);
                        test.assertSuccess(tcpServerResult);
                        try (final TCPServer tcpServer = tcpServerResult.getValue())
                        {
                            final Result<TCPClient> acceptedClientResult = tcpServer.accept();
                            test.assertSuccess(acceptedClientResult);
                            try (final TCPClient acceptedClient = acceptedClientResult.getValue())
                            {
                                test.assertEqual(bytes, acceptedClient.readBytes(bytes.length));
                                test.assertSuccess(true, acceptedClient.write(bytes));
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
                    });

                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        final Result<TCPClient> tcpClientResult = network.createTCPClient(localhost, port);
                        test.assertSuccess(tcpClientResult);
                        try (final TCPClient tcpClient = tcpClientResult.getValue())
                        {
                            test.assertSuccess(true, tcpClient.write(bytes));
                            test.assertEqual(bytes, tcpClient.readBytes(bytes.length));
                        }
                        catch (Exception e)
                        {
                            test.fail(e);
                        }
                    });

                    clientTask.await();
                    serverTask.await();
                });
            });

            runner.testGroup("createTCPServer(int)", () ->
            {
                runner.test("with -1 localPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(-1);
                    test.assertError(new IllegalArgumentException("localPort (-1) must be between 1 and 65535."), tcpServerResult);
                });

                runner.test("with 0 localPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(0);
                    test.assertError(new IllegalArgumentException("localPort (0) must be between 1 and 65535."), tcpServerResult);
                });

                runner.test("with 8088 localPort", (Test test) ->
                {
                    final JavaNetwork network = new JavaNetwork(test.getParallelAsyncRunner());
                    final Result<TCPServer> tcpServerResult = network.createTCPServer(8088);
                    test.assertSuccess(tcpServerResult);
                    test.assertSuccess(tcpServerResult.getValue().dispose());
                });
            });
        });
    }
}
