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
        byteWriteStream.dispose()
            .throwError(java.io.IOException.class);
    }

    @Override
    public void write(int b) throws java.io.IOException
    {
        byteWriteStream.writeByte((byte)b)
            .throwError(java.io.IOException.class);
    }

    @Override
    public void write(byte[] bytes) throws java.io.IOException
    {
        byteWriteStream.writeAllBytes(bytes)
            .throwError(java.io.IOException.class);
    }

    @Override
    public void write(byte[] bytes, int startIndex, int length) throws java.io.IOException
    {
        byteWriteStream.writeAllBytes(bytes, startIndex, length)
            .throwError(java.io.IOException.class);
    }
}
