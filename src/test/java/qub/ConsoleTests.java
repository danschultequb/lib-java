package qub;

import org.junit.Test;

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

        final ByteWriteStream writeStream = console.getWriteStream();
        assertNotNull(writeStream);
        assertTrue(writeStream instanceof StandardOutputWriteStream);
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
}
