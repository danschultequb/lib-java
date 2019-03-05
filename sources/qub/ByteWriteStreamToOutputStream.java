package qub;

public class ByteWriteStreamToOutputStream extends java.io.OutputStream
{
    private final ByteWriteStream byteWriteStream;

    public ByteWriteStreamToOutputStream(ByteWriteStream byteWriteStream)
    {
        this.byteWriteStream = byteWriteStream;
    }

    @Override
    public void close() throws java.io.IOException
    {
        byteWriteStream.dispose().awaitError(java.io.IOException.class);
    }

    @Override
    public void write(int b) throws java.io.IOException
    {
        byteWriteStream.writeByte((byte)b).awaitError(java.io.IOException.class);
    }

    @Override
    public void write(byte[] bytes) throws java.io.IOException
    {
        byteWriteStream.writeAllBytes(bytes).awaitError(java.io.IOException.class);
    }

    @Override
    public void write(byte[] bytes, int startIndex, int length) throws java.io.IOException
    {
        byteWriteStream.writeAllBytes(bytes, startIndex, length).awaitError(java.io.IOException.class);
    }
}
