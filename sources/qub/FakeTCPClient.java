package qub;

import java.io.InputStream;

public class FakeTCPClient extends AsyncDisposableBase implements TCPClient
{
    private final AsyncRunner asyncRunner;
    private final IPv4Address localIPAddress;
    private final int localPort;
    private final IPv4Address remoteIPAddress;
    private final int remotePort;
    private final ByteReadStream socketReadStream;
    private final ByteWriteStream socketWriteStream;
    private boolean disposed;

    private FakeTCPClient(AsyncRunner asyncRunner, IPv4Address localIPAddress, int localPort, IPv4Address remoteIPAddress, int remotePort, ByteReadStream socketReadStream, ByteWriteStream socketWriteStream)
    {
        this.asyncRunner = asyncRunner;
        this.localIPAddress = localIPAddress;
        this.localPort = localPort;
        this.remoteIPAddress = remoteIPAddress;
        this.remotePort = remotePort;
        this.socketReadStream = socketReadStream;
        this.socketWriteStream = socketWriteStream;
    }

    static Result<TCPClient> create(IPv4Address localIPAddress, int localPort, IPv4Address remoteIPAddress, int remotePort, ByteReadStream readStream, ByteWriteStream writeStream, AsyncRunner asyncRunner)
    {
        Result<TCPClient> result = FakeTCPClient.validateLocalIPAddress(localIPAddress);
        if (result == null)
        {
            result = FakeTCPClient.validateLocalPort(localPort);
            if (result == null)
            {
                result = FakeTCPClient.validateRemoteIPAddress(remoteIPAddress);
                if (result == null)
                {
                    result = FakeTCPClient.validateRemotePort(remotePort);
                    if (result == null)
                    {
                        result = FakeTCPClient.validateReadStream(readStream);
                        if (result == null)
                        {
                            result = FakeTCPClient.validateWriteStream(writeStream);
                            if (result == null)
                            {
                                result = FakeTCPClient.validateAsyncRunner(asyncRunner);
                                {
                                    result = Result.<TCPClient>success(new FakeTCPClient(asyncRunner, localIPAddress, localPort, remoteIPAddress, remotePort, readStream, writeStream));
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    static AsyncFunction<Result<TCPClient>> createAsync(final IPv4Address localIPAddress, final int localPort, final IPv4Address remoteIPAddress, final int remotePort, final ByteReadStream readStream, final ByteWriteStream writeStream, final AsyncRunner asyncRunner)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<TCPClient>> result = currentAsyncRunner.notNull(asyncRunner, "asyncRunner");
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<TCPClient>>()
                {
                    @Override
                    public Result<TCPClient> run()
                    {
                        return FakeTCPClient.create(localIPAddress, localPort, remoteIPAddress, remotePort, readStream, writeStream, asyncRunner);
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    private static <T> Result<T> validateIPAddress(IPv4Address ipAddress, String parameterName)
    {
        return Result.<T>notNull(ipAddress, parameterName);
    }

    private static <T> Result<T> validateLocalIPAddress(IPv4Address localIPAddress)
    {
        return FakeTCPClient.validateIPAddress(localIPAddress, "localIPAddress");
    }

    private static <T> Result<T> validateRemoteIPAddress(IPv4Address remoteIPAddress)
    {
        return FakeTCPClient.validateIPAddress(remoteIPAddress, "remoteIPAddress");
    }

    private static <T> Result<T> validatePort(int port, String parameterName)
    {
        return Result.<T>between(1, port, 65535, parameterName);
    }

    private static <T> Result<T> validateLocalPort(int localPort)
    {
        return FakeTCPClient.validatePort(localPort, "localPort");
    }

    private static <T> Result<T> validateRemotePort(int remotePort)
    {
        return FakeTCPClient.validatePort(remotePort, "remotePort");
    }

    private static <T> Result<T> validateDisposable(Disposable disposable, String parameterName)
    {
        Result<T> result = Result.notNull(disposable, parameterName);
        if (result == null)
        {
            result = Result.equal(false, disposable.isDisposed(), parameterName + ".isDisposed()");
        }
        return result;
    }

    private static <T> Result<T> validateReadStream(ByteReadStream readStream)
    {
        return validateDisposable(readStream, "readStream");
    }

    private static <T> Result<T> validateWriteStream(ByteWriteStream writeStream)
    {
        return validateDisposable(writeStream, "writeStream");
    }

    private static <T> Result<T> validateAsyncRunner(AsyncRunner asyncRunner)
    {
        return validateDisposable(asyncRunner, "asyncRunner");
    }

    @Override
    public Result<Byte> readByte()
    {
        return socketReadStream.readByte();
    }

    @Override
    public AsyncFunction<Result<Byte>> readByteAsync()
    {
        return socketReadStream.readByteAsync();
    }

    @Override
    public Result<byte[]> readBytes(int bytesToRead)
    {
        return socketReadStream.readBytes(bytesToRead);
    }

    @Override
    public AsyncFunction<Result<byte[]>> readBytesAsync(int bytesToRead)
    {
        return socketReadStream.readBytesAsync(bytesToRead);
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes)
    {
        return socketReadStream.readBytes(outputBytes);
    }

    @Override
    public AsyncFunction<Result<Integer>> readBytesAsync(byte[] outputBytes)
    {
        return socketReadStream.readBytesAsync(outputBytes);
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return socketReadStream.readBytes(outputBytes, startIndex, length);
    }

    @Override
    public AsyncFunction<Result<Integer>> readBytesAsync(byte[] outputBytes, int startIndex, int length)
    {
        return socketReadStream.readBytesAsync(outputBytes, startIndex, length);
    }

    @Override
    public Result<byte[]> readAllBytes()
    {
        return socketReadStream.readAllBytes();
    }

    @Override
    public AsyncFunction<Result<byte[]>> readAllBytesAsync()
    {
        return socketReadStream.readAllBytesAsync();
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
    public Result<Boolean> write(byte toWrite)
    {
        return socketWriteStream.write(toWrite);
    }

    @Override
    public Result<Boolean> write(byte[] toWrite)
    {
        return socketWriteStream.write(toWrite);
    }

    @Override
    public Result<Boolean> write(byte[] toWrite, int startIndex, int length)
    {
        return socketWriteStream.write(toWrite, startIndex, length);
    }

    @Override
    public Result<Boolean> writeAll(ByteReadStream byteReadStream)
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
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
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
        if (isDisposed())
        {
            result = Result.successFalse();
        }
        else
        {
            disposed = true;
            
            final List<Throwable> errors = new ArrayList<Throwable>();

            result = socketReadStream.dispose();
            if (result.hasError())
            {
                errors.add(result.getError());
            }

            result = socketWriteStream.dispose();
            if (result.hasError())
            {
                errors.add(result.getError());
            }

            if (errors.any())
            {
                result = Result.error(ErrorIterable.from(errors));
            }
            else
            {
                result = Result.successTrue();
            }
        }
        return result;
    }
}
