package qub;

import java.util.concurrent.atomic.AtomicInteger;

public class TCPEchoServerTests
{
    public static void test(TestRunner runner)
    {
        final AtomicInteger port = new AtomicInteger(14000);

        runner.testGroup(TCPEchoServer.class, () ->
        {
            runner.test("echo()", (Test test) ->
            {
                try (final ParallelAsyncRunner asyncRunner = new ParallelAsyncRunner())
                {
                    final Network network = new JavaNetwork(asyncRunner);
                    try (final TCPEchoServer echoServer = TCPEchoServer.create(network, port.incrementAndGet()).getValue())
                    {
                        asyncRunner.schedule(echoServer::echo);

                        asyncRunner.schedule(() ->
                        {
                            try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port.get()).getValue())
                            {
                                final LineWriteStream tcpClientLineWriteStream = tcpClient.asLineWriteStream();
                                final LineReadStream tcpClientLineReadStream = tcpClient.asLineReadStream();

                                tcpClientLineWriteStream.writeLine("Hello");
                                test.assertEqual("Hello", tcpClientLineReadStream.readLine());

                                tcpClientLineWriteStream.writeLine("World");
                                test.assertEqual("World", tcpClientLineReadStream.readLine());
                            }
                        });

                        AsyncRunnerRegistry.getCurrentThreadAsyncRunner().await();
                    }
                }
            });

            runner.test("echoAsync()", (Test test) ->
            {
                try (final ParallelAsyncRunner asyncRunner = new ParallelAsyncRunner())
                {
                    final Network network = new JavaNetwork(asyncRunner);
                    try (final TCPEchoServer echoServer = TCPEchoServer.create(network, port.incrementAndGet()).getValue())
                    {
                        echoServer.echoAsync();

                        asyncRunner.schedule(() ->
                        {
                            final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port.get()).getValue();
                            final LineWriteStream tcpClientLineWriteStream = tcpClient.asLineWriteStream();
                            final LineReadStream tcpClientLineReadStream = tcpClient.asLineReadStream();

                            tcpClientLineWriteStream.writeLine("Hello");
                            test.assertEqual("Hello", tcpClientLineReadStream.readLine());

                            tcpClientLineWriteStream.writeLine("World");
                            test.assertEqual("World", tcpClientLineReadStream.readLine());
                        });

                        AsyncRunnerRegistry.getCurrentThreadAsyncRunner().await();
                    }
                }
            });
        });
    }
}
