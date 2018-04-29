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

    public Result<Boolean> echo()
    {
        Result<Boolean> result = Result.successTrue();

        final Result<TCPClient> acceptResult = tcpServer.accept();
        if (acceptResult.hasError())
        {
            result = Result.error(acceptResult.getError());
        }
        else
        {
            try (final TCPClient tcpClient = acceptResult.getValue())
            {
                final LineReadStream tcpClientLineReadStream = tcpClient.asLineReadStream(true);
                final LineWriteStream tcpClientLineWriteStream = tcpClient.asLineWriteStream();

                Result<String> readLineResult = tcpClientLineReadStream.readLine();
                while (readLineResult.getValue() != null)
                {
                    tcpClientLineWriteStream.write(readLineResult.getValue());
                    readLineResult = tcpClientLineReadStream.readLine();
                }
            }
            catch (Exception e)
            {
                result = Result.error(e);
            }
        }

        return result;
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
                    try
                    {
                        echo();
                    }
                    catch (Exception e)
                    {
                        Exceptions.throwAsRuntime(e);
                    }
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
