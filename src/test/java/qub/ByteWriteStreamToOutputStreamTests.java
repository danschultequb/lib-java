package qub;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ByteWriteStreamToOutputStreamTests
{
    @Test
    public void close() throws IOException
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final ByteWriteStreamToOutputStream outputStream = new ByteWriteStreamToOutputStream(byteWriteStream);
        outputStream.close();
        assertFalse(byteWriteStream.isOpen());
    }

    @Test
    public void writeByte() throws IOException
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final ByteWriteStreamToOutputStream outputStream = new ByteWriteStreamToOutputStream(byteWriteStream);
        outputStream.write((byte)15);
        assertArrayEquals(new byte[] { 15 }, byteWriteStream.getBytes());
    }

    @Test
    public void writeByteArray() throws IOException
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final ByteWriteStreamToOutputStream outputStream = new ByteWriteStreamToOutputStream(byteWriteStream);
        outputStream.write(new byte[] { 16, 17, 18, 19, 20 });
        assertArrayEquals(new byte[] { 16, 17, 18, 19, 20 }, byteWriteStream.getBytes());
    }
}
