package qub;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class InMemoryByteReadStreamTests
{
    @Test
    public void constructorWithNoArguments()
    {
        final InMemoryByteReadStream readStream = new InMemoryByteReadStream();
        assertTrue(readStream.isOpen());
    }

    @Test
    public void close()
    {
        final InMemoryByteReadStream readStream = new InMemoryByteReadStream();
        assertTrue(readStream.close());
        assertFalse(readStream.isOpen());
        assertFalse(readStream.close());
    }

    @Test
    public void readBytesWithBytesToRead() throws IOException
    {
        InMemoryByteReadStream readStream = new InMemoryByteReadStream();

        assertArrayEquals(null, readStream.readBytes(-5));
        assertArrayEquals(null, readStream.readBytes(0));
        assertArrayEquals(null, readStream.readBytes(1));
        assertArrayEquals(null, readStream.readBytes(2));

        readStream = new InMemoryByteReadStream(new byte[] { 0, 1, 2, 3 });

        assertArrayEquals(null, readStream.readBytes(-5));
        assertArrayEquals(null, readStream.readBytes(0));
        assertArrayEquals(new byte[] { 0 }, readStream.readBytes(1));
        assertArrayEquals(new byte[] { 1, 2 }, readStream.readBytes(2));
        assertArrayEquals(new byte[] { 3 }, readStream.readBytes(3));
        assertArrayEquals(null, readStream.readBytes(1));

        assertTrue(readStream.close());

        assertNull(readStream.readBytes(1));
    }

    @Test
    public void asLineReadStream()
    {
        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
        final LineReadStream lineReadStream = byteReadStream.asLineReadStream();
        assertEquals(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
        assertFalse(lineReadStream.getIncludeNewLines());
        assertSame(byteReadStream, lineReadStream.asByteReadStream());
    }

    @Test
    public void asLineReadStreamWithCharacterEncoding()
    {
        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
        final LineReadStream lineReadStream = byteReadStream.asLineReadStream(CharacterEncoding.US_ASCII);
        assertEquals(CharacterEncoding.US_ASCII, lineReadStream.getCharacterEncoding());
        assertFalse(lineReadStream.getIncludeNewLines());
        assertSame(byteReadStream, lineReadStream.asByteReadStream());
    }

    @Test
    public void asLineReadStreamWithIncludeNewLines()
    {
        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
        final LineReadStream lineReadStream = byteReadStream.asLineReadStream(true);
        assertEquals(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
        assertTrue(lineReadStream.getIncludeNewLines());
        assertSame(byteReadStream, lineReadStream.asByteReadStream());
    }

    @Test
    public void asLineReadStreamWithCharacterEncodingAndIncludeNewLines()
    {
        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
        final LineReadStream lineReadStream = byteReadStream.asLineReadStream(CharacterEncoding.UTF_8, false);
        assertEquals(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
        assertFalse(lineReadStream.getIncludeNewLines());
        assertSame(byteReadStream, lineReadStream.asByteReadStream());
    }
}
