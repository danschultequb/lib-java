package qub;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaTCPClientTests
{
    public static void test(TestRunner runner)
    {
        final AtomicInteger port = new AtomicInteger(13000);

        runner.testGroup(JavaTCPClient.class, () ->
        {
            runner.testGroup("create(Socket)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Result<TCPClient> tcpClientResult = JavaTCPClient.create((Socket)null);
                    test.assertError(new IllegalArgumentException("socket cannot be null."), tcpClientResult);
                });
            });

            runner.testGroup("create(IPv4Address,int)", () ->
            {
                runner.test("with null remoteIPAddress", (Test test) ->
                {
                    final Result<TCPClient> tcpClient = JavaTCPClient.create(null, 80);
                    test.assertError(new IllegalArgumentException("remoteIPAddress cannot be null."), tcpClient);
                });

                runner.test("with -1 remotePort", (Test test) ->
                {
                    final Result<TCPClient> tcpClient = JavaTCPClient.create(IPv4Address.localhost, -1);
                    test.assertError(new IllegalArgumentException("remotePort must be greater than 0."), tcpClient);
                });

                runner.test("with 0 remotePort", (Test test) ->
                {
                    final Result<TCPClient> tcpClient = JavaTCPClient.create(IPv4Address.localhost, 0);
                    test.assertError(new IllegalArgumentException("remotePort must be greater than 0."), tcpClient);
                });

                runner.test("with valid arguments but no server listening", (Test test) ->
                {
                    final Result<TCPClient> tcpClientResult = JavaTCPClient.create(IPv4Address.localhost, port.incrementAndGet());
                    test.assertError(new java.net.ConnectException("Connection refused: connect"), tcpClientResult);
                });

                runner.test("with valid arguments and with listening server", runner.skip(), (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                    final Network network = new JavaNetwork(asyncRunner);
                    final Result<TCPEchoServer> echoServerResult = TCPEchoServer.create(network, port.incrementAndGet());
                    test.assertSuccess(echoServerResult);

                    try (final TCPEchoServer echoServer = echoServerResult.getValue())
                    {
                        echoServer.echoAsync();

                        asyncRunner.schedule(() ->
                        {
                            final Result<TCPClient> tcpClientResult = JavaTCPClient.create(IPv4Address.localhost, port.get());
                            test.assertSuccess(tcpClientResult);

                            try (final TCPClient tcpClient = tcpClientResult.getValue())
                            {
                                tcpClient.asLineWriteStream().writeLine("abcdef");
                                test.assertEqual("abcdef", tcpClient.asLineReadStream().readLine());
                            }
                        }).await();
                    }
                });
            });
        });
    }
}
