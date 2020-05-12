package qub;

public interface TCPEchoServerTests
{
    static void test(TestRunner runner)
    {
        final IntegerValue port = new IntegerValue(14000);

        runner.testGroup(TCPEchoServer.class, () ->
        {
            runner.test("echo()", (Test test) ->
            {
                final Network network = JavaNetwork.create(test.getClock());
                try (final TCPEchoServer echoServer = TCPEchoServer.create(network, port.increment().getAsInt()).await())
                {
                    final AsyncRunner parallelAsyncRunner = test.getParallelAsyncRunner();
                    final Result<Void> serverTask = parallelAsyncRunner.schedule((() -> echoServer.echo().await()));

                    final Result<Void> clientTask = parallelAsyncRunner.schedule(() ->
                    {
                        try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port.get()).await())
                        {
                            final CharacterWriteStream tcpClientWriteStream = CharacterWriteStream.create(tcpClient);
                            final CharacterReadStream tcpClientReadStream = CharacterReadStream.create(tcpClient);

                            tcpClientWriteStream.writeLine("Hello").await();
                            test.assertEqual("Hello", tcpClientReadStream.readLine().await());

                            tcpClientWriteStream.writeLine("World").await();
                            test.assertEqual("World", tcpClientReadStream.readLine().await());
                        }
                    });

                    Result.await(clientTask, serverTask);
                }
            });
        });
    }
}
