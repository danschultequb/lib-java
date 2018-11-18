package qub;

public interface TCPClient extends ByteWriteStream, ByteReadStream, AsyncDisposable
{
    /**
     * Get the local IP address that this client is connected to.
     * @return The local IP address that this client is connected to.
     */
    IPv4Address getLocalIPAddress();

    /**
     * Get the local port that this client is connected to.
     * @return The local port that this client is connected to.
     */
    int getLocalPort();

    /**
     * Get the remote IP address that this client is connected to.
     * @return The remote IP address that this client is connected to.
     */
    IPv4Address getRemoteIPAddress();

    /**
     * Get the remote port that this client is connected to.
     * @return The remote port that this client is connected to.
     */
    int getRemotePort();

    /**
     * Get this TCPClient's internal ByteReadStream.
     * @return The internal ByteReadStream of this TCPClient.
     */
    ByteReadStream getReadStream();

    /**
     * Get this TCPClient's internal ByteWriteStream.
     * @return The internal ByteWriteStream of this TCPClient.
     */
    ByteWriteStream getWriteStream();

    @Override
    default Result<Byte> readByte()
    {
        return getReadStream().readByte();
    }

    @Override
    default AsyncFunction<Result<Byte>> readByteAsync()
    {
        return getReadStream().readByteAsync();
    }

    @Override
    default Result<byte[]> readBytes(int bytesToRead)
    {
        return getReadStream().readBytes(bytesToRead);
    }

    @Override
    default AsyncFunction<Result<byte[]>> readBytesAsync(int bytesToRead)
    {
        return getReadStream().readBytesAsync(bytesToRead);
    }

    @Override
    default Result<Integer> readBytes(byte[] outputBytes)
    {
        return getReadStream().readBytes(outputBytes);
    }

    @Override
    default AsyncFunction<Result<Integer>> readBytesAsync(byte[] outputBytes)
    {
        return getReadStream().readBytesAsync(outputBytes);
    }

    @Override
    default Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return getReadStream().readBytes(outputBytes, startIndex, length);
    }

    @Override
    default AsyncFunction<Result<Integer>> readBytesAsync(byte[] outputBytes, int startIndex, int length)
    {
        return getReadStream().readBytesAsync(outputBytes, startIndex, length);
    }

    @Override
    default Result<byte[]> readAllBytes()
    {
        return getReadStream().readAllBytes();
    }

    @Override
    default AsyncFunction<Result<byte[]>> readAllBytesAsync()
    {
        return getReadStream().readAllBytesAsync();
    }

    @Override
    default Result<byte[]> readBytesUntil(byte stopByte)
    {
        return getReadStream().readBytesUntil(stopByte);
    }

    @Override
    default AsyncFunction<Result<byte[]>> readBytesUntilAsync(byte stopByte)
    {
        return getReadStream().readBytesUntilAsync(stopByte);
    }

    @Override
    default Result<byte[]> readBytesUntil(byte[] stopBytes)
    {
        return getReadStream().readBytesUntil(stopBytes);
    }

    @Override
    default AsyncFunction<Result<byte[]>> readBytesUntilAsync(byte[] stopBytes)
    {
        return getReadStream().readBytesUntilAsync(stopBytes);
    }

    @Override
    default Result<byte[]> readBytesUntil(Iterable<Byte> stopBytes)
    {
        return getReadStream().readBytesUntil(stopBytes);
    }

    @Override
    default AsyncFunction<Result<byte[]>> readBytesUntilAsync(Iterable<Byte> stopBytes)
    {
        return getReadStream().readBytesUntilAsync(stopBytes);
    }

    @Override
    default java.io.InputStream asInputStream()
    {
        return getReadStream().asInputStream();
    }

    @Override
    default CharacterReadStream asCharacterReadStream()
    {
        return getReadStream().asCharacterReadStream();
    }

    @Override
    default CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding)
    {
        return getReadStream().asCharacterReadStream(characterEncoding);
    }

    @Override
    default LineReadStream asLineReadStream()
    {
        return getReadStream().asLineReadStream();
    }

    @Override
    default LineReadStream asLineReadStream(CharacterEncoding characterEncoding)
    {
        return getReadStream().asLineReadStream(characterEncoding);
    }

    @Override
    default LineReadStream asLineReadStream(boolean includeNewLines)
    {
        return getReadStream().asLineReadStream(includeNewLines);
    }

    @Override
    default LineReadStream asLineReadStream(CharacterEncoding characterEncoding, boolean includeNewLines)
    {
        return getReadStream().asLineReadStream(characterEncoding, includeNewLines);
    }

    @Override
    default Result<Boolean> writeByte(byte toWrite)
    {
        return getWriteStream().writeByte(toWrite);
    }

    @Override
    default Result<Integer> writeBytes(byte[] toWrite)
    {
        return getWriteStream().writeBytes(toWrite);
    }

    @Override
    default Result<Integer> writeBytes(byte[] toWrite, int startIndex, int length)
    {
        return getWriteStream().writeBytes(toWrite, startIndex, length);
    }

    @Override
    default Result<Boolean> writeAllBytes(ByteReadStream byteReadStream)
    {
        return getWriteStream().writeAllBytes(byteReadStream);
    }

    @Override
    default CharacterWriteStream asCharacterWriteStream()
    {
        return getWriteStream().asCharacterWriteStream();
    }

    @Override
    default CharacterWriteStream asCharacterWriteStream(CharacterEncoding characterEncoding)
    {
        return getWriteStream().asCharacterWriteStream(characterEncoding);
    }

    @Override
    default LineWriteStream asLineWriteStream()
    {
        return getWriteStream().asLineWriteStream();
    }

    @Override
    default LineWriteStream asLineWriteStream(CharacterEncoding characterEncoding)
    {
        return getWriteStream().asLineWriteStream(characterEncoding);
    }

    @Override
    default LineWriteStream asLineWriteStream(String lineSeparator)
    {
        return getWriteStream().asLineWriteStream(lineSeparator);
    }

    @Override
    default LineWriteStream asLineWriteStream(CharacterEncoding characterEncoding, String lineSeparator)
    {
        return getWriteStream().asLineWriteStream(characterEncoding, lineSeparator);
    }

    @Override
    default void ensureHasStarted()
    {
        getReadStream().ensureHasStarted();
    }

    @Override
    default boolean hasStarted()
    {
        return getReadStream().hasStarted();
    }

    @Override
    default boolean hasCurrent()
    {
        return getReadStream().hasCurrent();
    }

    @Override
    default Byte getCurrent()
    {
        return getReadStream().getCurrent();
    }

    @Override
    default boolean next()
    {
        return getReadStream().next();
    }

    @Override
    default Byte takeCurrent()
    {
        return getReadStream().takeCurrent();
    }

    @Override
    default boolean any()
    {
        return getReadStream().any();
    }

    @Override
    default int getCount()
    {
        return getReadStream().getCount();
    }

    @Override
    default Byte first()
    {
        return getReadStream().first();
    }

    @Override
    default Byte first(Function1<Byte, Boolean> condition)
    {
        return getReadStream().first(condition);
    }

    @Override
    default Byte last()
    {
        return getReadStream().last();
    }

    @Override
    default Byte last(Function1<Byte, Boolean> condition)
    {
        return getReadStream().last(condition);
    }

    @Override
    default boolean contains(Byte value)
    {
        return getReadStream().contains(value);
    }

    @Override
    default boolean contains(Function1<Byte, Boolean> condition)
    {
        return getReadStream().contains(condition);
    }

    @Override
    default Iterator<Byte> take(int toTake)
    {
        return getReadStream().take(toTake);
    }

    @Override
    default Iterator<Byte> skip(int toSkip)
    {
        return getReadStream().skip(toSkip);
    }

    @Override
    default Iterator<Byte> skipUntil(Function1<Byte, Boolean> condition)
    {
        return getReadStream().skipUntil(condition);
    }

    @Override
    default Iterator<Byte> where(Function1<Byte, Boolean> condition)
    {
        return getReadStream().where(condition);
    }

    @Override
    default <U> Iterator<U> map(Function1<Byte, U> conversion)
    {
        return getReadStream().map(conversion);
    }

    @Override
    default <U> Iterator<U> instanceOf(Class<U> type)
    {
        return getReadStream().instanceOf(type);
    }

    @Override
    default java.util.Iterator<Byte> iterator()
    {
        return getReadStream().iterator();
    }
}
