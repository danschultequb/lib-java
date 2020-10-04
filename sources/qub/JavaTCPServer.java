package qub;

class JavaTCPServer implements TCPServer
{
    private static final int tcpClientBacklog = 50;

    private final java.net.ServerSocket serverSocket;
    private final Clock clock;

    private JavaTCPServer(java.net.ServerSocket serverSocket, Clock clock)
    {
        PreCondition.assertNotNull(serverSocket, "serverSocket");
        PreCondition.assertNotNull(clock, "clock");

        this.serverSocket = serverSocket;
        this.clock = clock;
    }

    static Result<TCPServer> create(int localPort, Clock clock)
    {
        Network.validateLocalPort(localPort);
        PreCondition.assertNotNull(clock, "clock");

        Result<TCPServer> result;
        try
        {
            final java.net.ServerSocket serverSocket = new java.net.ServerSocket(localPort, tcpClientBacklog);
            result = Result.success(new JavaTCPServer(serverSocket, clock));
        }
        catch (Throwable error)
        {
            result = Result.error(error);
        }
        return result;
    }

    static Result<TCPServer> create(IPv4Address localIPAddress, int localPort, Clock clock)
    {
        Network.validateLocalIPAddress(localIPAddress);
        Network.validateLocalPort(localPort);
        PreCondition.assertNotNull(clock, "clock");

        Result<TCPServer> result;
        try
        {
            final byte[] localIPAddressBytes = localIPAddress.toBytes();
            final java.net.InetAddress localInetAddress = java.net.InetAddress.getByAddress(localIPAddressBytes);
            final java.net.ServerSocket serverSocket = new java.net.ServerSocket(localPort, tcpClientBacklog, localInetAddress);
            result = Result.success(new JavaTCPServer(serverSocket, clock));
        }
        catch (Throwable error)
        {
            result = Result.error(error);
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
        return IPv4Address.parse(inetAddressString).await();
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
            final java.net.Socket socket = serverSocket.accept();
            result = JavaTCPClient.create(socket);
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public Result<TCPClient> accept(Duration timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertNotDisposed(this, "this");

        final DateTime dateTimeTimeout = clock.getCurrentDateTime().plus(timeout);
        return accept(dateTimeTimeout);
    }

    @Override
    public Result<TCPClient> accept(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");

        Result<TCPClient> result;
        try
        {
            final java.net.Socket socket = serverSocket.accept();
            result = JavaTCPClient.create(socket);
        }
        catch (java.io.IOException e)
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
            catch (java.io.IOException e)
            {
                result = Result.error(e);
            }
        }
        return result;
    }
}
