package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryTextWriteStreamTests
{
    @Test
    public void constructorWithNoArguments()
    {
        final InMemoryTextWriteStream writeStream = new InMemoryTextWriteStream();
        assertSame(TextWriteStreamBase.defaultCharacterEncoding, writeStream.getCharacterEncoding());
        assertEquals(TextWriteStreamBase.defaultNewLine, writeStream.getNewLine());
        assertTrue(writeStream.isOpen());
    }

    @Test
    public void constructorWithCharacterEncoding()
    {
        final InMemoryTextWriteStream writeStream = new InMemoryTextWriteStream(CharacterEncoding.ASCII);
        assertSame(CharacterEncoding.ASCII, writeStream.getCharacterEncoding());
        assertEquals(TextWriteStreamBase.defaultNewLine, writeStream.getNewLine());
        assertTrue(writeStream.isOpen());
    }

    @Test
    public void constructorWithNewLine()
    {
        final InMemoryTextWriteStream writeStream = new InMemoryTextWriteStream("\r\n");
        assertSame(TextWriteStreamBase.defaultCharacterEncoding, writeStream.getCharacterEncoding());
        assertEquals("\r\n", writeStream.getNewLine());
        assertTrue(writeStream.isOpen());
    }

    @Test
    public void constructorWithCharacterEncodingAndNewLine()
    {
        final InMemoryTextWriteStream writeStream = new InMemoryTextWriteStream(CharacterEncoding.ASCII, "\n");
        assertSame(CharacterEncoding.ASCII, writeStream.getCharacterEncoding());
        assertEquals("\n", writeStream.getNewLine());
        assertTrue(writeStream.isOpen());
    }

    @Test
    public void getText()
    {
        final InMemoryTextWriteStream writeStream = new InMemoryTextWriteStream(CharacterEncoding.ASCII, "\n");
        assertEquals("", writeStream.getText());

        writeStream.write("Hello there!");
        assertEquals("Hello there!", writeStream.getText());

        writeStream.writeLine(null);
        assertEquals("Hello there!\n", writeStream.getText());
    }

    @Test
    public void close()
    {
        final InMemoryTextWriteStream writeStream = new InMemoryTextWriteStream();
        assertTrue(writeStream.isOpen());

        assertTrue(writeStream.close());
        assertFalse(writeStream.isOpen());

        writeStream.write((byte)5);
        assertNull(writeStream.getBytes());
        assertNull(writeStream.getText());

        assertFalse(writeStream.close());
        assertFalse(writeStream.isOpen());
    }

    @Test
    public void clear()
    {
        final InMemoryTextWriteStream byteWriteStream = new InMemoryTextWriteStream();
        byteWriteStream.clear();
        assertArrayEquals(new byte[0], byteWriteStream.getBytes());

        byteWriteStream.write(new byte[] { 1, 2, 3, 4 });
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, byteWriteStream.getBytes());

        byteWriteStream.clear();
        assertArrayEquals(new byte[0], byteWriteStream.getBytes());

        byteWriteStream.write("Hello");
        assertEquals("Hello", byteWriteStream.getText());

        byteWriteStream.clear();
        assertEquals("", byteWriteStream.getText());
    }
}
