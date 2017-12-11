package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryByteWriteStreamTests
{
    @Test
    public void constructor()
    {
        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
        assertArrayEquals(new byte[0], writeStream.getBytes());
        assertTrue(writeStream.isOpen());
    }

    @Test
    public void close()
    {
        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
        writeStream.close();
        assertFalse(writeStream.isOpen());
        assertNull(writeStream.getBytes());
        writeStream.close();
        assertNull(writeStream.getBytes());
    }

    @Test
    public void writeByte()
    {
        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
        writeStream.write((byte)17);
        assertArrayEquals(new byte[] { 17 }, writeStream.getBytes());
    }

    @Test
    public void writeByteArray()
    {
        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
        writeStream.write(new byte[0]);
        assertArrayEquals(new byte[0], writeStream.getBytes());

        writeStream.write(new byte[] { 1, 2, 3, 4 });
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, writeStream.getBytes());
    }

    @Test
    public void writeByteArrayWithOffsetAndLength()
    {
        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
        writeStream.write(new byte[0], 0, 0);
        assertArrayEquals(new byte[0], writeStream.getBytes());

        writeStream.write(new byte[] { 1, 2, 3, 4 }, 1, 0);
        assertArrayEquals(new byte[0], writeStream.getBytes());

        writeStream.write(new byte[] { 1, 2, 3, 4 }, 1, 2);
        assertArrayEquals(new byte[] { 2, 3 }, writeStream.getBytes());
    }

    @Test
    public void writeAllWithNull()
    {
        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
        assertFalse(writeStream.writeAll(null));
        assertArrayEquals(new byte[0], writeStream.getBytes());
    }

    @Test
    public void clear()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        byteWriteStream.clear();
        assertArrayEquals(new byte[0], byteWriteStream.getBytes());

        byteWriteStream.write(new byte[] { 1, 2, 3, 4 });
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, byteWriteStream.getBytes());

        byteWriteStream.clear();
        assertArrayEquals(new byte[0], byteWriteStream.getBytes());
    }

    @Test
    public void asCharacterWriteStream()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final CharacterWriteStream characterWriteStream = byteWriteStream.asCharacterWriteStream();
        assertNotNull(characterWriteStream);
        assertSame(byteWriteStream, characterWriteStream.asByteWriteStream());
    }

    @Test
    public void asCharacterWriteStreamWithEncoding()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final CharacterWriteStream characterWriteStream = byteWriteStream.asCharacterWriteStream(CharacterEncoding.US_ASCII);
        assertNotNull(characterWriteStream);
        assertSame(byteWriteStream, characterWriteStream.asByteWriteStream());
    }

    @Test
    public void asLineWriteStream()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final LineWriteStream lineWriteStream = byteWriteStream.asLineWriteStream();
        assertNotNull(lineWriteStream);
        assertSame(byteWriteStream, lineWriteStream.asByteWriteStream());
    }

    @Test
    public void asLineWriteStreamWithEncoding()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final LineWriteStream lineWriteStream = byteWriteStream.asLineWriteStream(CharacterEncoding.US_ASCII);
        assertNotNull(lineWriteStream);
        assertSame(byteWriteStream, lineWriteStream.asByteWriteStream());
    }

    @Test
    public void asLineWriteStreamWithLineSeparator()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final LineWriteStream lineWriteStream = byteWriteStream.asLineWriteStream("\r\n");
        assertNotNull(lineWriteStream);
        assertEquals("\r\n", lineWriteStream.getLineSeparator());
        assertSame(byteWriteStream, lineWriteStream.asByteWriteStream());
    }

    @Test
    public void asLineWriteStreamWithEncodingAndLineSeparator()
    {
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        final LineWriteStream lineWriteStream = byteWriteStream.asLineWriteStream(CharacterEncoding.US_ASCII, "\r\n");
        assertNotNull(lineWriteStream);
        assertSame(CharacterEncoding.US_ASCII, lineWriteStream.getCharacterEncoding());
        assertEquals("\r\n", lineWriteStream.getLineSeparator());
        assertSame(byteWriteStream, lineWriteStream.asByteWriteStream());
    }
}
