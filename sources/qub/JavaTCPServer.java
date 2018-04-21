package qub;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class JavaTCPServer extends AsyncDisposableBase implements TCPServer
{
    private static final int tcpClientBacklog = 50;

    private final ServerSocket serverSocket;
    private final AsyncRunner asyncRunner;
    private boolean disposed;

    JavaTCPServer(ServerSocket serverSocket, AsyncRunner asyncRunner)
    {
        this.serverSocket = serverSocket;
        this.asyncRunner = asyncRunner;
    }

    static Result<TCPServer> create(int localPort, AsyncRunner asyncRunner)
    {
        Result<TCPServer> result;
        if (asyncRunner == null)
        {
            result = Result.error(new IllegalArgumentException("asyncRunner cannot be null."));
        }
        else
        {
            result = createAsync(localPort, asyncRunner).awaitReturn();
        }
        return result;
    }

    static AsyncFunction<Result<TCPServer>> createAsync(final int localPort, final AsyncRunner asyncRunner)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<TCPServer>> result;

        if (localPort <= 0)
        {
            result = currentAsyncRunner.error(new IllegalArgumentException("localPort must be greater than 0."));
        }
        else if (localPort >= 65536)
        {
            result = currentAsyncRunner.error(new IllegalArgumentException("localPort must be less than 65536."));
        }
        else
        {
            result = asyncRunner.schedule(new Function0<Result<TCPServer>>()
                {
                    @Override
                    public Result<TCPServer> run()
                    {
                        Result<TCPServer> result;
                        try
                        {
                            final ServerSocket serverSocket = new ServerSocket(localPort, tcpClientBacklog);
                            result = Result.<TCPServer>success(new JavaTCPServer(serverSocket, asyncRunner));
                        }
                        catch (IOException e)
                        {
                            result = Result.error(e);
                        }
                        return result;
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    static Result<TCPServer> create(IPv4Address localIPAddress, int localPort, AsyncRunner asyncRunner)
    {
        Result<TCPServer> result;
        if (asyncRunner == null)
        {
            result = Result.error(new IllegalArgumentException("asyncRunner cannot be null."));
        }
        else
        {
            result = createAsync(localIPAddress, localPort, asyncRunner).awaitReturn();
        }
        return result;
    }

    static AsyncFunction<Result<TCPServer>> createAsync(final IPv4Address localIPAddress, final int localPort, final AsyncRunner asyncRunner)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<TCPServer>> result;

        if (localIPAddress == null)
        {
            result = currentAsyncRunner.error(new IllegalArgumentException("localIPAddress cannot be null."));
        }
        else if (localPort <= 0)
        {
            result = currentAsyncRunner.error(new IllegalArgumentException("localPort must be greater than 0."));
        }
        else if (localPort >= 65536)
        {
            result = currentAsyncRunner.error(new IllegalArgumentException("localPort must be less than 65536."));
        }
        else
        {
            result = asyncRunner.schedule(new Function0<Result<TCPServer>>()
                {
                    @Override
                    public Result<TCPServer> run()
                    {
                        Result<TCPServer> result;
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
                        return result;
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public Result<TCPClient> accept()
    {
        return acceptAsync().awaitReturn();
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
            })
            .thenOn(currentAsyncRunner);
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public AsyncFunction<Result<Boolean>> disposeAsync()
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Boolean>> result;
        if (disposed)
        {
            result = currentAsyncRunner.success(false);
        }
        else
        {
            disposed = true;
            result = asyncRunner.schedule(new Function0<Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run()
                    {
                        Result<Boolean> result;
                        try
                        {
                            serverSocket.close();
                            result = Result.success(true);
                        }
                        catch (IOException e)
                        {
                            result = Result.<Boolean>error(e);
                        }
                        return result;
                    }
                })
                .thenOn(currentAsyncRunner);
        }
        return result;
    }
}
