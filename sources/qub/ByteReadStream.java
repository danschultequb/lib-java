package qub;

public interface ByteReadStream extends AsyncDisposable, Iterator<Byte>
{
    /**
     * Read a single byte from this stream. This will block until a byte is available.
     * @return The single byte that was read, or an error if a byte could not be read.
     */
    Result<Byte> readByte();

    /**
     * Read a single byte from this stream. This function will not resolve until a byte becomes
     * available.
     * @return The single byte that was read, or an error if a byte could not be read.
     */
    AsyncFunction<Result<Byte>> readByteAsync();

    /**
     * Read up to the provided bytesToRead number of bytes from this stream. If fewer bytes than
     * bytesToRead are available, then fewer than bytesToRead bytes will be returned. If no bytes
     * are available, then this function will block until bytes become available.
     * @param bytesToRead The maximum number of bytes to read from this stream.
     * @return The bytes that were read, or an error if bytes could not be read.
     */
    Result<byte[]> readBytes(int bytesToRead);

    /**
     * Read up to the provided bytesToRead number of bytes from this stream. If fewer bytes than
     * bytesToRead are available, then fewer than bytesToRead bytes will be returned. If no bytes
     * are available, then this function will not resolve until bytes become available.
     * @param bytesToRead The maximum number of bytes to read from this stream.
     * @return The bytes that were read, or an error if bytes could not be read.
     */
    AsyncFunction<Result<byte[]>> readBytesAsync(int bytesToRead);

    /**
     * Read available bytes into the provided byte[] and return the number of bytes that were read.
     * If no bytes are available, then this function will not return until bytes become available.
     * @param outputBytes The byte array to read bytes into.
     * @return The number of bytes that were read, or an error if bytes could not be read.
     */
    Result<Integer> readBytes(byte[] outputBytes);

    /**
     * Read available bytes into the provided byte[] and return the number of bytes that were read.
     * If no bytes are available, then this function will not resolve until bytes become available.
     * @param outputBytes The byte array to read bytes into.
     * @return The number of bytes that were read, or an error if bytes could not be read.
     */
    AsyncFunction<Result<Integer>> readBytesAsync(byte[] outputBytes);

    Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length);

    Result<byte[]> readAllBytes();

    java.io.InputStream asInputStream();

    CharacterReadStream asCharacterReadStream();

    CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding);

    LineReadStream asLineReadStream();

    LineReadStream asLineReadStream(CharacterEncoding characterEncoding);

    LineReadStream asLineReadStream(boolean includeNewLines);

    LineReadStream asLineReadStream(CharacterEncoding characterEncoding, boolean includeNewLines);
}
