package qub;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

class JavaTCPClient extends AsyncDisposableBase implements TCPClient
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
        return createAsync(socket, asyncRunner).awaitReturn();
    }

    static AsyncFunction<Result<TCPClient>> createAsync(final Socket socket, final AsyncRunner asyncRunner)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<TCPClient>> result;

        if (socket == null)
        {
            result = currentAsyncRunner.error(new IllegalArgumentException("socket cannot be null."));
        }
        else
        {
            result = asyncRunner.schedule(new Function0<Result<TCPClient>>()
                {
                    @Override
                    public Result<TCPClient> run()
                    {
                        Result<TCPClient> result;
                        try
                        {
                            final ByteReadStream socketReadStream = new InputStreamToByteReadStream(socket.getInputStream());
                            final ByteWriteStream socketWriteStream = new OutputStreamToByteWriteStream(socket.getOutputStream());
                            result = Result.<TCPClient>success(new JavaTCPClient(socket, asyncRunner, socketReadStream, socketWriteStream));
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

    static Result<TCPClient> create(IPv4Address remoteIPAddress, int remotePort, AsyncRunner asyncRunner)
    {
        return createAsync(remoteIPAddress, remotePort, asyncRunner).awaitReturn();
    }

    static AsyncFunction<Result<TCPClient>> createAsync(final IPv4Address remoteIPAddress, final int remotePort, final AsyncRunner asyncRunner)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<TCPClient>> result;

        if (remoteIPAddress == null)
        {
            result = currentAsyncRunner.error(new IllegalArgumentException("remoteIPAddress cannot be null."));
        }
        else if (remotePort <= 0)
        {
            result = currentAsyncRunner.error(new IllegalArgumentException("remotePort must be greater than 0."));
        }
        else
        {
            result = asyncRunner.schedule(new Function0<Result<TCPClient>>()
                {
                    @Override
                    public Result<TCPClient> run()
                    {
                        Result<TCPClient> result;
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
                        return result;
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public Result<Byte> readByte()
    {
        return socketReadStream.readByte();
    }

    @Override
    public Result<byte[]> readBytes(int bytesToRead)
    {
        return socketReadStream.readBytes(bytesToRead);
    }

    @Override
    public int readBytes(byte[] outputBytes)
    {
        return socketReadStream.readBytes(outputBytes);
    }

    @Override
    public int readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return socketReadStream.readBytes(outputBytes, startIndex, length);
    }

    @Override
    public byte[] readAllBytes()
    {
        return socketReadStream.readAllBytes();
    }

    @Override
    public InputStream asInputStream()
    {
        return socketReadStream.asInputStream();
    }

    @Override
    public CharacterReadStream asCharacterReadStream()
    {
        return socketReadStream.asCharacterReadStream();
    }

    @Override
    public CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding)
    {
        return socketReadStream.asCharacterReadStream(characterEncoding);
    }

    @Override
    public LineReadStream asLineReadStream()
    {
        return socketReadStream.asLineReadStream();
    }

    @Override
    public LineReadStream asLineReadStream(CharacterEncoding characterEncoding)
    {
        return socketReadStream.asLineReadStream(characterEncoding);
    }

    @Override
    public LineReadStream asLineReadStream(boolean includeNewLines)
    {
        return socketReadStream.asLineReadStream(includeNewLines);
    }

    @Override
    public LineReadStream asLineReadStream(CharacterEncoding characterEncoding, boolean includeNewLines)
    {
        return socketReadStream.asLineReadStream(characterEncoding, includeNewLines);
    }

    @Override
    public void setExceptionHandler(Action1<IOException> exceptionHandler)
    {
        socketReadStream.setExceptionHandler(exceptionHandler);
        socketWriteStream.setExceptionHandler(exceptionHandler);
    }

    @Override
    public boolean write(byte toWrite)
    {
        return socketWriteStream.write(toWrite);
    }

    @Override
    public boolean write(byte[] toWrite)
    {
        return socketWriteStream.write(toWrite);
    }

    @Override
    public boolean write(byte[] toWrite, int startIndex, int length)
    {
        return socketWriteStream.write(toWrite, startIndex, length);
    }

    @Override
    public boolean writeAll(ByteReadStream byteReadStream)
    {
        return socketWriteStream.writeAll(byteReadStream);
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream()
    {
        return socketWriteStream.asCharacterWriteStream();
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream(CharacterEncoding characterEncoding)
    {
        return socketWriteStream.asCharacterWriteStream(characterEncoding);
    }

    @Override
    public LineWriteStream asLineWriteStream()
    {
        return socketWriteStream.asLineWriteStream();
    }

    @Override
    public LineWriteStream asLineWriteStream(CharacterEncoding characterEncoding)
    {
        return socketWriteStream.asLineWriteStream(characterEncoding);
    }

    @Override
    public LineWriteStream asLineWriteStream(String lineSeparator)
    {
        return socketWriteStream.asLineWriteStream(lineSeparator);
    }

    @Override
    public LineWriteStream asLineWriteStream(CharacterEncoding characterEncoding, String lineSeparator)
    {
        return socketWriteStream.asLineWriteStream(characterEncoding, lineSeparator);
    }

    @Override
    public void ensureHasStarted()
    {
        socketReadStream.ensureHasStarted();
    }

    @Override
    public boolean hasStarted()
    {
        return socketReadStream.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return socketReadStream.hasCurrent();
    }

    @Override
    public Byte getCurrent()
    {
        return socketReadStream.getCurrent();
    }

    @Override
    public boolean next()
    {
        return socketReadStream.next();
    }

    @Override
    public Byte takeCurrent()
    {
        return socketReadStream.takeCurrent();
    }

    @Override
    public boolean any()
    {
        return socketReadStream.any();
    }

    @Override
    public int getCount()
    {
        return socketReadStream.getCount();
    }

    @Override
    public Byte first()
    {
        return socketReadStream.first();
    }

    @Override
    public Byte first(Function1<Byte, Boolean> condition)
    {
        return socketReadStream.first(condition);
    }

    @Override
    public Byte last()
    {
        return socketReadStream.last();
    }

    @Override
    public Byte last(Function1<Byte, Boolean> condition)
    {
        return socketReadStream.last(condition);
    }

    @Override
    public boolean contains(Byte value)
    {
        return socketReadStream.contains(value);
    }

    @Override
    public boolean contains(Function1<Byte, Boolean> condition)
    {
        return socketReadStream.contains(condition);
    }

    @Override
    public Iterator<Byte> take(int toTake)
    {
        return socketReadStream.take(toTake);
    }

    @Override
    public Iterator<Byte> skip(int toSkip)
    {
        return socketReadStream.skip(toSkip);
    }

    @Override
    public Iterator<Byte> skipUntil(Function1<Byte, Boolean> condition)
    {
        return socketReadStream.skipUntil(condition);
    }

    @Override
    public Iterator<Byte> where(Function1<Byte, Boolean> condition)
    {
        return socketReadStream.where(condition);
    }

    @Override
    public <U> Iterator<U> map(Function1<Byte, U> conversion)
    {
        return socketReadStream.map(conversion);
    }

    @Override
    public <U> Iterator<U> instanceOf(Class<U> type)
    {
        return socketReadStream.instanceOf(type);
    }

    @Override
    public java.util.Iterator<Byte> iterator()
    {
        return socketReadStream.iterator();
    }

    @Override
    public boolean isDisposed()
    {
        return socket.isClosed();
    }

    @Override
    public AsyncFunction<Result<Boolean>> disposeAsync()
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Boolean>> result;
        if (isDisposed())
        {
            result = currentAsyncRunner.success(false);
        }
        else
        {
            result = asyncRunner.schedule(new Function0<Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run()
                    {
                        Result<Boolean> result;
                        try
                        {
                            socket.close();
                            result = Result.success(true);
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
}
