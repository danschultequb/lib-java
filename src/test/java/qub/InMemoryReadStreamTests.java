package qub;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class InMemoryReadStreamTests
{
    @Test
    public void constructor()
    {
        final InMemoryReadStream readStream = new InMemoryReadStream();
        assertArrayEquals(new byte[0], readStream.getBytes());
        assertTrue(readStream.isOpen());
    }

    @Test
    public void close()
    {
        final InMemoryReadStream readStream = new InMemoryReadStream();
        assertTrue(readStream.close());
        assertFalse(readStream.isOpen());
        assertNull(readStream.getBytes());
        assertFalse(readStream.close());
    }

    @Test
    public void addBytes()
    {
        final InMemoryReadStream readStream = new InMemoryReadStream();

        readStream.add();
        assertArrayEquals(new byte[0], readStream.getBytes());

        readStream.add((byte[])null);
        assertArrayEquals(new byte[0], readStream.getBytes());

        readStream.add((byte)0);
        assertArrayEquals(new byte[] { 0 }, readStream.getBytes());

        readStream.add((byte)1, (byte)2, (byte)3);
        assertArrayEquals(new byte[] { 0, 1, 2, 3 }, readStream.getBytes());
    }

    @Test
    public void addString()
    {
        final InMemoryReadStream readStream = new InMemoryReadStream();

        readStream.add((String)null);
        assertArrayEquals(new byte[0], readStream.getBytes());

        readStream.add("");
        assertArrayEquals(new byte[0], readStream.getBytes());

        readStream.add("xyz");
        assertArrayEquals(new byte[] { 120, 121, 122 }, readStream.getBytes());
    }

    @Test
    public void readBytesWithBytesToRead() throws IOException
    {
        final InMemoryReadStream readStream = new InMemoryReadStream();

        assertArrayEquals(new byte[0], readStream.readBytes(-5));
        assertArrayEquals(new byte[0], readStream.readBytes(0));
        assertArrayEquals(new byte[0], readStream.readBytes(1));
        assertArrayEquals(new byte[0], readStream.readBytes(2));

        readStream.add((byte)0, (byte)1, (byte)2, (byte)3);

        assertArrayEquals(new byte[0], readStream.readBytes(-5));
        assertArrayEquals(new byte[0], readStream.readBytes(0));
        assertArrayEquals(new byte[] { 0 }, readStream.readBytes(1));
        assertArrayEquals(new byte[] { 1, 2 }, readStream.readBytes(2));
        assertArrayEquals(new byte[] { 3 }, readStream.readBytes(3));
        assertArrayEquals(new byte[0], readStream.getBytes());

        assertTrue(readStream.close());

        assertNull(readStream.readBytes(1));
    }
}
