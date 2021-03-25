package qub;

class JavaTCPServer implements TCPServer
{
    private static final int tcpClientBacklog = 50;

    private final java.net.ServerSocket serverSocket;
    private final Clock clock;

    private JavaTCPServer(java.net.ServerSocket serverSocket, Clock clock)
    {
        PreCondition.assertNotNull(serverSocket, "serverSocket");

        this.serverSocket = serverSocket;
        this.clock = clock;
    }

    static Result<TCPServer> create(int localPort)
    {
        Network.validateLocalPort(localPort);

        Result<TCPServer> result;
        try
        {
            final java.net.ServerSocket serverSocket = new java.net.ServerSocket(localPort, JavaTCPServer.tcpClientBacklog);
            result = Result.success(new JavaTCPServer(serverSocket, null));
        }
        catch (Throwable error)
        {
            result = Result.error(error);
        }
        return result;
    }

    static Result<TCPServer> create(int localPort, Clock clock)
    {
        Network.validateLocalPort(localPort);
        PreCondition.assertNotNull(clock, "clock");

        Result<TCPServer> result;
        try
        {
            final java.net.ServerSocket serverSocket = new java.net.ServerSocket(localPort, JavaTCPServer.tcpClientBacklog);
            result = Result.success(new JavaTCPServer(serverSocket, clock));
        }
        catch (Throwable error)
        {
            result = Result.error(error);
        }
        return result;
    }

    static Result<TCPServer> create(IPv4Address localIPAddress, int localPort)
    {
        Network.validateLocalIPAddress(localIPAddress);
        Network.validateLocalPort(localPort);

        Result<TCPServer> result;
        try
        {
            final byte[] localIPAddressBytes = localIPAddress.toBytes();
            final java.net.InetAddress localInetAddress = java.net.InetAddress.getByAddress(localIPAddressBytes);
            final java.net.ServerSocket serverSocket = new java.net.ServerSocket(localPort, tcpClientBacklog, localInetAddress);
            result = Result.success(new JavaTCPServer(serverSocket, null));
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
        String inetAddressString = this.serverSocket.getInetAddress().toString();
        if (inetAddressString.startsWith("/")) {
            inetAddressString = inetAddressString.substring(1);
        }
        return IPv4Address.parse(inetAddressString).await();
    }

    @Override
    public int getLocalPort()
    {
        return this.serverSocket.getLocalPort();
    }

    @Override
    public Result<TCPClient> accept()
    {
        Result<TCPClient> result;
        try
        {
            this.serverSocket.setSoTimeout(0);
            final java.net.Socket socket = this.serverSocket.accept();
            result = JavaTCPClient.create(socket);
        }
        catch (java.io.IOException e)
        {
            if (Strings.equal(e.getMessage(), "Socket is closed"))
            {
                result = Result.error(new SocketClosedException(e));
            }
            else
            {
                result = Result.error(e);
            }
        }
        return result;
    }

    @Override
    public Result<TCPClient> accept(Duration timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertNotNull(this.clock, "this.clock");

        Result<TCPClient> result;
        try
        {
            this.serverSocket.setSoTimeout((int)timeout.toMilliseconds().getValue());
            final java.net.Socket socket = this.serverSocket.accept();
            result = JavaTCPClient.create(socket);
        }
        catch (java.net.SocketTimeoutException e)
        {
            result = Result.error(new TimeoutException());
        }
        catch (java.io.IOException e)
        {
            if (Strings.equal(e.getMessage(), "Socket is closed"))
            {
                result = Result.error(new SocketClosedException(e));
            }
            else
            {
                result = Result.error(e);
            }
        }
        return result;
    }

    @Override
    public Result<TCPClient> accept(DateTime timeout)
    {
        PreCondition.assertNotNull(timeout, "timeout");
        PreCondition.assertNotNull(this.clock, "this.clock");

        return this.accept(timeout.minus(this.clock.getCurrentDateTime()));
    }

    @Override
    public boolean isDisposed()
    {
        return this.serverSocket.isClosed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (this.isDisposed())
        {
            result = Result.successFalse();
        }
        else
        {
            try
            {
                this.serverSocket.close();
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
