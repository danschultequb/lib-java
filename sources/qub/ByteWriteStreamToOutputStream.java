package qub;

import java.io.IOException;
import java.io.OutputStream;

public class ByteWriteStreamToOutputStream extends OutputStream
{
    private final ByteWriteStream byteWriteStream;

    public ByteWriteStreamToOutputStream(ByteWriteStream byteWriteStream)
    {
        this.byteWriteStream = byteWriteStream;
    }

    @Override
    public void close() throws IOException
    {
        final Result<Boolean> result = byteWriteStream.dispose();
        result.throwError(IOException.class);
    }

    @Override
    public void write(int b) throws IOException
    {
        final Result<Boolean> result = byteWriteStream.write((byte)b);
        result.throwError(IOException.class);
    }

    @Override
    public void write(byte[] bytes) throws IOException
    {
        final Result<Boolean> result = byteWriteStream.write(bytes);
        result.throwError(IOException.class);
    }

    @Override
    public void write(byte[] bytes, int startIndex, int length) throws IOException
    {
        final Result<Boolean> result = byteWriteStream.write(bytes, startIndex, length);
        result.throwError(IOException.class);
    }
}
