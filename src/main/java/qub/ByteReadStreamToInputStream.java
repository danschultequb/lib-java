package qub;

import java.io.IOException;
import java.io.InputStream;

public class ByteReadStreamToInputStream extends InputStream
{
    private final ByteReadStream byteReadStream;

    public ByteReadStreamToInputStream(ByteReadStream byteReadStream)
    {
        this.byteReadStream = byteReadStream;
    }

    @Override
    public void close() throws IOException
    {
        byteReadStream.close();
    }

    @Override
    public int read() throws IOException
    {
        final Byte byteRead = byteReadStream.readByte();
        return byteRead == null ? -1 : byteRead.intValue();
    }

    @Override
    public int read(byte[] outputBytes) throws IOException
    {
        return byteReadStream.readBytes(outputBytes);
    }

    @Override
    public int read(byte[] outputBytes, int startIndex, int length) throws IOException
    {
        return byteReadStream.readBytes(outputBytes, startIndex, length);
    }
}
