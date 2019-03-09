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
                final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                final Network network = new JavaNetwork(asyncRunner);
                final Result<TCPEchoServer> echoServerResult = TCPEchoServer.create(network, port.incrementAndGet());
                test.assertSuccessDispose(echoServerResult,
                    (TCPEchoServer echoServer) ->
                    {
                        final AsyncAction serverTask = echoServer.echoAsync();

                        final AsyncAction clientTask = asyncRunner.schedule(() ->
                        {
                            try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port.get()).awaitError())
                            {
                                final LineWriteStream tcpClientLineWriteStream = tcpClient.asLineWriteStream();
                                final LineReadStream tcpClientLineReadStream = tcpClient.asLineReadStream();

                                tcpClientLineWriteStream.writeLine("Hello");
                                test.assertSuccess("Hello", tcpClientLineReadStream.readLine());

                                tcpClientLineWriteStream.writeLine("World");
                                test.assertSuccess("World", tcpClientLineReadStream.readLine());
                            }
                        });

                        asyncRunner.awaitAll(clientTask, serverTask);
                    });
            });

            runner.test("echoAsync()", (Test test) ->
            {
                final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                final Network network = new JavaNetwork(asyncRunner);
                try (final TCPEchoServer echoServer = TCPEchoServer.create(network, port.incrementAndGet()).awaitError())
                {
                    final AsyncAction echoServerTask = echoServer.echoAsync();

                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port.get()).awaitError())
                        {
                            final LineWriteStream tcpClientLineWriteStream = tcpClient.asLineWriteStream();
                            final LineReadStream tcpClientLineReadStream = tcpClient.asLineReadStream();

                            tcpClientLineWriteStream.writeLine("Hello");
                            test.assertSuccess("Hello", tcpClientLineReadStream.readLine());

                            tcpClientLineWriteStream.writeLine("World");
                            test.assertSuccess("World", tcpClientLineReadStream.readLine());
                        }
                    });

                    echoServerTask.await();
                    clientTask.await();
                }
            });
        });
    }
}
