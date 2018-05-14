package qub;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

class JavaTCPClient extends TCPClientBase
{
    private final Socket socket;
    private final AsyncRunner asyncRunner;
    private final ByteReadStream socketReadStream;
    private final ByteWriteStream socketWriteStream;

    private JavaTCPClient(Socket socket, AsyncRunner asyncRunner, ByteReadStream socketReadStream, ByteWriteStream socketWriteStream)
    {
        this.socket = socket;
        this.asyncRunner = asyncRunner;
        this.socketReadStream = socketReadStream;
        this.socketWriteStream = socketWriteStream;
    }

    static Result<TCPClient> create(Socket socket, AsyncRunner asyncRunner)
    {
        Result<TCPClient> result = Result.notNull(socket, "socket");
        if (result == null)
        {
            result = TCPClientBase.validateAsyncRunner(asyncRunner);
            if (result == null)
            {
                try
                {
                    final ByteReadStream socketReadStream = new InputStreamToByteReadStream(socket.getInputStream(), asyncRunner);
                    final ByteWriteStream socketWriteStream = new OutputStreamToByteWriteStream(socket.getOutputStream());
                    result = Result.<TCPClient>success(new JavaTCPClient(socket, asyncRunner, socketReadStream, socketWriteStream));
                }
                catch (IOException e)
                {
                    result = Result.error(e);
                }
            }
        }
        return result;
    }

    static AsyncFunction<Result<TCPClient>> createAsync(final Socket socket, final AsyncRunner asyncRunner)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<TCPClient>> result = currentAsyncRunner.notNull(socket, "socket");
        if (result == null)
        {
            result = currentAsyncRunner.notNull(asyncRunner, "asyncRunner");
            if (result == null)
            {
                asyncRunner.schedule(new Function0<Result<TCPClient>>()
                    {
                        @Override
                        public Result<TCPClient> run()
                        {
                            return JavaTCPClient.create(socket, asyncRunner);
                        }
                    })
                    .thenOn(currentAsyncRunner);
            }
        }

        return result;
    }

    static Result<TCPClient> create(IPv4Address remoteIPAddress, int remotePort, AsyncRunner asyncRunner)
    {
        Result<TCPClient> result = TCPClientBase.validateRemoteIPAddress(remoteIPAddress);
        if (result == null)
        {
            result = TCPClientBase.validateRemotePort(remotePort);
            if (result == null)
            {
                result = TCPClientBase.validateAsyncRunner(asyncRunner);
                if (result == null)
                {
                    try
                    {
                        final byte[] remoteIPAddressBytes = remoteIPAddress.toBytes();
                        final InetAddress remoteInetAddress = InetAddress.getByAddress(remoteIPAddressBytes);
                        final Socket socket = new Socket(remoteInetAddress, remotePort);
                        result = JavaTCPClient.create(socket, asyncRunner);
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

    static AsyncFunction<Result<TCPClient>> createAsync(final IPv4Address remoteIPAddress, final int remotePort, final AsyncRunner asyncRunner)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<TCPClient>> result = currentAsyncRunner.notNull(remoteIPAddress, "remoteIPAddress");
        if (result == null)
        {
            result = currentAsyncRunner.between(1, remotePort, 65535, "remotePort");
            if (result == null)
            {
                result = currentAsyncRunner.notNull(asyncRunner, "asyncRunner");
                if (result == null)
                {
                    result = asyncRunner.schedule(new Function0<Result<TCPClient>>()
                        {
                            @Override
                            public Result<TCPClient> run()
                            {
                                return JavaTCPClient.create(remoteIPAddress, remotePort, asyncRunner);
                            }
                        })
                        .thenOn(currentAsyncRunner);
                }
            }
        }

        return result;
    }

    @Override
    protected ByteReadStream getReadStream()
    {
        return socketReadStream;
    }

    @Override
    protected ByteWriteStream getWriteStream()
    {
        return socketWriteStream;
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }

    @Override
    public boolean isDisposed()
    {
        return socket.isClosed();
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
                socket.close();
                result = Result.successTrue();
            }
            catch (IOException e)
            {
                result = Result.error(e);
            }
        }
        return result;
    }
}
