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
    public void close()
    {
        byteReadStream.close();
    }

    @Override
    public int read() throws IOException
    {
        final Result<Byte> byteRead = byteReadStream.readByte();
        if (byteRead.hasError())
        {
            final Throwable error = byteRead.getError();
            if (error instanceof IOException)
            {
                throw (IOException)error;
            }
            else if (error instanceof RuntimeException)
            {
                throw (RuntimeException)error;
            }
        }
        return byteRead.getValue() == null ? -1 : byteRead.getValue().intValue();
    }

    @Override
    public int read(byte[] outputBytes) throws IOException
    {
        final Result<Integer> readBytesResult = byteReadStream.readBytes(outputBytes);
        if (readBytesResult.hasError())
        {
            final Throwable error = readBytesResult.getError();
            if (error instanceof IOException)
            {
                throw (IOException)error;
            }
            else if (error instanceof RuntimeException)
            {
                throw (RuntimeException)error;
            }
        }
        return readBytesResult.getValue() == null ? -1 : readBytesResult.getValue();
    }

    @Override
    public int read(byte[] outputBytes, int startIndex, int length) throws IOException
    {
        final Result<Integer> readBytesResult = byteReadStream.readBytes(outputBytes, startIndex, length);
        if (readBytesResult.hasError())
        {
            final Throwable error = readBytesResult.getError();
            if (error instanceof IOException)
            {
                throw (IOException)error;
            }
            else if (error instanceof RuntimeException)
            {
                throw (RuntimeException)error;
            }
        }
        return readBytesResult.getValue() == null ? -1 : readBytesResult.getValue();
    }
}
