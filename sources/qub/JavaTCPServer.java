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
        AsyncFunction<Result<TCPServer>> result;

        if (localPort <= 0)
        {
            result = Async.error(asyncRunner, new IllegalArgumentException("localPort must be greater than 0."));
        }
        else
        {
            result = async(asyncRunner, new Function0<Result<ServerSocket>>()
                {
                    @Override
                    public Result<ServerSocket> run()
                    {
                        Result<ServerSocket> result;
                        try
                        {
                            result = Result.success(new ServerSocket(localPort, tcpClientBacklog));
                        }
                        catch (IOException e)
                        {
                            result = Result.error(e);
                        }
                        return result;
                    }
                })
                .then(new Function1<Result<ServerSocket>,Result<TCPServer>>()
                {
                    @Override
                    public Result<TCPServer> run(Result<ServerSocket> serverSocketResult)
                    {
                        return serverSocketResult.hasError()
                            ? Result.<TCPServer>error(serverSocketResult.getError())
                            : Result.<TCPServer>success(new JavaTCPServer(serverSocketResult.getValue(), asyncRunner));
                    }
                });
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
        AsyncFunction<Result<TCPServer>> result;

        if (localIPAddress == null)
        {
            result = Async.error(asyncRunner, new IllegalArgumentException("localIPAddress cannot be null."));
        }
        else if (localPort <= 0)
        {
            result = Async.error(asyncRunner, new IllegalArgumentException("localPort must be greater than 0."));
        }
        else
        {
            result = async(asyncRunner, new Function0<Result<ServerSocket>>()
                {
                    @Override
                    public Result<ServerSocket> run()
                    {
                        Result<ServerSocket> result;
                        try
                        {
                            final byte[] localIPAddressBytes = localIPAddress.toBytes();
                            final InetAddress localInetAddress = InetAddress.getByAddress(localIPAddressBytes);
                            result = Result.success(new ServerSocket(localPort, tcpClientBacklog, localInetAddress));
                        }
                        catch (IOException e)
                        {
                            result = Result.error(e);
                        }
                        return result;
                    }
                })
                .then(new Function1<Result<ServerSocket>, Result<TCPServer>>()
                {
                    @Override
                    public Result<TCPServer> run(Result<ServerSocket> serverSocketResult)
                    {
                        return serverSocketResult.hasError()
                            ? Result.<TCPServer>error(serverSocketResult.getError())
                            : Result.<TCPServer>success(new JavaTCPServer(serverSocketResult.getValue(), asyncRunner));
                    }
                });
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
        return async(new Function0<Result<TCPClient>>()
        {
            @Override
            public Result<TCPClient> run()
            {
                Result<TCPClient> result;
                try
                {
                    final Socket socket = serverSocket.accept();
                    result = JavaTCPClient.create(socket);
                }
                catch (IOException e)
                {
                    result = Result.<TCPClient>error(e);
                }
                return result;
            }
        });
    }

    private <T> AsyncFunction<T> async(Function0<T> function)
    {
        return async(asyncRunner, function);
    }

    private static <T> AsyncFunction<T> async(AsyncRunner asyncRunner, Function0<T> function)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return asyncRunner
            .schedule(function)
            .thenOn(currentRunner);
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public AsyncFunction<Result<Boolean>> disposeAsync()
    {
        AsyncFunction<Result<Boolean>> result;
        if (disposed)
        {
            result = Async.success(asyncRunner, false);
        }
        else
        {
            disposed = true;
            result = async(new Function0<Result<Boolean>>()
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
            });
        }
        return result;
    }
}
