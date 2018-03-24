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
                    test.assertNotNull(tcpClientResult);
                    test.assertTrue(tcpClientResult.isError());
                    test.assertEqual("socket cannot be null.", tcpClientResult.getErrorMessage());
                });
            });

            runner.testGroup("create(IPv4Address,int)", () ->
            {
                runner.test("with null remoteIPAddress", (Test test) ->
                {
                    final Result<TCPClient> tcpClient = JavaTCPClient.create(null, 80);
                    test.assertTrue(tcpClient.isError());
                    test.assertEqual("remoteIPAddress cannot be null.", tcpClient.getErrorMessage());
                });

                runner.test("with -1 remotePort", (Test test) ->
                {
                    final Result<TCPClient> tcpClient = JavaTCPClient.create(IPv4Address.localhost, -1);
                    test.assertTrue(tcpClient.isError());
                    test.assertEqual("remotePort must be greater than 0.", tcpClient.getErrorMessage());
                });

                runner.test("with 0 remotePort", (Test test) ->
                {
                    final Result<TCPClient> tcpClient = JavaTCPClient.create(IPv4Address.localhost, 0);
                    test.assertTrue(tcpClient.isError());
                    test.assertEqual("remotePort must be greater than 0.", tcpClient.getErrorMessage());
                });

                runner.test("with valid arguments but no server listening", (Test test) ->
                {
                    final Result<TCPClient> tcpClientResult = JavaTCPClient.create(IPv4Address.localhost, port.incrementAndGet());
                    test.assertTrue(tcpClientResult.isError());
                    test.assertEqual("Connection refused: connect", tcpClientResult.getErrorMessage());
                });

                runner.test("with valid arguments and with listening server", runner.skip(), (Test test) ->
                {
                    try (final ParallelAsyncRunner asyncRunner = new ParallelAsyncRunner())
                    {
                        final Network network = new JavaNetwork(asyncRunner);
                        final TCPEchoServer echoServer = TCPEchoServer.create(network, port.incrementAndGet()).getValue();
                        echoServer.echoAsync();

                        asyncRunner.schedule(() ->
                        {
                            final Result<TCPClient> tcpClientResult = JavaTCPClient.create(IPv4Address.localhost, port.get());
                            test.assertTrue(tcpClientResult.isSuccess());

                            try (final TCPClient tcpClient = tcpClientResult.getValue())
                            {
                                tcpClient.asLineWriteStream().writeLine("abcdef");
                                test.assertEqual("abcdef", tcpClient.asLineReadStream().readLine());
                            }
                        });

                        AsyncRunnerRegistry.getCurrentThreadAsyncRunner().await();
                    }
                });
            });
        });
    }
}
