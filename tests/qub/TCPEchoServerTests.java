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

                    asyncRunner.await();
                }
            });

            runner.test("echoAsync()", (Test test) ->
            {
                final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                final Network network = new JavaNetwork(asyncRunner);
                try (final TCPEchoServer echoServer = TCPEchoServer.create(network, port.incrementAndGet()).getValue())
                {
                    final AsyncAction echoServerTask = echoServer.echoAsync();

                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        //PARALLEL
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

                    //MAIN
                    echoServerTask.await();
                    clientTask.await();
                }
            });
        });
    }
}
