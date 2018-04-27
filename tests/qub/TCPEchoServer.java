package qub;

public class TCPEchoServer extends AsyncDisposableBase
{
    private final TCPServer tcpServer;

    private TCPEchoServer(TCPServer tcpServer)
    {
        this.tcpServer = tcpServer;
    }

    public static Result<TCPEchoServer> create(Network network, int localPort)
    {
        Result<TCPEchoServer> result = Result.notNull(network, "network");
        if (result == null)
        {
            final Result<TCPServer> tcpServerResult = network.createTCPServer(localPort);
            if (tcpServerResult.hasError())
            {
                result = Result.error(tcpServerResult.getError());
            }
            else
            {
                final TCPEchoServer tcpEchoServer = new TCPEchoServer(tcpServerResult.getValue());
                result = Result.success(tcpEchoServer);
            }
        }
        return result;
    }

    public void echo()
    {
        final Result<TCPClient> acceptResult = tcpServer.accept();
        if (!acceptResult.hasError())
        {
            try (final TCPClient tcpClient = acceptResult.getValue())
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
        }
    }

    public AsyncAction echoAsync()
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final AsyncRunner tcpServerAsyncRunner = tcpServer.getAsyncRunner();
        return tcpServerAsyncRunner.schedule(new Action0()
            {
                @Override
                public void run()
                {
                    echo();
                }
            })
            .thenOn(currentAsyncRunner);
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
