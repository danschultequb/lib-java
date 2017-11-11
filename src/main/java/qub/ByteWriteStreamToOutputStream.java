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
        byteWriteStream.close();
    }

    @Override
    public void write(int b) throws IOException
    {
        byteWriteStream.write((byte)b);
    }

    @Override
    public void write(byte[] bytes) throws IOException
    {
        byteWriteStream.write(bytes);
    }

    @Override
    public void write(byte[] bytes, int startIndex, int length) throws IOException
    {
        byteWriteStream.write(bytes, startIndex, length);
    }
}
