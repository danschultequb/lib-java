package qub;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ConsoleTests
{
    @Test
    public void constructor()
    {
        final Console console = new Console();
        assertNotNull(console.writeStream);
        assertFalse(console.writeStream.hasValue());
        assertTrue(console.isOpen());
    }

    @Test
    public void close()
    {
        final Console console = new Console();
        assertFalse(console.close());
    }

    @Test
    public void getWriteStream()
    {
        final Console console = new Console();
        assertNotNull(console.writeStream);
        assertFalse(console.writeStream.hasValue());

        final TextWriteStream writeStream = console.getWriteStream();
        assertNotNull(writeStream);
        assertTrue(writeStream instanceof StandardOutputTextWriteStream);
        assertTrue(console.writeStream.hasValue());
        assertSame(writeStream, console.writeStream.get());
    }

    @Test
    public void setWriteStreamWithNull()
    {
        final Console console = new Console();
        console.setWriteStream(null);
        assertTrue(console.writeStream.hasValue());
        assertNull(console.writeStream.get());

        console.write((byte)50);
        console.write(new byte[]{51});
        console.write(new byte[]{52}, 0, 1);
        console.write("hello");
        console.writeLine("there!");
    }

    @Test
    public void setWriteStreamWithNonNull()
    {
        final Console console = new Console();
        final InMemoryTextWriteStream writeStream = new InMemoryTextWriteStream("\n");
        console.setWriteStream(writeStream);
        assertTrue(console.writeStream.hasValue());
        assertSame(writeStream, console.writeStream.get());

        console.write((byte)50);
        assertArrayEquals(new byte[]{50}, writeStream.getBytes());

        console.write(new byte[]{51});
        assertArrayEquals(new byte[]{50, 51}, writeStream.getBytes());

        console.write(new byte[]{52}, 0, 1);
        assertArrayEquals(new byte[]{50, 51, 52}, writeStream.getBytes());

        console.write("hello");
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111}, writeStream.getBytes());

        console.writeLine();
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10 }, writeStream.getBytes());

        console.writeLine("there!");
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, writeStream.getBytes());
    }

    @Test
    public void getReadStream()
    {
        final Console console = new Console();
        assertNotNull(console.readStream);
        assertFalse(console.readStream.hasValue());

        final TextReadStream readStream = console.getReadStream();
        assertNotNull(readStream);
        assertTrue(readStream instanceof StandardInputTextReadStream);
        assertTrue(console.readStream.hasValue());
        assertSame(readStream, console.readStream.get());
    }

    @Test
    public void setReadStreamWithNull() throws IOException
    {
        final Console console = new Console();
        console.setReadStream(null);
        assertTrue(console.readStream.hasValue());
        assertNull(console.readStream.get());

        assertNull(console.readBytes(5));
        assertEquals(-1, console.readBytes(null));
        assertEquals(-1, console.readBytes(null, 0, 0));
        assertNull(console.readCharacters(7));
        assertEquals(-1, console.readCharacters(null));
        assertEquals(-1, console.readCharacters(null, 0, 0));
        assertNull(console.readString(10));
        assertNull(console.readLine());
        assertNull(console.readLine(false));
    }

    @Test
    public void setReadStreamWithNonNull() throws IOException
    {
        final Console console = new Console();
        final InMemoryTextReadStream readStream = new InMemoryTextReadStream();
        console.setReadStream(readStream);
        assertTrue(console.readStream.hasValue());
        assertSame(readStream, console.readStream.get());

        readStream.add("hello there my good friend\nHow are you?\r\nI'm alright.");

        assertArrayEquals(new byte[] { 104, 101, 108, 108, 111 }, console.readBytes(5));

        final byte[] byteBuffer = new byte[2];
        assertEquals(2, console.readBytes(byteBuffer));
        assertArrayEquals(new byte[] { 32, 116 }, byteBuffer);

        assertEquals(1, console.readBytes(byteBuffer, 1, 1));
        assertArrayEquals(new byte[] { 32, 104 }, byteBuffer);

        assertArrayEquals(new char[] { 'e', 'r', 'e' }, console.readCharacters(3));

        final char[] characterBuffer = new char[4];
        assertEquals(4, console.readCharacters(characterBuffer));
        assertArrayEquals(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

        assertEquals(2, console.readCharacters(characterBuffer, 0, 2));
        assertArrayEquals(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

        assertEquals("od fr", console.readString(5));

        assertEquals("iend", console.readLine());

        assertEquals("How are you?\r\n", console.readLine(true));

        assertEquals("I'm alright.", console.readLine(false));
    }

    @Test
    public void getRandomInteger()
    {
        final Console console = new Console();

        // Use default JavaRandom object.
        assertNotEquals(console.getRandomInteger(), console.getRandomInteger());

        console.setRandom(new FixedRandom(10));
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(10, console.getRandomInteger());
        }

        console.setRandom(null);
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(0, console.getRandomInteger());
        }
    }

    @Test
    public void getRandomIntegerBetween()
    {
        final Console console = new Console();

        // Use default JavaRandom object.
        assertNotEquals(console.getRandomIntegerBetween(1, 100), console.getRandomIntegerBetween(1, 100));

        console.setRandom(new FixedRandom(10));
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(20, console.getRandomIntegerBetween(10, 30));
        }

        console.setRandom(null);
        for (int i = 0; i < 3; ++i)
        {
            assertEquals(72, console.getRandomIntegerBetween(72, 83));
        }
    }

    @Test
    public void getRandomBoolean()
    {
        final Console console = new Console();

        // Use default JavaRandom object.
        console.getRandomBoolean();

        console.setRandom(new FixedRandom(10));
        for (int i = 0; i < 10; ++i)
        {
            assertTrue(console.getRandomBoolean());
        }

        console.setRandom(null);
        for (int i = 0; i < 10; ++i)
        {
            assertFalse(console.getRandomBoolean());
        }
    }
}
