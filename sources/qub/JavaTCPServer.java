package qub;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

class JavaTCPServer extends DisposableBase implements TCPServer
{
    private static final int tcpClientBacklog = 50;

    private final ServerSocket serverSocket;
    private final AsyncRunner asyncRunner;
    private Action1<IOException> exceptionHandler;
    private boolean disposed;

    JavaTCPServer(ServerSocket serverSocket, AsyncRunner asyncRunner)
    {
        this.serverSocket = serverSocket;
        this.asyncRunner = asyncRunner;
    }

    static Result<TCPServer> create(int localPort, AsyncRunner asyncRunner)
    {
        Result<TCPServer> result;

        if (localPort <= 0)
        {
            result = Result.error(new IllegalArgumentException("localPort must be greater than 0."));
        }
        else
        {
            try
            {
                final ServerSocket serverSocket = new ServerSocket(localPort, tcpClientBacklog);
                final TCPServer tcpServer = new JavaTCPServer(serverSocket, asyncRunner);
                result = Result.success(tcpServer);
            }
            catch (IOException e)
            {
                result = Result.error(e);
            }
        }

        return result;
    }

    static Result<TCPServer> create(IPv4Address localIPAddress, int localPort, AsyncRunner asyncRunner)
    {
        Result<TCPServer> result;

        if (localIPAddress == null)
        {
            result = Result.error(new IllegalArgumentException("localIPAddress cannot be null."));
        }
        else if (localPort <= 0)
        {
            result = Result.error(new IllegalArgumentException("localPort must be greater than 0."));
        }
        else
        {
            try
            {
                final byte[] localIPAddressBytes = localIPAddress.toBytes();
                final InetAddress localInetAddress = InetAddress.getByAddress(localIPAddressBytes);
                final ServerSocket serverSocket = new ServerSocket(localPort, tcpClientBacklog, localInetAddress);
                final TCPServer tcpServer = new JavaTCPServer(serverSocket, asyncRunner);
                result = Result.success(tcpServer);
            }
            catch (IOException e)
            {
                result = Result.error(e);
            }
        }

        return result;
    }

    @Override
    public void setExceptionHandler(Action1<IOException> exceptionHandler)
    {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public Result<TCPClient> accept()
    {
        return null;
    }

    @Override
    public AsyncFunction<TCPClient> acceptAsync()
    {
        return async(new Function0<TCPClient>()
        {
            @Override
            public TCPClient run()
            {
                return accept().getValue();
            }
        });
    }

    private <T> AsyncFunction<T> async(Function0<T> function)
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
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (disposed)
        {
            result = Result.success(false);
        }
        else
        {
            disposed = true;
            try
            {
                serverSocket.close();
                result = Result.success(true);
            }
            catch (IOException e)
            {
                result = Result.<Boolean>error(e);
            }
        }
        return result;
    }
}
