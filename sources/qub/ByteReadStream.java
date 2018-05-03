package qub;

/**
 * A ReadStream interface that reads bytes.
 */
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

    /**
     * Read up to length available bytes into the provided byte[] at the provided startIndex and
     * reutrn the number of bytes that were read. If no bytes are available, then this function will
     * not return until bytes become available.
     * @param outputBytes The byte[] to read bytes into.
     * @param startIndex The start index in in outputBytes to start writing bytes to.
     * @param length The maximum number of bytes to read.
     * @return The number of bytes that were read.
     */
    Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length);

    /**
     * Read up to length available bytes into the provided byte[] at the provided startIndex and
     * reutrn the number of bytes that were read. If no bytes are available, then this function will
     * not resolve until bytes become available.
     * @param outputBytes The byte[] to read bytes into.
     * @param startIndex The start index in in outputBytes to start writing bytes to.
     * @param length The maximum number of bytes to read.
     * @return The number of bytes that were read.
     */
    AsyncFunction<Result<Integer>> readBytesAsync(byte[] outputBytes, int startIndex, int length);

    /**
     * Read all of the bytes in this stream. The termination of the stream is marked when getByte()
     * returns a null Byte. This function will not return until all of the bytes in the stream have
     * been read.
     * @return All of the bytes in this stream.
     */
    Result<byte[]> readAllBytes();

    /**
     * Read all of the bytes in this stream. The termination of the stream is marked when getByte()
     * returns a null Byte. This function will not resolve until all of the bytes in the stream have
     * been read.
     * @return All of the bytes in this stream.
     */
    AsyncFunction<Result<byte[]>> readAllBytesAsync();

    java.io.InputStream asInputStream();

    CharacterReadStream asCharacterReadStream();

    CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding);

    LineReadStream asLineReadStream();

    LineReadStream asLineReadStream(CharacterEncoding characterEncoding);

    LineReadStream asLineReadStream(boolean includeNewLines);

    LineReadStream asLineReadStream(CharacterEncoding characterEncoding, boolean includeNewLines);
}
