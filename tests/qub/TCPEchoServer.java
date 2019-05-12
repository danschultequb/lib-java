package qub;

public class TCPEchoServer implements Disposable
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
                    final CharacterReadStream tcpClientReadStream = tcpClient.asCharacterReadStream();
                    final CharacterWriteStream tcpClientWriteStream = tcpClient.asCharacterWriteStream();

                    String line = tcpClientReadStream.readLine(true)
                        .catchError(EndOfStreamException.class)
                        .await();
                    while (line != null)
                    {
                        tcpClientWriteStream.write(line).await();

                        line = tcpClientReadStream.readLine(true)
                            .catchError(EndOfStreamException.class)
                            .await();
                    }
                }
                finally
                {
                    tcpClient.dispose().await();
                }
            });
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
