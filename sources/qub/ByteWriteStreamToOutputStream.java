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
        final Result<Boolean> result = byteWriteStream.dispose();
        result.throwError(java.io.IOException.class);
    }

    @Override
    public void write(int b) throws java.io.IOException
    {
        final Result<Boolean> result = byteWriteStream.writeByte((byte)b);
        result.throwError(java.io.IOException.class);
    }

    @Override
    public void write(byte[] bytes) throws java.io.IOException
    {
        final Result<Boolean> result = byteWriteStream.writeAllBytes(bytes);
        result.throwError(java.io.IOException.class);
    }

    @Override
    public void write(byte[] bytes, int startIndex, int length) throws java.io.IOException
    {
        final Result<Boolean> result = byteWriteStream.writeAllBytes(bytes, startIndex, length);
        result.throwError(java.io.IOException.class);
    }
}
