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
                final Network network = new JavaNetwork(test.getClock(), test.getParallelAsyncRunner());
                try (final TCPEchoServer echoServer = TCPEchoServer.create(network, port.increment().getAsInt()).await())
                {
                    final Result<Void> serverTask = echoServer.echo();

                    final Result<Void> clientTask = test.getParallelAsyncRunner().schedule(() ->
                    {
                        try (final TCPClient tcpClient = network.createTCPClient(IPv4Address.localhost, port.get()).await())
                        {
                            final CharacterWriteStream tcpClientWriteStream = tcpClient.asCharacterWriteStream();
                            final CharacterReadStream tcpClientReadStream = tcpClient.asCharacterReadStream();

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
