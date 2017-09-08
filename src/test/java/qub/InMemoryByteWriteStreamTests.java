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
        assertTrue(writeStream.close());
        assertFalse(writeStream.isOpen());
        assertNull(writeStream.getBytes());
        assertFalse(writeStream.close());
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

//    @Test
//    public void writeString()
//    {
//        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
//        writeStream.write("");
//        assertArrayEquals(new byte[0], writeStream.getBytes());
//
//        writeStream.write("abc");
//        assertArrayEquals(new byte[] { 97, 98, 99 }, writeStream.getBytes());
//    }
//
//    @Test
//    public void writeLine()
//    {
//        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
//        writeStream.writeLine();
//        assertArrayEquals(new byte[] { 10 }, writeStream.getBytes());
//
//        writeStream.writeLine();
//        assertArrayEquals(new byte[] { 10, 10 }, writeStream.getBytes());
//    }
//
//    @Test
//    public void writeLineString()
//    {
//        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
//        writeStream.writeLine("");
//        assertArrayEquals(new byte[] { 10 }, writeStream.getBytes());
//
//        writeStream.writeLine("defg");
//        assertArrayEquals(new byte[] { 10, 100, 101, 102, 103, 10 }, writeStream.getBytes());
//    }
}
