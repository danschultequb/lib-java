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
        Network.validateLocalPort(localPort);

        Result<TCPServer> result;
        try
        {
            final ServerSocket serverSocket = new ServerSocket(localPort, tcpClientBacklog);
            result = Result.success(new JavaTCPServer(serverSocket, asyncRunner));
        }
        catch (IOException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    static AsyncFunction<Result<TCPServer>> createAsync(int localPort, AsyncRunner asyncRunner)
    {
        Network.validateLocalPort(localPort);
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");
        PreCondition.assertFalse(asyncRunner.isDisposed(), "asyncRunner.isDisposed()");

        return asyncRunner.scheduleSingle(() -> JavaTCPServer.create(localPort, asyncRunner));
    }

    static Result<TCPServer> create(IPv4Address localIPAddress, int localPort, AsyncRunner asyncRunner)
    {
        Network.validateLocalIPAddress(localIPAddress);
        Network.validateLocalPort(localPort);

        Result<TCPServer> result;
        try
        {
            final byte[] localIPAddressBytes = localIPAddress.toBytes();
            final InetAddress localInetAddress = InetAddress.getByAddress(localIPAddressBytes);
            final ServerSocket serverSocket = new ServerSocket(localPort, tcpClientBacklog, localInetAddress);
            result = Result.success(new JavaTCPServer(serverSocket, asyncRunner));
        }
        catch (IOException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    static AsyncFunction<Result<TCPServer>> createAsync(IPv4Address localIPAddress, int localPort, AsyncRunner asyncRunner)
    {
        Network.validateLocalIPAddress(localIPAddress);
        Network.validateLocalPort(localPort);
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");
        PreCondition.assertFalse(asyncRunner.isDisposed(), "asyncRunner.isDisposed()");

        return asyncRunner.scheduleSingle(() -> JavaTCPServer.create(localIPAddress, localPort, asyncRunner));
    }

    @Override
    public Clock getClock()
    {
        return asyncRunner.getClock();
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
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public Result<TCPClient> accept(DateTime timeout)
    {
        Result<TCPClient> result;
        try
        {
            final Socket socket = serverSocket.accept();
            result = JavaTCPClient.create(socket, asyncRunner);
        }
        catch (IOException e)
        {
            result = Result.error(e);
        }
        return result;
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
                result = Result.error(e);
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
