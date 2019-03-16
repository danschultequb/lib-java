package qub;

public class TCPEchoServerTests
{
    public static void test(TestRunner runner)
    {
        final IntegerValue port = new IntegerValue(14000);

        runner.testGroup(TCPEchoServer.class, () ->
        {
            runner.test("echo()", (Test test) ->
            {
                final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                final Network network = new JavaNetwork(asyncRunner);
                final Result<TCPEchoServer> echoServerResult = TCPEchoServer.create(network, port.increment().getAsInt());
                test.assertSuccessDispose(echoServerResult,
                    (TCPEchoServer echoServer) ->
                    {
                        final AsyncAction serverTask = echoServer.echoAsync();

                        final AsyncAction clientTask = asyncRunner.schedule(() ->
                        {
                            try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port.get()).await())
                            {
                                final CharacterWriteStream tcpClientCharacterWriteStream = tcpClient.asCharacterWriteStream();
                                final LineReadStream tcpClientLineReadStream = tcpClient.asLineReadStream();

                                tcpClientCharacterWriteStream.writeLine("Hello");
                                test.assertEqual("Hello", tcpClientLineReadStream.readLine().await());

                                tcpClientCharacterWriteStream.writeLine("World");
                                test.assertEqual("World", tcpClientLineReadStream.readLine().await());
                            }
                        });

                        asyncRunner.awaitAll(clientTask, serverTask);
                    });
            });

            runner.test("echoAsync()", (Test test) ->
            {
                final AsyncRunner asyncRunner = test.getParallelAsyncRunner();

                final Network network = new JavaNetwork(asyncRunner);
                try (final TCPEchoServer echoServer = TCPEchoServer.create(network, port.increment().getAsInt()).await())
                {
                    final AsyncAction echoServerTask = echoServer.echoAsync();

                    final AsyncAction clientTask = asyncRunner.schedule(() ->
                    {
                        try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port.get()).await())
                        {
                            final CharacterWriteStream tcpClientCharacterWriteStream = tcpClient.asCharacterWriteStream();
                            final LineReadStream tcpClientLineReadStream = tcpClient.asLineReadStream();

                            tcpClientCharacterWriteStream.writeLine("Hello");
                            test.assertEqual("Hello", tcpClientLineReadStream.readLine().await());

                            tcpClientCharacterWriteStream.writeLine("World");
                            test.assertEqual("World", tcpClientLineReadStream.readLine().await());
                        }
                    });

                    echoServerTask.await();
                    clientTask.await();
                }
            });
        });
    }
}
