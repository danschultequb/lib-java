package qub;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class InMemoryTextReadStreamTests
{
    @Test
    public void constructorWithNoArguments()
    {
        final InMemoryTextReadStream readStream = new InMemoryTextReadStream();
        assertArrayEquals(new byte[0], readStream.getBytes());
        assertEquals("", readStream.getText());
        assertTrue(readStream.isOpen());
    }

    @Test
    public void constructorWithCharacterEncoding()
    {
        final InMemoryTextReadStream readStream = new InMemoryTextReadStream(CharacterEncoding.ASCII);
        assertArrayEquals(new byte[0], readStream.getBytes());
        assertEquals("", readStream.getText());
        assertTrue(readStream.isOpen());
    }

    @Test
    public void close()
    {
        final InMemoryTextReadStream readStream = new InMemoryTextReadStream();
        assertTrue(readStream.close());
        assertFalse(readStream.isOpen());
        assertNull(readStream.getBytes());
        assertNull(readStream.getText());
        assertFalse(readStream.close());
    }

    @Test
    public void addBytes()
    {
        final InMemoryTextReadStream readStream = new InMemoryTextReadStream();

        readStream.add();
        assertArrayEquals(new byte[0], readStream.getBytes());
        assertEquals("", readStream.getText());

        readStream.add((byte[])null);
        assertArrayEquals(new byte[0], readStream.getBytes());
        assertEquals("", readStream.getText());

        readStream.add((byte)97);
        assertArrayEquals(new byte[] { 97 }, readStream.getBytes());
        assertEquals("a", readStream.getText());

        readStream.add((byte)98, (byte)99, (byte)100);
        assertArrayEquals(new byte[] { 97, 98, 99, 100 }, readStream.getBytes());
        assertEquals("abcd", readStream.getText());
    }

    @Test
    public void addString()
    {
        final InMemoryTextReadStream readStream = new InMemoryTextReadStream();

        readStream.add((String)null);
        assertArrayEquals(new byte[0], readStream.getBytes());
        assertEquals("", readStream.getText());

        readStream.add("");
        assertArrayEquals(new byte[0], readStream.getBytes());
        assertEquals("", readStream.getText());

        readStream.add("xyz");
        assertArrayEquals(new byte[] { 120, 121, 122 }, readStream.getBytes());
        assertEquals("xyz", readStream.getText());
    }

    @Test
    public void readBytesWithBytesToRead() throws IOException
    {
        final InMemoryTextReadStream readStream = new InMemoryTextReadStream();

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

    @Test
    public void readCharactersWithCharactersToReadInteger() throws IOException
    {
        final InMemoryTextReadStream readStream = new InMemoryTextReadStream();
        assertArrayEquals(new char[0], readStream.readCharacters(-1));
        assertArrayEquals(new char[0], readStream.readCharacters(0));
        assertArrayEquals(new char[0], readStream.readCharacters(500));

        readStream.add("hello there!");
        assertArrayEquals(new char[0], readStream.readCharacters(-1));
        assertArrayEquals(new char[0], readStream.readCharacters(0));
        assertArrayEquals(new char[] { 'h' }, readStream.readCharacters(1));
        assertArrayEquals(new char[] { 'e', 'l' }, readStream.readCharacters(2));
        assertEquals("lo ", readStream.readString(3));
        assertEquals("ther", readStream.readString(4));
        assertEquals("e!", readStream.readString(5));
        assertEquals("", readStream.readString(6));

        readStream.close();
        assertNull(readStream.readCharacters(100));
        assertNull(readStream.readString(100));
    }

    @Test
    public void readLine() throws IOException
    {
        final InMemoryTextReadStream readStream = new InMemoryTextReadStream();
        assertNull(readStream.readLine());

        readStream.add("test1");
        assertEquals("test1", readStream.readLine());
        assertNull(readStream.readLine());

        readStream.add("a\nb\r\nc\nd\r\n");
        assertEquals("a\n", readStream.readLine(true));
        assertEquals("b", readStream.readLine(false));
        assertEquals("c", readStream.readLine(false));
        assertEquals("d\r\n", readStream.readLine(true));
        assertNull(readStream.readLine());

        readStream.add("abc");
        readStream.close();
        assertNull(readStream.readLine());
    }
}
