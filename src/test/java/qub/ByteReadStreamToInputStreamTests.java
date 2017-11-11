package qub;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ByteReadStreamToInputStreamTests
{
    @Test
    public void close() throws IOException
    {
        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
        final ByteReadStreamToInputStream inputStream = new ByteReadStreamToInputStream(byteReadStream);
        inputStream.close();
        assertFalse(byteReadStream.isOpen());
    }

    @Test
    public void read() throws IOException
    {
        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
        final ByteReadStreamToInputStream inputStream = new ByteReadStreamToInputStream(byteReadStream);
        final int byteRead = inputStream.read();
        assertEquals(-1, byteRead);
    }

    @Test
    public void readByteArray() throws IOException
    {
        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
        final ByteReadStreamToInputStream inputStream = new ByteReadStreamToInputStream(byteReadStream);
        final byte[] bytes = new byte[100];
        final int bytesRead = inputStream.read(bytes);
        assertEquals(-1, bytesRead);
        assertArrayEquals(new byte[100], bytes);
    }
}
