package qub;

public class ByteReadStreamToInputStream extends java.io.InputStream
{
    private final ByteReadStream byteReadStream;

    public ByteReadStreamToInputStream(ByteReadStream byteReadStream)
    {
        this.byteReadStream = byteReadStream;
    }

    @Override
    public void close() throws java.io.IOException
    {
        byteReadStream.dispose()
            .awaitError(java.io.IOException.class);
    }

    @Override
    public int read() throws java.io.IOException
    {
        return byteReadStream.readByte()
            .then((Byte valueRead) -> valueRead == null ? -1 : Bytes.toUnsignedInt(valueRead))
            .catchError(EndOfStreamException.class, () -> -1)
            .awaitError(java.io.IOException.class);
    }

    @Override
    public int read(byte[] outputBytes) throws java.io.IOException
    {
        return byteReadStream.readBytes(outputBytes)
            .then((Integer bytesRead) -> bytesRead == null ? -1 : bytesRead)
            .catchError(EndOfStreamException.class, () -> -1)
            .awaitError(java.io.IOException.class);
    }

    @Override
    public int read(byte[] outputBytes, int startIndex, int length) throws java.io.IOException
    {
        return byteReadStream.readBytes(outputBytes, startIndex, length)
            .then((Integer bytesRead) -> bytesRead == null ? -1 : bytesRead)
            .catchError(EndOfStreamException.class, () -> -1)
            .awaitError(java.io.IOException.class);
    }
}
