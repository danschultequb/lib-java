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
        this.byteWriteStream.dispose().await(java.io.IOException.class);
    }

    @Override
    public void write(int b) throws java.io.IOException
    {
        this.byteWriteStream.write((byte)b).await(java.io.IOException.class);
    }

    @Override
    public void write(byte[] bytes) throws java.io.IOException
    {
        this.byteWriteStream.writeAll(bytes).await(java.io.IOException.class);
    }

    @Override
    public void write(byte[] bytes, int startIndex, int length) throws java.io.IOException
    {
        this.byteWriteStream.writeAll(bytes, startIndex, length).await(java.io.IOException.class);
    }
}
