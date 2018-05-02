package qub;

public interface ByteReadStream extends AsyncDisposable, Iterator<Byte>
{
    /**
     * Read a single byte from this stream. This will block until a byte is available.
     * @return The single byte that was read, or an error if a byte could not be read.
     */
    Result<Byte> readByte();

    /**
     * Read a single byte from this stream.
     * @return The single byte that was read, or an error if a byte could not be read.
     */
    AsyncFunction<Result<Byte>> readByteAsync();

    Result<byte[]> readBytes(int bytesToRead);

    Result<Integer> readBytes(byte[] outputBytes);

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
