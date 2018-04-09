package qub;

public class TCPEchoServer extends AsyncDisposableBase
{
    private final TCPServer tcpServer;

    private TCPEchoServer(TCPServer tcpServer)
    {
        this.tcpServer = tcpServer;
    }

    public static Result<TCPEchoServer> create(Network network, int port)
    {
        Result<TCPEchoServer> result;

        final Result<TCPServer> tcpServerResult = network.createTCPServer(port);
        if (tcpServerResult.hasError())
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

    public void echo()
    {
        echoAsync().await();
    }

    public AsyncAction echoAsync()
    {
        return tcpServer.acceptAsync()
            .then(new Action1<Result<TCPClient>>()
            {
                @Override
                public void run(Result<TCPClient> tcpClientResult)
                {
                    //MAIN
                    if (!tcpClientResult.hasError())
                    {
                        try (final TCPClient tcpClient = tcpClientResult.getValue())
                        {
                            final LineReadStream tcpClientLineReadStream = tcpClient.asLineReadStream(true);
                            final LineWriteStream tcpClientLineWriteStream = tcpClient.asLineWriteStream();

                            //BLOCKING
                            String line = tcpClientLineReadStream.readLine();
                            while (line != null)
                            {
                                //BLOCKING
                                tcpClientLineWriteStream.write(line);
                                //BLOCKING
                                line = tcpClientLineReadStream.readLine();
                            }
                        }
                    }
                }
            });
    }

    @Override
    public boolean isDisposed()
    {
        return tcpServer.isDisposed();
    }

    @Override
    public AsyncFunction<Result<Boolean>> disposeAsync()
    {
        return tcpServer.disposeAsync();
    }
}
