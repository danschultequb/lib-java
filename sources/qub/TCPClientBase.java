package qub;

import java.io.InputStream;

public abstract class TCPClientBase extends AsyncDisposableBase implements TCPClient
{
    protected abstract ByteReadStream getReadStream();
    
    protected abstract ByteWriteStream getWriteStream();
    
    @Override
    public Result<Byte> readByte()
    {
        return getReadStream().readByte();
    }

    @Override
    public AsyncFunction<Result<Byte>> readByteAsync()
    {
        return getReadStream().readByteAsync();
    }

    @Override
    public Result<byte[]> readBytes(int bytesToRead)
    {
        return getReadStream().readBytes(bytesToRead);
    }

    @Override
    public AsyncFunction<Result<byte[]>> readBytesAsync(int bytesToRead)
    {
        return getReadStream().readBytesAsync(bytesToRead);
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes)
    {
        return getReadStream().readBytes(outputBytes);
    }

    @Override
    public AsyncFunction<Result<Integer>> readBytesAsync(byte[] outputBytes)
    {
        return getReadStream().readBytesAsync(outputBytes);
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return getReadStream().readBytes(outputBytes, startIndex, length);
    }

    @Override
    public AsyncFunction<Result<Integer>> readBytesAsync(byte[] outputBytes, int startIndex, int length)
    {
        return getReadStream().readBytesAsync(outputBytes, startIndex, length);
    }

    @Override
    public Result<byte[]> readAllBytes()
    {
        return getReadStream().readAllBytes();
    }

    @Override
    public AsyncFunction<Result<byte[]>> readAllBytesAsync()
    {
        return getReadStream().readAllBytesAsync();
    }

    @Override
    public InputStream asInputStream()
    {
        return getReadStream().asInputStream();
    }

    @Override
    public CharacterReadStream asCharacterReadStream()
    {
        return getReadStream().asCharacterReadStream();
    }

    @Override
    public CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding)
    {
        return getReadStream().asCharacterReadStream(characterEncoding);
    }

    @Override
    public LineReadStream asLineReadStream()
    {
        return getReadStream().asLineReadStream();
    }

    @Override
    public LineReadStream asLineReadStream(CharacterEncoding characterEncoding)
    {
        return getReadStream().asLineReadStream(characterEncoding);
    }

    @Override
    public LineReadStream asLineReadStream(boolean includeNewLines)
    {
        return getReadStream().asLineReadStream(includeNewLines);
    }

    @Override
    public LineReadStream asLineReadStream(CharacterEncoding characterEncoding, boolean includeNewLines)
    {
        return getReadStream().asLineReadStream(characterEncoding, includeNewLines);
    }

    @Override
    public Result<Boolean> write(byte toWrite)
    {
        return getWriteStream().write(toWrite);
    }

    @Override
    public Result<Boolean> write(byte[] toWrite)
    {
        return getWriteStream().write(toWrite);
    }

    @Override
    public Result<Boolean> write(byte[] toWrite, int startIndex, int length)
    {
        return getWriteStream().write(toWrite, startIndex, length);
    }

    @Override
    public Result<Boolean> writeAll(ByteReadStream byteReadStream)
    {
        return getWriteStream().writeAll(byteReadStream);
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream()
    {
        return getWriteStream().asCharacterWriteStream();
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream(CharacterEncoding characterEncoding)
    {
        return getWriteStream().asCharacterWriteStream(characterEncoding);
    }

    @Override
    public LineWriteStream asLineWriteStream()
    {
        return getWriteStream().asLineWriteStream();
    }

    @Override
    public LineWriteStream asLineWriteStream(CharacterEncoding characterEncoding)
    {
        return getWriteStream().asLineWriteStream(characterEncoding);
    }

    @Override
    public LineWriteStream asLineWriteStream(String lineSeparator)
    {
        return getWriteStream().asLineWriteStream(lineSeparator);
    }

    @Override
    public LineWriteStream asLineWriteStream(CharacterEncoding characterEncoding, String lineSeparator)
    {
        return getWriteStream().asLineWriteStream(characterEncoding, lineSeparator);
    }

    @Override
    public void ensureHasStarted()
    {
        getReadStream().ensureHasStarted();
    }

    @Override
    public boolean hasStarted()
    {
        return getReadStream().hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return getReadStream().hasCurrent();
    }

    @Override
    public Byte getCurrent()
    {
        return getReadStream().getCurrent();
    }

    @Override
    public boolean next()
    {
        return getReadStream().next();
    }

    @Override
    public Byte takeCurrent()
    {
        return getReadStream().takeCurrent();
    }

    @Override
    public boolean any()
    {
        return getReadStream().any();
    }

    @Override
    public int getCount()
    {
        return getReadStream().getCount();
    }

    @Override
    public Byte first()
    {
        return getReadStream().first();
    }

    @Override
    public Byte first(Function1<Byte, Boolean> condition)
    {
        return getReadStream().first(condition);
    }

    @Override
    public Byte last()
    {
        return getReadStream().last();
    }

    @Override
    public Byte last(Function1<Byte, Boolean> condition)
    {
        return getReadStream().last(condition);
    }

    @Override
    public boolean contains(Byte value)
    {
        return getReadStream().contains(value);
    }

    @Override
    public boolean contains(Function1<Byte, Boolean> condition)
    {
        return getReadStream().contains(condition);
    }

    @Override
    public Iterator<Byte> take(int toTake)
    {
        return getReadStream().take(toTake);
    }

    @Override
    public Iterator<Byte> skip(int toSkip)
    {
        return getReadStream().skip(toSkip);
    }

    @Override
    public Iterator<Byte> skipUntil(Function1<Byte, Boolean> condition)
    {
        return getReadStream().skipUntil(condition);
    }

    @Override
    public Iterator<Byte> where(Function1<Byte, Boolean> condition)
    {
        return getReadStream().where(condition);
    }

    @Override
    public <U> Iterator<U> map(Function1<Byte, U> conversion)
    {
        return getReadStream().map(conversion);
    }

    @Override
    public <U> Iterator<U> instanceOf(Class<U> type)
    {
        return getReadStream().instanceOf(type);
    }

    @Override
    public java.util.Iterator<Byte> iterator()
    {
        return getReadStream().iterator();
    }

    protected static <T> Result<T> validateIPAddress(IPv4Address ipAddress, String parameterName)
    {
        return Result.<T>notNull(ipAddress, parameterName);
    }

    protected static <T> Result<T> validateLocalIPAddress(IPv4Address localIPAddress)
    {
        return TCPClientBase.validateIPAddress(localIPAddress, "localIPAddress");
    }

    protected static <T> Result<T> validateRemoteIPAddress(IPv4Address remoteIPAddress)
    {
        return TCPClientBase.validateIPAddress(remoteIPAddress, "remoteIPAddress");
    }

    protected static <T> Result<T> validatePort(int port, String parameterName)
    {
        return Result.<T>between(1, port, 65535, parameterName);
    }

    protected static <T> Result<T> validateLocalPort(int localPort)
    {
        return TCPClientBase.validatePort(localPort, "localPort");
    }

    protected static <T> Result<T> validateRemotePort(int remotePort)
    {
        return TCPClientBase.validatePort(remotePort, "remotePort");
    }

    protected static <T> Result<T> validateDisposable(Disposable disposable, String parameterName)
    {
        Result<T> result = Result.notNull(disposable, parameterName);
        if (result == null)
        {
            result = Result.equal(false, disposable.isDisposed(), parameterName + ".isDisposed()");
        }
        return result;
    }

    protected static <T> Result<T> validateReadStream(ByteReadStream readStream)
    {
        return TCPClientBase.validateDisposable(readStream, "readStream");
    }

    protected static <T> Result<T> validateWriteStream(ByteWriteStream writeStream)
    {
        return TCPClientBase.validateDisposable(writeStream, "writeStream");
    }

    protected static <T> Result<T> validateAsyncRunner(AsyncRunner asyncRunner)
    {
        return TCPClientBase.validateDisposable(asyncRunner, "asyncRunner");
    }
}
