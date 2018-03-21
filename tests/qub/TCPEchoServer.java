package qub;

public class TCPEchoServer extends DisposableBase
{
    private final TCPServer tcpServer;

    private TCPEchoServer(TCPServer tcpServer)
    {
        this.tcpServer = tcpServer;
    }

    private void echo(TCPClient tcpClient)
    {
        try
        {
            final LineReadStream tcpClientLineReadStream = tcpClient.asLineReadStream(true);
            final LineWriteStream tcpClientLineWriteStream = tcpClient.asLineWriteStream();

            String line = tcpClientLineReadStream.readLine();
            while (line != null)
            {
                tcpClientLineWriteStream.write(line);
                line = tcpClientLineReadStream.readLine();
            }
        }
        finally
        {
            tcpClient.dispose();
        }
    }

    public void echo()
    {
        final Result<TCPClient> tcpClientResult = tcpServer.accept();
        if (tcpClientResult.isSuccess())
        {
            echo(tcpClientResult.getValue());
        }
    }

    public void echoAsync()
    {
        tcpServer.acceptAsync()
            .then((Action1<TCPClient>)this::echo);
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

    public static Result<TCPEchoServer> create(Network network, int port)
    {
        Result<TCPEchoServer> result;

        final Result<TCPServer> tcpServerResult = network.createTCPServer(port);
        if (tcpServerResult.isError())
        {
            result = Result.error(tcpServerResult.getError());
        }
        else
        {
            final TCPEchoServer tcpEchoServer = new TCPEchoServer(tcpServerResult.getValue());
            result = Result.success(tcpEchoServer);
        }

        return result;
    }
}
