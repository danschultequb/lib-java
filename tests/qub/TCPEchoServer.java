package qub;

public class TCPEchoServer implements AsyncDisposable
{
    private final TCPServer tcpServer;

    private TCPEchoServer(TCPServer tcpServer)
    {
        this.tcpServer = tcpServer;
    }

    public static Result<TCPEchoServer> create(Network network, int localPort)
    {
        PreCondition.assertNotNull(network, "network");
        Network.validateLocalPort(localPort);

        return network.createTCPServer(localPort)
            .then(TCPEchoServer::new);
    }

    public Result<Void> echo()
    {
        return tcpServer.accept()
            .then((TCPClient tcpClient) ->
            {
                try
                {
                    final LineReadStream tcpClientLineReadStream = tcpClient.asLineReadStream(true);
                    final LineWriteStream tcpClientLineWriteStream = tcpClient.asLineWriteStream();

                    String line = tcpClientLineReadStream.readLine().awaitError();
                    while (line != null)
                    {
                        tcpClientLineWriteStream.write(line);
                        line = tcpClientLineReadStream.readLine().awaitError();
                    }
                }
                finally
                {
                    tcpClient.dispose().awaitError();
                }
            });
    }

    public AsyncAction echoAsync()
    {
        return getAsyncRunner().scheduleSingle(this::echo);
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return tcpServer.getAsyncRunner();
    }

    @Override
    public boolean isDisposed()
    {
        return tcpServer.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return tcpServer.dispose();
    }
}
