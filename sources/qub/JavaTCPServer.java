package qub;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class JavaTCPServer implements TCPServer
{
    private static final int tcpClientBacklog = 50;

    private final ServerSocket serverSocket;
    private final AsyncRunner asyncRunner;

    JavaTCPServer(ServerSocket serverSocket, AsyncRunner asyncRunner)
    {
        this.serverSocket = serverSocket;
        this.asyncRunner = asyncRunner;
    }

    static Result<TCPServer> create(int localPort, AsyncRunner asyncRunner)
    {
        Result<TCPServer> result = Result.between(1, localPort, 65535, "localPort");
        if (result == null)
        {
            result = Result.notNull(asyncRunner, "asyncRunner");
            if (result == null)
            {
                try
                {
                    final ServerSocket serverSocket = new ServerSocket(localPort, tcpClientBacklog);
                    result = Result.<TCPServer>success(new JavaTCPServer(serverSocket, asyncRunner));
                }
                catch (IOException e)
                {
                    result = Result.error(e);
                }
            }
        }
        return result;
    }

    static AsyncFunction<Result<TCPServer>> createAsync(final int localPort, final AsyncRunner asyncRunner)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<TCPServer>> result = currentAsyncRunner.between(1, localPort, 65535, "localPort");
        if (result == null)
        {
            result = currentAsyncRunner.notNull(asyncRunner, "asyncRunner");
            if (result == null)
            {
                result = asyncRunner.schedule(new Function0<Result<TCPServer>>()
                    {
                        @Override
                        public Result<TCPServer> run()
                        {
                            return JavaTCPServer.create(localPort, asyncRunner);
                        }
                    })
                    .thenOn(currentAsyncRunner);
            }
        }
        return result;
    }

    static Result<TCPServer> create(IPv4Address localIPAddress, int localPort, AsyncRunner asyncRunner)
    {
        Result<TCPServer> result = Result.notNull(localIPAddress, "localIPAddress");
        if (result == null)
        {
            result = Result.between(1, localPort, 65535, "localPort");
            if (result == null)
            {
                result = Result.notNull(asyncRunner, "asyncRunner");
                if (result == null)
                {
                    try
                    {
                        final byte[] localIPAddressBytes = localIPAddress.toBytes();
                        final InetAddress localInetAddress = InetAddress.getByAddress(localIPAddressBytes);
                        final ServerSocket serverSocket = new ServerSocket(localPort, tcpClientBacklog, localInetAddress);
                        result = Result.<TCPServer>success(new JavaTCPServer(serverSocket, asyncRunner));
                    }
                    catch (IOException e)
                    {
                        result = Result.error(e);
                    }
                }
            }
        }
        return result;
    }

    static AsyncFunction<Result<TCPServer>> createAsync(final IPv4Address localIPAddress, final int localPort, final AsyncRunner asyncRunner)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<TCPServer>> result = currentAsyncRunner.notNull(localIPAddress, "localIPAddress");
        if (result == null)
        {
            result = currentAsyncRunner.between(1, localPort, 65535, "localPort");
            if (result == null)
            {
                result = currentAsyncRunner.notNull(asyncRunner, "asyncRunner");
                if (result == null)
                {
                    result = asyncRunner.schedule(new Function0<Result<TCPServer>>()
                        {
                            @Override
                            public Result<TCPServer> run()
                            {
                                return JavaTCPServer.create(localIPAddress, localPort, asyncRunner);
                            }
                        })
                        .thenOn(currentAsyncRunner);
                }
            }
        }

        return result;
    }

    @Override
    public IPv4Address getLocalIPAddress()
    {
        String inetAddressString = serverSocket.getInetAddress().toString();
        if (inetAddressString.startsWith("/")) {
            inetAddressString = inetAddressString.substring(1);
        }
        return IPv4Address.parse(inetAddressString);
    }

    @Override
    public int getLocalPort()
    {
        return serverSocket.getLocalPort();
    }

    @Override
    public Result<TCPClient> accept()
    {
        Result<TCPClient> result;
        try
        {
            final Socket socket = serverSocket.accept();
            result = JavaTCPClient.create(socket, asyncRunner);
        }
        catch (IOException e)
        {
            result = Result.<TCPClient>error(e);
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<TCPClient>> acceptAsync()
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        return asyncRunner.schedule(new Function0<Result<TCPClient>>()
            {
                @Override
                public Result<TCPClient> run()
                {
                    return accept();
                }
            })
            .thenOn(currentAsyncRunner);
    }

    @Override
    public boolean isDisposed()
    {
        return serverSocket.isClosed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (isDisposed())
        {
            result = Result.successFalse();
        }
        else
        {
            try
            {
                serverSocket.close();
                result = Result.successTrue();
            }
            catch (IOException e)
            {
                result = Result.<Boolean>error(e);
            }
        }
        return result;
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }
}
