package qub;

public interface TCPEchoServerTests
{
    public static void test(TestRunner runner)
    {
        final IntegerValue port = IntegerValue.create(14000);

        runner.testGroup(TCPEchoServer.class, () ->
        {
            runner.test("echo()",
                (TestResources resources) -> Tuple.create(resources.getClock(), resources.getParallelAsyncRunner()),
                (Test test, Clock clock, AsyncRunner parallelAsyncRunner) ->
            {
                final Network network = JavaNetwork.create(clock);
                try (final TCPEchoServer echoServer = TCPEchoServer.create(network, port.increment().getAsInt()).await())
                {
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
